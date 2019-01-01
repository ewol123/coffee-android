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
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.Injection.provideOrderQuantity
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

        initAdapter(barcode)

        observeCart(adapter)
        observeFavorites(adapter)

    }

     /*
     * Adapter beállítása
     */

    private fun initAdapter(barcode:String?){
        adapter = FavoriteAdapter ({
            val bundle = Bundle()
            bundle.putInt("coffeeId", it.id)
            findNavController().navigate(R.id.action_fav_to_single, bundle)
        }, {
            //add
            if(barcode != "coffeeshop123"){
            favoriteProgress.visibility = View.VISIBLE
            favoriteViewModel.increaseProduct(provideOrderQuantity(barcode, it.toString()), { success ->
                mHandler.post {
                    favoriteProgress.visibility = View.GONE
                }
            }, { error ->
                mHandler.post {
                    favoriteProgress.visibility = View.GONE
                }
                Toast.makeText(context,error, Toast.LENGTH_SHORT).show()

            })
        } else Toast.makeText(context,"Please scan your table",Toast.LENGTH_SHORT).show()
        },{
            //delete
            favoriteProgress.visibility = View.VISIBLE

            favoriteViewModel.decreaseProduct(provideOrderQuantity(barcode,it.toString()), {success ->
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
            favoriteViewModel.deleteFavorite(it,{success ->
                mHandler.post {
                    favoriteProgress.visibility = View.GONE
                }
            }, {error ->
                mHandler.post {
                    favoriteProgress.visibility = View.GONE
                }
                Toast.makeText(context,error, Toast.LENGTH_SHORT).show()

            })
        })
        favoriteList.layoutManager =  LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        favoriteList.adapter = adapter
    }


     /*
     * Kedvencek figyelése
     */

    private fun observeFavorites(adapter:FavoriteAdapter){
        favoriteViewModel.favorites.observe(this, Observer<List<Favorite>>{

            if(it!!.isNotEmpty()){
                favEmpty.visibility = View.GONE
                favoriteList.visibility = View.VISIBLE
                adapter.addToFav(it)
            } else {
                favEmpty.visibility = View.VISIBLE
                favoriteList.visibility = View.GONE
                adapter.addToFav(emptyList())

            }

        })
    }

    /*
     * Kosár figyelése
     */

    private fun observeCart(adapter: FavoriteAdapter){

        favoriteViewModel.cart.observe(this, Observer { cart ->
            adapter.setCart(cart?: emptyList())
        })
    }


}
