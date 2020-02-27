package com.adsandurl.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager

class Helper {

    companion object {
        fun isNetworkAvailable(activity: Activity): Boolean {
            val connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null
        }
        // hiding keyboard on Back
        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive) {
                if (activity.currentFocus != null) {
                    inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
                }
            }
        }




    }





}