package com.example.x.coffeetime.application.db

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.util.Log
import com.example.x.coffeetime.application.model.Coffee
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class CoffeeLocalCache(
        private val coffeeDao: CoffeeDao,
        private val tokenDao: TokenDao,
        private val ioExecutor: Executor
) {

    /**
     * Insert a list of coffees in the database, on a background thread.
     */
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
        // appending '%' so we can allow other characters to be before and after the query string
        val query = "%${name.replace(' ', '%')}%"
        return coffeeDao.coffeesByName(query)
    }

    fun coffeesById(id: Int): LiveData<List<Coffee>>  {
        return coffeeDao.coffeesById(id)
    }

}