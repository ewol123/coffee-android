package com.example.x.coffeetime.application

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.x.coffeetime.application.data.AuthRepository
import com.example.x.coffeetime.application.data.CartRepository
import com.example.x.coffeetime.application.data.CoffeeRepository
import com.example.x.coffeetime.application.data.FavoriteRepository
import com.example.x.coffeetime.application.ui.cart.CartViewModel
import com.example.x.coffeetime.application.ui.cart.CheckoutViewModel
import com.example.x.coffeetime.application.ui.favorite.FavoriteViewModel
import com.example.x.coffeetime.application.ui.login.LoginViewModel
import com.example.x.coffeetime.application.ui.product.ProductViewModel
import com.example.x.coffeetime.application.ui.product.SingleProduct.SingleProductViewModel
import com.example.x.coffeetime.application.ui.register.RegisterViewModel
import com.example.x.coffeetime.application.ui.reset_password.ResetPassViewModel


class ViewModelFactory(private val coffeeRepo: CoffeeRepository,
                       private val cartRepo: CartRepository,
                       private val authRepo: AuthRepository,
                       private val favoriteRepo: FavoriteRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(coffeeRepo, cartRepo, authRepo,favoriteRepo) as T
        }
        else if (modelClass.isAssignableFrom(SingleProductViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SingleProductViewModel(coffeeRepo, cartRepo,favoriteRepo) as T
        }
        else if (modelClass.isAssignableFrom(CartViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartRepo,authRepo) as T
        }
        else if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepo) as T
        }
        else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authRepo) as T
        }
        else if (modelClass.isAssignableFrom(ResetPassViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ResetPassViewModel(authRepo) as T
        }
        else if(modelClass.isAssignableFrom(CheckoutViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CheckoutViewModel(cartRepo,authRepo) as T
        }
        else if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(favoriteRepo,cartRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}