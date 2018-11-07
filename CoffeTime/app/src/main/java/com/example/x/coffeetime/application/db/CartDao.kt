package com.example.x.coffeetime.application.db

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.x.coffeetime.application.model.Cart

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cart: List<Cart>)


    @Query( "SELECT * FROM cart" )
    fun getOrders(): LiveData<List<Cart>>

    @Query("DELETE FROM cart")
    fun delete()

    @Query("SELECT  * FROM cart WHERE (id == :coffeeId)")
    fun getCoffeeQuantity(coffeeId: Int) : LiveData<List<Cart>>


}