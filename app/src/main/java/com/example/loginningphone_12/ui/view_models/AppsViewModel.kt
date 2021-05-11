package com.example.loginningphone_12.ui.view_models

import android.app.usage.UsageStats
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginningphone_12.models.App
import com.example.loginningphone_12.models.AppsList
import com.example.loginningphone_12.repository.AppsRepository
import kotlinx.coroutines.launch

class AppsViewModel(
        val appsRepository: AppsRepository
) : ViewModel() {
    val apps: MutableLiveData<AppsList> = MutableLiveData()

    fun getAppsToday(context: Context) {
        val list = appsRepository.getAppsList(context)
        apps.postValue(list)
    }

//    private fun handleAppsResponse(usageStats: List<UsageStats>): List<App>{
//
//    }
}