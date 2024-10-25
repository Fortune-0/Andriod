package com.example.efficilog

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
class EfficiLogApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}