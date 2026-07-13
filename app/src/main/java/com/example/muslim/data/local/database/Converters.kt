package com.example.muslim.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromIntSet(value: Set<Int>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toIntSet(value: String?): Set<Int>? {
        val listType = object : TypeToken<Set<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
