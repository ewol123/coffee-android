package com.example.x.coffeetime.application.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.x.coffeetime.application.api.ApiService
import com.example.x.coffeetime.application.api.BindingModel.CreateUserModel
import com.example.x.coffeetime.application.api.MainService
import com.example.x.coffeetime.application.db.TokenDao
import com.example.x.coffeetime.application.model.Token
import java.util.concurrent.Executor

class RegisterRepository(
        private val ApiService: ApiService,
        private val mainService: MainService
){



     fun register(createUserModel: CreateUserModel, context: Context?, onSuccess: (success: String) -> Unit,
                  onError: (error: String) -> Unit){
         Log.d("RegisterRepository", "New Registration!")

         ApiService.register(mainService,createUserModel, { success ->
             Log.d("register-success","success: $success")
             Toast.makeText(context,"Register successful, please log-in",Toast.LENGTH_SHORT).show()
             onSuccess("success")
         }, {error ->
             Log.d("register-failed","failed with: $error")
             onError("error")
             Toast.makeText(context,error,Toast.LENGTH_SHORT).show()
         })
     }

}