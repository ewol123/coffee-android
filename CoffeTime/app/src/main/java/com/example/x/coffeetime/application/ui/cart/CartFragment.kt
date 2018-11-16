package com.example.x.coffeetime.application.ui.cart

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.model.Cart
import kotlinx.android.synthetic.main.cart_fragment.*

class CartFragment : Fragment() {

    companion object {
        fun newInstance() = CartFragment()
    }

    private lateinit var cartViewModel: CartViewModel
    private var totalPrice : Int = 0
    private val mHandler: Handler = Handler(Looper.getMainLooper())


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cart_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cartViewModel = ViewModelProviders.of(this,
                Injection.provideViewModelFactory(context!!)).get(CartViewModel::class.java)

                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                val defaultValue = "coffeeshop123"
                val barcode = sharedPref?.getString(getString(R.string.preference_file_key), defaultValue)
                val token = sharedPref?.getString(getString(R.string.preference_token_key),defaultValue)

                cartViewModel.initCart(token!!)

                var adapter = CartAdapter(arrayListOf(),{cart ->
                    val bundle = Bundle()
                    bundle.putInt("coffeeId", cart.coffeeId)
                    findNavController().navigate(R.id.action_cart_to_SingleItem,bundle)
                },{id ->
                    Log.d("Add product", id.toString())
                    cartProgress.visibility = View.VISIBLE
                    var orderQuantityModel = OrderQuantityModel(
                            TableNum = barcode!!,
                            Quantity = "1",
                            ProductId = id.toString())
                    cartViewModel.increaseProduct(orderQuantityModel,  token, {success ->
                        mHandler.post {
                            cartProgress.visibility = View.GONE
                        }
                    }, {error ->
                        mHandler.post {
                            cartProgress.visibility = View.GONE
                        }
                    })

                }, {id ->
                    Log.d("Remove product", id.toString())
                    cartProgress.visibility = View.VISIBLE
                    var orderQuantityModel = OrderQuantityModel(
                            TableNum = barcode!!,
                            Quantity = "1",
                            ProductId = id.toString())
                    cartViewModel.decreaseProduct(orderQuantityModel, token, {success ->
                        mHandler.post {
                            cartProgress.visibility = View.GONE
                        }
                    }, {error ->
                        mHandler.post {
                            cartProgress.visibility = View.GONE
                        }
                    })

                } )

                cartList.layoutManager =  LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

                cartList.adapter = adapter


        cartViewModel.cart.observe(this, Observer<List<Cart>>{

            if(it!!.isNotEmpty()){
                cartEmpty.visibility = View.GONE

                adapter.addToCart(it)


                totalPrice = 0
                it.forEach{
                    totalPrice += it.totalPrice
                }

                cartList.visibility = View.VISIBLE
                cartSubtotal.visibility = View.VISIBLE
                cartPrice.visibility = View.VISIBLE
                cartIncludes.visibility = View.VISIBLE
                cartButton.visibility = View.VISIBLE


                cartPrice.text = "$" + "$totalPrice"

            }
            else {
                cartEmpty.visibility = View.VISIBLE

                cartList.visibility = View.GONE
                cartSubtotal.visibility = View.GONE
                cartPrice.visibility = View.GONE
                cartIncludes.visibility = View.GONE
                cartButton.visibility = View.GONE


            }
        })

        cartButton.setOnClickListener {
            findNavController().navigate(R.id.action_cart_to_checkout)
        }
    }

}
