package com.example.x.coffeetime.application.db

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.util.Log
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee
import java.util.concurrent.Executor

/**
 * Local cache osztályok feladata a DAO local data sourcek kezelése gyakran másik szálon.
 */
class CoffeeLocalCache(
        private val coffeeDao: CoffeeDao,
        private val ioExecutor: Executor
) {

    fun insert(coffees: List<Coffee>, insertFinished: ()-> Unit) {
        ioExecutor.execute {
            Log.d("CoffeeLocalCache", "inserting ${coffees.size} coffees")
            coffeeDao.insert(coffees)
            insertFinished()
        }
    }

    fun delete(deleteFinished: () ->Unit){
        ioExecutor.execute {
            coffeeDao.delete()
            deleteFinished()
        }
    }

    fun coffeesByName(name: String): DataSource.Factory<Int,Coffee> {

        val query = "%${name.replace(' ', '%')}%"
        return coffeeDao.coffeesByName(query)
    }

    fun coffeesById(id: Int): LiveData<List<Coffee>>  {
        return coffeeDao.coffeesById(id)
    }




}