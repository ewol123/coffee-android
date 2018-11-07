package com.example.x.coffeetime.application.ui.cart

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel;
import android.util.Log
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.LoginRepository
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Token

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CartRepository
    private val loginRepo: LoginRepository

    init {
        repository = Injection.provideCartRepository()
        loginRepo = Injection.provideLoginRepository()
    }


    fun initCart(token : String){

        repository.getCart({success ->
            Log.d("CART_SUCCESS", success)
        }, {error ->
            Log.d("CART_ERROR", error)
        },token)
    }

    val token = loginRepo.token
    val cart = repository.findOrders()

}
