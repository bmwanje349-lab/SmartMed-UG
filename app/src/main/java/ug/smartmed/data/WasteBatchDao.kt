package ug.smartmed.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WasteBatchDao {
    @Query("SELECT * FROM waste_batches WHERE isArchived = 0 ORDER BY createdAtEpochMillis DESC")
    fun observeActive(): Flow<List<WasteBatch>>

    @Query("SELECT * FROM waste_batches ORDER BY createdAtEpochMillis DESC")
    fun observeAllForInternalUse(): Flow<List<WasteBatch>>

    @Query("SELECT * FROM waste_batches WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): WasteBatch?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(batch: WasteBatch)

    @Update
    suspend fun update(batch: WasteBatch)

    @Query("SELECT COUNT(*) FROM waste_batches WHERE isArchived = 0")
    suspend fun countActive(): Int
}
