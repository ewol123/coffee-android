package com.example.x.coffeetime.application.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.x.coffeetime.application.model.Token
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch

@Database(
        entities = [Token::class],
        version = 1,
        exportSchema = false
)
abstract class appDatabase : RoomDatabase() {

    abstract fun tokenDao(): TokenDao

    companion object {

        @Volatile
        private var INSTANCE: appDatabase? = null

        fun getInstance(context: Context
        ): appDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        appDatabase::class.java, "coffee_app")
                        .build()






    }

}