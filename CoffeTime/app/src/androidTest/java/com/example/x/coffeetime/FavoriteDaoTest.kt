package com.example.x.coffeetime

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.model.Favorite
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
open class FavoriteDaoTest {

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
    fun insertFavoriteData() {
        var cachedFavorites = mutableListOf<Favorite>()
        var fav = Favorite(1,"Americano","nincs","5","nincs",3)
        cachedFavorites.add(fav)

        appDatabase.favoriteDao().insert(cachedFavorites)
        val favItem = appDatabase.favoriteDao().getFavorites()
        assert(favItem.value != null)
    }



    @Test
    fun clearFavoriteData() {
        var cachedFavorites = mutableListOf<Favorite>()
        var fav = Favorite(1,"Americano","nincs","5","nincs",3)
        cachedFavorites.add(fav)

        appDatabase.favoriteDao().insert(cachedFavorites)

        appDatabase.favoriteDao().delete()
        assert(appDatabase.favoriteDao().getFavorites().value.isNullOrEmpty())
    }

}
