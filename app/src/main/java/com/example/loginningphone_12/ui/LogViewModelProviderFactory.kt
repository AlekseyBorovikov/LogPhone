package com.example.loginningphone_12.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginningphone_12.repository.LogRepository
import com.example.loginningphone_12.ui.view_models.LogViewModel

class LogViewModelProviderFactory(
        val app: Application,
        private val logRepository: LogRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LogViewModel(app, logRepository) as T
    }
}