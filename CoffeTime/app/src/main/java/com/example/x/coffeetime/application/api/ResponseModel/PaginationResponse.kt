package com.example.x.coffeetime.application.api.ResponseModel

import com.example.x.coffeetime.application.model.Coffee
import com.google.gson.annotations.SerializedName


data class PaginationResponse(
        val items: List<Coffee> = emptyList()
)

