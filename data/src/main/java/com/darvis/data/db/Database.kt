package com.darvis.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NotificationEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun Dao() : Dao<NotificationEntity>

    companion object {
        const val DATABASE_NAME: String = "darvis_database"
    }
}