package com.example.x.coffeetime.application.data

import android.arch.lifecycle.LiveData
import android.content.Context
import android.widget.Toast
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.BindingModel.CreateUserModel
import com.example.x.coffeetime.application.db.TokenDao
import com.example.x.coffeetime.application.model.Token
import java.util.concurrent.Executor


class AuthRepository(
        private val ApiService: ApiService,
        private val mainService: MainService,
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

        ApiService.requestAuth( mainService, username, password, { token ->
            ioExecutor.execute {
            tokenDao.deleteAll()
            var newToken = Token(token)
            tokenDao.insert(newToken)
            }
            onSuccess("success")
        }, { error ->
            Toast.makeText(context,error,Toast.LENGTH_SHORT).show()
            onError("error")
        })

    }
     val token: LiveData<List<Token>> = tokenDao.getToken()

    fun register(createUserModel: CreateUserModel, context: Context?, onSuccess: (success: String) -> Unit,
                 onError: (error: String) -> Unit){

        ApiService.register(mainService,createUserModel, { success ->
            Toast.makeText(context,"Register successful, please log-in",Toast.LENGTH_SHORT).show()
            onSuccess("success")
        }, {error ->
            onError("error")
            Toast.makeText(context,error,Toast.LENGTH_SHORT).show()
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