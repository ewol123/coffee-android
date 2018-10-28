package com.example.x.coffeetime.application.ui.product

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.x.coffeetime.application.model.Coffee


/**
 * Adapter for the list of repositories.
 */
class CoffeesAdapter(val listener: (Coffee) -> Unit) : PagedListAdapter<Coffee, RecyclerView.ViewHolder>(COFFEE_COMPARATOR) {

    var onItemClick: ((Coffee) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CoffeeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val coffeeItem = getItem(position)
        if (coffeeItem != null) {
            (holder as CoffeeViewHolder).bind(coffeeItem)

            holder.itemView.setOnClickListener {
                listener(coffeeItem)
            }


        }
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