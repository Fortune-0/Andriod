package com.example.efficilog

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


object ThemeHelper {

    private const val  PREFS_NAME = "app_prefs"
    private const val  KEY_DARK_MODE = "dark_mode"

    /**
     * Check if dark mode is enabled.
     */

    fun isDarkMode(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(KEY_DARK_MODE, false)
}

    /**
     * Enable or disable dark mode.
     */
    fun setDarkMode(context: Context, enabled: Boolean) {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean(KEY_DARK_MODE, enabled)
        editor.apply()
    }

    /**
     * Apply the current theme based on dark mode setting.
     */
    fun applyTheme(context: Context) {
        val isDarkMode = isDarkMode(context)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    /**
     * Toggle between dark mode and light mode.
     */

    fun toggleTheme(context: Context) {
        val currentMode = isDarkMode(context)
        setDarkMode(context, !currentMode)
        applyTheme(context)
}
}