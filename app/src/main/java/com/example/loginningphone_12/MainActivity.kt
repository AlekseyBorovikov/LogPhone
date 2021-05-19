package com.example.loginningphone_12

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.loginningphone_12.db.LogDatabase
import com.example.loginningphone_12.receivers.ProcessReceiver
import com.example.loginningphone_12.repository.LogRepository
import com.example.loginningphone_12.services.ProcessService
import com.example.loginningphone_12.ui.LogViewModelProviderFactory
import com.example.loginningphone_12.ui.view_models.LogViewModel
import com.example.loginningphone_12.util.Constants
import com.example.loginningphone_12.util.FormatStrings
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: LogViewModel
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appsRepository = LogRepository(LogDatabase(this))
        val viewModelProviderFactory = LogViewModelProviderFactory(application, appsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(LogViewModel::class.java)

        val newsNavHostFragment = supportFragmentManager.findFragmentById(R.id.navFragment) as NavHostFragment
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

        val intent2check = Intent(this, ProcessReceiver::class.java)
        val pendingIntent = PendingIntent
                .getBroadcast(this, Constants.INTENT_PROCESS_ID, intent2check, PendingIntent.FLAG_NO_CREATE)
        if (pendingIntent == null) {
            createRepeatingService()
            Log.e(TAG, "Create alarm")
        }
    }

    private fun createRepeatingService(){
        var time = SystemClock.elapsedRealtime()
        val ctime = Calendar.getInstance()
        ctime.add(Calendar.HOUR_OF_DAY, 1)
        ctime.set(Calendar.MINUTE, 0)
        ctime.set(Calendar.SECOND, 0)
        ctime.set(Calendar.MILLISECOND, 0)
        val curretnCal = Calendar.getInstance()
        time += (ctime.time.time - curretnCal.time.time)
        Log.e(TAG, "set time: ${FormatStrings.formatDate(time)}")
        ProcessService(this).setRepetitiveAlarm(time)
    }
}