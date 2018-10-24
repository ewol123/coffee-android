package com.example.x.coffeetime.application.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.x.coffeetime.application.model.Coffee
import com.example.x.coffeetime.application.model.Token
import java.util.concurrent.Executors

@Database(
        entities = [Token::class, Coffee::class],
        version = 1,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tokenDao(): TokenDao
    abstract fun coffeeDao(): CoffeeDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context
        ): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "coffee_app")
                        .addCallback(DatabaseCallback())
                        .build()



        private class DatabaseCallback(

        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    Executors.newSingleThreadExecutor().execute {
                        database.coffeeDao().delete()
                     }
                    }
                }



            }
        }



    }
