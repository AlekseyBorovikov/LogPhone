package com.example.loginningphone_12.tools

import android.Manifest
import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Process
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import com.example.loginningphone_12.R
import com.example.loginningphone_12.models.App
import com.example.loginningphone_12.models.AppsList
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class UStats {
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("M-d-yyyy HH:mm:ss z", Locale.getDefault())

    val TAG = UStats::class.java.simpleName

    fun getUsageStatsList(context: Context): AppsList {
        val usm = getUsageStatsManager(context)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val calendar: Calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val startTime: Long = calendar.timeInMillis
        Log.d(TAG, "Range start:" + dateFormat.format(startTime))
        Log.d(TAG, "Range end: $endTime")
        val res = mutableListOf<UsageStats>()
        usm.queryUsageStats(UsageStatsManager.INTERVAL_BEST,  startTime,  endTime).forEach {
            if(it.totalTimeInForeground > 0){ res.add(it) }
        }
        val time = if(res.size > 0) res[0].firstTimeStamp else 0
        val list = createAppsList(context, res)

        return AppsList(list, time)
    }

    private fun createAppsList(context: Context, stats: List<UsageStats>): List<App> {
        val appsList: ArrayList<App> = ArrayList()
        if (stats.size > 0) {
            val mySortedMap: MutableMap<String, UsageStats> = TreeMap()
            for (usageStats in stats) {
                mySortedMap[usageStats.packageName] = usageStats
            }
            appsList.addAll(convert(mySortedMap, context))
        }
        return appsList
    }


    private fun convert(mySortedMap: Map<String, UsageStats>, context: Context): List<App> {
        //public void showAppsUsage(List<UsageStats> usageStatsList) {
        val appsList: ArrayList<App> = ArrayList()
        val usageStatsList: List<UsageStats> = ArrayList(mySortedMap.values)
        // sort the applications by time spent in foreground
        Collections.sort(usageStatsList) { z1, z2 -> java.lang.Long.compare(z1.getTotalTimeInForeground(), z2.getTotalTimeInForeground()) }

        // get total time of apps usage to calculate the usagePercentage for each app
        var totalTime: Long = 0
        usageStatsList.forEach { totalTime += it.totalTimeInForeground }

        //fill the appsList
        for (usageStats in usageStatsList) {
            try {
                usageStats.firstTimeStamp
                val packageName = usageStats.packageName
                var icon: Drawable? = AppCompatResources.getDrawable(context, R.drawable.ic_launcher_foreground)
                val packageNames = packageName.split("\\.".toRegex()).toTypedArray()
                var appName = packageNames[packageNames.size - 1].trim { it <= ' ' }
                if (isAppInfoAvailable(usageStats, context)) {
                    val ai: ApplicationInfo = context.applicationContext.packageManager.getApplicationInfo(
                            packageName,
                            0
                    )

                    appName = context.applicationContext.packageManager.getApplicationLabel(ai).toString()
//                    val usagePercentage = (usageStats.totalTimeInForeground * 100 / totalTime).toInt()

                    icon = context.applicationContext.packageManager.getApplicationIcon(ai)
                    val img = Bitmap.createBitmap(icon.intrinsicWidth, icon.intrinsicHeight, Bitmap.Config.ARGB_8888)
                    val canvas: Canvas = Canvas(img)
                    icon.setBounds(0, 0, canvas.width, canvas.height)
                    icon.draw(canvas)
                    appsList.add(App(img, appName, usageStats.totalTimeInForeground, usageStats.totalTimeInForeground))
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }

        // reverse the list to get most usage first
        appsList.reverse()
        return appsList
    }

    /**
     * check if the application info is still existing in the device / otherwise it's not possible to show app detail
     * @return true if application info is available
     */
    private fun isAppInfoAvailable(usageStats: UsageStats, context: Context): Boolean {
        return try {
            context.packageManager?.getApplicationInfo(usageStats.packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * check if PACKAGE_USAGE_STATS permission is allowed for this application
     * @return true if permission granted
     */
    private fun getGrantStatus(context: Context): Boolean {
        val appOps = context.applicationContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = context.applicationContext.packageName.let {
            appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), it)
        }
        return if (mode == AppOpsManager.MODE_DEFAULT) {
            context.applicationContext?.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
    }

    private fun getUsageStatsManager(context: Context): UsageStatsManager {
        return context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }
}