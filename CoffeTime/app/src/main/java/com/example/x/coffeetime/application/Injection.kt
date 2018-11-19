package com.example.x.coffeetime.application

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.CoffeeRepository
import com.example.x.coffeetime.application.data.AuthRepository
import com.example.x.coffeetime.application.db.CoffeeLocalCache
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.db.CartLocalCache
import java.util.concurrent.Executors


/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

     fun provideContext(application: Application): Context {
        return application
    }

    private fun provideCoffeeCache(context: Context): CoffeeLocalCache {
        val database = AppDatabase.getInstance(context)
        return CoffeeLocalCache(database.coffeeDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideCoffeeRepository(context: Context): CoffeeRepository {
        return CoffeeRepository(MainService.create(), provideCoffeeCache(context))
    }

    private fun provideCartCache(context: Context): CartLocalCache {
        val database = AppDatabase.getInstance(context)
        return CartLocalCache(database.cartDao(),Executors.newSingleThreadExecutor())

    }
    private fun provideCartRepository(context: Context): CartRepository{
        return CartRepository(MainService.create(), ApiService(), provideCartCache(context))
    }

     private fun provideAuthRepository(): AuthRepository{
        val database = AppDatabase.getInstance(provideContext(Application()))
        return AuthRepository(ApiService(), MainService.create(),database.tokenDao(), Executors.newSingleThreadExecutor())
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideCoffeeRepository(context), provideCartRepository(context), provideAuthRepository())
    }

}