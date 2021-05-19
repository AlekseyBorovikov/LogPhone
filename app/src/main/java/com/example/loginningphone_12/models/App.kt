package com.example.loginningphone_12.models

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
        tableName = "apps"
)
data class App(
    val appIcon: Bitmap,
    val appName: String,
    var usageDuration: Long,
    var lastForegroundValue: Long,
    val created: Date? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}