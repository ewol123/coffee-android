package com.example.x.coffeetime.application.ui.cart

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.model.Cart
import kotlinx.android.synthetic.main.cart_fragment.*

class CartFragment : Fragment() {

    companion object {
        fun newInstance() = CartFragment()
    }

    private lateinit var viewModel: CartViewModel
    private var totalPrice : Int = 0
    private val adapter = CartAdapter(arrayListOf(),{cart ->
        val bundle = Bundle()
        bundle.putInt("coffeeId", cart.coffeeId)
        bundle.putInt("id", cart.id)
        findNavController().navigate(R.id.action_cart_to_SingleItem,bundle)
    },{id ->
        Log.d("Add product", id.toString())
    }, {id ->
        Log.d("Remove product", id.toString())
    } )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cart_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)



        cartList.layoutManager =  LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        cartList.adapter = adapter

        viewModel.token.observe(this, Observer{ token ->
            if(token!!.isNotEmpty()){
                Log.d("ORDER-TOKEN", token[0].token)
                viewModel.initCart(token[0].token)
            }
        })

        viewModel.cart.observe(this, Observer<List<Cart>>{

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

    }

}
