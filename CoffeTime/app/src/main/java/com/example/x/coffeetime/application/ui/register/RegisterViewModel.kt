package com.example.x.coffeetime.application.ui.register

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import com.example.x.coffeetime.application.Validator
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.BindingModel.CreateUserModel
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.data.RegisterRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RegisterRepository
    private val apiService: ApiService
    private val mainService: MainService

    init {
        mainService = MainService.create()
        apiService =  ApiService()
        repository = RegisterRepository(apiService,mainService)
    }

    fun register(createUserModel: CreateUserModel, context: Context?, onSuccess: (success: String) -> Unit,
                 onError: (error: String) -> Unit){

        repository.register(createUserModel,context,{success ->onSuccess("success")},{error ->onError("error")})
    }

    var validator: Validator = Validator()



}
