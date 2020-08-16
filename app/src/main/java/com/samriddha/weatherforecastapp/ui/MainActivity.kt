package com.samriddha.weatherforecastapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.samriddha.weatherforecastapp.LifecycleBoundLocationManager
import com.samriddha.weatherforecastapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1
class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance<FusedLocationProviderClient>()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

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


        requestLocationPermission()

        if (hasLocationPermission())
            bindLocationManager()
        else
            requestLocationPermission()


    }

    private fun bindLocationManager() {

        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient,
            locationCallback
        )

    }

    private fun requestLocationPermission() {

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==
                PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED)
            bindLocationManager()
        else
            Toast.makeText(this,"Showing Weather For Custom Location.",Toast.LENGTH_LONG).show()
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