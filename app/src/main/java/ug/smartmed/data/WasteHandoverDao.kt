package ug.smartmed.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WasteHandoverDao {
    @Query("SELECT * FROM waste_handovers ORDER BY timestampEpochMillis DESC")
    fun observeAll(): Flow<List<WasteHandover>>

    @Insert
    suspend fun insert(handover: WasteHandover)

    @Query("SELECT * FROM waste_handovers WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): WasteHandover?

    @Update
    suspend fun update(handover: WasteHandover)

    @Query("SELECT * FROM waste_handovers WHERE syncState = 'PENDING' ORDER BY timestampEpochMillis ASC")
    suspend fun getPendingForSync(): List<WasteHandover>
}
