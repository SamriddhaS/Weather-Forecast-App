package com.samriddha.weatherforecastapp.utils

import android.Manifest
import android.content.Context
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions

object LocationUtility {

    //This function will tell us if the app already has the required permissions.
    fun hasLocationPermission(context: Context) =
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION)

}