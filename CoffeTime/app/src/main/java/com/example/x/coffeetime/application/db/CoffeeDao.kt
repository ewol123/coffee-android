package com.example.x.coffeetime.application.db

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.x.coffeetime.application.model.Coffee

@Dao
interface CoffeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coffees: List<Coffee>)

    @Query("SELECT * FROM coffees WHERE (name LIKE :queryString) ORDER BY name ASC")
    fun coffeesByName(queryString: String): DataSource.Factory<Int,Coffee>

    @Query("SELECT * FROM coffees WHERE (coffeeId == :coffeeId)")
    fun coffeesById(coffeeId: Int): List<Coffee>

    @Query("DELETE FROM coffees")
    fun delete()


}