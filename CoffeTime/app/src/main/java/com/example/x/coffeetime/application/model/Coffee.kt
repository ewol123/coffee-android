package com.example.x.coffeetime.application.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "coffees")
data class Coffee(
        @PrimaryKey @field:SerializedName("coffeeId") val coffeeId: Int,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("imagePath") val imagePath: String,
        @field:SerializedName("price") val price: String,
        @field:SerializedName("description") val description: String,
        @field:SerializedName("strength") val strength: Int

)