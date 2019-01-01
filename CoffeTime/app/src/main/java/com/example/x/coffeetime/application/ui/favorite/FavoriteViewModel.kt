package com.example.x.coffeetime.application.ui.favorite

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel;
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.FavoriteRepository
import com.example.x.coffeetime.application.model.Cart

class FavoriteViewModel(private val favoriteRepo: FavoriteRepository,
                        private val cartRepo: CartRepository) : ViewModel() {



    fun deleteFavorite(id:Int,
                       onSuccess: (success: String) -> Unit,
                       onError: (error: String) -> Unit){
        favoriteRepo.deleteFavorite({ success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },id)
    }


    fun increaseProduct(orderQuantityModel: OrderQuantityModel,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit ){
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


    val cart: LiveData<List<Cart>> = cartRepo.findOrders()

    val favorites = favoriteRepo.findFavorites()

}
