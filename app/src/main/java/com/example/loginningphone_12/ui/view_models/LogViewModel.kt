package com.example.loginningphone_12.ui.view_models

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginningphone_12.LogApplication
import com.example.loginningphone_12.models.App
import com.example.loginningphone_12.models.AppsList
import com.example.loginningphone_12.repository.LogRepository
import kotlinx.coroutines.launch
import java.util.*

class LogViewModel(
        app: Application,
        val logRepository: LogRepository
) : AndroidViewModel(app) {
    val apps: MutableLiveData<AppsList> = MutableLiveData()

    init {
        getAppsToday()
    }

    fun getAppsToday() = viewModelScope.launch {
        val usageApps = logRepository.getUsageAppsList(getApplication<LogApplication>())
        val today = Calendar.getInstance()
        today.time = Date()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        Log.d("TIME", "${today.time.time}")
        usageApps.appsList.forEach { usageApp ->
            val dbApp = logRepository.getAppTodayByName(today.time.time, usageApp.appName)
            dbApp?.let { app ->
                if (app.lastForegroundValue < usageApp.lastForegroundValue){
                    app.usageDuration += (usageApp.lastForegroundValue - app.lastForegroundValue)
                } else if(app.lastForegroundValue > usageApp.lastForegroundValue){
                    app.usageDuration += app.lastForegroundValue
                }
                app.lastForegroundValue = usageApp.lastForegroundValue
                logRepository.updateApp(app)
            } ?: logRepository.addApp(App(usageApp.appIcon, usageApp.appName, 0, usageApp.lastForegroundValue))
        }

        val list = logRepository.getAllByCreated(today.time.time)
        list.let{ apps.postValue(AppsList(it, today.time.time)) }
    }

    fun saveApp(app: App) = viewModelScope.launch {
        logRepository.addApp(app)
    }

    fun getSavedApps() = viewModelScope.launch {
        apps.postValue(AppsList(logRepository.getAllApps(), 0))
    }

    fun deleteApp(app: App) = viewModelScope.launch {
        logRepository.deleteApp(app)
    }
//    private fun handleAppsResponse(usageStats: List<UsageStats>): List<App>{
//
//    }
}