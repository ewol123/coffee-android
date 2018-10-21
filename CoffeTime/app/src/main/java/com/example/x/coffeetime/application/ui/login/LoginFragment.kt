package com.example.x.coffeetime.application.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.example.x.coffeetime.R

class LoginFragment : Fragment() {

    @JvmField
    @BindView(R.id.loginButton)
    var loginButton: Button? = null

    @JvmField
    @BindView(R.id.loginEmail)
    var loginEmail: EditText? = null

    @JvmField
    @BindView(R.id.loginPassword)
    var loginPassword: EditText? = null

    @JvmField
    @BindView(R.id.sign_up_text)
    var signUpText: TextView? = null

    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.login_fragment, container, false)
        ButterKnife.bind(this,view)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModel.delete()
        super.onActivityCreated(savedInstanceState)
        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        viewModel.token.observe(this, Observer { token ->
            if(token!!.isNotEmpty()){
                findNavController().navigate(R.id.to_menu, null)
            }
            })

        loginButton?.setOnClickListener({
            var email : String = loginEmail?.text.toString()
            var password : String = loginPassword?.text.toString()
            viewModel.login(email, password,context)
        })

        signUpText?.setOnClickListener {
            findNavController().navigate(R.id.go_to_register)
        }

    }




}
