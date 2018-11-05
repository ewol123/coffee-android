package com.example.x.coffeetime.application.ui.product

import android.app.ActionBar
import android.content.Context
import android.support.design.shape.RoundedCornerTreatment
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.model.Coffee
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.product_item.view.*
import butterknife.ButterKnife




/**
 * View Holder for a Coffee RecyclerView list item.
 */
class CoffeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    private val name: TextView = view.findViewById(R.id.coffee_name)
    private val image: ImageView = view.findViewById(R.id.coffee_image)
    private val price: TextView = view.findViewById(R.id.coffee_price)
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
        fun create(parent: ViewGroup): CoffeeViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_item, parent, false)
            return CoffeeViewHolder(view)
        }
    }
}