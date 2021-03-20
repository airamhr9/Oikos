package com.example.oikos

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androidnetworking.AndroidNetworking
import com.example.oikos.ui.search.FichaMapFragment
import com.example.oikos.ui.search.MapSearchFragment
import objects.DatosInmueble

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_user))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //hasta encontrar como hacerlo en xml
        supportActionBar?.elevation = 0f

        AndroidNetworking.initialize(applicationContext)
    }


    fun changeToMapFragment(view: View, listaInmueble : ArrayList<DatosInmueble>){
        val mapFragment: Fragment = MapSearchFragment()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putSerializable("inmueble", listaInmueble)
        mapFragment.arguments = bundle

        transaction.replace(R.id.nav_host_fragment, mapFragment)
        transaction.addToBackStack(null)

        transaction.commit()
    }



    fun isNetworkConnected(): Boolean {
        val connectivityManager = this.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}