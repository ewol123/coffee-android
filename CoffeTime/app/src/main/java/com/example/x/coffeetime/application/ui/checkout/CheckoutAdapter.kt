package com.example.x.coffeetime.application.ui.checkout

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.model.Cart
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.cart_item.view.*


class CheckoutAdapter(cart: List<Cart>) : RecyclerView.Adapter<CheckoutAdapter.RecyclerViewHolder>() {


    private var listCart: List<Cart> = cart


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.checkout_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listCart.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        var currentCart: Cart = listCart[position]

        var imagePath = currentCart.imagePath
        var name = currentCart.name
        var price = currentCart.price
        var subPrice = currentCart.totalPrice
        var quantity = currentCart.quantity


        Picasso.get()
                .load(imagePath)
                .transform(RoundedCornersTransformation(20,5))
                .resize(100,100).into(holder!!.mImage)


        holder.mCoffeeName.text = name
        holder.mCoffeePrice.text = "$ $price/ea"
        holder.mCoffeeTotalPrice.text =  "$" + "$subPrice"
        holder.mCoffeeQuantity.text = "Quantity:" + "$quantity"



    }

    fun addToCart(listCart: List<Cart>) {
        this.listCart = listCart
        notifyDataSetChanged()
    }

    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImage = itemView.findViewById<ImageView>(R.id.checkout_image)!!
        var mCoffeeName = itemView.findViewById<TextView>(R.id.checkout_coffee_name)!!
        var mCoffeePrice = itemView.findViewById<TextView>(R.id.checkout_coffee_price)!!
        var mCoffeeTotalPrice = itemView.findViewById<TextView>(R.id.checkout_sub_price)!!
        var mCoffeeQuantity = itemView.findViewById<TextView>(R.id.checkout_coffee_quantity)!!

    }

}