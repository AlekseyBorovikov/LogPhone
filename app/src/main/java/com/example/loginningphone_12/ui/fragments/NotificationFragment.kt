package com.example.loginningphone_12.ui.fragments

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.loginningphone_12.R
import com.example.loginningphone_12.databinding.NotificationFragmentBinding
import com.example.loginningphone_12.receivers.ProcessReceiver
import com.example.loginningphone_12.services.ProcessService
import com.example.loginningphone_12.util.Constants
import com.example.loginningphone_12.util.FormatStrings


class NotificationFragment: Fragment(R.layout.notification_fragment) {
    private var _binding: NotificationFragmentBinding ?= null
    private val binding get() = _binding!!
    private val TAG = "NotificationFragment"

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotificationFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notificationBut.setOnClickListener {
            activity?.let { activityContext ->
                val alarmManager = activityContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent2check = Intent(activityContext, ProcessReceiver::class.java)
                intent2check.setAction(Constants.ACTION_PROCESS_SERVICE)
                val pendingIntent = PendingIntent
                    .getService(activityContext, 101, intent2check, PendingIntent.FLAG_NO_CREATE)
                if (pendingIntent != null) {
                    //TODO КАК ОСТАНОВИТЬ?!!!
                    alarmManager.cancel(pendingIntent)
                    Log.e(TAG, "Cancel alarm")
                }else{
                    createRepeatingService()
                    Log.e(TAG, "Create alarm")
                }
            }
        }
    }

    private fun createRepeatingService(){
        activity?.let { context ->
            val time = SystemClock.elapsedRealtime()
            Log.e(TAG, "set time: ${FormatStrings.formatDate(time)}")
            ProcessService(context).setRepetitiveAlarm(time)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}