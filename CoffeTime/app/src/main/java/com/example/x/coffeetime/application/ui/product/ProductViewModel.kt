package com.example.x.coffeetime.application.ui.product

import android.app.Application
import android.arch.lifecycle.*
import android.arch.paging.PagedList
import android.util.Log
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.AuthRepository
import com.example.x.coffeetime.application.data.CoffeeRepository
import com.example.x.coffeetime.application.data.FavoriteRepository
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee
import com.example.x.coffeetime.application.model.CoffeeSearchResult
import com.example.x.coffeetime.application.model.Token

class ProductViewModel(private val coffeeRepo: CoffeeRepository,
                       private val cartRepo: CartRepository,
                       private val authRepo: AuthRepository,
                       private val favoriteRepo: FavoriteRepository) : ViewModel() {


    fun initFavorites(token : String){

        favoriteRepo.getFavorites({success ->
            Log.d("CART_SUCCESS", success)
        }, {error ->
            Log.d("CART_ERROR", error)
        },token)
    }

    fun getCart(token : String){

        cartRepo.getCart({success ->
            Log.d("CART_SUCCESS", success)
        }, {error ->
            Log.d("CART_ERROR", error)
        },token)
    }

    val token: LiveData<List<Token>> = authRepo.token
    val cart: LiveData<List<Cart>> = cartRepo.findOrders()

    private val queryLiveData = MutableLiveData<String>()
    private val coffeeResult: LiveData<CoffeeSearchResult> = Transformations.map(queryLiveData, {
        coffeeRepo.search(it)
    })

    val coffees: LiveData<PagedList<Coffee>> = Transformations.switchMap(coffeeResult,
            { it -> it.data })
    val networkErrors: LiveData<String> = Transformations.switchMap(coffeeResult,
            { it -> it.networkErrors })


    fun searchCoffee(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun lastQueryValue(): String? = queryLiveData.value

    fun increaseProduct(orderQuantityModel: OrderQuantityModel, token: String,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit ){
        cartRepo.increaseProduct({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel,token)
    }

    fun decreaseProduct(orderQuantityModel: OrderQuantityModel, token: String,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit){
        cartRepo.decreaseProduct({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel,token)
    }
}
