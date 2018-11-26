package com.example.x.coffeetime.application.ui.favorite

import android.arch.lifecycle.ViewModel;
import android.util.Log
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.data.FavoriteRepository

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {


    fun initFavorites(token : String){

        favoriteRepository.getFavorites({success ->
            Log.d("CART_SUCCESS", success)
        }, {error ->
            Log.d("CART_ERROR", error)
        },token)
    }

    fun addFavorite(id:Int, token: String,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit){
        favoriteRepository.addFavorite({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },id,token)
    }

    fun deleteFavorite(id:Int, token: String,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit){
        favoriteRepository.deleteFavorite({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },id,token)
    }

    val favorites = favoriteRepository.findFavorites()

}
