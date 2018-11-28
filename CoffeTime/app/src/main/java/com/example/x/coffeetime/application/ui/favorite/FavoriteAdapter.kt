package com.example.x.coffeetime.application.ui.favorite

import android.media.Image
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Favorite
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.favorite_item.view.*
import kotlinx.android.synthetic.main.product_item.view.*

class FavoriteAdapter(
                  val listener: (Favorite) -> Unit,
                  val addItem: (Int) -> Unit,
                  val deleteItem: (Int) ->Unit,
                  val favoriteListener: (Int) -> Unit
                  ) : RecyclerView.Adapter<FavoriteAdapter.RecyclerViewHolder>() {

    private var favorites: List<Favorite> = emptyList()
    private var cart : List<Cart> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false))
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        var favorite: Favorite = favorites[position]

        var imagePath = favorite.imagePath
        var name = favorite.name
        var price = favorite.price

        Picasso.get()
                .load(imagePath)
                .transform(RoundedCornersTransformation(20,5))
                .resize(150,150).into(holder!!.mImage)


        holder.mCoffeeName.text = name
        holder.mCoffeePrice.text = "$price" + "$/ea"

        holder.mfabRemoveProductCart.hide()
        holder.mCoffeeQuantity.text = "Quantity: 0"
        holder.mCoffeeTotalPrice.text = "in cart: $0"



        if(cart.isNotEmpty()){
            var cartItem = cart.find { item -> item.name == favorite.name }

            if(cartItem !=null ){
                holder.mfabRemoveProductCart.show()
                holder.mCoffeeQuantity.text = "Quantity: ${cartItem.quantity}"
                holder.mCoffeeTotalPrice.text = "in cart: $${cartItem.totalPrice}"
            }
        }

        holder.itemView.setOnClickListener {
            listener(favorite)
        }

        holder.mfabAddProductCart.setOnClickListener {
            addItem(favorite.id)
        }

        holder.mfabRemoveProductCart.setOnClickListener {
            deleteItem(favorite.id)
        }


        holder.mFavBtn.setOnClickListener {
            favoriteListener(favorite.id)
        }


    }


    fun setCart(list: List<Cart>){
        cart = list
        Log.d("LIST:",cart.toString())
        notifyDataSetChanged()
    }

    fun addToFav(favs: List<Favorite>) {
        this.favorites = favs
        notifyDataSetChanged()
    }

    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImage = itemView.findViewById<ImageView>(R.id.fav_c_image)!!
        var mCoffeeName = itemView.findViewById<TextView>(R.id.fav_c_name)!!
        var mCoffeePrice = itemView.findViewById<TextView>(R.id.fav_c_price)!!
        var mCoffeeTotalPrice = itemView.findViewById<TextView>(R.id.fav_c_total_price)!!
        var mCoffeeQuantity = itemView.findViewById<TextView>(R.id.fav_c_quantity)!!
        var mfabAddProductCart = itemView.findViewById<FloatingActionButton>(R.id.favProductAdd)!!
        var mfabRemoveProductCart = itemView.findViewById<FloatingActionButton>(R.id.favProductRemove)!!
        var mFavBtn = itemView.findViewById<ImageButton>(R.id.favRemove)
    }

}