package com.currentdetection.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.currentdetection.data.local.entities.NetworkEntity
import com.currentdetection.data.local.entities.PowerEventEntity

@Database(entities = [NetworkEntity::class, PowerEventEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun networkDao(): NetworkDao
    abstract fun powerEventDao(): PowerEventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "current_detection_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
