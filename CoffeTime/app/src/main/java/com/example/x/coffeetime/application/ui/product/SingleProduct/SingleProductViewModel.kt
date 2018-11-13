package com.example.x.coffeetime.application.ui.product.SingleProduct

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.widget.TextView
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.CoffeeRepository
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee

class SingleProductViewModel(private val coffeeRepository: CoffeeRepository, private val cartRepo: CartRepository) : ViewModel() {


    fun coffeeById(id: Int) : LiveData<List<Coffee>>  {
        return coffeeRepository.coffeeById(id)
    }



    var cart : LiveData<List<Cart>> = cartRepo.findOrders()

    fun increaseProduct(orderQuantityModel: OrderQuantityModel,token: String){
        cartRepo.increaseProduct({_ ->
        },{_ ->
        },orderQuantityModel,token)
    }

    fun decreaseProduct(orderQuantityModel: OrderQuantityModel, token: String){
        cartRepo.decreaseProduct({_ ->
        },{_ ->
        },orderQuantityModel,token)
    }




}
