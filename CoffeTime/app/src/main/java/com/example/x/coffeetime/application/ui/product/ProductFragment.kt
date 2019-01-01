package com.example.x.coffeetime.application.ui.product

import android.Manifest
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.model.Coffee
import kotlinx.android.synthetic.main.product_fragment.*
import android.support.v7.widget.LinearLayoutManager
import com.example.x.coffeetime.application.Injection.provideOrderQuantity


class ProductFragment : Fragment() {

    private val CAMERA_REQUEST_CODE = 100
    private lateinit var productViewModel: ProductViewModel
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private lateinit var adapter: ProductAdapter
    private var barcode: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.product_fragment, container, false)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        productViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(context!!))
                .get(ProductViewModel::class.java)


        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = "coffeeshop123"
        barcode = sharedPref?.getString(getString(R.string.preference_file_key), defaultValue)

        observeToken(barcode)

        scanButton?.setOnClickListener {
         setupPermissions()
        }

        rescanButton?.setOnClickListener {
            setupPermissions()
        }
    }

    /*
     * Token figyelése
     */
    private fun observeToken(barcode: String?){
        productViewModel.token.observe(this, Observer { token ->
            if(token!!.isEmpty()){
                createDialog()?.show()
            } else if(token.isNotEmpty()) {


                productViewModel.initFavorites()
                productViewModel.getCart()

                if(barcode != "coffeeshop123") {
                    if (validateBarcode(barcode!!)) {
                        invalidBarcode.visibility = View.GONE
                        scanButton?.visibility = View.GONE
                        barcodeText?.visibility = View.GONE
                        emptyList?.visibility = View.VISIBLE
                        list?.visibility = View.VISIBLE
                        input_layout?.visibility = View.VISIBLE
                        tableText?.text = barcode
                        bottomBar?.visibility = View.VISIBLE

                        val query = productViewModel.lastQueryValue() ?: DEFAULT_QUERY
                        productViewModel.searchCoffee(query)


                        initAdapter(barcode)
                        observeCoffees()
                        observeCart(adapter)
                        initSearch(adapter)

                    } else {
                        scanButton?.visibility = View.GONE
                        barcodeText?.visibility = View.GONE
                        emptyList?.visibility = View.GONE
                        tableText?.text = barcode
                        list?.visibility = View.GONE
                        input_layout?.visibility = View.GONE
                        bottomBar?.visibility = View.VISIBLE
                        invalidBarcode.visibility = View.VISIBLE
                    }

                }
            }
        })
    }

    /*
     * Alert dialog létrehozása
     */

    private fun createDialog(): AlertDialog? {
         val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
             builder.setMessage("In order to use this application you need to login, proceed to login?")
             builder.setCancelable(false)
             builder.apply {
                setPositiveButton("OK"
                ) { _,_->
                    findNavController().navigate(R.id.action_menu_to_login,null)
                }
            }
            builder.create()
        }
        return alertDialog
    }
    /*
     * Kosár figyelése
     */
    private fun observeCart(adapter: ProductAdapter){

        productViewModel.cart.observe(this, Observer { cart ->
          adapter.setCart(cart?: emptyList())
        })
    }


    /*
     * Vonalkód érvényességének egyszerű ellenőrzése
     */
    private fun validateBarcode(barcode: String): Boolean{

        var isValid = 0
        for (a in 1..15){
            if(barcode == (a.toString())){
                isValid++
            }
        }
        return isValid > 0

    }

    /*
     * Adapter beállítása
     */
    private fun initAdapter(barcode: String?) {
         adapter = ProductAdapter({ coffee ->

            val bundle = Bundle()
            bundle.putInt("coffeeId", coffee.coffeeId)


            findNavController().navigate(R.id.action_menu_to_SingleItem, bundle)

        }, {id ->
            productProgress.visibility = View.VISIBLE

            productViewModel.increaseProduct(provideOrderQuantity(barcode,id.toString()), {success ->
                mHandler.post {
                    productProgress.visibility = View.GONE
                }
            }, {error ->
                mHandler.post {
                    productProgress.visibility = View.GONE
                }
                Toast.makeText(context,error, Toast.LENGTH_SHORT).show()

            })
        }, {id ->
            productProgress.visibility = View.VISIBLE

            productViewModel.decreaseProduct(provideOrderQuantity(barcode,id.toString()), {success ->
                mHandler.post {
                    productProgress.visibility = View.GONE
                }


            }, {error ->
                mHandler.post {
                    productProgress.visibility = View.GONE
                }
            })
        })
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

    }

    /*
     * Kávék listájának figyelése
     */
    private fun observeCoffees(){
        productViewModel.coffees.observe(this, Observer<PagedList<Coffee>> {
            showEmptyList(it?.size == 0)
            adapter.submitList(it)


        })
        productViewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(context, "\uD83D\uDE28 Wooops ${it}", Toast.LENGTH_LONG).show()
        })
    }

    /*
     * Kereső sáv alapján keresés a kávék között
     */
    private fun initSearch(adapter: ProductAdapter) {
        search_coffee.setText(productViewModel.lastQueryValue())

        search_coffee.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateCoffeeListFromInput(adapter)
                true
            } else {
                false
            }
        }
        search_coffee.setOnKeyListener{ _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateCoffeeListFromInput(adapter)
                true
            } else {
                false
            }
        }
    }

    /*
     * Kávék lekérése
     */
    private fun updateCoffeeListFromInput(adapter: ProductAdapter) {
        search_coffee.text.trim().let {
            list.scrollToPosition(0)
            productViewModel.searchCoffee(it.toString())
            adapter.submitList(null)
        }
    }


    /*
     * Üres lista mutatása ha nincs találat
     */
    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            list.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            list.visibility = View.VISIBLE
        }
    }

    /*
     * Jogosultságok kezelése
     */
    private fun setupPermissions() {
        val permission = ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
        else {
            findNavController().navigate(R.id.action_menu_to_Camera)
        }
    }

    /*
     * Kamera hozzáférés kérése
     */
    private fun makeRequest() {
        requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE)
    }


    /*
     * Figyeljük megkaptuk e a kamera hozzáférést
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(context,"You can't use the app without camera permission, sorry :(", Toast.LENGTH_SHORT)
                            .show()
                } else {
                    findNavController().navigate(R.id.action_menu_to_Camera)
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_QUERY = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_menu, menu)
    }



}
