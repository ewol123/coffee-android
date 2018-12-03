package com.example.x.coffeetime.application.model

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

/**
 * CoffeeSearchResult egy keresésből, ami egy LiveData<List<Coffee>>-t tartalmaz az adatokkal,
 * és egy LiveData<String>-t network hibákkal.
 */
data class CoffeeSearchResult(
        val data: LiveData<PagedList<Coffee>>,
        val networkErrors: LiveData<String>
)