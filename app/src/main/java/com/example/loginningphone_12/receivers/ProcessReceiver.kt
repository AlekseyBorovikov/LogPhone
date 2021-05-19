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
import com.example.loginningphone_12.db.LogDatabase
import com.example.loginningphone_12.models.App
import com.example.loginningphone_12.services.ProcessService
import com.example.loginningphone_12.tools.UStats
import com.example.loginningphone_12.ui.fragments.NotificationFragment
import com.example.loginningphone_12.util.Constants
import com.example.loginningphone_12.util.FormatStrings
import io.karn.notify.Notify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ProcessReceiver: BroadcastReceiver(){
        private val TAG = "PROCESS_RECEIVER"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "is set service")
        GlobalScope.launch(Dispatchers.IO){
            if(intent?.action == Constants.ACTION.APPS_SERVICE) {
                context?.let {
                    var adding = 0
                    var updating = 0
                    var t = ""
                    val usageApps = UStats().getUsageStatsList(it)
                    val db = LogDatabase(context)
                    val dao = db.getLogDao()

                    val today = Calendar.getInstance()
                    today.time = Date()
                    today.set(Calendar.HOUR_OF_DAY, 0)
                    today.set(Calendar.MINUTE, 0)
                    today.set(Calendar.SECOND, 0)
                    today.set(Calendar.MILLISECOND, 0)
                    Log.d("TIME", "${today.time.time}")
                    usageApps.appsList.forEach { usageApp ->
                        val dbApp = dao.getTodayByName(today.time.time, usageApp.appName)
                        dbApp?.let { app ->
                            if (app.lastForegroundValue < usageApp.lastForegroundValue){
                                app.usageDuration += (usageApp.lastForegroundValue - app.lastForegroundValue)
                                updating++
                            } else if(app.lastForegroundValue > usageApp.lastForegroundValue){
                                app.usageDuration += app.lastForegroundValue
                                updating++
                            }
                            app.lastForegroundValue = usageApp.lastForegroundValue
                            dao.update(app)
                        } ?: let{
                            var duration: Long = 0
                            if (usageApp.usageDuration <= Constants.REPEAT_SECONDS) duration = usageApp.usageDuration
                            dao.add(App(usageApp.appIcon, usageApp.appName, duration, usageApp.lastForegroundValue))
                            adding++
                        }
                    }
                    t = FormatStrings.formatDate(today.time.time)
                    buildNotification(it, "Добавлено $adding, обновлено $updating | $t")
                }
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