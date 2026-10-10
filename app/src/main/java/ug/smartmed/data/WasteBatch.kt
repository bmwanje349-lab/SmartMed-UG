package ug.smartmed.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "waste_batches")
data class WasteBatch(
    @PrimaryKey val id: String,
    val category: String,
    val quantity: Double,
    val unit: String,
    val collectionPoint: String,
    val createdAtEpochMillis: Long,
    val recordStatus: String = "RECORDED",
    @ColumnInfo(defaultValue = "'UNASSIGNED'") val facilityName: String = "UNASSIGNED",
    @ColumnInfo(defaultValue = "''") val subcategory: String = "",
    @ColumnInfo(defaultValue = "'UNASSIGNED'") val recordedByRole: String = "UNASSIGNED",
    @ColumnInfo(defaultValue = "'device-local-user'") val recordedByLocalId: String = "device-local-user",
    @ColumnInfo(defaultValue = "0") val updatedAtEpochMillis: Long = 0L,
    @ColumnInfo(defaultValue = "0") val isArchived: Boolean = false,
    @ColumnInfo(defaultValue = "'PENDING'") val syncState: String = "PENDING",
    @ColumnInfo(defaultValue = "1") val revision: Int = 1,
    @ColumnInfo(defaultValue = "''") val notes: String = ""
)
