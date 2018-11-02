package com.example.x.coffeetime.application.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.BindingModel.CreateUserModel
import com.example.x.coffeetime.application.api.MainService

class ResetPassRepository(
        private val ApiService: ApiService,
        private val mainService: MainService
){


    fun sendPasswordReset(email: String, onSuccess: (success: String) -> Unit, onError: (error: String) -> Unit){
        ApiService.sendPasswordReset(mainService,email, {success ->
            onSuccess(success)
        }, {error ->
            onError(error)
        })
    }

    fun provideToken(
            token: String,
            email: String,
            newPass: String,
            onSuccess: (success: String) -> Unit,
            onError: (error: String) -> Unit ) {

        ApiService.provideToken(mainService,token,email,newPass,{success ->
            onSuccess(success)
        },{error ->
            onError(error)
        })
    }


}