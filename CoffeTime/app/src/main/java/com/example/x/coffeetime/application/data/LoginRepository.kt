package com.example.x.coffeetime.application.data

import android.arch.lifecycle.LiveData
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.db.TokenDao
import com.example.x.coffeetime.application.model.Token
import java.util.concurrent.Executor


/**
 * Repository class that works with local and remote data sources.
 */
class LoginRepository(
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
        Log.d("LoginRepository", "New login")

        ApiService.requestAuth( mainService, username, password, { token ->
            ioExecutor.execute {
            tokenDao.deleteAll()
            var newToken = Token(token)
            tokenDao.insert(newToken)
            }
            onSuccess("success")
        }, { error ->
            Log.d("error","not inserted, $error")
            Toast.makeText(context,error,Toast.LENGTH_SHORT).show()
            onError("error")
        })

    }
     val token: LiveData<List<Token>> = tokenDao.getToken()



    }