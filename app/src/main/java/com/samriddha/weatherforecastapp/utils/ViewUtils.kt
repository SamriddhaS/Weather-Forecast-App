package com.samriddha.weatherforecastapp.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.toast(massage:String){
    Toast.makeText(this,massage,Toast.LENGTH_LONG).show()
}

fun View.snackbar(message:String){
    Snackbar.make(this,message,Snackbar.LENGTH_LONG).show()
}