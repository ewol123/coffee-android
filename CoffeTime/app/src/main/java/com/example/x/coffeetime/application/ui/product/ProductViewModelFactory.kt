package com.example.x.coffeetime.application.ui.product

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.x.coffeetime.application.data.CoffeeRepository
import com.example.x.coffeetime.application.ui.product.SingleProduct.SingleProductViewModel


class ProductViewModelFactory(private val repository: CoffeeRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchProductViewModel(repository) as T
        }
        else if (modelClass.isAssignableFrom(SingleProductViewModel::class.java)){
            @Suppress("UNCHECKED CAST")
            return SingleProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}