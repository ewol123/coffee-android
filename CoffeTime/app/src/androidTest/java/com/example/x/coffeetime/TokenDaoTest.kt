package com.example.x.coffeetime

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.model.Token
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
open class TokenDaoTest {

    private lateinit var appDatabase: AppDatabase

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).build()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun insertToken() {
        var token = Token("k3k563lgldgsé4ekzk")

        appDatabase.tokenDao().insert(token)
        val tokenList = appDatabase.tokenDao().getToken()
        assert(tokenList.value != null)
    }



    @Test
    fun clearToken() {
        var token = Token("k3k563lgldgsé4ekzk")

        appDatabase.tokenDao().insert(token)

        appDatabase.tokenDao().deleteAll()
        assert(appDatabase.tokenDao().getToken().value.isNullOrEmpty())
    }

}
