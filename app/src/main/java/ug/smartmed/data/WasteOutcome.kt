package ug.smartmed.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class WasteOutcomeStage(val code: String, val label: String) {
    TREATMENT("TREATMENT", "Treatment"),
    FINAL_DISPOSAL("FINAL_DISPOSAL", "Final disposal");

    companion object {
        fun fromCode(code: String): WasteOutcomeStage? = values().firstOrNull { it.code == code }
    }
}

@Entity(
    tableName = "waste_outcomes",
    foreignKeys = [
        ForeignKey(
            entity = WasteBatch::class,
            parentColumns = ["id"],
            childColumns = ["batchId"],
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["batchId"]),
        Index(value = ["stage"]),
        Index(value = ["performedAtEpochMillis"])
    ]
)
data class WasteOutcome(
    @PrimaryKey val id: String,
    val batchId: String,
    val stage: String,
    val methodOrFacility: String,
    val performedAtEpochMillis: Long,
    val recordedAtEpochMillis: Long,
    val recordedByRole: String,
    @ColumnInfo(defaultValue = "''") val evidenceReference: String = "",
    @ColumnInfo(defaultValue = "'AWAITING_EVIDENCE'") val evidenceStatus: String = "AWAITING_EVIDENCE",
    val reviewedByRole: String? = null,
    val reviewedAtEpochMillis: Long? = null,
    @ColumnInfo(defaultValue = "''") val reviewNote: String = "",
    @ColumnInfo(defaultValue = "'PENDING'") val syncState: String = "PENDING"
)
