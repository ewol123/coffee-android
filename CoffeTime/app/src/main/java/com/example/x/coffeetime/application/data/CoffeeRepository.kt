package com.example.x.coffeetime.application.data

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.db.CoffeeLocalCache
import com.example.x.coffeetime.application.model.Coffee
import com.example.x.coffeetime.application.model.CoffeeSearchResult

class CoffeeRepository(
        private val service: MainService,
        private val coffeeCache: CoffeeLocalCache
) {

    fun search(query: String ): CoffeeSearchResult {

        //data source factory elkérése a cacheből
        val dataSourceFactory = coffeeCache.coffeesByName(query)

        // boundary callback létrehozása
        val boundaryCallback = CoffeeBoundaryCallback(query, service, coffeeCache)

        val networkErrors = boundaryCallback.networkErrors

        var config = PagedList.Config.Builder()
                .setPrefetchDistance(0)
                .setInitialLoadSizeHint(DATABASE_PAGE_SIZE)
                .setPageSize(DATABASE_PAGE_SIZE)
                .build()

        // paged list létrehozása a data source factory és boundary callback megadásával
        val data = LivePagedListBuilder(dataSourceFactory, config )
                .setBoundaryCallback(boundaryCallback)
                .build()


        return CoffeeSearchResult(data, networkErrors)
    }


    fun coffeeById(id: Int) : LiveData<List<Coffee>> {
        return coffeeCache.coffeesById(id)
    }



    companion object {
        private const val DATABASE_PAGE_SIZE = 30
    }
}