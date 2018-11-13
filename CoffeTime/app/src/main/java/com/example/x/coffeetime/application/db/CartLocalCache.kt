package com.example.x.coffeetime.application.db

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.Query
import android.util.Log
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee
import com.example.x.coffeetime.application.model.Token
import java.util.concurrent.Executor

class CartLocalCache(
        private val cartDao: CartDao,
        private val ioExecutor: Executor
) {


    fun insert(cart: List<Cart>, insertFinished: () ->Unit) {
        ioExecutor.execute {
            delete{
                cartDao.insert(cart)
                insertFinished()
            }
            Log.d("CartLocalCache", "inserting ${cart.size} orders")
        }
    }

    fun delete(deleteFinished: () ->Unit){
        ioExecutor.execute {
            cartDao.delete()
            deleteFinished()
        }
    }

    fun findOrders(): LiveData<List<Cart>> {
        return cartDao.getOrders()
    }





}