package com.example.loginningphone_12.repository

import android.content.Context
import com.example.loginningphone_12.models.AppsList
import com.example.loginningphone_12.tools.UStats

class AppsRepository {
    fun getAppsList(context: Context): AppsList = UStats().getUsageStatsList(context)
}