package com.example.x.coffeetime.application.ui.register

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.api.BindingModel.CreateUserModel
import kotlinx.android.synthetic.main.register_fragment.*

class RegisterFragment : Fragment() {


    private lateinit var registerViewModel: RegisterViewModel
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.register_fragment, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerViewModel = ViewModelProviders.of(this,
                Injection.provideViewModelFactory(context!!)).get(RegisterViewModel::class.java)


        regGoBack?.setOnClickListener {
            Toast.makeText(context,"Clicked the back button",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_register_to_Login)
        }

        setRegisterButtonListener()

    }

    private fun setRegisterButtonListener(){
        regButton?.setOnClickListener {
            registerProgressBar.visibility = View.VISIBLE
            var email: String = regEmail?.text.toString()
            var password: String = regPassword?.text.toString()
            var confirmPassword: String = regConfirmPassword?.text.toString()

            var createUserModel = CreateUserModel(email,password,confirmPassword)

            var isValidEmail : Boolean = registerViewModel.validator.validateEmail(regEmail)
            var isValidPassword : Boolean = registerViewModel.validator.validatePassword(regPassword,regConfirmPassword)

            if(isValidEmail && isValidPassword){
                registerViewModel.register(createUserModel,context,{ success ->
                    mHandler.post {
                        registerProgressBar.visibility = View.GONE
                    }
                    regEmail?.text?.clear()
                    regPassword?.text?.clear()
                    regConfirmPassword?.text?.clear()
                }, {error ->
                    mHandler.post {
                        registerProgressBar.visibility = View.GONE
                    }
                    Log.d("error","error placeholder")
                }) }
        }
    }

}
