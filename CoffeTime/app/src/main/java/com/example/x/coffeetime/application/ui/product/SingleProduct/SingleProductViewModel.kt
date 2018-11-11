package com.example.x.coffeetime.application.ui.product.SingleProduct

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel;
import com.example.x.coffeetime.application.data.CoffeeRepository
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee

class SingleProductViewModel(private val repository: CoffeeRepository) : ViewModel() {


    fun coffeeById(id: Int) : LiveData<List<Coffee>>  {
        return repository.coffeeById(id)
    }

    fun coffeeQuantityById(id: Int) : LiveData<List<Cart>> {
        return repository.coffeeQuantityById(id)
    }


}
