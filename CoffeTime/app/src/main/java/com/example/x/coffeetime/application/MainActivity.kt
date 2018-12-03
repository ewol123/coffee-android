package com.example.x.coffeetime.application

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.x.coffeetime.R
import android.content.Context
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        ButterKnife.bind(this)

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        sharedPref?.edit()?.clear()?.apply()

        setSupportActionBar(toolbar)


        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController


        setupActionBar(navController)

        setupNavigationMenu(navController)

        navController.addOnNavigatedListener { _, destination ->

            Log.d("DESTINATIONLABEL", destination.label.toString())
            when (destination.label) {
                "menu_fragment" -> {
                    toolbar?.title = "Menu"
                    toolbar?.visibility = View.VISIBLE
                    container?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

                }
                "login_fragment" -> {
                    toolbar?.visibility = View.GONE
                    container?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                }
                "barcode_fragment" -> {
                    toolbar?.title ="Scan table"
                }
                "ProductSingleItem" ->{
                    toolbar?.title="Details"
                }
                "reset_pass_fragment" ->{
                    toolbar?.title="Reset password"
                }
                "cart_fragment" ->{
                    toolbar?.title="Cart"
                }
                "checkout_fragment"->{
                    toolbar?.title="Checkout"
                }
                "favorite_fragment"->{
                    toolbar?.title="Favorites"
                }

            }


            Log.d("NavigationActivity", "Navigated to ${destination.label}")
        }

    }



    private fun setupNavigationMenu(navController: NavController) {
        nav_view?. let { navigationView ->
            NavigationUI.setupWithNavController(navigationView, navController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
       //csak akkor adjuk hozzá a menü itemeket ha még nincs NavigationView
        if (nav_view == null) {
            menuInflater.inflate(R.menu.menu_nav_drawer, menu)
            return true
        }
        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //A NavHelper az id alapján kikeresi a megfelelő célt/actiont és oda navigál, ha nincs akkor
        //visszatér a főképernyőre
        return NavigationUI.onNavDestinationSelected(item,
                Navigation.findNavController(this, R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }




    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController, container)
    }



    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(container,
                Navigation.findNavController(this, R.id.my_nav_host_fragment))
    }

    override fun onBackPressed() {
      finishAfterTransition()
    }


    override fun onDestroy() {
        Log.d("onDestroy","DESTROYED")
        super.onDestroy()
    }
}
