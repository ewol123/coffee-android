package com.example.x.coffeetime.application.ui.cart

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.data.CartRepository

class CartViewModel(private val cartRepo: CartRepository) : ViewModel() {



    fun initCart(token : String){

        cartRepo.getCart({ success ->
            Log.d("CART_SUCCESS", success)
        }, {error ->
            Log.d("CART_ERROR", error)
        },token)
    }

    fun increaseProduct(orderQuantityModel: OrderQuantityModel, token: String,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit){
        cartRepo.increaseProduct({ success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel,token)
    }

    fun decreaseProduct(orderQuantityModel: OrderQuantityModel, token: String,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit){
        cartRepo.decreaseProduct({ success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel,token)
    }

    fun deleteProduct(id: Int,token:String, onSuccess: (success: String ) -> Unit, onError: (error:String) -> Unit){
        cartRepo.deleteProduct({ success ->
            onSuccess(success)
        }, {error ->
            onError(error)
        },id,token)
    }

    val cart = cartRepo.findOrders()

}
