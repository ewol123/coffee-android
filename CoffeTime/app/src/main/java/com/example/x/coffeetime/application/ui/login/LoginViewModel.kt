package com.example.x.coffeetime.application.ui.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.x.coffeetime.application.data.AuthRepository
import com.example.x.coffeetime.application.model.Token

class LoginViewModel(private val authRepo: AuthRepository) : ViewModel() {


        fun login(username: String, password: String, context: Context?, onSuccess: (success: String) -> Unit,
                  onError: (error: String) -> Unit)  {

            authRepo.login(username,password, context, {success -> onSuccess(success) } , { error -> onError(error)  })
        }

        fun delete() {
            authRepo.delete()
        }

        val token: LiveData<List<Token>> = authRepo.token

}
