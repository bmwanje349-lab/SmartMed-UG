package ug.smartmed.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SmartMedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SmartMedRepository(SmartMedDatabase.getInstance(application))

    val batches: StateFlow<List<WasteBatch>> =
        repository.observeActiveBatches().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val handovers: StateFlow<List<WasteHandover>> =
        repository.observeHandovers().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val auditEvents: StateFlow<List<BatchAuditEvent>> =
        repository.observeRecentAuditEvents().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun saveBatch(
        draft: WasteBatchDraft,
        editingId: String?,
        correctionReason: String,
        onComplete: (Boolean, String) -> Unit
    ) {
        val errors = WasteBatchValidator.validate(draft)
        if (errors.isNotEmpty()) {
            onComplete(false, errors.joinToString(" "))
            return
        }
        if (editingId != null && correctionReason.isBlank()) {
            onComplete(false, "Enter a reason for the correction.")
            return
        }
        viewModelScope.launch {
            try {
                val batch = if (editingId == null) {
                    repository.createBatch(draft)
                } else {
                    repository.updateBatch(editingId, draft, correctionReason)
                }
                onComplete(true, if (editingId == null) "Batch saved on this phone: ${batch.id}" else "Correction saved and added to the local audit trail.")
            } catch (error: Exception) {
                onComplete(false, error.message ?: "The batch could not be saved.")
            }
        }
    }

    fun archiveBatch(id: String, actorRole: String, reason: String, onComplete: (Boolean, String) -> Unit) {
        if (reason.isBlank()) {
            onComplete(false, "Enter a reason before archiving.")
            return
        }
        viewModelScope.launch {
            try {
                repository.archiveBatch(id, actorRole, reason)
                onComplete(true, "Batch archived. Its history has been retained.")
            } catch (error: Exception) {
                onComplete(false, error.message ?: "The batch could not be archived.")
            }
        }
    }

    fun recordHandover(
        batchId: String,
        senderRole: String,
        receiverRole: String,
        evidenceReference: String,
        notes: String,
        onComplete: (Boolean, String) -> Unit
    ) {
        if (batchId.isBlank()) {
            onComplete(false, "Choose a batch.")
            return
        }
        if (notes.length > 1000 || evidenceReference.length > 240) {
            onComplete(false, "Evidence reference must be at most 240 characters and notes at most 1,000 characters.")
            return
        }
        viewModelScope.launch {
            try {
                repository.recordHandover(batchId, senderRole, receiverRole, evidenceReference, notes)
                onComplete(true, "Handover saved. Evidence status is shown separately.")
            } catch (error: Exception) {
                onComplete(false, error.message ?: "The handover could not be saved.")
            }
        }
    }

    fun reviewHandover(id: String, reviewerRole: String, note: String, onComplete: (Boolean, String) -> Unit) {
        if (note.length > 1000) {
            onComplete(false, "Review note must be 1,000 characters or fewer.")
            return
        }
        viewModelScope.launch {
            try {
                repository.reviewHandover(id, reviewerRole, note)
                onComplete(true, "Review entry recorded; it is not certification of safe disposal.")
            } catch (error: Exception) {
                onComplete(false, error.message ?: "The review could not be recorded.")
            }
        }
    }
}
