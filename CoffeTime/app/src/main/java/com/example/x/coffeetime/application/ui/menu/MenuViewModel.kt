package com.example.x.coffeetime.application.ui.menu

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.data.LoginRepository
import com.example.x.coffeetime.application.db.appDatabase
import com.example.x.coffeetime.application.model.Token
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MenuViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LoginRepository
    private val ApiService: ApiService
    private val mainService: MainService
    private val executor: Executor
    val token: LiveData<List<Token>>


    init {
        val tokenDao = appDatabase.getInstance(application).tokenDao()
        mainService = MainService.create()
        ApiService =  ApiService()
        executor = Executors.newSingleThreadExecutor()
        repository = LoginRepository(ApiService, mainService ,tokenDao,executor)
        token = repository.token
    }




}
