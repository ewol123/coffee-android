package com.example.x.coffeetime.application.ui.cart

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
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

    fun increaseProduct(orderQuantityModel: OrderQuantityModel, token: String,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit){
        cartRepository.increaseProduct({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel,token)
    }

    fun decreaseProduct(orderQuantityModel: OrderQuantityModel, token: String,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit){
        cartRepository.decreaseProduct({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel,token)
    }

    fun deleteProduct(id: Int,token:String, onSuccess: (success: String ) -> Unit, onError: (error:String) -> Unit){
        cartRepository.deleteProduct({success ->
            onSuccess(success)
        }, {error ->
            onError(error)
        },id,token)
    }



    val token = authRepo.token
    val cart = cartRepository.findOrders()

}
