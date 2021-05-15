package com.example.loginningphone_12.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.loginningphone_12.db.LogDatabase
import com.example.loginningphone_12.models.App
import com.example.loginningphone_12.models.AppsList
import com.example.loginningphone_12.tools.UStats
import java.util.*

class LogRepository(
    val db: LogDatabase
) {
    fun getUsageAppsList(context: Context): AppsList = UStats().getUsageStatsList(context)

    suspend fun addApp(app: App) = db.getLogDao().add(app)

    suspend fun deleteApp(app: App) = db.getLogDao().delete(app)

    suspend fun updateApp(app: App) = db.getLogDao().update(app)

    suspend fun getAllApps() = db.getLogDao().getAll()

    suspend fun getAllByCreated(created: Long) = db.getLogDao().getAllByCreated(created)

    suspend fun getAppTodayByName(created: Long, appName: String): App? =
            db.getLogDao().getTodayByName(created, appName)
}