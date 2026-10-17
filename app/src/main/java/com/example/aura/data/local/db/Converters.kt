package com.example.aura.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String = gson.toJson(value.orEmpty())

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromStringMap(value: Map<String, String>?): String = gson.toJson(value.orEmpty())

    @TypeConverter
    fun toStringMap(value: String?): Map<String, String> {
        if (value.isNullOrBlank()) return emptyMap()
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, mapType)
    }
}
