package com.example.loginningphone_12.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.loginningphone_12.models.App

@Database(
    entities = [App::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class LogDatabase : RoomDatabase() {

    abstract fun getLogDao(): AppDao

    companion object{
        @Volatile
        private var instance: LogDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                LogDatabase::class.java,
                "log_db.db"
            ).build()
    }
}