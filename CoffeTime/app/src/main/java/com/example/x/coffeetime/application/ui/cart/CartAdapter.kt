package com.example.x.coffeetime.application.ui.cart

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

class CartAdapter(cart: List<Cart>,
                  val listener: (Cart) -> Unit,
                  val addItem: (Int) -> Unit,
                  val removeItem: (Int) -> Unit,
                  val deleteItem: (Int) ->Unit) : RecyclerView.Adapter<CartAdapter.RecyclerViewHolder>() {

    private var listCart: List<Cart> = cart


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listCart.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        var currentCart: Cart = listCart[position]

        var imagePath = currentCart.imagePath
        var name = currentCart.name
        var price = currentCart.price
        var totalPrice = currentCart.totalPrice
        var quantity = currentCart.quantity


        Picasso.get()
                .load(imagePath)
                .transform(RoundedCornersTransformation(20,5))
                .resize(150,150).into(holder!!.mImage)


        holder.mCoffeeName.text = name
        holder.mCoffeePrice.text = "$price" + "$/ea"
        holder.mCoffeeTotalPrice.text = "$totalPrice" + "$"
        holder.mCoffeeQuantity.text = "Quantity:" + "$quantity"


        holder.itemView.setOnClickListener {
            listener(currentCart)
        }

        holder.mfabAddProductCart.setOnClickListener {
            addItem(currentCart.coffeeId)
        }

        holder.mfabRemoveProductCart.setOnClickListener {
            removeItem(currentCart.coffeeId)
        }

        holder.mDeleteCartButton.setOnClickListener {
            deleteItem(currentCart.id)
        }

    }

    fun addToCart(listCart: List<Cart>) {
        this.listCart = listCart
        notifyDataSetChanged()
    }


    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImage = itemView.findViewById<ImageView>(R.id.cart_image)!!
        var mCoffeeName = itemView.findViewById<TextView>(R.id.cart_coffee_name)!!
        var mCoffeePrice = itemView.findViewById<TextView>(R.id.cart_coffee_price)!!
        var mCoffeeTotalPrice = itemView.findViewById<TextView>(R.id.coffee_total_price)!!
        var mCoffeeQuantity = itemView.findViewById<TextView>(R.id.cart_coffee_quantity)!!
        var mfabAddProductCart = itemView.findViewById<FloatingActionButton>(R.id.cartAddProduct)!!
        var mfabRemoveProductCart = itemView.findViewById<FloatingActionButton>(R.id.cartSubstractProduct)!!
        var mDeleteCartButton = itemView.findViewById<ImageButton>(R.id.deleteProduct)!!
    }

}