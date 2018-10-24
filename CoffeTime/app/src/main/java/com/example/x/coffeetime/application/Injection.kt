package com.example.x.coffeetime.application

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.data.CoffeeRepository
import com.example.x.coffeetime.application.db.CoffeeLocalCache
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.ui.product.ProductViewModelFactory
import java.util.concurrent.Executors


/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    private fun provideCache(context: Context): CoffeeLocalCache {
        val database = AppDatabase.getInstance(context)
        return CoffeeLocalCache(database.coffeeDao(), database.tokenDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideCoffeeRepository(context: Context): CoffeeRepository {
        return CoffeeRepository(MainService.create(), provideCache(context))
    }

    fun provideProductViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ProductViewModelFactory(provideCoffeeRepository(context))
    }

}