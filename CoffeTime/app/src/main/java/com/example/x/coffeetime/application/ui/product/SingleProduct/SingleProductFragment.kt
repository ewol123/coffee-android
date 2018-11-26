package com.example.x.coffeetime.application.ui.product.SingleProduct

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_single_item_fragment.*

class SingleProductFragment : Fragment() {

    private lateinit var singleViewModel: SingleProductViewModel
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    companion object {
        fun newInstance() = SingleProductFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.product_single_item_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        singleViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(context!!))
                .get(SingleProductViewModel::class.java)

        var coffeeId = arguments?.getInt("coffeeId") ?: 0


        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = "coffeeshop123"
        val barcode = sharedPref?.getString(getString(R.string.preference_file_key), defaultValue)

        var orderQuantityModel = OrderQuantityModel(
                TableNum = barcode!!,
                Quantity = "1",
                ProductId = coffeeId.toString())

        val tokenPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val defaultToken = "0"
        val token = tokenPref?.getString(getString(R.string.preference_token_key), defaultToken)

        viewOrdersBtn.setOnClickListener {
            findNavController().navigate(R.id.action_singleItem_to_cart)
        }

        singleViewModel.cart.observe(this, Observer { cart ->
            initOrder(cart, coffeeId, productQuantity, removeProduct, addProduct, orderQuantityModel, token)
        })

        if (coffeeId != 0) {

            singleViewModel.coffeeById(coffeeId).observe(this, Observer { coffee ->
                Log.d("id", coffeeId.toString())
                Log.d("coffee:", coffee.toString())

                if (coffee!!.isNotEmpty()) {
                    initDetails(coffee)
                }
            })
        }


        singleViewModel.favorites.observe(this,Observer{favorites ->
            if(favorites!!.isEmpty()){
                removeFav.visibility = View.GONE
                addFav.visibility = View.VISIBLE

            }
            else if(favorites!!.isNotEmpty()){
                var favoriteItem = favorites.find { favorite -> favorite.id == coffeeId }

                if(favoriteItem != null){
                    removeFav.visibility = View.VISIBLE
                    addFav.visibility = View.GONE
                } else {
                    removeFav.visibility = View.GONE
                    addFav.visibility = View.VISIBLE
                }


            }
        })

        addFav.setOnClickListener {
            singleProductProgress.visibility = View.VISIBLE

            singleViewModel.addFavorite(coffeeId,token!!,{success ->
                mHandler.post {
                    singleProductProgress.visibility = View.GONE
                }

            }, {error ->
                mHandler.post {
                    singleProductProgress.visibility = View.GONE
                }
            })
        }
        removeFav.setOnClickListener {
            singleProductProgress.visibility = View.VISIBLE

            singleViewModel.deleteFavorite(coffeeId,token!!,{success ->
                mHandler.post {
                    singleProductProgress.visibility = View.GONE
                }
            }, {error ->
                mHandler.post {
                    singleProductProgress.visibility = View.GONE
                }
            })
        }

    }

    private fun initDetails(coffee: List<Coffee>) {
        Picasso.get()
                .load(coffee[0].imagePath)
                .fit()
                .centerCrop()
                .into(coffeeImage)
        coffeeName.text = coffee[0].name
        coffeeDescription.text = coffee[0].description
        coffeePrice.text = "$ ${coffee[0].price}/ea"

        for (i in 1..coffee[0].strength) {

            var param: LinearLayout.LayoutParams =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            var image = ImageView(context)
            image.layoutParams = param
            image.id = i
            image.setPadding(5, 0, 5, 0)
            image.setImageResource(R.drawable.ic_coffee_bean)
            coffeeBeanLayout.addView(image)
        }
    }


    private fun initOrder(
            cartList: List<Cart>?,
            coffeeId: Int,
            productQuantity: TextView,
            removeProduct: FloatingActionButton,
            addProduct: FloatingActionButton,
            orderQuantityModel: OrderQuantityModel,
            token: String?
    ) {
        if (coffeeId != 0) {
            var cartItem = cartList?.find { item -> item.coffeeId == coffeeId }
            var quantity = cartItem?.quantity ?: 0
            productQuantity.text = quantity.toString()

            if(cartItem == null){
                removeProduct.hide()
            } else {
                removeProduct.show()
            }
        }

        removeProduct.setOnClickListener {
            singleProductProgress.visibility = View.VISIBLE
            Log.d("id+coffeeId:", "id: $id coffeeId: $coffeeId")
                singleViewModel.decreaseProduct(orderQuantityModel, token!!, {success ->
                    mHandler.post {
                        singleProductProgress.visibility = View.GONE
                    }
                }, {error ->
                    mHandler.post {
                        singleProductProgress.visibility = View.GONE
                    }
                })
        }

        addProduct.setOnClickListener {
            singleProductProgress.visibility = View.VISIBLE
            Log.d("id+coffeeId:", "id: $id coffeeId: $coffeeId")
                singleViewModel.increaseProduct(orderQuantityModel, token!!, {success ->
                    mHandler.post {
                        singleProductProgress.visibility = View.GONE
                    }
                }, {error ->
                    mHandler.post {
                        singleProductProgress.visibility = View.GONE
                    }
                })
        }
    }


}
