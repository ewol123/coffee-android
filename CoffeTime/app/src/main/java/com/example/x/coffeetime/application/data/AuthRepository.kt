package com.example.x.coffeetime.application.data

import android.arch.lifecycle.LiveData
import android.content.Context
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.BindingModel.CreateUserModel
import com.example.x.coffeetime.application.db.TokenDao
import com.example.x.coffeetime.application.model.Token
import java.util.concurrent.Executor


class AuthRepository(
        private val ApiService: ApiService,
        private val mainService: MainService,
        private val authService: MainService,
        private val tokenDao: TokenDao,
        private val ioExecutor: Executor
) {

    fun delete(){
        ioExecutor.execute {
        tokenDao.deleteAll()
        }
    }

     fun login(username: String,password : String, context: Context?,  onSuccess: (success: String) -> Unit,
               onError: (error: String) -> Unit) {

        ApiService.requestAuth( authService, username, password, { token, refresh_token ->
            ioExecutor.execute {
            tokenDao.deleteAll()
            var newToken = Token(token, refresh_token)
            tokenDao.insert(newToken)
            }
            onSuccess("success")
        }, { error ->
            onError(error)
        })

    }
     val token: LiveData<List<Token>> = tokenDao.getToken()

    fun register(createUserModel: CreateUserModel, context: Context?, onSuccess: (success: String) -> Unit,
                 onError: (error: String) -> Unit){

        ApiService.register(mainService,createUserModel, { success ->
            onSuccess("Register successful, please log-in")
        }, {error ->
            onError(error)
        })
    }

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