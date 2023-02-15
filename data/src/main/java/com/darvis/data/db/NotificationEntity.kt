package com.darvis.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(

    @ColumnInfo(name = "tier") val tier: String,
    @ColumnInfo(name = "tierStatus") val tierStatus: String,
    @ColumnInfo(name = "dwell_time") val dwellTime: String,
    @ColumnInfo(name = "time_stamp") val timeStamp: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}