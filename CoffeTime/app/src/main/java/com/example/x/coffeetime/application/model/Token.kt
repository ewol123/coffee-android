package com.example.x.coffeetime.application.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "token")
data class Token(
        @PrimaryKey @field:SerializedName("token") val token: String,
        @field:SerializedName("refresh_token") val refresh_token: String
)
