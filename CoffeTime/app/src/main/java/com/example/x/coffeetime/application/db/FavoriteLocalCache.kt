package com.example.x.coffeetime.application.db

import android.arch.lifecycle.LiveData
import android.util.Log
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Favorite
import java.util.concurrent.Executor

class FavoriteLocalCache(
        private val favoriteDao: FavoriteDao,
        private val ioExecutor: Executor
) {

    fun insert(favorites: List<Favorite>, insertFinished: () ->Unit) {
        ioExecutor.execute {
            delete{
                favoriteDao.insert(favorites)
                insertFinished()
            }
            Log.d("FavoriteLocalCache", "inserting ${favorites.size} favorites")
        }
    }

    fun delete(deleteFinished: () ->Unit){
        ioExecutor.execute {
            favoriteDao.delete()
            deleteFinished()
        }
    }

    fun findFavorites(): LiveData<List<Favorite>> {
        return favoriteDao.getFavorites()
    }




}