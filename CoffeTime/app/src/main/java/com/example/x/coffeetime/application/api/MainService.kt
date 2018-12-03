package com.example.x.coffeetime.application.api

import android.util.Log
import com.example.x.coffeetime.application.api.BindingModel.CreateUserModel
import com.example.x.coffeetime.application.api.ResponseModel.TokenResponse
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.model.Coffee
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Favorite
import kotlinx.serialization.Optional
import kotlinx.serialization.json.JSON
import kotlinx.serialization.Serializable

private const val TAG = "MainService"
private const val CLIENT_ID = "5bd1d38ccf7a428ab3b963ac8bd1e4de"

@Serializable
data class Data(val error: String,  val error_description: String)

class ApiService {

    fun requestAuth(
            service: MainService,
            username: String,
            password: String,
            onSuccess: (token: String) -> Unit,
            onError: (error: String) -> Unit) {


        service.requestAuth(username, password, "password", CLIENT_ID).enqueue(
                object : Callback<TokenResponse> {
                    override fun onFailure(call: Call<TokenResponse>?, t: Throwable) {
                        Log.d(TAG, "fail to get data")
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(
                            call: Call<TokenResponse>?,
                            response: Response<TokenResponse>
                    ) {
                        Log.d(TAG, "got a response $response")
                        if (response.isSuccessful) {

                            val token = response.body()?.token ?: ""
                            onSuccess(token)
                        } else {
                            val obj = JSON.parse(Data.serializer(), response.errorBody()?.string()!!)

                            onError(obj.error_description)
                        }
                    }
                }
        )
    }

    fun register(
            service: MainService,
            createuserModel: CreateUserModel,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit
    ) {

        if (createuserModel.Password != createuserModel.ConfirmPassword) {
            onError("Passwords does not match")
            return
        }

        service.register(createuserModel).enqueue(
                object : Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d(TAG, "fail to get data")
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Log.d(TAG, "got a response $response")
                        if (response.isSuccessful) {

                            onSuccess(response.message() ?: "unknown error")
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }

                    }
                }
        )
    }

    fun searchCoffees(
            service: MainService,
            query: String,
            page: Int,
            itemsPerPage: Int,
            onSuccess: (coffees: List<Coffee>) -> Unit,
            onError: (error: String) -> Unit) {
        Log.d(TAG, "query: $query, page: $page, itemsPerPage: $itemsPerPage")

        var queryString = ""

        if (query == "") queryString = "all"
        else queryString = query


        service.searchCoffees(page, itemsPerPage, queryString).enqueue(
                object : Callback<List<Coffee>> {
                    override fun onFailure(call: Call<List<Coffee>>?, t: Throwable) {
                        Log.d(TAG, "fail to get data")
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(
                            call: Call<List<Coffee>>?,
                            response: Response<List<Coffee>>
                    ) {
                        Log.d(TAG, "got a response $response")
                        if (response.isSuccessful) {
                            val coffees = response.body() ?: emptyList()
                            Log.d("coffees", coffees.toString())
                            onSuccess(coffees)
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }
                    }
                }
        )
    }

    fun findOrders(
            service: MainService,
            onSuccess: (orders: List<Cart>) -> Unit,
            onError: (error: String) -> Unit,
            token: String) {


        service.findOrders("Bearer $token").enqueue(
                object : Callback<List<Cart>> {
                    override fun onFailure(call: Call<List<Cart>>?, t: Throwable) {
                        Log.d(TAG, "fail to get data")
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(
                            call: Call<List<Cart>>?,
                            response: Response<List<Cart>>
                    ) {
                        Log.d(TAG, "got a response $response")
                        if (response.isSuccessful) {
                            val orders = response.body() ?: emptyList()
                            Log.d("orders", orders.toString())
                            onSuccess(orders)
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }
                    }
                }
        )
    }

    fun sendPasswordReset(
            service: MainService,
            email: String,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit) {

        service.sendPasswordReset(email).enqueue(
                object : Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {

                        if (response.isSuccessful) {
                            onSuccess("Token sent")
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }
                    }
                }
        )
    }

    fun provideToken(
            service: MainService,
            token: String,
            email: String,
            newPass: String,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit) {

        service.provideToken(token, email, newPass).enqueue(
                object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            onSuccess(response.body() ?: "Password changed")
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }
                    }
                }
        )
    }

    fun increaseProduct(
            service: MainService,
            token: String,
            orderQuantityModel: OrderQuantityModel,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit) {

        service.increaseProduct("Bearer $token", orderQuantityModel).enqueue(
                object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            onSuccess(response.body() ?: "changed")
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }
                    }
                }
        )
    }

    fun decreaseProduct(
            service: MainService,
            token: String,
            orderQuantityModel: OrderQuantityModel,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit) {

        service.decreaseProduct("Bearer $token", orderQuantityModel).enqueue(
                object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            onSuccess(response.body() ?: "changed")
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }
                    }
                }
        )
    }

    fun deleteProduct(
            service: MainService,
            token: String,
            id: Int,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit) {

        service.deleteProduct("Bearer $token", id).enqueue(
                object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            onSuccess(response.body() ?: "deleted")
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }
                    }
                }
        )
    }

    fun updateOrder(
            service: MainService,
            token: String,
            paymentMethod: String,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit) {
        service.updateOrder("Bearer $token", paymentMethod).enqueue(
                object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {

                        onError(t.message ?: "unknown error")

                    }


                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            onSuccess(response.body() ?: "Order Successful")
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }

                    }
                }
        )
    }

    fun addFavorite(
            service: MainService,
            id: Int,
            token: String,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit
            ) {


        service.addFavorite("Bearer $token",id).enqueue(
                object : Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable) {
                        Log.d(TAG, "fail to get data")
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(
                            call: Call<Void>?,
                            response: Response<Void>
                    ) {
                        Log.d(TAG, "got a response $response")
                        if (response.isSuccessful) {
                            onSuccess("Added to favorites")
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }
                    }
                }
        )
    }

    fun deleteFavorite(
            service: MainService,
            id: Int,
            token: String,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit
           ) {


        service.deleteFavorite("Bearer $token",id).enqueue(
                object : Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable) {
                        Log.d(TAG, "fail to get data")
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(
                            call: Call<Void>?,
                            response: Response<Void>
                    ) {
                        Log.d(TAG, "got a response $response")
                        if (response.isSuccessful) {
                            onSuccess("Deleted from favorites")
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }
                    }
                }
        )
    }


    fun getFavorites(
            service: MainService,
            onSuccess: (success: List<Favorite>) -> Unit,
            onError: (error: String) -> Unit,
            token: String) {
        service.getFavorites("Bearer $token").enqueue(
                object : Callback<List<Favorite>> {
                    override fun onFailure(call: Call<List<Favorite>>?, t: Throwable) {
                        Log.d(TAG, "fail to get data")
                        onError(t.message ?: "unknown error")
                    }

                    override fun onResponse(
                            call: Call<List<Favorite>>?,
                            response: Response<List<Favorite>>
                    ) {
                        Log.d(TAG, "got a response $response")
                        if (response.isSuccessful) {
                            val favorites = response.body() ?: emptyList()
                            Log.d("orders", favorites.toString())
                            onSuccess(favorites)
                        } else {
                            onError(response.errorBody()?.string() ?: "Unknown error")
                        }
                    }
                }
        )
    }


}



/**
 *  API communication setup via Retrofit.
 */
interface MainService {

    @FormUrlEncoded
    @POST("/oauth2/token")
    fun requestAuth(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("grant_type") grant_type: String,
            @Field("client_id") client_id: String): Call<TokenResponse>

    @POST("/api/accounts/create")
    fun register(
            @Body createUserModel : CreateUserModel): Call<Void>

    @GET("/api/coffees/pagination")
    fun searchCoffees(
                    @Query("page") page: Int,
                    @Query("itemsPerPage") itemsPerPage: Int,
                    @Query("query") query: String): Call<List<Coffee>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("/api/orders")
    fun findOrders(
            @Header("Authorization") token: String): Call<List<Cart>>


    @GET("api/accounts/sendpasswordreset")
    fun sendPasswordReset(
            @Query("email") email: String
    ): Call<Void>

    @GET("api/accounts/providetoken")
    fun provideToken(
            @Query("token") token: String,
            @Query("email") email: String,
            @Query("newPass") newPass: String
    ): Call<String>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/orders/increaseProduct")
    fun increaseProduct(
            @Header("Authorization") token: String,
            @Body orderQuantityModel : OrderQuantityModel
    ): Call<String>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/orders/decreaseProduct")
    fun decreaseProduct(
            @Header("Authorization") token: String,
            @Body orderQuantityModel : OrderQuantityModel
    ): Call<String>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @DELETE("api/orders/deleteProduct")
    fun deleteProduct(
            @Header("Authorization") token: String,
            @Query("id") id: Int
    ): Call<String>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PUT("api/orders/updateOrder")
    fun updateOrder(
            @Header("Authorization") token: String,
            @Query("paymentMethod") paymentMethod: String
    ): Call<String>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/favorite/addFavorite")
    fun addFavorite(
            @Header("Authorization") token: String,
            @Query("id") id: Int
    ): Call<Void>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @DELETE("api/favorite/deleteFavorite")
    fun deleteFavorite(
            @Header("Authorization") token: String,
            @Query("id") id: Int
    ): Call<Void>


    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/favorite/getFavorites")
    fun getFavorites(
            @Header("Authorization") token: String
    ): Call<List<Favorite>>


    companion object {
        private const val BASE_URL ="http://192.168.1.103:5819"


        fun create(): MainService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MainService::class.java)
        }
    }
}
