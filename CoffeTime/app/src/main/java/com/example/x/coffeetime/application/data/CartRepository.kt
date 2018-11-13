package com.example.x.coffeetime.application.data

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.util.Log
import android.widget.Toast
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.db.CartLocalCache
import com.example.x.coffeetime.application.db.CoffeeLocalCache
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee
import com.example.x.coffeetime.application.model.CoffeeSearchResult
import com.example.x.coffeetime.application.model.Token

class CartRepository(
        private val service: MainService,
        private val apiService: ApiService,
        private val cache: CartLocalCache
) {

    fun insertOrders(cart: List<Cart>, insertFinished: () -> Unit){
        cache.insert(cart){
            insertFinished()
        }
    }

    fun findOrders(): LiveData<List<Cart>> {

        return cache.findOrders()
    }

    fun getCart(onSuccess: (success: String) -> Unit,
                onError: (error: String) -> Unit,
                token: String){
        apiService.findOrders(service,{orders ->
            insertOrders(orders){
                onSuccess("success")
            }
        }, {error ->
            cache.delete {}
            onError(error)
        },token)
    }


    fun increaseProduct(onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit,
                        orderQuantityModel: OrderQuantityModel,
                        token:String){
        apiService.increaseProduct(service,token,orderQuantityModel,{_ ->
            getCart({_ ->
                onSuccess("updated")
            }, {error ->
                onError(error)
            }, token)
        }, {error ->
            onError(error)
        })
    }


    fun decreaseProduct(onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit,
                        orderQuantityModel: OrderQuantityModel,
                        token:String){
        apiService.decreaseProduct(service,token,orderQuantityModel,{_ ->
            getCart({_ ->
                onSuccess("updated")
            },{error ->
                onError(error)
            }, token)
        }, {error ->
            onError(error)
        })
    }

    fun deleteProduct(onSuccess: (success: String) -> Unit,
                      onError: (error: String) -> Unit,
                      id:Int,
                      token:String){
        apiService.deleteProduct(service,token,id,{_ ->
            getCart({_->
                onSuccess("deleted")
            },{error ->
                onError(error)
            },token)
        }, {error ->
            onError(error)
        })
    }









}