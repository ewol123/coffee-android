package com.example.x.coffeetime.application.ui.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.data.AuthRepository
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.model.Token
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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
