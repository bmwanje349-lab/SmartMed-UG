package ug.smartmed

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ug.smartmed.data.BatchAuditEvent
import ug.smartmed.data.ReportCsvExporter
import ug.smartmed.data.SmartMedOptions
import ug.smartmed.data.SmartMedViewModel
import ug.smartmed.data.WasteBatch
import ug.smartmed.data.WasteBatchDraft
import ug.smartmed.data.WasteCategory
import ug.smartmed.data.WasteHandover
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val smartMedViewModel: SmartMedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SmartMedApp(smartMedViewModel)
                }
            }
        }
    }
}

private enum class SmartMedTab(val label: String) {
    BATCHES("Batches"),
    HANDOVERS("Handovers"),
    REPORTS("Reports"),
    AUDIT("Audit trail")
}

@Composable
private fun SmartMedApp(viewModel: SmartMedViewModel) {
    val batches by viewModel.batches.collectAsState()
    val handovers by viewModel.handovers.collectAsState()
    val auditEvents by viewModel.auditEvents.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableStateOf(SmartMedTab.BATCHES) }
    var facilityName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(WasteCategory.INFECTIOUS) }
    var selectedSubcategory by remember { mutableStateOf(WasteCategory.INFECTIOUS.subcategories.first()) }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("kg") }
    var collectionPoint by remember { mutableStateOf("") }
    var recorderRole by remember { mutableStateOf(SmartMedOptions.authorisedRoles.first()) }
    var batchNotes by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<String?>(null) }
    var correctionReason by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Offline mode ready. Records remain on this phone.") }

    var selectedBatchId by remember { mutableStateOf("") }
    var senderRole by remember { mutableStateOf(SmartMedOptions.authorisedRoles[0]) }
    var receiverRole by remember { mutableStateOf(SmartMedOptions.authorisedRoles[1]) }
    var evidenceReference by remember { mutableStateOf("") }
    var handoverNotes by remember { mutableStateOf("") }
    var reviewerRole by remember { mutableStateOf(SmartMedOptions.authorisedRoles[1]) }
    var reviewNote by remember { mutableStateOf("") }

    var archiveTarget by remember { mutableStateOf<WasteBatch?>(null) }
    var archiveReason by remember { mutableStateOf("") }
    var pendingCsv by remember { mutableStateOf("") }

    val createCsv = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? ->
        if (uri == null) {
            message = "Report export was cancelled."
        } else {
            message = writeCsv(context.contentResolver, uri, pendingCsv)
        }
    }

    fun resetBatchForm() {
        facilityName = ""
        selectedCategory = WasteCategory.INFECTIOUS
        selectedSubcategory = WasteCategory.INFECTIOUS.subcategories.first()
        quantity = ""
        unit = "kg"
        collectionPoint = ""
        recorderRole = SmartMedOptions.authorisedRoles.first()
        batchNotes = ""
        editingId = null
        correctionReason = ""
    }

    val filteredBatches = batches.filter { batch ->
        val query = searchText.trim().lowercase(Locale.ROOT)
        query.isBlank() ||
            batch.id.lowercase(Locale.ROOT).contains(query) ||
            batch.facilityName.lowercase(Locale.ROOT).contains(query) ||
            batch.collectionPoint.lowercase(Locale.ROOT).contains(query) ||
            (WasteCategory.fromCode(batch.category)?.label ?: batch.category).lowercase(Locale.ROOT).contains(query)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {
            Text("SmartMed UG", style = MaterialTheme.typography.headlineMedium)
            Text("Offline healthcare-waste traceability prototype")
            Text(
                "Role labels do not authenticate users. A record or review entry is not proof of safe treatment or disposal.",
                style = MaterialTheme.typography.bodySmall
            )
        }
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            SmartMedTab.values().forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = { Text(tab.label) }
                )
            }
        }
        Text(message, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp), style = MaterialTheme.typography.bodySmall)

        when (selectedTab) {
            SmartMedTab.BATCHES -> LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(if (editingId == null) "Register waste batch" else "Correct batch $editingId", style = MaterialTheme.typography.titleLarge)
                            Text("Use fictional data while testing. Do not handle waste or record patient information.")
                            OutlinedTextField(
                                value = facilityName,
                                onValueChange = { facilityName = it },
                                label = { Text("Facility name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            DropdownField(
                                label = "Waste category",
                                value = selectedCategory.code,
                                options = WasteCategory.values().map { it.code to it.label },
                                onSelected = { code ->
                                    selectedCategory = WasteCategory.fromCode(code) ?: WasteCategory.OTHER_REQUIRES_REVIEW
                                    selectedSubcategory = selectedCategory.subcategories.first()
                                }
                            )
                            DropdownField(
                                label = "Subcategory",
                                value = selectedSubcategory,
                                options = selectedCategory.subcategories.map { it to it },
                                onSelected = { selectedSubcategory = it }
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = quantity,
                                    onValueChange = { quantity = it },
                                    label = { Text("Quantity") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                Box(modifier = Modifier.weight(1f)) {
                                    DropdownField(
                                        label = "Unit",
                                        value = unit,
                                        options = SmartMedOptions.quantityUnits.map { it to it },
                                        onSelected = { unit = it }
                                    )
                                }
                            }
                            OutlinedTextField(
                                value = collectionPoint,
                                onValueChange = { collectionPoint = it },
                                label = { Text("Department / collection point") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            DropdownField(
                                label = "Recorder role",
                                value = recorderRole,
                                options = SmartMedOptions.authorisedRoles.map { it to it },
                                onSelected = { recorderRole = it }
                            )
                            OutlinedTextField(
                                value = batchNotes,
                                onValueChange = { if (it.length <= 1000) batchNotes = it },
                                label = { Text("Notes (no patient data)") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2
                            )
                            if (editingId != null) {
                                OutlinedTextField(
                                    value = correctionReason,
                                    onValueChange = { if (it.length <= 500) correctionReason = it },
                                    label = { Text("Required reason for correction") },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 2
                                )
                            }
                            Button(
                                onClick = {
                                    val draft = WasteBatchDraft(
                                        facilityName = facilityName,
                                        categoryCode = selectedCategory.code,
                                        subcategory = selectedSubcategory,
                                        quantityText = quantity,
                                        unit = unit,
                                        collectionPoint = collectionPoint,
                                        recorderRole = recorderRole,
                                        notes = batchNotes
                                    )
                                    viewModel.saveBatch(draft, editingId, correctionReason) { success, result ->
                                        message = result
                                        if (success) resetBatchForm()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (editingId == null) "Save batch offline" else "Save correction")
                            }
                            if (editingId != null) {
                                OutlinedButton(onClick = { resetBatchForm(); message = "Correction cancelled." }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Cancel correction")
                                }
                            }
                        }
                    }
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        label = { Text("Search by batch, facility, category or point") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Text("Active batches: ${filteredBatches.size}", style = MaterialTheme.typography.titleMedium)
                }
                if (filteredBatches.isEmpty()) {
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                if (searchText.isBlank()) "No batches yet. Register a fictional test batch to begin."
                                else "No active batches match this search.",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
                items(filteredBatches, key = { it.id }) { batch ->
                    BatchCard(
                        batch = batch,
                        onEdit = {
                            val category = WasteCategory.fromCode(batch.category) ?: WasteCategory.OTHER_REQUIRES_REVIEW
                            editingId = batch.id
                            facilityName = batch.facilityName.takeUnless { it == "UNASSIGNED" }.orEmpty()
                            selectedCategory = category
                            selectedSubcategory = batch.subcategory.takeIf { it in category.subcategories } ?: category.subcategories.first()
                            quantity = batch.quantity.toString()
                            unit = batch.unit.takeIf { it in SmartMedOptions.quantityUnits } ?: "kg"
                            collectionPoint = batch.collectionPoint
                            recorderRole = batch.recordedByRole.takeIf { it in SmartMedOptions.authorisedRoles } ?: SmartMedOptions.authorisedRoles.first()
                            batchNotes = batch.notes
                            correctionReason = ""
                            message = "Enter a correction reason before saving. Original and corrected snapshots will be retained."
                        },
                        onArchive = { archiveTarget = batch; archiveReason = "" }
                    )
                }
            }

            SmartMedTab.HANDOVERS -> LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Record batch handover", style = MaterialTheme.typography.titleLarge)
                            Text("Enter a document or register reference when available. A reference is not automatically verified.")
                            DropdownField(
                                label = "Batch",
                                value = selectedBatchId,
                                options = listOf("" to "Choose a batch") + batches.map {
                                    it.id to "${it.id} — ${WasteCategory.fromCode(it.category)?.label ?: it.category}"
                                },
                                onSelected = { selectedBatchId = it }
                            )
                            DropdownField(
                                label = "Sender role",
                                value = senderRole,
                                options = SmartMedOptions.authorisedRoles.map { it to it },
                                onSelected = { senderRole = it }
                            )
                            DropdownField(
                                label = "Receiver role",
                                value = receiverRole,
                                options = SmartMedOptions.authorisedRoles.map { it to it },
                                onSelected = { receiverRole = it }
                            )
                            OutlinedTextField(
                                value = evidenceReference,
                                onValueChange = { if (it.length <= 240) evidenceReference = it },
                                label = { Text("Supporting evidence reference (optional)") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = handoverNotes,
                                onValueChange = { if (it.length <= 1000) handoverNotes = it },
                                label = { Text("Handover notes (no patient data)") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2
                            )
                            Button(
                                onClick = {
                                    viewModel.recordHandover(
                                        selectedBatchId, senderRole, receiverRole, evidenceReference, handoverNotes
                                    ) { success, result ->
                                        message = result
                                        if (success) {
                                            evidenceReference = ""
                                            handoverNotes = ""
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Save handover offline") }
                            Text("Evidence review")
                            DropdownField(
                                label = "Reviewer role",
                                value = reviewerRole,
                                options = SmartMedOptions.authorisedRoles.map { it to it },
                                onSelected = { reviewerRole = it }
                            )
                            OutlinedTextField(
                                value = reviewNote,
                                onValueChange = { if (it.length <= 1000) reviewNote = it },
                                label = { Text("Review note (optional)") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2
                            )
                        }
                    }
                    Text("Handover records: ${handovers.size}", style = MaterialTheme.typography.titleMedium)
                }
                if (handovers.isEmpty()) {
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text("No handovers yet. Use fictional batches for initial testing.", modifier = Modifier.padding(16.dp))
                        }
                    }
                }
                items(handovers, key = { it.id }) { handover ->
                    HandoverCard(
                        handover = handover,
                        onReview = {
                            viewModel.reviewHandover(handover.id, reviewerRole, reviewNote) { _, result ->
                                message = result
                            }
                        }
                    )
                }
            }

            SmartMedTab.REPORTS -> LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Local summary", style = MaterialTheme.typography.titleLarge)
                            Text("Active batches: ${batches.size}")
                            Text("Handover records: ${handovers.size}")
                            Text("Handovers missing a reference: ${handovers.count { it.evidenceStatus == "AWAITING_EVIDENCE" }}")
                            Text("References awaiting a recorded review: ${handovers.count { it.evidenceStatus == "PRESENT_UNREVIEWED" }}")
                            Text("Review entries recorded: ${handovers.count { it.evidenceStatus == "REVIEW_RECORDED" }}")
                            Text("Quantity is not added across different units; categories must be checked against current national guidance before a pilot.")
                        }
                    }
                }
                item { Text("Batch count by category", style = MaterialTheme.typography.titleMedium) }
                items(batches.groupBy { it.category }.toList(), key = { it.first }) { (code, group) ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(WasteCategory.fromCode(code)?.label ?: "Legacy / unclassified: $code", modifier = Modifier.weight(1f))
                            Text(group.size.toString(), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
                item {
                    Button(
                        onClick = {
                            pendingCsv = ReportCsvExporter.export(batches, handovers)
                            createCsv.launch("smartmed-report-${System.currentTimeMillis()}.csv")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Export report as CSV") }
                    Text("The CSV is generated locally. It is not uploaded or sent to NEMA or the Ministry.")
                }
            }

            SmartMedTab.AUDIT -> LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text("Recent local audit events", style = MaterialTheme.typography.titleLarge)
                    Text("Events record changes and reasons, but local storage is not tamper-proof and role labels are not login credentials.")
                }
                if (auditEvents.isEmpty()) {
                    item { Text("No audit events recorded yet.") }
                }
                items(auditEvents, key = { it.eventId }) { event -> AuditEventCard(event) }
            }
        }
    }

    if (archiveTarget != null) {
        AlertDialog(
            onDismissRequest = { archiveTarget = null; archiveReason = "" },
            title = { Text("Archive batch?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("This hides the batch from the active list but keeps its history and related handovers.")
                    OutlinedTextField(
                        value = archiveReason,
                        onValueChange = { if (it.length <= 500) archiveReason = it },
                        label = { Text("Required archive reason") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val target = archiveTarget
                        if (target != null) {
                            viewModel.archiveBatch(target.id, recorderRole, archiveReason) { success, result ->
                                message = result
                                if (success) {
                                    archiveTarget = null
                                    archiveReason = ""
                                    if (editingId == target.id) resetBatchForm()
                                }
                            }
                        }
                    },
                    enabled = archiveReason.isNotBlank()
                ) { Text("Archive") }
            },
            dismissButton = {
                TextButton(onClick = { archiveTarget = null; archiveReason = "" }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun BatchCard(batch: WasteBatch, onEdit: () -> Unit, onArchive: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(batch.id, style = MaterialTheme.typography.titleMedium)
            Text(batch.facilityName)
            Text("${WasteCategory.fromCode(batch.category)?.label ?: "Unclassified category"} — ${batch.subcategory}")
            Text("${batch.quantity} ${batch.unit} • ${batch.collectionPoint}")
            Text("Recorded by role: ${batch.recordedByRole}")
            Text("Created: ${formatTimestamp(batch.createdAtEpochMillis)}")
            Text("Revision ${batch.revision} • Sync: ${batch.syncState} (local only)")
            Text("Status: ${batch.recordStatus}; not proof of treatment or safe disposal", style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onEdit, modifier = Modifier.weight(1f)) { Text("Correct") }
                OutlinedButton(onClick = onArchive, modifier = Modifier.weight(1f)) { Text("Archive") }
            }
        }
    }
}

@Composable
private fun HandoverCard(handover: WasteHandover, onReview: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(handover.id, style = MaterialTheme.typography.titleMedium)
            Text("Batch: ${handover.batchId}")
            Text("Sender: ${handover.senderRole} → Receiver: ${handover.receiverRole}")
            Text("Date: ${formatTimestamp(handover.timestampEpochMillis)}")
            Text("Evidence reference: ${handover.evidenceReference.ifBlank { "Not provided" }}")
            Text(
                when (handover.evidenceStatus) {
                    "AWAITING_EVIDENCE" -> "Evidence status: missing reference"
                    "PRESENT_UNREVIEWED" -> "Evidence status: reference recorded; review pending"
                    "REVIEW_RECORDED" -> "Evidence status: review entry recorded; not certification of safe disposal"
                    else -> "Evidence status: ${handover.evidenceStatus}"
                }
            )
            if (handover.reviewedByRole != null) {
                Text("Review role: ${handover.reviewedByRole} • ${formatTimestamp(handover.reviewedAtEpochMillis ?: 0L)}")
                if (handover.reviewNote.isNotBlank()) Text("Review note: ${handover.reviewNote}")
            }
            if (handover.evidenceReference.isNotBlank() && handover.evidenceStatus != "REVIEW_RECORDED") {
                OutlinedButton(onClick = onReview, modifier = Modifier.fillMaxWidth()) {
                    Text("Record evidence review")
                }
            }
        }
    }
}

@Composable
private fun AuditEventCard(event: BatchAuditEvent) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(event.eventType, style = MaterialTheme.typography.titleMedium)
            Text("Batch: ${event.batchId}")
            Text("${formatTimestamp(event.timestampEpochMillis)} • Role label: ${event.actorRole}")
            Text("Reason: ${event.reason}")
        }
    }
}

@Composable
private fun DropdownField(
    label: String,
    value: String,
    options: List<Pair<String, String>>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.firstOrNull { it.first == value }?.second ?: value.ifBlank { "Choose…" }
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text("$label: $selectedLabel")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            options.forEach { (optionValue, optionLabel) ->
                DropdownMenuItem(
                    text = { Text(optionLabel) },
                    onClick = {
                        onSelected(optionValue)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun formatTimestamp(epochMillis: Long): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(epochMillis))

private fun writeCsv(contentResolver: ContentResolver, uri: Uri, csv: String): String =
    try {
        val stream = contentResolver.openOutputStream(uri)
            ?: return "Could not open the selected location for report export."
        stream.bufferedWriter(Charsets.UTF_8).use { it.write(csv) }
        "CSV report saved. It remains wherever you chose to store it."
    } catch (error: Exception) {
        "Report export failed. Choose a writable location and try again."
    }
