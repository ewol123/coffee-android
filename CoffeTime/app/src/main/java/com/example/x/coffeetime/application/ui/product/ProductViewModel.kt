package com.example.x.coffeetime.application.ui.product

import android.arch.lifecycle.*
import android.arch.paging.PagedList
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


    fun initFavorites(){

        favoriteRepo.getFavorites({success ->
        }, {error ->
        })
    }

    fun getCart(){

        cartRepo.getCart({success ->
        }, {error ->
        })
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

    fun increaseProduct(orderQuantityModel: OrderQuantityModel,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit ){
        cartRepo.increaseProduct({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel)
    }

    fun decreaseProduct(orderQuantityModel: OrderQuantityModel,
                        onSuccess: (success: String) -> Unit,
                        onError: (error: String) -> Unit){
        cartRepo.decreaseProduct({success ->
            onSuccess(success)
        },{error ->
            onError(error)
        },orderQuantityModel)
    }
}
