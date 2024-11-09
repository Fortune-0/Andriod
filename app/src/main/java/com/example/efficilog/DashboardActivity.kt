package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class DashboardActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    // Size options map
    private val sizeOptions = mapOf(
        "Cross-Over" to listOf("2 3/8", "2 7/8", "3 1/2", "4 1/2", "5 1/2", "7", "7 5/8"),
        "Casing Pipe" to listOf("9 5/8", "10 3/4", "13 3/8", "15"),
        "Drill Pipe" to listOf("2 3/8", "2 7/8", "3 1/2", "4", "5", "5 1/2")
        // Add entries for "Pub-Joint", "Blank-Joint", "Bull-Plug", etc.
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setupButtons()

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)

        // Set up the open drawer button
        val openDrawerButton = findViewById<Button>(R.id.menu_button)
        openDrawerButton.setOnClickListener {
            drawerLayout.open()
        }

        // Set up ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle navigation item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_drawer -> Toast.makeText(this, "Drawer clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun setupButtons() {
        val buttonMap = mapOf(
            R.id.button1 to "Cross-Over",
            R.id.button2 to "Pup-Joint",
            R.id.button3 to "Blank-Joint",
            R.id.button4 to "Heavy-Weight",
            R.id.button5 to "Casing Pipe",
            R.id.button6 to "Drill Pipe",
            R.id.button7 to "Bull-Plug",
            R.id.button8 to "Test-Cap",
            R.id.button9 to "Flanges"
        )

        for ((buttonId, featureName) in buttonMap) {
            findViewById<Button>(buttonId).setOnClickListener {
                openThreadInfoActivity(featureName, sizeOptions[featureName] ?: emptyList())
            }
        }
    }

    private fun openThreadInfoActivity(featureName: String, sizeOptions: List<String>) {
        val intent = Intent(this, ThreadInfoActivity::class.java)
        intent.putExtra("FEATURE_NAME", featureName)
        intent.putStringArrayListExtra("SIZE_OPTIONS", ArrayList(sizeOptions))
        startActivity(intent)
    }
}
