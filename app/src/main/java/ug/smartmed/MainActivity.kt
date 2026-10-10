package ug.smartmed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ug.smartmed.data.SmartMedDatabase
import ug.smartmed.data.WasteBatch
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = SmartMedDatabase.getInstance(applicationContext).wasteBatchDao()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BatchHome(
                        onSave = { category, quantityText, unit, point, onResult ->
                            val quantity = quantityText.toDoubleOrNull()
                            if (category.isBlank() || quantity == null || quantity <= 0.0 || unit.isBlank() || point.isBlank()) {
                                onResult("Enter a category, a positive quantity, a unit, and a collection point.")
                            } else {
                                lifecycleScope.launch {
                                    try {
                                        val batch = WasteBatch(
                                            id = "SM-" + UUID.randomUUID().toString().take(8).uppercase(),
                                            category = category.trim(),
                                            quantity = quantity,
                                            unit = unit.trim(),
                                            collectionPoint = point.trim(),
                                            createdAtEpochMillis = System.currentTimeMillis()
                                        )
                                        dao.insert(batch)
                                        onResult("Batch saved on this phone: ${batch.id}")
                                    } catch (error: Exception) {
                                        onResult("Could not save this batch. Check the details and try again.")
                                    }
                                }
                            }
                        },
                        batches = dao.observeAll()
                    )
                }
            }
        }
    }
}

@Composable
private fun BatchHome(
    onSave: (String, String, String, String, (String) -> Unit) -> Unit,
    batches: kotlinx.coroutines.flow.Flow<List<WasteBatch>>
) {
    var category by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("kg") }
    var point by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Records are stored locally; no internet is required.") }
    val scope = rememberCoroutineScope()
    val batchList by batches.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("SmartMed UG", style = MaterialTheme.typography.headlineMedium)
        Text("Offline healthcare-waste batch register")
        Text("Prototype only — follow facility procedures and never handle waste to enter a record.")

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Waste category") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = unit,
                onValueChange = { unit = it },
                label = { Text("Unit (e.g. kg)") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }
        OutlinedTextField(
            value = point,
            onValueChange = { point = it },
            label = { Text("Department / collection point") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Button(
            onClick = {
                onSave(category, quantity, unit, point) { result ->
                    scope.launch { message = result }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save batch offline")
        }
        Text(message, style = MaterialTheme.typography.bodyMedium)
        Text("Saved batches (${batchList.size})", style = MaterialTheme.typography.titleMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(batchList, key = { it.id }) { batch ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(batch.id, style = MaterialTheme.typography.titleSmall)
                        Text("${batch.category} • ${batch.quantity} ${batch.unit}")
                        Text("Point: ${batch.collectionPoint}")
                        Text("Status: ${batch.recordStatus} — record entered, not proof of safe disposal")
                    }
                }
            }
        }
    }
}
