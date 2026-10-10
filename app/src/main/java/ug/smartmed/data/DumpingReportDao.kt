package ug.smartmed.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DumpingReportDao {
    @Query("SELECT * FROM dumping_reports ORDER BY reportedAtEpochMillis DESC")
    fun observeAll(): Flow<List<DumpingReport>>

    @Insert
    suspend fun insert(report: DumpingReport)
}
