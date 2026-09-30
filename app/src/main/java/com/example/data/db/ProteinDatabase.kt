package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserProfileEntity::class, MistakeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProteinDatabase : RoomDatabase() {
    abstract fun proteinDao(): ProteinDao

    companion object {
        @Volatile
        private var INSTANCE: ProteinDatabase? = null

        fun getDatabase(context: Context): ProteinDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProteinDatabase::class.java,
                    "protein_builder.db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
