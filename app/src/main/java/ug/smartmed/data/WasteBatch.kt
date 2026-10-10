package ug.smartmed.data

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
    val recordStatus: String = "RECORDED"
)
