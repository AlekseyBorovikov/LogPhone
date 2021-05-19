package com.example.loginningphone_12.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.loginningphone_12.receivers.ProcessReceiver
import com.example.loginningphone_12.util.Constants
import com.example.loginningphone_12.util.FormatStrings

class ProcessService(private val context: Context) {

    private val TAG = "ProcessService"

    private val alarmManager: AlarmManager? =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    fun setAlarm(timeInMillis: Long){
        setAlarm(
            timeInMillis,
            getPendingIntent(
                Intent(context, ProcessReceiver::class.java).apply {
                    action = Constants.ACTION.APPS_SERVICE
                }
            )
        )
    }

    fun setRepetitiveAlarm(timeInMillis: Long){
        setRepeating(
            timeInMillis,
            getPendingIntent(
                Intent(context, ProcessReceiver::class.java).apply {
                    action = Constants.ACTION.APPS_SERVICE
                }
            )
        )
    }

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent){
        alarmManager?.let {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                it.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }else{
                it.set(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
            Log.e(TAG, "set time: ${FormatStrings.formatDate(timeInMillis)}")
        }
    }

    private fun setRepeating(timeInMillis: Long, pendingIntent: PendingIntent){
        alarmManager?.let {
            alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                timeInMillis + Constants.REPEAT_SECONDS,
                Constants.REPEAT_SECONDS,
                pendingIntent
        )
        }
        Log.e(TAG, "set repeating: ${FormatStrings.formatDate(timeInMillis)}")
    }

    private fun getIntent() = Intent(context, ProcessReceiver::class.java)

    private fun getPendingIntent(intent: Intent) =
        PendingIntent.getBroadcast(
                context,
                Constants.INTENT_PROCESS_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
}