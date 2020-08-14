package com.samriddha.weatherforecastapp.data.local

import androidx.room.TypeConverter
import com.samriddha.weatherforecastapp.pojo.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class ObjectListTypeConverter {

    @TypeConverter
    fun fromCountryLangList(countryLang: List<Weather?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather?>?>() {}.getType()
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toCountryLangList(countryLangString: String?): List<Weather>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather?>?>() {}.getType()
        return gson.fromJson<List<Weather>>(countryLangString, type)
    }
}