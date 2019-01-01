package com.example.x.coffeetime.application.ui.cart

import android.arch.lifecycle.ViewModel
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.data.CartRepository

class CartViewModel(private val cartRepo: CartRepository) : ViewModel() {




    fun increaseProduct(orderQuantityModel: OrderQuantityModel,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit){
        cartRepo.increaseProduct({ success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel)
    }

    fun decreaseProduct(orderQuantityModel: OrderQuantityModel,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit){
        cartRepo.decreaseProduct({ success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel)
    }

    fun deleteProduct(id: Int, onSuccess: (success: String ) -> Unit, onError: (error:String) -> Unit){
        cartRepo.deleteProduct({ success ->
            onSuccess(success)
        }, {error ->
            onError(error)
        },id)
    }

    val cart = cartRepo.findOrders()

}
