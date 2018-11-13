package com.example.x.coffeetime.application.ui.register

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.x.coffeetime.application.Validator
import com.example.x.coffeetime.application.api.BindingModel.CreateUserModel
import com.example.x.coffeetime.application.data.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {


    fun register(createUserModel: CreateUserModel, context: Context?, onSuccess: (success: String) -> Unit,
                 onError: (error: String) -> Unit){

        authRepository.register(createUserModel,context,{success ->onSuccess("success")},{error ->onError("error")})
    }

    var validator: Validator = Validator()



}
