package ug.smartmed.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WasteBatchDao {
    @Query("SELECT * FROM waste_batches ORDER BY createdAtEpochMillis DESC")
    fun observeAll(): Flow<List<WasteBatch>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(batch: WasteBatch)

    @Query("SELECT COUNT(*) FROM waste_batches")
    suspend fun count(): Int
}
