package com.example.x.coffeetime.application.api.ResponseModel

import kotlinx.serialization.Serializable

@Serializable
data class loginResponse(val error: String,  val error_description: String)
