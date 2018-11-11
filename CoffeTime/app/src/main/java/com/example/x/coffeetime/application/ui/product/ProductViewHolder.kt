package com.example.x.coffeetime.application.ui.product

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.model.Coffee
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


/**
 * View Holder for a Coffee RecyclerView list item.
 */
class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    private val name: TextView = view.findViewById(R.id.coffee_name)
    private val image: ImageView = view.findViewById(R.id.coffee_image)
    private val price: TextView = view.findViewById(R.id.coffee_price)
    private val fabSubstractProduct: FloatingActionButton = view.findViewById(R.id.fabSubstractProduct)
    private val coffee_item_quantity: TextView = view.findViewById(R.id.coffee_item_quantity)
    private val coffee_item_total_price: TextView = view.findViewById(R.id.coffee_item_total_price)
    private var coffee: Coffee? = null

    fun bind(coffee: Coffee?) {
        if (coffee == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            image.visibility = View.GONE
            price.text = resources.getString(R.string.loading)
        } else {
            showCoffeeData(coffee, itemView.context)
        }

    }

    private fun showCoffeeData(coffee: Coffee, context: Context) {
        this.coffee = coffee
        name.text = coffee.name
        price.text = "$" + coffee.price + "/ea"
        Picasso.get()
                .load(coffee.imagePath)
                .transform(RoundedCornersTransformation(20,5))
                .resize(150,150).into(image)
    }

    companion object {
        fun create(parent: ViewGroup): ProductViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_item, parent, false)
            return ProductViewHolder(view)
        }
    }
}