package com.example.capstone

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class Converters {
    @TypeConverter
    fun fromString(value: String?): MutableList<String?>? {
        val listType: Type = object : TypeToken<MutableList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayLisr(list: MutableList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}