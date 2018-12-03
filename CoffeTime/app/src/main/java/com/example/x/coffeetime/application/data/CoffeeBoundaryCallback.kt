package com.example.x.coffeetime.application.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import android.util.Log
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.db.CoffeeLocalCache
import com.example.x.coffeetime.application.model.Coffee


class CoffeeBoundaryCallback(
        private val query: String,
        private val service: MainService,
        private val cache: CoffeeLocalCache
) : PagedList.BoundaryCallback<Coffee>() {


    private var lastRequestedPage = 1
    val apiService = ApiService()
    private val _networkErrors = MutableLiveData<String>()

    val networkErrors: LiveData<String>
        get() = _networkErrors

    // változó több request elküldésének elkerülése céljából
    private var isRequestInProgress = false


    override fun onZeroItemsLoaded() {
        Log.d("onZeroItemsLoaded", "onZeroItemsLoaded")
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Coffee) {
        Log.d("onItemAtEndLoaded", itemAtEnd.toString())
        requestAndSaveData(query)
    }



    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        apiService.searchCoffees(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { coffees ->
            cache.insert(coffees) {
                lastRequestedPage++
                isRequestInProgress = false
             }
        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }
}