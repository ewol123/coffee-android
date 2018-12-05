package com.example.x.coffeetime

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.model.Cart
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
open class CartDaoTest {

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
    fun insertCartData() {
        var cachedCart = mutableListOf<Cart>()
        var cart = Cart(1,"Americano",
                "nincs",
                "5",
                "valami",
                2,1,1,1)
        cachedCart.add(cart)

        appDatabase.cartDao().insert(cachedCart)
        val cartItem = appDatabase.cartDao().getOrders()
        assert(cartItem.value != null)

    }



    @Test
    fun clearCartData() {
        var cachedCart = mutableListOf<Cart>()
        var cart = Cart(1,"Americano",
                "nincs",
                "5",
                "valami",
                2,1,1,1)
        cachedCart.add(cart)

        appDatabase.cartDao().insert(cachedCart)

        appDatabase.cartDao().delete()
        assert(appDatabase.cartDao().getOrders().value.isNullOrEmpty())
    }

}
