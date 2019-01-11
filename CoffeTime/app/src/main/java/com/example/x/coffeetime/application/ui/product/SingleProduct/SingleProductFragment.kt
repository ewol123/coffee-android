package com.example.x.coffeetime.application.ui.product.SingleProduct

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.Injection.provideOrderQuantity
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_single_item_fragment.*

class SingleProductFragment : Fragment() {

    private lateinit var singleViewModel: SingleProductViewModel
    private val mHandler: Handler = Handler(Looper.getMainLooper())



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




        viewOrdersBtn.setOnClickListener {
            findNavController().navigate(R.id.action_singleItem_to_cart)
        }

        observeCart(barcode,coffeeId)

        if (coffeeId != 0) {

            observeSingleCoffee(coffeeId)

            observeFavorites(coffeeId)
        }



    }

    /*
     * Kosár figyelése
     */
    private fun observeCart(barcode:String?,coffeeId:Int?){
        singleViewModel.cart.observe(this, Observer { cart ->
            initOrder(cart, coffeeId!!, productQuantity, removeProduct, addProduct, provideOrderQuantity(barcode,coffeeId.toString()))
        })
    }

    /*
     * Termék adatainak figyelése
     */
    private fun observeSingleCoffee(coffeeId: Int?){
        singleViewModel.coffeeById(coffeeId!!).observe(this, Observer { coffee ->
            if (coffee!!.isNotEmpty()) {
                initDetails(coffee)
            }
        })
    }

    /*
     * Kedvencek figyelése
     */
    private fun observeFavorites(coffeeId: Int?){
        singleViewModel.favorites.observe(this,Observer{favorites ->
            if(favorites!!.isEmpty()){
                removeFav.visibility = View.GONE
                addFav.visibility = View.VISIBLE

            }
            else if(favorites.isNotEmpty()){
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
    }

    /*
     * Termék adatainak beállítása
     */
    @SuppressLint("SetTextI18n")
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


    /*
     * A termék kosárban lévő adatainak figyelése
     */
    private fun initOrder(
            cartList: List<Cart>?,
            coffeeId: Int,
            productQuantity: TextView,
            removeProduct: FloatingActionButton,
            addProduct: FloatingActionButton,
            orderQuantityModel: OrderQuantityModel
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


        /*
         * Termék mennyiségének csökkentése
         */
        removeProduct.setOnClickListener {
            singleProductProgress.visibility = View.VISIBLE
                singleViewModel.decreaseProduct(orderQuantityModel, {success ->
                    mHandler.post {
                        singleProductProgress.visibility = View.GONE
                    }
                }, {error ->
                    mHandler.post {
                        singleProductProgress.visibility = View.GONE
                    }


                })
        }

        /*
         * Termék mennyiségének növelése
         */
        addProduct.setOnClickListener {
            singleProductProgress.visibility = View.VISIBLE
                singleViewModel.increaseProduct(orderQuantityModel,  {success ->
                    mHandler.post {
                        singleProductProgress.visibility = View.GONE
                    }
                }, {error ->
                    mHandler.post {
                        singleProductProgress.visibility = View.GONE
                    }
                    Toast.makeText(context,error, Toast.LENGTH_SHORT).show()


                })
        }

        /*
         * Kedvenc hozzáadása
         */
        addFav.setOnClickListener {
            singleProductProgress.visibility = View.VISIBLE

            singleViewModel.addFavorite(coffeeId,{success ->
                mHandler.post {
                    singleProductProgress.visibility = View.GONE
                }

            }, {error ->
                mHandler.post {
                    singleProductProgress.visibility = View.GONE
                }
                Toast.makeText(context,error, Toast.LENGTH_SHORT).show()

            })
        }

        /*
         * Kedvenc eltávolítása
         */
        removeFav.setOnClickListener {
            singleProductProgress.visibility = View.VISIBLE

            singleViewModel.deleteFavorite(coffeeId,{success ->
                mHandler.post {
                    singleProductProgress.visibility = View.GONE
                }
            }, {error ->
                mHandler.post {
                    singleProductProgress.visibility = View.GONE
                }
                Toast.makeText(context,error, Toast.LENGTH_SHORT).show()


            })
        }

    }
}
