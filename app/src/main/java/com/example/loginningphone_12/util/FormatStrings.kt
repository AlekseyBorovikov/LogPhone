package com.example.loginningphone_12.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class FormatStrings {

    companion object{

        @SuppressLint("SimpleDateFormat")
        fun formatDate(timesInMillis: Long, format: String = "yyyy.MM.dd HH:mm"): String{
            val dateFormat = SimpleDateFormat(format)
            return dateFormat.format(timesInMillis)
        }
    }

}