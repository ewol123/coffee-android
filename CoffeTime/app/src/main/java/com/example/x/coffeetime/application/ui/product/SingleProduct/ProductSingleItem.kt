package com.example.x.coffeetime.application.ui.product.SingleProduct

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.ChangeBounds
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.model.Coffee
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.product_single_item_fragment.*

class ProductSingleItem : Fragment() {

    private lateinit var singleProductViewModel: ProductSingleItemViewModel

    companion object {
        fun newInstance() = ProductSingleItem()
    }

    private lateinit var viewModel: ProductSingleItemViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.product_single_item_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        singleProductViewModel = ViewModelProviders.of(this, Injection.provideProductViewModelFactory(context!!))
                .get(ProductSingleItemViewModel::class.java)

        var id = arguments?.getInt("id")



        singleProductViewModel.coffeeById(id!!).observe(this, Observer<List<Coffee>> {

            if(it!!.isNotEmpty()){
                Picasso.get()
                        .load(it[0].imagePath)
                        .fit()
                        .centerCrop()
                        .into(coffeeImage)
                coffeeName.text = it[0].name
                coffeeDescription.text = it[0].description
                coffeePrice.text = it[0].price +"$"

                for(i in 1..it[0].strength){

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



}
