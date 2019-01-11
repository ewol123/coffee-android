package com.example.x.coffeetime.application.api

import android.content.Context
import android.util.Log
import com.example.x.coffeetime.application.api.BindingModel.CreateUserModel
import com.example.x.coffeetime.application.api.ResponseModel.TokenResponse
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.model.Coffee
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
import com.example.x.coffeetime.application.model.Token
import okhttp3.*
import retrofit2.http.Headers


private const val TAG = "MainService"
private const val CLIENT_ID = "5bd1d38ccf7a428ab3b963ac8bd1e4de"
private const val ERR_MSG ="Failed to connect, please login again"

class ApiService {


    fun requestAuth(
            service: MainService,
            username: String,
            password: String,
            onSuccess: (token: String, refresh_token: String) -> Unit,
            onError: (error: String) -> Unit) {


        service.requestAuth(username, password, "password", CLIENT_ID).enqueue(
                object : Callback<TokenResponse> {
                    override fun onFailure(call: Call<TokenResponse>?, t: Throwable) {
                        onError("Failed to connect")
                    }

                    override fun onResponse(
                            call: Call<TokenResponse>?,
                            response: Response<TokenResponse>
                    ) {
                        if (response.isSuccessful) {

                            val token = response.body()?.token ?: ""
                            val refresh_token = response.body()?.refresh_token ?: ""
                            onSuccess(token, refresh_token)
                        } else {

                            onError("Wrong credentials, please try again")
                        }
                    }
                }
        )
    }

    fun refreshToken(
            service: MainService,
            refreshToken: String,
            onSuccess: (token: String, refresh_token: String) -> Unit,
            onError: (error: String) -> Unit) {


        service.refreshToken(refreshToken, "refresh_token", CLIENT_ID).enqueue(
                object : Callback<TokenResponse> {
                    override fun onFailure(call: Call<TokenResponse>?, t: Throwable) {
                        onError(ERR_MSG)
                    }

                    override fun onResponse(
                            call: Call<TokenResponse>?,
                            response: Response<TokenResponse>
                    ) {
                        if (response.isSuccessful) {

                            val token = response.body()?.token ?: ""
                            val refresh_token = response.body()?.refresh_token ?: ""
                            onSuccess(token, refresh_token)
                        } else {

                            onError("Wrong credentials, please try again")
                        }
                    }
                }
        )
    }

    fun refreshTokenSync(
            service: MainService,
            refreshToken: String?) : TokenResponse?
    {
        var res = service.refreshToken(refreshToken, "refresh_token", CLIENT_ID).execute()
        return res.body()

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
                        onError("Register failed, please try again")

                    }

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {

                            onSuccess(response.message() ?: "unknown error")
                        } else {

                            onError("Error, please try again with a different email")
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

        var queryString = ""

        if (query == "") queryString = "all"
        else queryString = query


        service.searchCoffees(page, itemsPerPage, queryString).enqueue(
                object : Callback<List<Coffee>> {
                    override fun onFailure(call: Call<List<Coffee>>?, t: Throwable) {
                        onError(ERR_MSG)

                    }

                    override fun onResponse(
                            call: Call<List<Coffee>>?,
                            response: Response<List<Coffee>>
                    ) {
                        if (response.isSuccessful) {
                            val coffees = response.body() ?: emptyList()
                            onSuccess(coffees)
                        } else {
                            onError("Error while searching coffees")
                        }
                    }
                }
        )
    }

    fun findOrders(
            service: MainService,
            onSuccess: (orders: List<Cart>) -> Unit,
            onError: (error: String) -> Unit
           ) {


        service.findOrders().enqueue(
                object : Callback<List<Cart>> {
                    override fun onFailure(call: Call<List<Cart>>?, t: Throwable) {
                        onError(ERR_MSG)

                    }

                    override fun onResponse(
                            call: Call<List<Cart>>?,
                            response: Response<List<Cart>>
                    ) {
                        if (response.isSuccessful) {
                            val orders = response.body() ?: emptyList()
                            onSuccess(orders)
                        } else {
                            onError("Error while searching orders")
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
                        onError("Failed to connect, please try again")

                    }

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {

                        if (response.isSuccessful) {
                            onSuccess("Token sent")
                        } else {
                            onError("Can't send password reset")
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
                        onError("Failed to connect, please try again")

                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            onSuccess( "Password changed")
                        } else {
                            onError( "Error, please try again")
                        }
                    }
                }
        )
    }

    fun increaseProduct(
            service: MainService,
            orderQuantityModel: OrderQuantityModel,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit) {

        service.increaseProduct(orderQuantityModel).enqueue(
                object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        onError(ERR_MSG)

                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            onSuccess(response.body()!!)
                        } else {
                            onError("Error while increasing product")
                        }
                    }
                }
        )
    }

    fun decreaseProduct(
            service: MainService,
            orderQuantityModel: OrderQuantityModel,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit) {

        service.decreaseProduct(orderQuantityModel).enqueue(
                object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        onError(ERR_MSG)

                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            onSuccess(response.body()!!)
                        } else {
                            onError("Error while decreasing product")
                        }
                    }
                }
        )
    }

    fun deleteProduct(
            service: MainService,
            id: Int,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit) {

        service.deleteProduct(id).enqueue(
                object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        onError(ERR_MSG)

                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            onSuccess(response.body()!!)
                        } else {
                            onError("Error while deleting product")
                        }
                    }
                }
        )
    }

    fun updateOrder(
            service: MainService,
            paymentMethod: String,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit) {
        service.updateOrder(paymentMethod).enqueue(
                object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        onError(ERR_MSG)
                    }


                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            onSuccess(response.body() ?: "Order Successful")
                        } else {
                            onError("Error while updating order")
                        }

                    }
                }
        )
    }

    fun addFavorite(
            service: MainService,
            id: Int,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit
            ) {


        service.addFavorite(id).enqueue(
                object : Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable) {
                        onError(ERR_MSG)
                    }

                    override fun onResponse(
                            call: Call<Void>?,
                            response: Response<Void>
                    ) {
                        if (response.isSuccessful) {
                            onSuccess("Added to favorites")
                        } else {
                            onError("Error while adding favorite")
                        }
                    }
                }
        )
    }

    fun deleteFavorite(
            service: MainService,
            id: Int,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit
           ) {


        service.deleteFavorite(id).enqueue(
                object : Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable) {
                        onError(ERR_MSG)
                    }

                    override fun onResponse(
                            call: Call<Void>?,
                            response: Response<Void>
                    ) {
                        if (response.isSuccessful) {
                            onSuccess("Deleted from favorites")
                        } else {
                            onError("Error while deleting favorite")
                        }
                    }
                }
        )
    }


    fun getFavorites(
            service: MainService,
            onSuccess: (success: List<Favorite>) -> Unit,
            onError: (error: String) -> Unit
            ) {
        service.getFavorites().enqueue(
                object : Callback<List<Favorite>> {
                    override fun onFailure(call: Call<List<Favorite>>?, t: Throwable) {
                        onError(ERR_MSG)
                    }

                    override fun onResponse(
                            call: Call<List<Favorite>>?,
                            response: Response<List<Favorite>>
                    ) {
                        if (response.isSuccessful) {
                            val favorites = response.body() ?: emptyList()
                            onSuccess(favorites)
                        } else {
                            onError("Error while finding favorites")
                        }
                    }
                }
        )
    }


}



/**
 *  API kommunikáció Retrofit-el
 */
interface MainService {

    @FormUrlEncoded
    @POST("/oauth2/token")
    fun requestAuth(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("grant_type") grant_type: String,
            @Field("client_id") client_id: String): Call<TokenResponse>


    @FormUrlEncoded
    @POST("/oauth2/token")
    fun refreshToken(
            @Field("refresh_token") refresh_token: String?,
            @Field("grant_type") grant_type: String,
            @Field("client_id") client_id: String): Call<TokenResponse>

    @POST("/api/accounts/createResObject")
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
           ): Call<List<Cart>>


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
            @Body orderQuantityModel : OrderQuantityModel
    ): Call<String>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/orders/decreaseProduct")
    fun decreaseProduct(
            @Body orderQuantityModel : OrderQuantityModel
    ): Call<String>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @DELETE("api/orders/deleteProduct")
    fun deleteProduct(
            @Query("id") id: Int
    ): Call<String>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PUT("api/orders/updateOrder")
    fun updateOrder(
            @Query("paymentMethod") paymentMethod: String
    ): Call<String>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/favorite/addFavorite")
    fun addFavorite(
            @Query("id") id: Int
    ): Call<Void>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @DELETE("api/favorite/deleteFavorite")
    fun deleteFavorite(
            @Query("id") id: Int
    ): Call<Void>


    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/favorite/getFavorites")
    fun getFavorites(
    ): Call<List<Favorite>>


    companion object {
        private const val RES_BASE_URL ="http://192.168.1.104:5819"
        private const val AUTH_BASE_URL = "http://192.168.1.104:5821"

        fun createResObject(context: Context): MainService {
            var token: String? = null
            var refreshToken: String? = null
            AppDatabase.getInstance(context).tokenDao().getToken().observeForever {
                if(it!!.isNotEmpty()) {
                    token = it.get(0).token
                    refreshToken = it.get(0).refresh_token
                    Log.d("refreshToken", refreshToken)
                }
            }
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .authenticator(Authenticator( fun (route: Route?, response: okhttp3.Response): Request? {
                        if (response.code() == 401) {

                            if(refreshToken == null){
                                Log.d("AUTHENTICATOR", "refresh token null")
                                return null
                            }
                            val res = ApiService()
                                    .refreshTokenSync(MainService.createAuthObject(), refreshToken)


                            if (res != null) {

                                var db = AppDatabase.getInstance(context)
                                db.tokenDao().deleteAll()
                                db.tokenDao().insert(Token(res.token, res.refresh_token))
                                return response.request().newBuilder()
                                        .header("Authorization", "Bearer ${res.token}")
                                        .build()
                            } else {
                                Log.d("AUTHENTICATOR","response null")
                                return null
                            }
                        }
                        else {
                            Log.d("AUTHENTICATOR","not 401 error")
                            return null
                        }
                    }))
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val request = original.newBuilder()
                                .header("Accept", "application/json")
                                .header("Authorization", "Bearer $token")
                                .header("Content-Type", "application/json")
                                .method(original.method(), original.body())
                                .build()
                        chain.proceed(request)
                    }
                    .build()

            return Retrofit.Builder()
                    .baseUrl(RES_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MainService::class.java)
        }

        fun createAuthObject(): MainService {

            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(AUTH_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MainService::class.java)
        }
    }
}
