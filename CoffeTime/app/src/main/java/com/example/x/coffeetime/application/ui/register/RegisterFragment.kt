package com.example.x.coffeetime.application.ui.register

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import butterknife.BindView
import butterknife.ButterKnife

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.api.BindingModel.CreateUserModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty

class RegisterFragment : Fragment() {

    @JvmField
    @BindView(R.id.regGoBack)
    var regBackButton: Button? = null

    @JvmField
    @BindView(R.id.regButton)
    var regButton: Button? = null

    @JvmField
    @BindView(R.id.regEmail)
    var regEmail: EditText? = null

    @JvmField
    @BindView(R.id.regPassword)
    var regPassword: EditText? = null

    @JvmField
    @BindView(R.id.regConfirmPassword)
    var regConfirmPassword: EditText? = null



    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.register_fragment, container, false)
        ButterKnife.bind(this,view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel

        regBackButton?.setOnClickListener {
            Toast.makeText(context,"Clicked the back button",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.go_to_login)
        }

        regButton?.setOnClickListener {

            var email: String = regEmail?.text.toString()
            var password: String = regPassword?.text.toString()
            var confirmPassword: String = regConfirmPassword?.text.toString()

            var createUserModel = CreateUserModel(email,password,confirmPassword)

            var isValidEmail : Boolean = viewModel.validator.validateEmail(regEmail)
            var isValidPassword : Boolean = viewModel.validator.validatePassword(regPassword,regConfirmPassword)

            if(isValidEmail && isValidPassword){
            viewModel.register(createUserModel,context,{success ->
                regEmail?.text?.clear()
                regPassword?.text?.clear()
                regConfirmPassword?.text?.clear()
            }, {error ->
                Log.d("error","error placeholder")
            }) }
        }

    }

}
