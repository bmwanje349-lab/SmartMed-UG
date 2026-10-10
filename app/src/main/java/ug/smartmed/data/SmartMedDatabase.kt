package ug.smartmed.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [WasteBatch::class, BatchAuditEvent::class, WasteHandover::class, DumpingReport::class],
    version = 3,
    exportSchema = false
)
abstract class SmartMedDatabase : RoomDatabase() {
    abstract fun wasteBatchDao(): WasteBatchDao
    abstract fun batchAuditDao(): BatchAuditDao
    abstract fun wasteHandoverDao(): WasteHandoverDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE waste_batches ADD COLUMN facilityName TEXT NOT NULL DEFAULT 'UNASSIGNED'")
                db.execSQL("ALTER TABLE waste_batches ADD COLUMN subcategory TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE waste_batches ADD COLUMN recordedByRole TEXT NOT NULL DEFAULT 'UNASSIGNED'")
                db.execSQL("ALTER TABLE waste_batches ADD COLUMN recordedByLocalId TEXT NOT NULL DEFAULT 'device-local-user'")
                db.execSQL("ALTER TABLE waste_batches ADD COLUMN updatedAtEpochMillis INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE waste_batches ADD COLUMN isArchived INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE waste_batches ADD COLUMN syncState TEXT NOT NULL DEFAULT 'PENDING'")
                db.execSQL("ALTER TABLE waste_batches ADD COLUMN revision INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE waste_batches ADD COLUMN notes TEXT NOT NULL DEFAULT ''")

                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS batch_audit_events (
                        eventId TEXT NOT NULL,
                        batchId TEXT NOT NULL,
                        eventType TEXT NOT NULL,
                        timestampEpochMillis INTEGER NOT NULL,
                        actorRole TEXT NOT NULL,
                        reason TEXT NOT NULL,
                        oldSnapshot TEXT NOT NULL,
                        newSnapshot TEXT NOT NULL,
                        PRIMARY KEY(eventId)
                    )""".trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_batch_audit_events_batchId ON batch_audit_events(batchId)")

                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS waste_handovers (
                        id TEXT NOT NULL,
                        batchId TEXT NOT NULL,
                        senderRole TEXT NOT NULL,
                        receiverRole TEXT NOT NULL,
                        timestampEpochMillis INTEGER NOT NULL,
                        evidenceReference TEXT NOT NULL DEFAULT '',
                        notes TEXT NOT NULL DEFAULT '',
                        evidenceStatus TEXT NOT NULL DEFAULT 'AWAITING_EVIDENCE',
                        syncState TEXT NOT NULL DEFAULT 'PENDING',
                        reviewedByRole TEXT,
                        reviewedAtEpochMillis INTEGER,
                        reviewNote TEXT NOT NULL DEFAULT '',
                        PRIMARY KEY(id),
                        FOREIGN KEY(batchId) REFERENCES waste_batches(id)
                            ON UPDATE NO ACTION ON DELETE RESTRICT
                    )""".trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_waste_handovers_batchId ON waste_handovers(batchId)")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS dumping_reports (
                        id TEXT NOT NULL,
                        description TEXT NOT NULL,
                        locationDescription TEXT NOT NULL,
                        observedAtEpochMillis INTEGER NOT NULL,
                        reportedAtEpochMillis INTEGER NOT NULL,
                        reporterRole TEXT NOT NULL,
                        status TEXT NOT NULL DEFAULT 'UNVERIFIED_LOCAL',
                        referralReference TEXT NOT NULL DEFAULT '',
                        followUpNotes TEXT NOT NULL DEFAULT '',
                        syncState TEXT NOT NULL DEFAULT 'PENDING',
                        PRIMARY KEY(id)
                    )""".trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_dumping_reports_reportedAtEpochMillis ON dumping_reports(reportedAtEpochMillis)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_dumping_reports_status ON dumping_reports(status)")
            }
        }

        @Volatile
        private var INSTANCE: SmartMedDatabase? = null

        fun getInstance(context: Context): SmartMedDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SmartMedDatabase::class.java,
                    "smartmed-local.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
