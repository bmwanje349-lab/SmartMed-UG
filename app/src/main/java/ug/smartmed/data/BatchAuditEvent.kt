package ug.smartmed.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "batch_audit_events",
    indices = [Index(value = ["batchId"])]
)
data class BatchAuditEvent(
    @PrimaryKey val eventId: String,
    val batchId: String,
    val eventType: String,
    val timestampEpochMillis: Long,
    val actorRole: String,
    val reason: String,
    val oldSnapshot: String,
    val newSnapshot: String
)
