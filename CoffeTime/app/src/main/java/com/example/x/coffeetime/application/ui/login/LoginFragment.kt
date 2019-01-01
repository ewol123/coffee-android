package com.example.x.coffeetime.application.ui.login

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.navigation.fragment.findNavController
import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {


    private lateinit var loginViewModel: LoginViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        loginViewModel = ViewModelProviders.of(this,
                Injection.provideViewModelFactory(context!!)).get(LoginViewModel::class.java)


        loginViewModel.delete()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPref?.edit()?.clear()?.apply()

        setLoginButtonListener()

        sign_up_text?.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_Register)
        }

        resetPass?.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_resetPass)
        }

    }

    private fun setLoginButtonListener(){
        loginButton?.setOnClickListener {

            var email : String = loginEmail?.text.toString()
            var password : String = loginPassword?.text.toString()
            loginProgressBar?.visibility = View.VISIBLE
            loginViewModel.login(email, password,context, { success ->
                loginProgressBar?.visibility = View.GONE
                findNavController().navigate(R.id.to_menu,null)
            }, {error ->
                loginProgressBar?.visibility = View.GONE
                Toast.makeText(context,error, Toast.LENGTH_SHORT).show()
            })
        }
    }




}
