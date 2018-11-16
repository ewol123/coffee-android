package com.example.x.coffeetime.application.ui.checkout

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.x.coffeetime.R
import kotlinx.android.synthetic.main.checkout_fragment.*
import android.text.Spannable
import android.graphics.Typeface
import android.text.style.StyleSpan
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import com.braintreepayments.api.BraintreeFragment
import com.braintreepayments.api.PayPal
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener
import com.braintreepayments.api.models.PayPalRequest
import com.braintreepayments.api.models.PayPalAccountNonce
import com.braintreepayments.api.models.PaymentMethodNonce






class CheckoutFragment : Fragment() {


    companion object {
        private const val DROP_IN_REQUEST: Int = 150
    }

    private lateinit var viewModel: CheckoutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.checkout_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CheckoutViewModel::class.java)
        // TODO: Use the ViewModel
        val styledText = "PayPal"
        val normalText = "Pay with "
        val paypalStr = SpannableString(normalText + styledText)
        paypalStr.setSpan(StyleSpan(Typeface.BOLD), 9, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        paypalStr.setSpan(StyleSpan(Typeface.ITALIC), 9, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        paypalStr.setSpan(RelativeSizeSpan(1.6f), 9, 15, 0)
        payPaypalBtn.text = paypalStr
        var mBraintreeFragment = BraintreeFragment.newInstance(activity, "sandbox_dw398mmw_czrg5hprsvbkxws3")
        mBraintreeFragment.addListener(PaymentMethodNonceCreatedListener {
            val nonce = it.nonce

            if (it is PayPalAccountNonce) {
                Log.d("nonce", nonce)
                // Access additional information
                it.
                val email = it.email
                val firstName = it.firstName
                val lastName = it.lastName
                val phone = it.phone

                // See PostalAddress.java for details
                val billingAddress = it.billingAddress
                val shippingAddress = it.shippingAddress
            }
        })

        payPaypalBtn.setOnClickListener {


            val request = PayPalRequest("30")
                    .currencyCode("USD")
                    .intent(PayPalRequest.INTENT_AUTHORIZE)
            PayPal.requestOneTimePayment(mBraintreeFragment, request)




        }


    }

}
