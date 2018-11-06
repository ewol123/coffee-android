package com.example.x.coffeetime.application.ui.cart

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel;
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.model.Cart

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CartRepository
    val cart: LiveData<List<Cart>>


    init {
        repository = Injection.provideCartRepository()
        cart = repository.findOrders()
    }




}
