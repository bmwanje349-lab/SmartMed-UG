package ug.smartmed.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dumping_reports",
    indices = [
        Index(value = ["reportedAtEpochMillis"]),
        Index(value = ["status"])
    ]
)
data class DumpingReport(
    @PrimaryKey val id: String,
    val description: String,
    val locationDescription: String,
    val observedAtEpochMillis: Long,
    val reportedAtEpochMillis: Long,
    val reporterRole: String,
    @ColumnInfo(defaultValue = "'UNVERIFIED_LOCAL'") val status: String = "UNVERIFIED_LOCAL",
    @ColumnInfo(defaultValue = "''") val referralReference: String = "",
    @ColumnInfo(defaultValue = "''") val followUpNotes: String = "",
    @ColumnInfo(defaultValue = "'PENDING'") val syncState: String = "PENDING"
)
