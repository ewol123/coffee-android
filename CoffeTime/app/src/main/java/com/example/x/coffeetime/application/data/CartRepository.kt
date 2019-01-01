package com.example.x.coffeetime.application.data

import android.arch.lifecycle.LiveData
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.db.CartLocalCache
import com.example.x.coffeetime.application.model.Cart

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
                onError: (error: String) -> Unit
               ){
        apiService.findOrders(service,{orders ->
            insertOrders(orders){
                onSuccess("success")
            }
        }, {error ->
            cache.delete {}
            onError(error)
        })
    }


    fun increaseProduct(onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit,
                        orderQuantityModel: OrderQuantityModel
                       ){
        apiService.increaseProduct(service,orderQuantityModel,{_ ->
            getCart({_ ->
                onSuccess("updated")
            }, {error ->
                onError(error)
            })
        }, {error ->
            onError(error)
        })
    }


    fun decreaseProduct(onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit,
                        orderQuantityModel: OrderQuantityModel
                        ){
        apiService.decreaseProduct(service,orderQuantityModel,{_ ->
            getCart({_ ->
                onSuccess("updated")
            },{error ->
                onError(error)
            })
        }, {error ->
            onError(error)
        })
    }

    fun deleteProduct(onSuccess: (success: String) -> Unit,
                      onError: (error: String) -> Unit,
                      id:Int
                      ){
        apiService.deleteProduct(service,id,{_ ->
            getCart({_->
                onSuccess("deleted")
            },{error ->
                onError(error)
            })
        }, {error ->
            onError(error)
        })
    }

    fun updateOrder(onSuccess: (success: String) -> Unit,
                      onError: (error: String) -> Unit,
                      paymentMethod:String
                     ){
        apiService.updateOrder(service,paymentMethod,{success ->
            getCart({_->
            },{_ ->
            })
            onSuccess(success)
        }, {error ->
            onError(error)
        })
    }





}