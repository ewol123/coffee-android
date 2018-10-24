package com.example.x.coffeetime.application.model

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

/**
 * CoffeeSearchResult from a search, which contains LiveData<List<Coffee>> holding query data,
 * and a LiveData<String> of network error state.
 */
data class CoffeeSearchResult(
        val data: LiveData<PagedList<Coffee>>,
        val networkErrors: LiveData<String>
)