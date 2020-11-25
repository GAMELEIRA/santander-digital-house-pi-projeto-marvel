package com.example.marvelworld.home.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.marvelworld.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), OnFilterListener {
    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }
    private val drawerLayout by lazy { findViewById<DrawerLayout>(R.id.drawer_layout) }
    private val navigationView by lazy { findViewById<NavigationView>(R.id.navigationView) }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var filterIcon: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView.setupWithNavController(navController)
        navigationView.itemIconTintList = null

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)
        filterIcon = menu!!.findItem(R.id.filtersFragment)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            filterIcon.itemId -> navController.navigate(R.id.filtersFragment)

            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView)
                    return true
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showFilterIcon() {
        try {
            filterIcon.isVisible = true
        } catch (e: Exception) {
        }
    }

    override fun hideFilterIton() {
        try {
            filterIcon.isVisible = false
        } catch (e: Exception) {
        }
    }


}