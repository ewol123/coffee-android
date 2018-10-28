package com.example.x.coffeetime.application.ui.product

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.util.Log
import com.example.x.coffeetime.application.data.CoffeeRepository
import com.example.x.coffeetime.application.model.Coffee
import com.example.x.coffeetime.application.model.CoffeeSearchResult

/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


class SearchProductViewModel(private val repository: CoffeeRepository) : ViewModel() {


    private val queryLiveData = MutableLiveData<String>()
    private val coffeeResult: LiveData<CoffeeSearchResult> = Transformations.map(queryLiveData, {
        repository.search(it)
    })

    val coffees: LiveData<PagedList<Coffee>> = Transformations.switchMap(coffeeResult,
            { it -> it.data })
    val networkErrors: LiveData<String> = Transformations.switchMap(coffeeResult,
            { it -> it.networkErrors })


    fun searchCoffee(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun lastQueryValue(): String? = queryLiveData.value
}