package com.example.x.coffeetime.application.ui.checkout

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.x.coffeetime.R
import kotlinx.android.synthetic.main.checkout_fragment.*
import android.text.Spannable
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.text.style.StyleSpan
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.braintreepayments.api.BraintreeFragment
import com.braintreepayments.api.PayPal
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener
import com.braintreepayments.api.models.PayPalRequest
import com.braintreepayments.api.models.PayPalAccountNonce
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.ui.cart.CheckoutViewModel
import kotlinx.android.synthetic.main.cart_fragment.*
import kotlinx.android.synthetic.main.product_fragment.*


class CheckoutFragment : Fragment() {
    private lateinit var checkoutViewModel: CheckoutViewModel
    private var totalPrice : Int = 0
    private lateinit var mBraintreeFragment : BraintreeFragment
    private lateinit var adapter: CheckoutAdapter
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.checkout_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkoutViewModel = ViewModelProviders.of(this,
                Injection.provideViewModelFactory(context!!)).get(CheckoutViewModel::class.java)

        mBraintreeFragment = BraintreeFragment.newInstance(activity, "sandbox_dw398mmw_czrg5hprsvbkxws3")

        setPaypalText()

        initAdapter()

        observeCart()

        setCashPayClickListener()

        setPaypalPayClickListener()

    }

     /*
     * PayPal gombunk szövegének megformázása
     */
    private fun setPaypalText(){
        val normalText = "Pay with"
        val styledText = "PayPal"
        val paypalStr = SpannableString(normalText + styledText)
        paypalStr.setSpan(StyleSpan(Typeface.BOLD), 8, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        paypalStr.setSpan(StyleSpan(Typeface.ITALIC), 8, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        paypalStr.setSpan(RelativeSizeSpan(1.6f), 8, 14, 0)
        payPaypalBtn.text = paypalStr
    }

     /*
     * Adapter beállítása
     */
    private fun initAdapter(){
          adapter = CheckoutAdapter(arrayListOf())
         checkoutList.layoutManager =  LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
         checkoutList.adapter = adapter
    }

    /*
     * Figyeljük a kosár változását
     */
    @SuppressLint("SetTextI18n")
    private fun observeCart(){
        checkoutViewModel.cart.observe(this, Observer<List<Cart>>{

            if(it!!.isNotEmpty()){
                adapter.addToCart(it)

                totalPrice = 0
                it.forEach{
                    totalPrice += it.totalPrice
                }
                checkoutPrice.text = "$$totalPrice"
            }
        })
    }

     /*
     * Készpénzes fizetés gomb click esemény figyelése
     */
    private fun setCashPayClickListener(){
        payCashBtn.setOnClickListener {
            checkoutProgress.visibility = View.VISIBLE
            checkoutViewModel.updateOrder({success ->
                Toast.makeText(context,"Order placed",Toast.LENGTH_SHORT).show()
                mHandler.post {
                    checkoutProgress.visibility = View.GONE
                }
                findNavController().navigate(R.id.action_checkout_to_menu)
            },{error ->
                Toast.makeText(context,"Error placing order",Toast.LENGTH_SHORT).show()
                mHandler.post {
                    checkoutProgress.visibility = View.GONE
                }
            },"cash")

        }
    }

    /*
   * Paypalos fizetés gomb click esemény figyelése, a visszakapott eredmény figyelésével
   */
    private fun setPaypalPayClickListener(){
        payPaypalBtn.setOnClickListener {
            val request = PayPalRequest(totalPrice.toString())
                    .currencyCode("USD")
                    .intent(PayPalRequest.INTENT_AUTHORIZE)
            PayPal.requestOneTimePayment(mBraintreeFragment, request)
        }

        mBraintreeFragment.addListener(PaymentMethodNonceCreatedListener {
            val nonce = it.nonce
            if (it is PayPalAccountNonce) {
                Log.d("nonce", nonce)

                checkoutProgress.visibility = View.VISIBLE
                checkoutViewModel.updateOrder({success ->
                    Toast.makeText(context,"Order placed",Toast.LENGTH_SHORT).show()
                    mHandler.post {
                        checkoutProgress.visibility = View.GONE
                    }
                    findNavController().navigate(R.id.action_checkout_to_menu)
                },{error ->
                    Toast.makeText(context,"Error placing order",Toast.LENGTH_SHORT).show()
                    mHandler.post {
                        checkoutProgress.visibility = View.GONE
                    }


                },"online")
            }

        })
    }





}
