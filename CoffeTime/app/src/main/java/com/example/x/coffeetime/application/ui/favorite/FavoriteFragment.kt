package com.example.x.coffeetime.application.ui.favorite

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import com.example.x.coffeetime.application.model.Favorite
import kotlinx.android.synthetic.main.favorite_fragment.*

class FavoriteFragment : Fragment() {


    private lateinit var favoriteViewModel: FavoriteViewModel
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private lateinit var adapter: FavoriteAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoriteViewModel = ViewModelProviders.of(this,
                Injection.provideViewModelFactory(context!!)).get(FavoriteViewModel::class.java)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = "coffeeshop123"
        val barcode = sharedPref?.getString(getString(R.string.preference_file_key), defaultValue)
        val token = sharedPref?.getString(getString(R.string.preference_token_key),defaultValue)

        initAdapter(token,barcode)

        observeCart(adapter)
        observeFavorites(adapter)

    }

    private fun initAdapter(token:String?,barcode:String?){
        adapter = FavoriteAdapter ({
            // go to singel item
            val bundle = Bundle()
            bundle.putInt("coffeeId", it.id)
            findNavController().navigate(R.id.action_fav_to_single, bundle)
        },{
            //add
            favoriteProgress.visibility = View.VISIBLE
            val orderQuantityModel = OrderQuantityModel(
                    TableNum = barcode!!,
                    Quantity = "1",
                    ProductId = it.toString())
            favoriteViewModel.increaseProduct(orderQuantityModel,token!!, {success ->
                mHandler.post {
                    favoriteProgress.visibility = View.GONE
                }
            }, {error ->
                mHandler.post {
                    favoriteProgress.visibility = View.GONE
                }
            })
        },{
            //delete
            favoriteProgress.visibility = View.VISIBLE
            val orderQuantityModel = OrderQuantityModel(
                    TableNum = barcode!!,
                    Quantity = "1",
                    ProductId = it.toString())
            favoriteViewModel.decreaseProduct(orderQuantityModel,token!!, {success ->
                mHandler.post {
                    favoriteProgress.visibility = View.GONE
                }
            }, {error ->
                mHandler.post {
                    favoriteProgress.visibility = View.GONE
                }
            })
        },{
            // fav
            favoriteProgress.visibility = View.VISIBLE
            favoriteViewModel.deleteFavorite(it,token!!,{success ->
                mHandler.post {
                    favoriteProgress.visibility = View.GONE
                }
            }, {error ->
                mHandler.post {
                    favoriteProgress.visibility = View.GONE
                }
            })
        })
        favoriteList.layoutManager =  LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        favoriteList.adapter = adapter
    }


    private fun observeFavorites(adapter:FavoriteAdapter){
        favoriteViewModel.favorites.observe(this, Observer<List<Favorite>>{

            if(it!!.isNotEmpty()){
                favEmpty.visibility = View.GONE

                adapter.addToFav(it)
            } else {
                favEmpty.visibility = View.VISIBLE
                adapter.addToFav(emptyList())

            }

        })
    }

    private fun observeCart(adapter: FavoriteAdapter){

        favoriteViewModel.cart.observe(this, Observer { cart ->
            adapter.setCart(cart?: emptyList())
        })
    }
}
