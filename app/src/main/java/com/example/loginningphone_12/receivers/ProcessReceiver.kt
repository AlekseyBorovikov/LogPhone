package com.example.loginningphone_12.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.loginningphone_12.R
import com.example.loginningphone_12.services.ProcessService
import com.example.loginningphone_12.tools.UStats
import com.example.loginningphone_12.ui.fragments.NotificationFragment
import com.example.loginningphone_12.util.Constants
import com.example.loginningphone_12.util.FormatStrings
import io.karn.notify.Notify

class ProcessReceiver: BroadcastReceiver(){
        private val TAG = "PROCESS_RECEIVER"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "is set service")
        if(intent?.action == Constants.ACTION_PROCESS_SERVICE) {
            context?.let {
                var size = 0
                var t: String = ""
                val appsList = UStats().getUsageStatsList(it)
                size = appsList.appsList.size
                t = FormatStrings.formatDate(appsList.time)
                buildNotification(it, "Received apps count: $size [datetime: $t]")
            }
        }
    }

    private fun buildNotification(context: Context, text_notif: String){
        Notify
                .with(context)
                .content {
                    title = "Update apps list"
                    text = text_notif
                }
                .show()
    }


    private fun createNotification(context: Context?){
        context?.let { serviceContext ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel = NotificationChannel(NotificationFragment.CHANNEL_ID, "Notification Title", NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = "Notification description"
                }
                val notificationManager: NotificationManager = serviceContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }


            val builder = NotificationCompat.Builder(serviceContext, NotificationFragment.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Напоминание")
                    .setContentText("Пора покормить кота")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(serviceContext)) {
                notify(NotificationFragment.NOTIFICATION_ID, builder.build()) // посылаем уведомление
            }
        }
    }
}