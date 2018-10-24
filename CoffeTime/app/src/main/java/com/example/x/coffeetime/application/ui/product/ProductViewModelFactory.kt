package com.example.x.coffeetime.application.ui.product

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.x.coffeetime.application.data.CoffeeRepository


class ProductViewModelFactory(private val repository: CoffeeRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}