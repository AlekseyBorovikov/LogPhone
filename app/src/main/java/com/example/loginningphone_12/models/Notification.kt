package com.example.loginningphone_12.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
        tableName = "notifications"
)
data class Notification(
        val appName: String,
        val title: String?,
        val text: String?,
        val notifIcon: Bitmap?,
        val created: Date? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}