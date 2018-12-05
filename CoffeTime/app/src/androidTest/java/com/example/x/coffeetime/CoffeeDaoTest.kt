package com.example.x.coffeetime

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.model.Coffee
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
open class CoffeeDaoTest {

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
    fun insertCoffeeData() {
        var cachedCoffees = mutableListOf<Coffee>()
        var coffee = Coffee(1,"Americano","none","12","Nincs",3)
        cachedCoffees.add(coffee)

        appDatabase.coffeeDao().insert(cachedCoffees)
        val coffeesByName = appDatabase.coffeeDao().coffeesByName("Americano")
        val coffees = appDatabase.coffeeDao().coffeesById(1)
        assert(coffees.value != null)
        assert(!coffeesByName.create().isInvalid)
    }



    @Test
    fun clearBufferoosClearsData() {
        var cachedCoffees = mutableListOf<Coffee>()
        var coffee = Coffee(1,"Americano","none","12","Nincs",3)
        cachedCoffees.add(coffee)

        appDatabase.coffeeDao().insert(cachedCoffees)

        appDatabase.coffeeDao().delete()
        assert(appDatabase.coffeeDao().coffeesById(1).value.isNullOrEmpty())
    }

}
