package com.example.myapplication.DTO

import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat.getSystemService

class Util {
    object StaticFunction {
        private var instance: Util? = null
        fun getInstance(): Util {
            if (instance == null)
                instance = Util()
            return instance!!
        }
    }

    fun getMylocation(){
 //       val im = getSystemService(Context.LOCATION_SERVICE)as LocationManager

    }
}