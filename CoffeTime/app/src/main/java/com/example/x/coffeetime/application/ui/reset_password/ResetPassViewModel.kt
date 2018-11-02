package com.example.x.coffeetime.application.ui.reset_password

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel;
import android.content.Context
import com.example.x.coffeetime.application.Validator
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.BindingModel.CreateUserModel
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.data.RegisterRepository
import com.example.x.coffeetime.application.data.ResetPassRepository

class ResetPassViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ResetPassRepository
    private val apiService: ApiService
    private val mainService: MainService

    init {
        mainService = MainService.create()
        apiService =  ApiService()
        repository = ResetPassRepository(apiService,mainService)
    }

    fun sendPasswordRequest(email: String, onSuccess: (success: String) -> Unit, onError: (error: String) -> Unit){

        repository.sendPasswordReset(email,{success ->
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

        repository.provideToken(token,email,newPass,{success ->
            onSuccess(success)
        }, {error ->
            onError(error)
        })
    }


    var validator: Validator = Validator()

}
