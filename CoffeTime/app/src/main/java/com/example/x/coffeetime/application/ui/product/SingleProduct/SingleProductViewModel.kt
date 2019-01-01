package com.example.x.coffeetime.application.ui.product.SingleProduct

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.CoffeeRepository
import com.example.x.coffeetime.application.data.FavoriteRepository
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee

class SingleProductViewModel(private val coffeeRepo: CoffeeRepository,
                             private val cartRepo: CartRepository,
                             private val favoriteRepo: FavoriteRepository) : ViewModel() {


    fun coffeeById(id: Int) : LiveData<List<Coffee>>  {
        return coffeeRepo.coffeeById(id)
    }

    var cart : LiveData<List<Cart>> = cartRepo.findOrders()

    fun increaseProduct(orderQuantityModel: OrderQuantityModel,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit){
        cartRepo.increaseProduct({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel)
    }

    fun decreaseProduct(orderQuantityModel: OrderQuantityModel,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit ){
        cartRepo.decreaseProduct({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel)
    }



    fun addFavorite(id:Int,
                    onSuccess: (success: String) -> Unit,
                    onError: (error: String) -> Unit){
        favoriteRepo.addFavorite({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },id)
    }

    fun deleteFavorite(id:Int,
                       onSuccess: (success: String) -> Unit,
                       onError: (error: String) -> Unit){
        favoriteRepo.deleteFavorite({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },id)
    }

    val favorites = favoriteRepo.findFavorites()



}
