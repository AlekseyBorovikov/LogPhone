package com.example.loginningphone_12

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.loginningphone_12.repository.AppsRepository
import com.example.loginningphone_12.ui.AppsViewModelProviderFactory
import com.example.loginningphone_12.ui.view_models.AppsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: AppsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appsRepository = AppsRepository()
        val viewModelProviderFactory = AppsViewModelProviderFactory(appsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(AppsViewModel::class.java)

        val newsNavHostFragment = supportFragmentManager.findFragmentById(R.id.navFragment) as NavHostFragment
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

        viewModel.getAppsToday(this)

    }
}