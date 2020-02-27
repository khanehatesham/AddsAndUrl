package com.adsandurl.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adsandurl.dataModel.HotChildren
import com.adsandurl.dataModel.NewChildren
import com.adsandurl.database.dao.DataDao


@Database(entities = [HotChildren::class, NewChildren::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao


    companion object {
        @Volatile
        private var appDatabase: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return appDatabase ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AdsAndUrl"
                )
                    .allowMainThreadQueries().build()

                appDatabase = instance
                return@synchronized instance
            }
        }
    }
}