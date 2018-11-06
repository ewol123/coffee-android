package com.example.x.coffeetime.application.data

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.util.Log
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.db.CartLocalCache
import com.example.x.coffeetime.application.db.CoffeeLocalCache
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee
import com.example.x.coffeetime.application.model.CoffeeSearchResult
import com.example.x.coffeetime.application.model.Token

class CartRepository(
        private val service: MainService,
        private val cache: CartLocalCache
) {

    fun insertOrders(cart: List<Cart>){
        cache.insert(cart)
    }

    fun findOrders(): LiveData<List<Cart>> {
        return cache.findOrders()
    }


}