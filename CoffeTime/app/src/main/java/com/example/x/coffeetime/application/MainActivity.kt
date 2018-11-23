package com.example.x.coffeetime.application

import android.Manifest
import android.content.res.Resources
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import butterknife.BindView
import com.example.x.coffeetime.R
import com.example.x.coffeetime.application.ui.login.LoginFragment
import android.support.v4.app.ActivityCompat.requestPermissions
import android.Manifest.permission
import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.support.v4.app.ActivityCompat
import butterknife.ButterKnife


class MainActivity : AppCompatActivity() {

    @JvmField
    @BindView(R.id.container)
    var drawerLayout: DrawerLayout? = null

    @JvmField
    @BindView(R.id.toolbar)
    var toolbar: Toolbar? = null

    @JvmField
    @BindView(R.id.nav_view)
    var navigationView: NavigationView? = null


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
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }
            Log.d("DESTINATIONLABEL", destination.label.toString())
            when (destination.label) {
                "menu_fragment" -> {
                    toolbar?.title = "Menu"
                    toolbar?.visibility = View.VISIBLE
                    drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

                }
                "login_fragment" -> {
                    toolbar?.visibility = View.GONE
                    drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

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

            Toast.makeText(this@MainActivity, "Navigated to $dest",
                    Toast.LENGTH_SHORT).show()
            Log.d("NavigationActivity", "Navigated to ${destination.label}")
        }

    }



    private fun setupNavigationMenu(navController: NavController) {
        navigationView?. let { navigationView ->
            NavigationUI.setupWithNavController(navigationView, navController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
        if (navigationView == null) {
            menuInflater.inflate(R.menu.menu_nav_drawer, menu)
            return true
        }
        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Have the NavHelper look for an action or destination matching the menu
        // item id and navigate there if found.
        // Otherwise, bubble up to the parent.
        return NavigationUI.onNavDestinationSelected(item,
                Navigation.findNavController(this, R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }




    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    }



    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(drawerLayout,
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
