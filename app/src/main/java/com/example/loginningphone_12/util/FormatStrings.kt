package com.example.loginningphone_12.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class FormatStrings {

    companion object{

        @SuppressLint("SimpleDateFormat")
        fun formatDate(timesInMillis: Long, format: String = "yyyy.MM.dd"): String{
            val dateFormat = SimpleDateFormat(format)
            return dateFormat.format(timesInMillis)
        }

        /**
         * helper method to get string in format hh:mm:ss from miliseconds
         *
         * @param millis (application time in foreground)
         * @return string in format hh:mm:ss from miliseconds
         */
        fun getDurationBreakdown(millis: Long): String {
            var millis = millis
            require(millis >= 0) { "Duration must be greater than zero!" }
            val hours: Long = TimeUnit.MILLISECONDS.toHours(millis)
            millis -= TimeUnit.HOURS.toMillis(hours)
            val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millis)
            millis -= TimeUnit.MINUTES.toMillis(minutes)
            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(millis)
            return "$hours h $minutes m $seconds s"
        }
    }

}