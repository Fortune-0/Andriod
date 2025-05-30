package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import android.content.Context
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User


class DashboardActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var pieChart: PieChart

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val workData = mutableMapOf (
        "Cross-Over" to 0,
        "Pup-Joint" to 0,
        "Blank-Joint" to 0,
        "Heavy-Weight" to 0,
        "Casing Pipe" to 0,
        "Drill Pipe" to 0,
        "Bull-Plug" to 0,
        "Test-Cap" to 0,
        "Flanges" to 0
    )

    // Size options map
    private val sizeOptions = mapOf(
        "Cross-Over" to listOf("Select Size", "2 3/8", "2 7/8", "3 1/2", "4 1/2", "5 1/2", "7", "7 5/8"),
        "Casing Pipe" to listOf("Select Size", "9 5/8", "10 3/4", "13 3/8", "15"),
        "Drill Pipe" to listOf("Select Size", "2 3/8", "2 7/8", "3 1/2", "4", "4 1/2", "5", "5 1/2", "6 5/8"),
        "Pup-Joint" to listOf("Select Size", "2 3/8", "2 7/8", "3 1/2", "4", "4 1/2", "5", "5 1/2", "6 5/8"),
        "Heavy-Weight" to listOf("Select Size", "4 1/2", "5", "5 1/2", "6", "6 5/8")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        pieChart = findViewById(R.id.pieChart)
        setupPieChart()

        loadWorkData()
        updatePieChart()

        setupButtons()
        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)

        // Set up the open drawer button
        val menuButton: ImageButton = findViewById(R.id.menu_button)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
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
                R.id.nav_history -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }
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
            R.id.button8 to "Special-Job",

        )

        for ((buttonId, featureName) in buttonMap) {
            findViewById<Button>(buttonId).setOnClickListener {

                workData[featureName] = workData.getOrDefault(featureName, 0) + 1
                updatePieChart()
                saveWorkData()
                openThreadInfoActivity(featureName, sizeOptions[featureName] ?: emptyList())
            }
        }
    }

    private fun setupPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setDrawHoleEnabled(true)
        pieChart.legend.isEnabled = (true)
        pieChart.setDrawEntryLabels(false)
        pieChart.setHoleColor(android.R.color.transparent)
        pieChart.centerText = "Work Overview"
        pieChart.setCenterTextSize(18f)
        pieChart.animateY(1000)

        pieChart.data?.setDrawValues(false)
    }

//    private fun updatePieChart() {
//        val entries = workData.map { PieEntry(it.value.toFloat(), it.key) }
//        val dataSet = PieDataSet(entries, "Work Overview")
//
//        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
//        dataSet.valueTextSize = 14f
//
//        pieChart.data = PieData(dataSet)
//        pieChart.invalidate()
//    }

    private fun updatePieChart() {
        val entries = workData.filter { it.value > 0 }.map { PieEntry(it.value.toFloat(), it.key) }

        if (entries.isNotEmpty()) {
            val dataSet = PieDataSet(entries, "Work Overview")
            dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
            dataSet.valueTextSize = 14f

            dataSet.setDrawValues(false)

            pieChart.data = PieData(dataSet)
            pieChart.invalidate()
        } else {
            pieChart.clear()
        }
    }
    private fun openThreadInfoActivity(featureName: String, sizeOptions: List<String>) {
        val intent = Intent(this, ThreadInfoActivity::class.java)
        intent.putExtra("FEATURE_NAME", featureName)
        intent.putStringArrayListExtra("SIZE_OPTIONS", ArrayList(sizeOptions))
        startActivity(intent)
    }

    // Get the current user ID
    private fun getCurrentUserId(): String {
        val userPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return userPref.getString("current_user_id", "guest") ?: "guest"
    }

    private fun saveWorkData() {
        val sharedPref = getSharedPreferences("EfficiLogPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val userId = getCurrentUserId()

        for ((key, value) in workData) {
            editor.putInt("user_${userId}_workData_$key", value)
        }


        editor.apply()
    }

    // Load work data from SharedPreferences
    private fun loadWorkData() {
        val sharedPref = getSharedPreferences("EfficiLogPrefs", Context.MODE_PRIVATE)
        val userId = getCurrentUserId()

        for (key in workData.keys) {
            val value = sharedPref.getInt("user_${userId}_workData_$key", 0)
            workData[key] = value
        }
    }

    // Override onPause to save state when app is minimized or closed
    override fun onPause() {
        super.onPause()
        saveWorkData()
    }
}