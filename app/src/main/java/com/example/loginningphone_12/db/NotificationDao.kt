package com.example.loginningphone_12.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.loginningphone_12.models.App
import com.example.loginningphone_12.models.Notification

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(notification: Notification)

    @Delete
    suspend fun delete(app: App)

    @Update
    suspend fun update(app: App)

    @Query("select * from notifications")
    fun getAll(): LiveData<List<Notification>>

}