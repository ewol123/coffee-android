package com.example.x.coffeetime.application.ui.product

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.model.Coffee
import kotlinx.android.synthetic.main.product_fragment.*
import android.support.v7.widget.LinearLayoutManager


class ProductFragment : Fragment() {

    private val CAMERA_REQUEST_CODE = 100
    private lateinit var productViewModel: ProductViewModel
    var adapter = ProductAdapter({ coffee ->
        Log.i("Coffee:", "$coffee clicked")

        val bundle = Bundle()
        bundle.putInt("coffeeId", coffee.coffeeId)


        findNavController().navigate(R.id.action_menu_to_SingleItem, bundle)

    }, {id ->
        Log.i("Add product", id.toString())
    }, {id ->
        Log.i("Decrease product", id.toString())

    })


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.product_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        productViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(context!!))
                .get(ProductViewModel::class.java)

        observeCart()

        productViewModel.token.observe(this, Observer { token ->
            if(token!!.isEmpty()){

               findNavController().navigate(R.id.action_menu_to_login,null)



            } else if(token.isNotEmpty()) {

               saveToken(token.get(0).token)

                productViewModel.getCart(token.get(0).token)

                Log.d("ez a token", token.get(0).token)

                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                val defaultValue = "coffeeshop123"
                val barcode = sharedPref?.getString(getString(R.string.preference_file_key), defaultValue)

                if(barcode != "coffeeshop123") {
                    if (validateBarcode(barcode!!)) {
                        nope.visibility = View.GONE
                        scanButton?.visibility = View.GONE
                        barcodeText?.visibility = View.GONE
                        emptyList?.visibility = View.VISIBLE
                        list?.visibility = View.VISIBLE
                        input_layout?.visibility = View.VISIBLE
                        tableText?.text = barcode
                        bottomBar?.visibility = View.VISIBLE

                        val query = productViewModel.lastQueryValue() ?: DEFAULT_QUERY
                        productViewModel.searchCoffee(query)
                        initAdapter()
                        initSearch()

                    } else {
                        scanButton?.visibility = View.GONE
                        barcodeText?.visibility = View.GONE
                        emptyList?.visibility = View.GONE
                        tableText?.text = barcode
                        list?.visibility = View.GONE
                        input_layout?.visibility = View.GONE
                        bottomBar?.visibility = View.VISIBLE
                        nope.visibility = View.VISIBLE
                    }

                }
            }
        })

        scanButton?.setOnClickListener {
         setupPermissions()
        }

        rescanButton?.setOnClickListener {
            setupPermissions()
        }
    }

    private fun observeCart(){


        productViewModel.cart.observe(this, Observer { cart ->
          adapter.setCart(cart?: emptyList())
        })
    }



    private fun saveToken(token: String){
        val tokenPref = activity?.getPreferences(Context.MODE_PRIVATE)
        tokenPref
                ?.edit()
                ?.putString(getString(R.string.preference_token_key), token )
                ?.apply()
    }

    private fun validateBarcode(barcode: String): Boolean{


        var isValid = 0
        for (a in 1..15){
            if(barcode == (a.toString())){
                isValid++
            }
        }
        return if (isValid > 0 ) true else false

    }


    private fun initAdapter() {
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        productViewModel.coffees.observe(this, Observer<PagedList<Coffee>> {
            Log.d("Activity", "list: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapter.submitList(it)


        })
        productViewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(context, "\uD83D\uDE28 Wooops ${it}", Toast.LENGTH_LONG).show()
        })
    }

    private fun initSearch() {
        search_coffee.setText(productViewModel.lastQueryValue())

        search_coffee.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateCoffeeListFromInput()
                true
            } else {
                false
            }
        }
        search_coffee.setOnKeyListener{ _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateCoffeeListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateCoffeeListFromInput() {
        search_coffee.text.trim().let {

            list.scrollToPosition(0)
            productViewModel.searchCoffee(it.toString())
            adapter.submitList(null)

        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            list.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            list.visibility = View.VISIBLE
        }
    }


    private fun setupPermissions() {
        val permission = ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("permission", "Permission to record denied")
            makeRequest()
        }
        else {
            findNavController().navigate(R.id.action_menu_to_Camera)
        }
    }

    private fun makeRequest() {
        requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("permission", "Permission has been denied by user")
                    Toast.makeText(context,"You can't use the app without camera permission, sorry :(", Toast.LENGTH_SHORT)
                            .show()
                } else {
                    Log.i("permission", "Permission has been granted by user")
                    findNavController().navigate(R.id.action_menu_to_Camera)
                }
            }
        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_menu, menu)
    }



}
