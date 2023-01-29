package com.example.cardictionary.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Suit::class], version = 1, exportSchema = false)
abstract class SuitDatabase : RoomDatabase() {
    abstract fun suitDao(): SuitDao

    companion object {
        private var INSTANCE: SuitDatabase? = null
        fun getInstance(context: Context): SuitDatabase {
            if (INSTANCE != null) {
                return INSTANCE!!
            }
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            Room.databaseBuilder(context, SuitDatabase::class.java, "suit_db")
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}