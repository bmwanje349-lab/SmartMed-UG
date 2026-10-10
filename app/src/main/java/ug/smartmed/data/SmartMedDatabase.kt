package ug.smartmed.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WasteBatch::class], version = 1, exportSchema = false)
abstract class SmartMedDatabase : RoomDatabase() {
    abstract fun wasteBatchDao(): WasteBatchDao

    companion object {
        @Volatile private var INSTANCE: SmartMedDatabase? = null

        fun getInstance(context: Context): SmartMedDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SmartMedDatabase::class.java,
                    "smartmed-local.db"
                ).build().also { INSTANCE = it }
            }
    }
}
