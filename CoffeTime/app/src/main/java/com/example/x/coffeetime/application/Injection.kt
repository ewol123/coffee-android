package com.example.x.coffeetime.application

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.CoffeeRepository
import com.example.x.coffeetime.application.data.LoginRepository
import com.example.x.coffeetime.application.db.CoffeeLocalCache
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.db.CartLocalCache
import com.example.x.coffeetime.application.ui.product.ProductViewModelFactory
import java.util.concurrent.Executors


/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    private fun provideContext(application: Application): Context {
        return application
    }

    private fun provideCache(context: Context): CoffeeLocalCache {
        val database = AppDatabase.getInstance(context)
        return CoffeeLocalCache(database.coffeeDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideCartCache(): CartLocalCache {
        val database = AppDatabase.getInstance(provideContext(Application()))
        return CartLocalCache(database.cartDao(),Executors.newSingleThreadExecutor())

    }

    private fun provideCoffeeRepository(context: Context): CoffeeRepository {
        return CoffeeRepository(MainService.create(), provideCache(context))
    }



    fun provideCartRepository(): CartRepository{
        val database = AppDatabase.getInstance(provideContext(Application()))
        return CartRepository(MainService.create(), provideCartCache())
    }

     fun provideLoginRepository(): LoginRepository{
        val database = AppDatabase.getInstance(provideContext(Application()))
        return LoginRepository(ApiService(), MainService.create(),database.tokenDao(), Executors.newSingleThreadExecutor())
    }

    fun provideProductViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ProductViewModelFactory(provideCoffeeRepository(context))
    }

}