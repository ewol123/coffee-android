package com.example.x.coffeetime.application.ui.cart

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.AuthRepository

class CartViewModel(private val cartRepository: CartRepository,
                    private val authRepo: AuthRepository) : ViewModel() {



    fun initCart(token : String){

        cartRepository.getCart({success ->
            Log.d("CART_SUCCESS", success)
        }, {error ->
            Log.d("CART_ERROR", error)
        },token)
    }

    val token = authRepo.token
    val cart = cartRepository.findOrders()

}
