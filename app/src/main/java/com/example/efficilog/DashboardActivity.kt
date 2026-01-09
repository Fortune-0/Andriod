package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.airbnb.lottie.LottieAnimationView
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var pieChart: PieChart

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val workData = mutableMapOf(
        "Cross-Over" to 0,
        "Pup-Joint" to 0,
        "Blank-Joint" to 0,
        "Heavy-Weight" to 0,
        "Casing Pipe" to 0,
        "Drill Pipe" to 0,
        "Bull-Plug" to 0,
        "Special-Job" to 0,
        "Flanges" to 0
    )

    private val sizeOptions = mapOf(
        "Cross-Over" to listOf("Select Size", "2 3/8", "2 7/8", "3 1/2", "4 1/2", "5 1/2", "7", "7 5/8"),
        "Casing Pipe" to listOf("Select Size", "9 5/8", "10 3/4", "13 3/8", "15"),
        "Drill Pipe" to listOf("Select Size", "2 3/8", "2 7/8", "3 1/2", "4", "4 1/2", "5", "5 1/2", "6 5/8"),
        "Pup-Joint" to listOf("Select Size", "2 3/8", "2 7/8", "3 1/2", "4", "4 1/2", "5", "5 1/2", "6 5/8"),
        "Heavy-Weight" to listOf("Select Size", "4 1/2", "5", "5 1/2", "6", "6 5/8"),
        "Bull-Plug" to listOf("Select Size", "1/8", "1/4", "3/8", "1/2", "3/4", "1", "1-1/4", "1-1/2", "2", "2-1/2", "3", "3-1/2", "4", "5", "6"),
        "Flanges" to listOf("Select Size", "1/2\"", "3/4\"", "1\"", "1 1/4\"", "1 1/2\"", "2\"", "3\"", "4\"", "6\"", "8\"", "10\"", "12\"")
    )

    companion object {
        private const val THREAD_INFO_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initializeViews()
        setupPieChart()
        setupButtons()
        setupNavigationDrawer()
    }

    private fun initializeViews() {
        pieChart = findViewById(R.id.pieChart)
        loadingAnimation = findViewById(R.id.loadingAnimation)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)
    }

    private fun setupNavigationDrawer() {
        // Set up the menu button
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
                    startActivity(Intent(this, HistoryActivity::class.java))
                    finish()
                }
                R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
                R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.nav_logout -> {
                    startActivity(Intent(this, MainActivity::class.java))
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
            R.id.button4 to "Drill Collar",
            R.id.button5 to "Casing Pipe",
            R.id.button6 to "Bull-Plug",
            R.id.button7 to "Flange",
            R.id.button8 to "Special-Job",

            )

        for ((buttonId, featureName) in buttonMap) {
            findViewById<Button>(buttonId).setOnClickListener {
                if (featureName == "Special-Job") {
                    startActivity(Intent(this, UnderDevelopmentActivity::class.java))
                } else {
                    openThreadInfoActivity(featureName, sizeOptions[featureName] ?: emptyList())
                }
            }
        }
    }

    private fun setupPieChart() {
        pieChart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            setDrawHoleEnabled(true)
            legend.isEnabled = true
            setDrawEntryLabels(false)
            setHoleColor(android.R.color.transparent)
            centerText = "Work Overview"
            setCenterTextSize(18f)
            animateY(1000)
            data?.setDrawValues(false)
        }
    }

    private fun updatePieChart() {
        val entries = workData.filter { it.value > 0 }.map { PieEntry(it.value.toFloat(), it.key) }

        if (entries.isNotEmpty()) {
            val dataSet = PieDataSet(entries, "Work Overview").apply {
                colors = ColorTemplate.COLORFUL_COLORS.toList()
                valueTextSize = 14f
                setDrawValues(false)
            }

            pieChart.data = PieData(dataSet)
            pieChart.invalidate()
        } else {
            pieChart.clear()
        }
    }

    private fun openThreadInfoActivity(featureName: String, sizeOptions: List<String>) {
        val intent = Intent(this, ThreadInfoActivity::class.java).apply {
            putExtra("FEATURE_NAME", featureName)
            putStringArrayListExtra("SIZE_OPTIONS", ArrayList(sizeOptions))
        }
        startActivityForResult(intent, THREAD_INFO_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == THREAD_INFO_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.getStringExtra("SUBMITTED_FEATURE")?.let { featureName ->
                workData[featureName] = workData.getOrDefault(featureName, 0) + 1
                updatePieChart()
                saveWorkDataToFirestore()
                Toast.makeText(this, "Dashboard updated for $featureName!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentUserId(): String? = auth.currentUser?.uid

    private fun saveWorkDataToFirestore() {
        val userId = getCurrentUserId() ?: run {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val dashboardData = mapOf(
            "workData" to workData,
            "lastUpdated" to System.currentTimeMillis()
        )

        firestore.collection("users").document(userId)
            .collection("dashboard").document("workStats")
            .set(dashboardData)
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save dashboard data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadWorkData() {
        val userId = getCurrentUserId() ?: return

        // Prevent multiple simultaneous loads
        if (loadingAnimation.visibility == View.VISIBLE) return

        // Show loading animation
        loadingAnimation.visibility = View.VISIBLE
        loadingAnimation.playAnimation()
        pieChart.visibility = View.INVISIBLE

        firestore.collection("users").document(userId)
            .collection("dashboard").document("workStats")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val savedWorkData = document.get("workData") as? Map<String, Long>
                    savedWorkData?.let { data ->
                        for ((key, value) in data) {
                            if (workData.containsKey(key)) {
                                workData[key] = value.toInt()
                            }
                        }
                        updatePieChart()
                    }
                }

                // Hide loading animation and show pie chart
                loadingAnimation.cancelAnimation()
                loadingAnimation.visibility = View.GONE
                pieChart.visibility = View.VISIBLE
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load dashboard data: ${e.message}", Toast.LENGTH_SHORT).show()

                loadingAnimation.cancelAnimation()
                loadingAnimation.visibility = View.GONE
                pieChart.visibility = View.VISIBLE
            }
    }

    override fun onPause() {
        super.onPause()
        saveWorkDataToFirestore()
    }

    override fun onResume() {
        super.onResume()
        loadWorkData()
    }
}