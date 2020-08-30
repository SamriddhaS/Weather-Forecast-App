package com.samriddha.weatherforecastapp.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.samriddha.weatherforecastapp.R
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()

    private lateinit var navController: NavController
    private var doubleBackToExitPressedOnce = false
    private lateinit var toast: Toast

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme) // Changing Activity Theme from SplashTheme to AppTheme.NoActionBar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this,
            R.id.nav_host_fragment
        )
        bottom_nav.setupWithNavController(navController)
        toast = Toast.makeText(this,"Press Back again To Exit",Toast.LENGTH_SHORT)


    }

    override fun onBackPressed() {

        if (navController.currentDestination?.id==R.id.currentWeatherFragment){

            if (doubleBackToExitPressedOnce){
                toast.cancel()
                super.onBackPressed()
                return
            }
            doubleBackToExitPressedOnce = true
            toast.show()
            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)


        }else{
            super.onBackPressed()
        }

    }

}