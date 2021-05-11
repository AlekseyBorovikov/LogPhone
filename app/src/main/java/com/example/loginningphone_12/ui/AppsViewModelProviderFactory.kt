package com.example.loginningphone_12.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginningphone_12.repository.AppsRepository
import com.example.loginningphone_12.ui.view_models.AppsViewModel

class AppsViewModelProviderFactory(
        private val appsRepository: AppsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppsViewModel(appsRepository) as T
    }
}