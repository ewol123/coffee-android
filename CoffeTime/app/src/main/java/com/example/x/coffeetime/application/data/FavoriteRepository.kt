package com.example.x.coffeetime.application.data

import android.arch.lifecycle.LiveData
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.db.CartLocalCache
import com.example.x.coffeetime.application.db.FavoriteLocalCache
import com.example.x.coffeetime.application.model.Favorite

class FavoriteRepository(
        private val service: MainService,
        private val apiService: ApiService,
        private val cache: FavoriteLocalCache
) {

    fun insertFavorites(favorites: List<Favorite>, insertFinished: () -> Unit){
        cache.insert(favorites){
            insertFinished()
        }
    }

    fun findFavorites(): LiveData<List<Favorite>> {

        return cache.findFavorites()
    }

    fun getFavorites(onSuccess: (success: String) -> Unit,
                onError: (error: String) -> Unit,
                token: String){
        apiService.getFavorites(service,{favorites ->
            insertFavorites(favorites){
                onSuccess("success")
            }
        }, {error ->
            cache.delete {}
            onError(error)
        },token)
    }



    fun addFavorite(onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit,
                        id:Int,
                        token:String){
        apiService.addFavorite(service,id,token,{_ ->
            getFavorites({_ ->
                onSuccess("added")
            }, {error ->
                onError(error)
            }, token)
        }, {error ->
            onError(error)
        })
    }


    fun deleteFavorite(onSuccess: (success: String) -> Unit,
                      onError: (error: String) -> Unit,
                      id:Int,
                      token:String){
        apiService.deleteFavorite(service,id,token,{_ ->
            getFavorites({_->
                onSuccess("deleted")
            },{error ->
                onError(error)
            },token)
        }, {error ->
            onError(error)
        })
    }










}