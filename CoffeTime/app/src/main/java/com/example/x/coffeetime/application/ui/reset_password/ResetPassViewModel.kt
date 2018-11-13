package com.example.x.coffeetime.application.ui.reset_password

import android.arch.lifecycle.ViewModel
import com.example.x.coffeetime.application.Validator
import com.example.x.coffeetime.application.data.AuthRepository

class ResetPassViewModel(private val authRepository: AuthRepository) : ViewModel() {


    fun sendPasswordRequest(email: String, onSuccess: (success: String) -> Unit, onError: (error: String) -> Unit){

        authRepository.sendPasswordReset(email,{success ->
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

        authRepository.provideToken(token,email,newPass,{success ->
            onSuccess(success)
        }, {error ->
            onError(error)
        })
    }


    var validator: Validator = Validator()

}
