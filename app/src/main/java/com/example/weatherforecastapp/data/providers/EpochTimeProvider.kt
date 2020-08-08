package com.example.weatherforecastapp.data.providers

import java.text.SimpleDateFormat
import java.util.*

object EpochTimeProvider {

    fun epochToTime(str: Long?) = str?.let {
        SimpleDateFormat("HH:mm").format(Date(str*1000))
    }


    fun epochToDate(str: Long?) = str?.let {
        SimpleDateFormat("dd/MM/yyyy").format(Date(str*1000))
    }

    fun dateToEpoch(dateTime: String?) = dateTime?.let {
        SimpleDateFormat("dd/MM/yyyy HH:mm").parse(it).getTime() / 1000
    }

    fun getCurrentEpoch():Long = System.currentTimeMillis() / 1000

    fun getTimeMinus(epochTime:Long, duration:Int):Long{

        val date = Date(epochTime-(duration * 60))

        return date.time
    }
}