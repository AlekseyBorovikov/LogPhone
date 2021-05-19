package com.example.loginningphone_12.services

import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.loginningphone_12.MainActivity
import com.example.loginningphone_12.R
import com.example.loginningphone_12.db.AppDao
import com.example.loginningphone_12.db.LogDatabase
import com.example.loginningphone_12.db.NotificationDao
import com.example.loginningphone_12.util.Constants
import com.example.loginningphone_12.util.FormatImg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationService: NotificationListenerService() {

    private lateinit var notificationManager: NotificationManager
    private val TAG: String = this.javaClass.simpleName
    private lateinit var dao: NotificationDao
    companion object{
        var is_running = false
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val db = LogDatabase(this)
        dao = db.getNotificationsDao()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.notification?.let { notification ->
            val appName = sbn.packageName.toString()
            val title = notification.extras.getString(Notification.EXTRA_TITLE)
            val text = notification.extras.getString(Notification.EXTRA_TEXT)
            val icon = this.applicationContext.packageManager.getApplicationIcon(appName)
            val img = FormatImg.drawableToBitmap(icon)
            val dbNotification = com.example.loginningphone_12.models.Notification(appName, title, text, img)
            GlobalScope.launch(Dispatchers.IO) {
                dao.add(dbNotification)
            }
            Log.d("Notification_created", packageName.toString())
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d("Notification_removed", sbn?.packageName.toString() + "\t" + sbn?.notification?.tickerText)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { serviceIntent ->
            when(serviceIntent.action){
                Constants.ACTION.START_ACTION -> {
                    Log.d(TAG, "start foreground intent")
                    startForeground(Constants.INTENT_NOTIFICATION_ID, prepareNotification())
                    is_running = true
                }
                Constants.ACTION.STOP_ACTION -> {
                    Log.d(TAG, "stop foreground intent")
                    stopForeground(true)
                    stopSelf()
                    is_running = false
                }
                else -> {
                    Log.d(TAG, "default")
//                    stopForeground(true)
//                    stopSelf()
                }
            }
        } ?:let {
            Log.d(TAG, "stop foreground intent")
            stopForeground(true)
            stopSelf()
            is_running = false
        }

        return START_NOT_STICKY
    }

    private fun prepareNotification(): Notification{
        if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                notificationManager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID) == null
        ){
            val serviceChannel: NotificationChannel = NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL_ID,
                    Constants.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            ).apply { enableVibration(false) }

            notificationManager.createNotificationChannel(serviceChannel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
        } else {
            NotificationCompat.Builder(this)
        }.apply {
            setContentTitle("Example service")
            setContentText("Text")
            setSmallIcon(R.drawable.ic_baseline_notifications_24)
            setContentIntent(pendingIntent)
        }.build()
    }

    override fun onDestroy() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE)
        } else{
            stopForeground(true)
        }
        Log.d("FOREGROUND_SERVICE", "stop")
        super.onDestroy()
    }
}