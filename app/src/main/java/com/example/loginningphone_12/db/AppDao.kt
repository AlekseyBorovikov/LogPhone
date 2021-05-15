package com.example.loginningphone_12.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.loginningphone_12.models.App
import java.util.Date

@Dao
interface AppDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(app: App)

    @Delete
    suspend fun delete(app: App)

    @Update
    suspend fun update(app: App)

    @Query("select * from apps")
    suspend fun getAll(): List<App>

    @Query("SELECT * FROM apps WHERE created=:created and usageDuration > 0")
    suspend fun getAllByCreated(created: Long): List<App>

    @Query("SELECT * FROM apps WHERE created=:created and appName=:appName")
    suspend fun getTodayByName(created: Long, appName: String): App?
}