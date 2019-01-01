package com.example.x.coffeetime.application

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.CoffeeRepository
import com.example.x.coffeetime.application.data.AuthRepository
import com.example.x.coffeetime.application.data.FavoriteRepository
import com.example.x.coffeetime.application.db.CoffeeLocalCache
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.db.CartLocalCache
import com.example.x.coffeetime.application.db.FavoriteLocalCache
import java.util.concurrent.Executors


/**
 * Objektumok beszúrását kezelő osztály
 */
object Injection {

     fun provideOrderQuantity(barcode: String?, id: String?): OrderQuantityModel {
        return OrderQuantityModel(
                TableNum = barcode!!,
                Quantity = "1",
                ProductId = id!!)
    }
    private fun provideCoffeeCache(context: Context): CoffeeLocalCache {
        val database = AppDatabase.getInstance(context)
        return CoffeeLocalCache(database.coffeeDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideCoffeeRepository(context: Context): CoffeeRepository {
        return CoffeeRepository(MainService.create(context), provideCoffeeCache(context))
    }

    private fun provideCartCache(context: Context): CartLocalCache {
        val database = AppDatabase.getInstance(context)
        return CartLocalCache(database.cartDao(),Executors.newSingleThreadExecutor())
    }
    private fun provideCartRepository(context: Context): CartRepository{
        return CartRepository(MainService.create(context), ApiService(), provideCartCache(context))
    }

     private fun provideAuthRepository(context: Context): AuthRepository{
        val database = AppDatabase.getInstance(context)
        return AuthRepository(ApiService(), MainService.create(context),database.tokenDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideFavoriteCache(context:Context): FavoriteLocalCache {
        val database = AppDatabase.getInstance(context)
        return FavoriteLocalCache(database.favoriteDao(),Executors.newSingleThreadExecutor())
    }
    private fun provideFavoriteRepository(context: Context): FavoriteRepository{
        return FavoriteRepository(MainService.create(context), ApiService(),provideFavoriteCache(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(
                provideCoffeeRepository(context),
                provideCartRepository(context),
                provideAuthRepository(context),
                provideFavoriteRepository(context))
    }

}