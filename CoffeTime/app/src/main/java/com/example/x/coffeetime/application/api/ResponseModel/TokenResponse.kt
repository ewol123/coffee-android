package com.example.x.coffeetime.application.api.ResponseModel

import com.google.gson.annotations.SerializedName


/**
 * Data class to hold repo responses from searchRepo API calls.
 */
data class TokenResponse(
        @SerializedName("access_token") val token: String,
        @SerializedName("token_type") val token_type: String,
        @SerializedName("expires_in") val expires_in: String
        )
