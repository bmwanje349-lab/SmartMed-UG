package ug.smartmed.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WasteOutcomeDao {
    @Query("SELECT * FROM waste_outcomes ORDER BY performedAtEpochMillis DESC")
    fun observeAll(): Flow<List<WasteOutcome>>

    @Query("SELECT * FROM waste_outcomes WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): WasteOutcome?

    @Insert
    suspend fun insert(outcome: WasteOutcome)

    @Update
    suspend fun update(outcome: WasteOutcome)

    @Query("SELECT * FROM waste_outcomes WHERE syncState = 'PENDING' ORDER BY recordedAtEpochMillis ASC")
    suspend fun getPendingForSync(): List<WasteOutcome>
}
