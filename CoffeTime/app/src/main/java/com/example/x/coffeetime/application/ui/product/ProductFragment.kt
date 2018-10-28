package com.example.x.coffeetime.application.ui.product

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
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
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.product_item.*


class ProductFragment : Fragment() {


    private lateinit var viewModel: ProductViewModel
    private lateinit var productViewModel: SearchProductViewModel
    private val adapter = CoffeesAdapter{ data ->
        Log.i("Coffee:", "${data.coffeeId} clicked")


        val bundle = Bundle()
        bundle.putInt("id", data.coffeeId)
        findNavController().navigate(R.id.action_menu_to_SingleItem, bundle)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.product_fragment, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        productViewModel = ViewModelProviders.of(this, Injection.provideProductViewModelFactory(context!!))
                .get(SearchProductViewModel::class.java)

        viewModel.token.observe(this, Observer { token ->
            if(token!!.isEmpty()){



               findNavController().navigate(R.id.to_login,null)


            } else if(token?.isNotEmpty()) {

                Log.d("ez a token", token.get(0).token)

                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                val defaultValue = "empty"
                val barcode = sharedPref?.getString(getString(R.string.preference_file_key), defaultValue)

                if(barcode != "empty"){
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

                }
            }
        })

        scanButton?.setOnClickListener {
            findNavController().navigate(R.id.go_to_camera,null)
        }

        rescanButton?.setOnClickListener {

            findNavController().navigate(R.id.go_to_camera,null)
        }
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





    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_menu, menu)
    }



}
