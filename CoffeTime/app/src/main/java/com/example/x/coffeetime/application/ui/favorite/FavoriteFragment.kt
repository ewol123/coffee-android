package com.example.x.coffeetime.application.ui.favorite

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.model.Cart
import com.example.x.coffeetime.application.model.Favorite
import com.example.x.coffeetime.application.ui.cart.CartViewModel

class FavoriteFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoriteViewModel = ViewModelProviders.of(this,
                Injection.provideViewModelFactory(context!!)).get(FavoriteViewModel::class.java)


        favoriteViewModel.favorites.observe(this, Observer<List<Favorite>>{


        })

    }

}
