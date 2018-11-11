package com.example.x.coffeetime.application.ui.product.SingleProduct

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_single_item_fragment.*

class SingleProductFragment : Fragment() {

    private lateinit var singleViewModel: SingleProductViewModel

    companion object {
        fun newInstance() = SingleProductFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.product_single_item_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        singleViewModel = ViewModelProviders.of(this, Injection.provideProductViewModelFactory(context!!))
                .get(SingleProductViewModel::class.java)

        var id = arguments?.getInt("id") ?: 0
        var coffeeId = arguments?.getInt("coffeeId") ?: 0

        viewOrdersBtn.setOnClickListener {
            findNavController().navigate(R.id.action_singleItem_to_cart)
        }


        if(id != 0){
            singleViewModel.coffeeQuantityById(id).observe(this,Observer{ cart ->
                Log.d("cart:", cart.toString())
                var quantity = cart?.get(0)?.quantity
                productQuantity.text = quantity.toString()
            })

        }


        if(coffeeId != 0 ){
        singleViewModel.coffeeById(coffeeId).observe(this, Observer { coffee ->
            Log.d("id", coffeeId.toString())
            Log.d("coffee:", coffee.toString())
            if(coffee!!.isNotEmpty()){
                Picasso.get()
                        .load(coffee[0].imagePath)
                        .fit()
                        .centerCrop()
                        .into(coffeeImage)
                coffeeName.text = coffee[0].name
                coffeeDescription.text = coffee[0].description
                coffeePrice.text = "$ ${coffee[0].price}/ea"

                for(i in 1..coffee[0].strength){

                    var  param: LinearLayout.LayoutParams =
                     LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)

                    var image = ImageView(context)
                    image.layoutParams = param
                    image.id = i
                    image.setPadding(5,0,5,0)
                    image.setImageResource(R.drawable.ic_coffee_bean)
                    coffeeBeanLayout.addView(image)
                  }
            }
        })
        }

            removeProduct.setOnClickListener {
                Log.d("id+coffeeId:", "id: $id coffeeId: $coffeeId")
            }

            addProduct.setOnClickListener {
                Log.d("id+coffeeId:", "id: $id coffeeId: $coffeeId")

            }


    }



}
