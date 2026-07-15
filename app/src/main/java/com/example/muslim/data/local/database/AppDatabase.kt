package com.example.muslim.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.muslim.data.local.database.dao.AlarmDao
import com.example.muslim.data.local.database.entity.AlarmEntity

@Database(entities = [AlarmEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

}
