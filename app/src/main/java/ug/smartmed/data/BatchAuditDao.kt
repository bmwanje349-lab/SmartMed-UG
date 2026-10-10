package ug.smartmed.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BatchAuditDao {
    @Insert
    suspend fun insert(event: BatchAuditEvent)

    @Query("SELECT * FROM batch_audit_events ORDER BY timestampEpochMillis DESC LIMIT 100")
    fun observeRecent(): Flow<List<BatchAuditEvent>>
}
