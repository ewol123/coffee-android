package com.example.x.coffeetime.application.ui.product

import android.app.Application
import android.arch.lifecycle.*
import android.util.Log
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.LoginRepository
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Token
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val cartRepository: CartRepository
    private val loginRepository: LoginRepository
    val token: LiveData<List<Token>>
    val cart: LiveData<List<Cart>>
    init {
        cartRepository = Injection.provideCartRepository(application)
        loginRepository = Injection.provideLoginRepository()
        token = loginRepository.token
        cart = cartRepository.findOrders()
    }



    fun initCart(token : String){

        cartRepository.getCart({success ->
            Log.d("CART_SUCCESS", success)
        }, {error ->
            Log.d("CART_ERROR", error)
        },token)
    }


}
