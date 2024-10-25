package com.example.efficilog

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.efficilog.SettingsActivity


class DashboardActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        // Button 1 - Cross-overs
        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            Toast.makeText(this, "Cross-over", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, CrossOversActivity::class.java)
           // startActivity(intent)
        }

        // Button 2 - Casing pipes
        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            Toast.makeText(this, "Casing pipes", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, CasingPipesActivity::class.java)
            // startActivity(intent)
        }

        // Button 3 - Drill pipes
        val button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener {
            Toast.makeText(this, "Drill pipes", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, DrillPipesActivity::class.java)
            // startActivity(intent)
        }

        // Button 4 - Bull-holes
        val button4 = findViewById<Button>(R.id.button4)
        button4.setOnClickListener {
            Toast.makeText(this, "Bull-holes", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, BullHolesActivity::class.java)
            // startActivity(intent)
        }

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)

        ViewCompat.setOnApplyWindowInsetsListener(drawerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
                R.id.nav_home -> {
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                    // Handle home action
                }
                R.id.nav_drawer -> {
                    Toast.makeText(this, "Drawer clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                    // Handle profile action
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                    // Handle settings action
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_logout -> {
                    Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    // Override the back button to close the drawer if it's open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navView)) {
            drawerLayout.closeDrawer(navView)
        } else {
            super.onBackPressed()
        }
    }
}
