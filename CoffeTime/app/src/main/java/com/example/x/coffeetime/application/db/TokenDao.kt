package com.example.x.coffeetime.application.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.x.coffeetime.application.model.Token

@Dao
interface TokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(token: Token)

    @Query( "SELECT * FROM token" )
    fun getToken(): LiveData<List<Token>>


    @Query("DELETE FROM token")
    fun deleteAll()

}