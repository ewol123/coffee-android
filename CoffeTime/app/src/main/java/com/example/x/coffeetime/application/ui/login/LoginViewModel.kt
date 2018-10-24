package com.example.x.coffeetime.application.ui.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.content.Context
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.data.LoginRepository
import com.example.x.coffeetime.application.db.AppDatabase
import com.example.x.coffeetime.application.model.Token
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class LoginViewModel(application: Application) : AndroidViewModel(application) {

        private val repository: LoginRepository
        private val apiService: ApiService
        private val mainService: MainService
        private val executor: Executor
        val token: LiveData<List<Token>>

        init {
            val tokenDao = AppDatabase.getInstance(application).tokenDao()
            mainService = MainService.create()
            apiService =  ApiService()
            executor = Executors.newSingleThreadExecutor()
            repository = LoginRepository(apiService, mainService ,tokenDao,executor)
            token = repository.token
        }


        fun login(username: String, password: String, context: Context?, onSuccess: (success: String) -> Unit,
                  onError: (error: String) -> Unit)  {

            repository.login(username,password, context, {success -> onSuccess(success) } , { error -> onError(error)  })
        }

        fun delete() {
            repository.delete()
        }



}
