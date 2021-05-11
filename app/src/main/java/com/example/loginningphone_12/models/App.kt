package com.example.loginningphone_12.models

import android.graphics.drawable.Drawable

class App(appIcon: Drawable, appName: String, usagePercentage: Int, usageDuration: String) {
    var appIcon: Drawable = appIcon
    var appName: String = appName
    var usagePercentage: Int = usagePercentage
    var usageDuration: String = usageDuration
}