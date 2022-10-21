package com.preonboarding.videorecorder.util

import java.sql.Timestamp
import java.text.SimpleDateFormat

/**
 * @Created by 김현국 2022/10/18
 * @Time 3:53 PM
 */
object DateUtil {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    private val minuteAndSecondFormat = SimpleDateFormat("mm:ss")

    fun getCurrentTime(): String {
        val timeStamp = Timestamp(System.currentTimeMillis())

        return dateFormat.format(timeStamp)
    }

    fun getTime(): Long {
        val timeStamp = Timestamp(System.currentTimeMillis())

        return timeStamp.time
    }

    fun convertMinuteAndSeconds(millis: Long): String {
        val timeStamp = Timestamp(millis)

        return minuteAndSecondFormat.format(timeStamp)
    }
}
