package com.example.x.coffeetime.application.ui.reset_password

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
import kotlinx.android.synthetic.main.reset_pass_fragment.*

class ResetPassFragment : Fragment() {



    private lateinit var resetPassViewModel: ResetPassViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.reset_pass_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resetPassViewModel = ViewModelProviders.of(this,
                Injection.provideViewModelFactory(context!!)).get(ResetPassViewModel::class.java)


        resetToLoginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_resetPass_to_login)
        }

        resetToLoginBtn2.setOnClickListener {
            findNavController().navigate(R.id.action_resetPass_to_login)
        }

        sendCodeBtnListener()

        resetPassBtnListener()

    }

    /*
     * Kód elküldése gomb figyelése
     */
    private fun sendCodeBtnListener(){
        sendCodeBtn?.setOnClickListener {
            var email: String = resetPassEmail?.text.toString()

            var isValidEmail : Boolean = resetPassViewModel.validator.validateEmail(resetPassEmail)

            if(isValidEmail){
                codeProgressBar.visibility = View.VISIBLE
                resetPassViewModel.sendPasswordRequest(email,{ success ->
                    codeProgressBar.visibility = View.GONE
                    Toast.makeText(context,"Code has been sent to your email",Toast.LENGTH_SHORT).show()
                    resetPassEmail.text?.clear()
                    resetPassEmail.visibility = View.GONE
                    sendCodeBtn.visibility = View.GONE
                    resetToLoginBtn.visibility = View.GONE

                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    sharedPref
                            ?.edit()
                            ?.putString(getString(R.string.email_file_key), email)
                            ?.apply()

                    resetPassCode.visibility = View.VISIBLE
                    resetPassPw.visibility = View.VISIBLE
                    resetPassPwConfirm.visibility = View.VISIBLE
                    resetPassBtn.visibility = View.VISIBLE
                    resetToLoginBtn2.visibility = View.VISIBLE

                }, {error ->
                    codeProgressBar.visibility = View.GONE
                    Toast.makeText(context,"Error, please try again",Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    /*
     * Jelszó visszaállítása gomb figyelése
     */
    private fun resetPassBtnListener(){
        resetPassBtn.setOnClickListener {

            var code: String = resetPassCode?.text.toString()
            var password: String = resetPassPw?.text.toString()

            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            val defaultValue = "empty"
            val email = sharedPref?.getString(getString(R.string.email_file_key), defaultValue) ?: "empty"


            var isValidPassword : Boolean = resetPassViewModel.validator.validatePassword(resetPassPw,resetPassPwConfirm)

            if(isValidPassword && email != "empty" ){
                resetProgressBar.visibility = View.VISIBLE
                resetPassViewModel.provideToken(code,email,password,{ success ->
                    resetProgressBar.visibility = View.GONE
                    Toast.makeText(context,"Password updated",Toast.LENGTH_SHORT).show()
                    resetPassCode.text?.clear()
                    resetPassPw.text?.clear()
                    resetPassPwConfirm.text?.clear()
                }, {error ->
                    resetProgressBar.visibility = View.GONE
                    Toast.makeText(context,"Failed, please try again",Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

}
