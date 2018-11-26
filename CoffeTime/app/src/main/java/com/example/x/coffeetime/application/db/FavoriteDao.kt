package com.example.x.coffeetime.application.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee
import com.example.x.coffeetime.application.model.Favorite

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cart: List<Favorite>)


    @Query( "SELECT * FROM favorites" )
    fun getFavorites(): LiveData<List<Favorite>>

    @Query("DELETE FROM favorites")
    fun delete()



}