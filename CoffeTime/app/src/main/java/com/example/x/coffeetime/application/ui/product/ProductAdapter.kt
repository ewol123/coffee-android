package com.example.x.coffeetime.application.ui.product

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Coffee
import kotlinx.android.synthetic.main.product_item.view.*


/**
 * Adapter for the list of coffees.
 */
class ProductAdapter(val listener: (Coffee) -> Unit,
                     val increaseProduct: (Int) -> Unit,
                     val substractProduct: (Int) -> Unit)
    : PagedListAdapter<Coffee, RecyclerView.ViewHolder>(COFFEE_COMPARATOR) {

    private var cart : List<Cart> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val coffeeItem = getItem(position)
        if (coffeeItem != null) {
            (holder as ProductViewHolder).bind(coffeeItem)



            holder.itemView.fabSubstractProduct.hide()
            holder.itemView.coffee_item_quantity.visibility = View.GONE
            holder.itemView.coffee_item_total_price.visibility = View.GONE

            if(cart.isNotEmpty()){
                var cartItem = cart.find { item -> item.name == coffeeItem.name }

                if(cartItem !=null ){
                holder.itemView.fabSubstractProduct.show()
                holder.itemView.coffee_item_quantity.visibility = View.VISIBLE
                holder.itemView.coffee_item_total_price.visibility = View.VISIBLE

                holder.itemView.coffee_item_quantity.text = "Quantity: ${cartItem.quantity}"
                holder.itemView.coffee_item_total_price.text = "$ ${cartItem.totalPrice}"
                }
            }


            holder.itemView.setOnClickListener {
                listener(coffeeItem)
            }

            holder.itemView.fabAddProduct.setOnClickListener {
                increaseProduct(coffeeItem.coffeeId)
            }

            holder.itemView.fabSubstractProduct.setOnClickListener {
                substractProduct(coffeeItem.coffeeId)
            }


        }
    }

    fun setCart(list: List<Cart>){
        cart = list
        Log.d("LIST:",cart.toString())
    }

    companion object {
        private val COFFEE_COMPARATOR = object : DiffUtil.ItemCallback<Coffee>() {
            override fun areItemsTheSame(oldItem: Coffee, newItem: Coffee): Boolean =
                    oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Coffee, newItem: Coffee): Boolean =
                    oldItem == newItem
        }
    }
}