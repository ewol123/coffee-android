package com.example.x.coffeetime.application.ui.cart

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.AuthRepository

class CheckoutViewModel(private val cartRepository: CartRepository,
                    private val authRepo: AuthRepository) : ViewModel() {


    val token = authRepo.token
    val cart = cartRepository.findOrders()

    fun updateOrder(onSuccess: (success: String) -> Unit,
                    onError: (error: String) -> Unit,
                    paymentMethod:String,
                    token:String){
        cartRepository.updateOrder({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },paymentMethod,token)
    }


}
