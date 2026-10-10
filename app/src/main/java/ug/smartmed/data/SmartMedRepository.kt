package ug.smartmed.data

import androidx.room.withTransaction
import java.util.UUID
import kotlinx.coroutines.flow.Flow

class SmartMedRepository(private val database: SmartMedDatabase) {
    private val batches = database.wasteBatchDao()
    private val audits = database.batchAuditDao()
    private val handovers = database.wasteHandoverDao()
    private val dumpingReports = database.dumpingReportDao()

    fun observeActiveBatches(): Flow<List<WasteBatch>> = batches.observeActive()
    fun observeHandovers(): Flow<List<WasteHandover>> = handovers.observeAll()
    fun observeRecentAuditEvents(): Flow<List<BatchAuditEvent>> = audits.observeRecent()
    fun observeDumpingReports(): Flow<List<DumpingReport>> = dumpingReports.observeAll()

    suspend fun createBatch(draft: WasteBatchDraft): WasteBatch {
        val now = System.currentTimeMillis()
        val batch = WasteBatch(
            id = "SM-" + UUID.randomUUID().toString().replace("-", "").take(12).uppercase(),
            facilityName = draft.facilityName.trim(),
            category = draft.categoryCode,
            subcategory = draft.subcategory.trim(),
            quantity = draft.quantityText.trim().toDouble(),
            unit = draft.unit,
            collectionPoint = draft.collectionPoint.trim(),
            createdAtEpochMillis = now,
            recordStatus = "RECORDED",
            recordedByRole = draft.recorderRole,
            updatedAtEpochMillis = now,
            syncState = "PENDING",
            revision = 1,
            notes = draft.notes.trim()
        )
        database.withTransaction {
            batches.insert(batch)
            audits.insert(
                BatchAuditEvent(
                    eventId = UUID.randomUUID().toString(),
                    batchId = batch.id,
                    eventType = "CREATED",
                    timestampEpochMillis = now,
                    actorRole = draft.recorderRole,
                    reason = "Initial record",
                    oldSnapshot = "",
                    newSnapshot = snapshot(batch)
                )
            )
        }
        return batch
    }

    suspend fun updateBatch(id: String, draft: WasteBatchDraft, reason: String): WasteBatch {
        require(reason.isNotBlank()) { "Enter a reason for correcting this record." }
        val existing = requireNotNull(batches.getById(id)) { "The batch no longer exists." }
        check(!existing.isArchived) { "Archived batches cannot be edited." }
        val now = System.currentTimeMillis()
        val updated = existing.copy(
            facilityName = draft.facilityName.trim(),
            category = draft.categoryCode,
            subcategory = draft.subcategory.trim(),
            quantity = draft.quantityText.trim().toDouble(),
            unit = draft.unit,
            collectionPoint = draft.collectionPoint.trim(),
            recordedByRole = draft.recorderRole,
            updatedAtEpochMillis = now,
            syncState = "PENDING",
            revision = existing.revision + 1,
            notes = draft.notes.trim()
        )
        database.withTransaction {
            batches.update(updated)
            audits.insert(
                BatchAuditEvent(
                    eventId = UUID.randomUUID().toString(),
                    batchId = id,
                    eventType = "CORRECTED",
                    timestampEpochMillis = now,
                    actorRole = draft.recorderRole,
                    reason = reason.trim(),
                    oldSnapshot = snapshot(existing),
                    newSnapshot = snapshot(updated)
                )
            )
        }
        return updated
    }

    suspend fun archiveBatch(id: String, actorRole: String, reason: String) {
        require(reason.isNotBlank()) { "A reason is required." }
        val existing = requireNotNull(batches.getById(id)) { "The batch no longer exists." }
        check(!existing.isArchived) { "The batch is already archived." }
        val now = System.currentTimeMillis()
        val archived = existing.copy(
            isArchived = true,
            updatedAtEpochMillis = now,
            syncState = "PENDING",
            revision = existing.revision + 1
        )
        database.withTransaction {
            batches.update(archived)
            audits.insert(
                BatchAuditEvent(
                    eventId = UUID.randomUUID().toString(),
                    batchId = id,
                    eventType = "ARCHIVED",
                    timestampEpochMillis = now,
                    actorRole = actorRole,
                    reason = reason.trim(),
                    oldSnapshot = snapshot(existing),
                    newSnapshot = snapshot(archived)
                )
            )
        }
    }

    suspend fun recordHandover(
        batchId: String,
        senderRole: String,
        receiverRole: String,
        evidenceReference: String,
        notes: String
    ): WasteHandover {
        require(senderRole in SmartMedOptions.authorisedRoles) { "Choose a valid sender role." }
        require(receiverRole in SmartMedOptions.authorisedRoles) { "Choose a valid receiver role." }
        val batch = requireNotNull(batches.getById(batchId)) { "Choose an existing batch." }
        check(!batch.isArchived) { "Archived batches cannot receive new handovers." }
        val now = System.currentTimeMillis()
        val reference = evidenceReference.trim()
        val handover = WasteHandover(
            id = "HO-" + UUID.randomUUID().toString().replace("-", "").take(12).uppercase(),
            batchId = batchId,
            senderRole = senderRole,
            receiverRole = receiverRole,
            timestampEpochMillis = now,
            evidenceReference = reference,
            notes = notes.trim(),
            evidenceStatus = if (reference.isBlank()) "AWAITING_EVIDENCE" else "PRESENT_UNREVIEWED",
            syncState = "PENDING"
        )
        database.withTransaction {
            handovers.insert(handover)
            audits.insert(
                BatchAuditEvent(
                    eventId = UUID.randomUUID().toString(),
                    batchId = batchId,
                    eventType = "HANDOVER_RECORDED",
                    timestampEpochMillis = now,
                    actorRole = senderRole,
                    reason = "Handover record created; this entry alone does not verify receipt or disposal.",
                    oldSnapshot = "",
                    newSnapshot = handoverSnapshot(handover)
                )
            )
        }
        return handover
    }

    suspend fun reviewHandover(id: String, reviewerRole: String, reviewNote: String) {
        require(reviewerRole in SmartMedOptions.authorisedRoles) { "Choose a valid reviewer role." }
        val existing = requireNotNull(handovers.getById(id)) { "The handover no longer exists." }
        require(existing.evidenceReference.isNotBlank()) { "Add a supporting-evidence reference before review." }
        require(existing.evidenceStatus != "REVIEW_RECORDED") { "A review has already been recorded." }
        val now = System.currentTimeMillis()
        val updated = existing.copy(
            evidenceStatus = "REVIEW_RECORDED",
            reviewedByRole = reviewerRole,
            reviewedAtEpochMillis = now,
            reviewNote = reviewNote.trim(),
            syncState = "PENDING"
        )
        database.withTransaction {
            handovers.update(updated)
            audits.insert(
                BatchAuditEvent(
                    eventId = UUID.randomUUID().toString(),
                    batchId = existing.batchId,
                    eventType = "EVIDENCE_REVIEW_RECORDED",
                    timestampEpochMillis = now,
                    actorRole = reviewerRole,
                    reason = "A local review was recorded. This does not certify safe treatment or final disposal.",
                    oldSnapshot = handoverSnapshot(existing),
                    newSnapshot = handoverSnapshot(updated)
                )
            )
        }
    }

    suspend fun recordDumpingReport(
        description: String,
        locationDescription: String,
        observedAtEpochMillis: Long,
        reporterRole: String,
        referralReference: String,
        followUpNotes: String
    ): DumpingReport {
        require(description.isNotBlank()) { "Describe only what was observed." }
        require(description.length <= 1500) { "Description must be 1,500 characters or fewer." }
        require(locationDescription.isNotBlank()) { "Enter a location description." }
        require(locationDescription.length <= 300) { "Location must be 300 characters or fewer." }
        require(observedAtEpochMillis > 0L && observedAtEpochMillis <= System.currentTimeMillis()) { "Enter a valid observed date." }
        require(reporterRole in SmartMedOptions.authorisedRoles) { "Choose an authorised role label." }
        require(referralReference.length <= 240) { "External reference must be 240 characters or fewer." }
        require(followUpNotes.length <= 1000) { "Follow-up notes must be 1,000 characters or fewer." }

        val report = DumpingReport(
            id = "DR-" + UUID.randomUUID().toString().replace("-", "").take(12).uppercase(),
            description = description.trim(),
            locationDescription = locationDescription.trim(),
            observedAtEpochMillis = observedAtEpochMillis,
            reportedAtEpochMillis = System.currentTimeMillis(),
            reporterRole = reporterRole,
            status = "UNVERIFIED_LOCAL",
            referralReference = referralReference.trim(),
            followUpNotes = followUpNotes.trim(),
            syncState = "PENDING"
        )
        dumpingReports.insert(report)
        return report
    }

    private fun snapshot(batch: WasteBatch): String = listOf(
        batch.id, batch.facilityName, batch.category, batch.subcategory,
        batch.quantity.toString(), batch.unit, batch.collectionPoint,
        batch.recordStatus, batch.recordedByRole, batch.isArchived.toString(),
        batch.syncState, batch.revision.toString(), batch.notes
    ).joinToString("|") { it.replace("\\", "\\\\").replace("|", "\\|").replace("\n", "\\n") }

    private fun handoverSnapshot(handover: WasteHandover): String = listOf(
        handover.id, handover.batchId, handover.senderRole, handover.receiverRole,
        handover.evidenceReference, handover.evidenceStatus,
        handover.reviewedByRole.orEmpty(), handover.reviewNote
    ).joinToString("|") { it.replace("\\", "\\\\").replace("|", "\\|").replace("\n", "\\n") }
}
