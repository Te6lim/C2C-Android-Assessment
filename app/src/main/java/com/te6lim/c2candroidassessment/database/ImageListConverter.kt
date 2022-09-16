package com.te6lim.c2candroidassessment.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class ImageListConverter {

    @TypeConverter
    fun List<String>?.toJson(): String {
        return Gson().toJson(this)
    }

    @TypeConverter
    fun String?.toList(): List<String> {
        return Gson().fromJson(this, object : TypeToken<List<String>?>() {}.type)
    }
}