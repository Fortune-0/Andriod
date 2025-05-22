package com.example.efficilog

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class EfficilogApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences("StaffSettings", MODE_PRIVATE)
        val darkModeEnabled = sharedPreferences.getBoolean("dark_mode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (darkModeEnabled)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
