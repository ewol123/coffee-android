package com.example.x.coffeetime.application.ui.login

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.navigation.fragment.findNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.ui.product.ProductFragment

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

    @JvmField
    @BindView(R.id.loginProgressBar)
    var loginProgressBar: ProgressBar? = null

    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.login_fragment, container, false)
        ButterKnife.bind(this,view)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        viewModel.delete()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPref?.edit()?.clear()?.apply()
        loginButton?.setOnClickListener({

            var email : String = loginEmail?.text.toString()
            var password : String = loginPassword?.text.toString()
            loginProgressBar?.visibility = View.VISIBLE
             viewModel.login(email, password,context, {success ->
                 loginProgressBar?.visibility = View.GONE
                 findNavController().navigate(R.id.to_menu,null)
             }, {error ->
                 loginProgressBar?.visibility = View.GONE
             })
        })

        signUpText?.setOnClickListener {
            findNavController().navigate(R.id.go_to_register)
        }

    }




}
