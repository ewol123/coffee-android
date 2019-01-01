package com.example.x.coffeetime.application.ui.cart

import android.arch.lifecycle.ViewModel
import com.example.x.coffeetime.application.data.CartRepository

class CheckoutViewModel(private val cartRepo: CartRepository) : ViewModel() {


    val cart = cartRepo.findOrders()

    fun updateOrder(onSuccess: (success: String) -> Unit,
                    onError: (error: String) -> Unit,
                    paymentMethod:String
                   ){
        cartRepo.updateOrder({ success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },paymentMethod)
    }


}
