package com.example.x.coffeetime.application

import android.widget.EditText
import com.wajahatkarim3.easyvalidation.core.rules.BaseRule
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.product_fragment.view.*

class Validator{

    fun validateEmail (email :EditText?): Boolean{

        var isEmpty = email?.text.toString().nonEmpty { msg ->
            email?.error = msg
        }

        var isEmail =  email?.text.toString().validEmail { msg ->
            email?.error = msg
        }
        return isEmpty && isEmail
    }


    fun validatePassword(password: EditText?, confirmPassword:EditText?) : Boolean {


     var isValidPassword = password!!.validator()
                .nonEmpty()
                .atleastOneLowerCase()
                .atleastOneSpecialCharacters()
                .atleastOneUpperCase()
                .minLength(6)
                .addRule(PasswordRule(confirmPassword?.text.toString()))
                .addErrorCallback {
                   password.error = it

                }
                .check()

        return isValidPassword
    }
}

class PasswordRule(
       var confirmPassword: String
) : BaseRule
{
    override fun validate(text: String) : Boolean
    {
        return text.equals(confirmPassword)
    }

    override fun getErrorMessage() : String
    {
        return "Passwords don't match"
    }
}