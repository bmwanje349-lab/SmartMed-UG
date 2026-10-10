package ug.smartmed.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "waste_handovers",
    foreignKeys = [
        ForeignKey(
            entity = WasteBatch::class,
            parentColumns = ["id"],
            childColumns = ["batchId"],
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["batchId"])]
)
data class WasteHandover(
    @PrimaryKey val id: String,
    val batchId: String,
    val senderRole: String,
    val receiverRole: String,
    val timestampEpochMillis: Long,
    @ColumnInfo(defaultValue = "''") val evidenceReference: String = "",
    @ColumnInfo(defaultValue = "''") val notes: String = "",
    @ColumnInfo(defaultValue = "''") val transporterOrganisation: String = "",
    @ColumnInfo(defaultValue = "''") val transportManifestReference: String = "",
    @ColumnInfo(defaultValue = "'AWAITING_EVIDENCE'") val evidenceStatus: String = "AWAITING_EVIDENCE",
    @ColumnInfo(defaultValue = "'PENDING'") val syncState: String = "PENDING",
    val reviewedByRole: String? = null,
    val reviewedAtEpochMillis: Long? = null,
    @ColumnInfo(defaultValue = "''") val reviewNote: String = ""
)
