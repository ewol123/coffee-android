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
import android.R.id.edit
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences




private const val TAG = "MainService"
private const val CLIENT_ID = "5bd1d38ccf7a428ab3b963ac8bd1e4de"

class ApiService {



fun requestAuth(
        service: MainService,
        username: String,
        password: String,
        onSuccess: (token: String) -> Unit,
        onError: (error: String) -> Unit) {



    service.requestAuth(username,password,"password",CLIENT_ID).enqueue(
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
                        onError(response.errorBody()?.string() ?: "Unknown error")
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
        ){

     if(createuserModel.Password != createuserModel.ConfirmPassword){
         onError("Passwords does not match")
         return
     }

    service.register(createuserModel).enqueue(
            object: Callback<Void> {
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

        if(query == "") queryString = "all"
        else queryString = query


        service.searchCoffees(page,itemsPerPage,queryString ).enqueue(
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
                            Log.d("repos",coffees.toString())
                            onSuccess(coffees)
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

    companion object {
        private const val BASE_URL ="http://192.168.1.104:5819"


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