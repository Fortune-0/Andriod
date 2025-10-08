package com.example.efficilog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import android.content.Context
import com.google.android.material.tabs.TabLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat

class AdminActivity : AppCompatActivity() {

    private lateinit var themeToggle: ImageView
    private var isDarkMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.applyTheme(this) // Apply saved theme before setting layout
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val themeSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.theme_switch)
        themeSwitch.isChecked = ThemeHelper.isDarkMode(this)

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            ThemeHelper.setDarkMode(this, isChecked)
            recreate() // Recreate to apply new theme
        }

//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        isDarkMode = ThemeHelper.isDarkModeEnabled(this)
//
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_admin)
//
//        // Initialize TabLayout
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
//
//        // Initialize theme toggle switch
//        themeToggle = findViewById(R.id.theme_toggle)
//        updateThemeIcon()
//
//        themeToggle.setOnClickListener {
//            isDarkMode = !isDarkMode
//            ThemeHelper.saveThemePreference(this, isDarkMode)
//            ThemeHelper.applyTheme(this, isDarkMode)
//            updateThemeIcon()
//        }

        // Clear any existing tabs and add the two tabs we need
        tabLayout.removeAllTabs()
        tabLayout.addTab(tabLayout.newTab().setText("Users"))
        tabLayout.addTab(tabLayout.newTab().setText("Production"))

        // Set the default fragment (UsersFragment)
        replaceFragment(UsersFragment())

        // Listen for tab selection and switch fragments accordingly
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> replaceFragment(UsersFragment()) // Shows the list of users
                    1 -> replaceFragment(ProductionFragment()) // Shows production history
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Setup bottom navigation if needed
        setupBottomNavigation()
    }

    // Helper method to switch fragments in the container
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Setup bottom navigation menu
    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation?.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_users -> {
                    replaceFragment(UsersFragment())
                    true
                }
                R.id.nav_analytics -> {
                    // Navigate to analytics section
                    val intent = Intent(this, UnderDevelopmentActivity::class.java)
                    intent.putExtra("FEATURE_NAME", "Analytics")
                    startActivity(intent)
                    true
                }
                R.id.nav_settings -> {
                    // Navigate to settings
                    replaceFragment(SettingsFragment())
//                    val intent = Intent(this, AdminSettingsActivity::class.java)
//                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
    // Theme management methods
    private fun isDarkModeEnabled(): Boolean {
        val sharedPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("dark_mode", false)
    }

    private fun setDarkMode(enabled: Boolean) {
        val sharedPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("dark_mode", enabled)
        editor.apply()

        applyTheme()
    }

    private fun applyTheme() {
        val isDarkMode = isDarkModeEnabled()
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
