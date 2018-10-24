package com.example.x.coffeetime.application.data

import android.arch.paging.LivePagedListBuilder
import android.util.Log
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.BindingModel.PaginationModel
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.db.CoffeeLocalCache
import com.example.x.coffeetime.application.model.Coffee
import com.example.x.coffeetime.application.model.CoffeeSearchResult

class CoffeeRepository(
        private val service: MainService,
        private val cache: CoffeeLocalCache
) {

    fun search(query: String ): CoffeeSearchResult {
        Log.d("CoffeeRepository", "New query: $query")

        // Get data source factory from the local cache
        val dataSourceFactory = cache.coffeesByName(query)

        // Construct the boundary callback
        val boundaryCallback = CoffeeBoundaryCallback(query, service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        // Get the network errors exposed by the boundary callback
        return CoffeeSearchResult(data, networkErrors)
    }


    fun coffeeById(id: Int) : List<Coffee> {

        return cache.coffeesById(id)
    }




    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}