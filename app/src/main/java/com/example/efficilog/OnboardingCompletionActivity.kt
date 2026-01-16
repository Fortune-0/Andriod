package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class OnboardingCompletionActivity : AppCompatActivity() {

    private lateinit var tvRoleMessage: TextView
    private lateinit var btnBack: Button
    private lateinit var btnGetStarted: Button

    // ✅ NEW: Add OnboardingManager
    private lateinit var onboardingManager: OnboardingManager

    private var selectedRole: String? = null

    companion object {
        private const val TAG = "OnboardingCompletion"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_completion)

        // ✅ NEW: Initialize OnboardingManager
        onboardingManager = OnboardingManager(this)

        Log.d(TAG, "OnboardingCompletionActivity started")

        // Get the selected role from previous activity
        selectedRole = intent.getStringExtra("SELECTED_ROLE")
        Log.d(TAG, "Received selected role: $selectedRole")

        initializeViews()
        setupDynamicContent()
        setupClickListeners()

        Log.d(TAG, "Activity initialization completed successfully")
    }

    private fun initializeViews() {
        try {
            tvRoleMessage = findViewById(R.id.tv_role)
            btnBack = findViewById(R.id.btn_back)
            btnGetStarted = findViewById(R.id.btn_get_started)
            Log.d(TAG, "Views initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing views", e)
            Toast.makeText(this, "Layout error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupDynamicContent() {
        try {
            // Update the message based on selected role
            val roleMessage = when (selectedRole) {
                "Administrator", OnboardingManager.ROLE_ADMINISTRATOR ->
                    "You're ready to use EfficiLog as an Administrator"
                "Staff", OnboardingManager.ROLE_STAFF ->
                    "You're ready to use EfficiLog as a Staff Member"
                else ->
                    "You're ready to use EfficiLog"
            }

            tvRoleMessage.text = roleMessage
            Log.d(TAG, "Dynamic content set: $roleMessage")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up dynamic content", e)
        }
    }

    private fun setupClickListeners() {
        try {
            btnBack.setOnClickListener {
                Log.d(TAG, "Back button clicked")
                onBackPressed()
            }

            btnGetStarted.setOnClickListener {
                Log.d(TAG, "Get Started button clicked")
                completeOnboarding()
            }
            Log.d(TAG, "Click listeners set up successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up click listeners", e)
        }
    }

    /**
     * ✅ FIXED: Use OnboardingManager instead of direct SharedPreferences
     */
    private fun completeOnboarding() {
        try {
            Log.d(TAG, "Starting onboarding completion process")

            val role = selectedRole ?: run {
                Log.e(TAG, "selectedRole is null")
                Toast.makeText(this, "User role missing. Please restart onboarding.", Toast.LENGTH_LONG).show()
                return
            }

            onboardingManager.completeOnboarding(role)

            Log.d(TAG, "Onboarding data saved via OnboardingManager")

//            onboardingManager.printDebugInfo()

            navigateToMainActivity()

        } catch (e: Exception) {
            Log.e(TAG, "Error completing onboarding", e)
            Toast.makeText(this, "Error completing onboarding: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun navigateToMainActivity() {
        try {
            Log.d(TAG, "Attempting to navigate to MainActivity")

            val intent = Intent(this, MainActivity::class.java)

            // Clear the entire back stack so user can't go back to onboarding
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // Pass the selected role to MainActivity
            intent.putExtra("USER_ROLE", selectedRole)

            Log.d(TAG, "Intent created with flags: ${intent.flags}")
            Log.d(TAG, "Starting MainActivity with role: $selectedRole")

            startActivity(intent)

            // Add transition animation with error handling
            try {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } catch (e: Exception) {
                Log.w(TAG, "Animation resources not found, using default transition", e)
            }

            finish()
            Log.d(TAG, "Navigation to MainActivity completed")

        } catch (e: Exception) {
            Log.e(TAG, "Critical error navigating to MainActivity", e)

            when {
                e.message?.contains("Unable to find explicit activity class") == true -> {
                    Toast.makeText(this, "MainActivity not found. Please check your project setup.", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "MainActivity class not found in AndroidManifest.xml")
                }
                e.message?.contains("Permission") == true -> {
                    Toast.makeText(this, "Permission error. Check AndroidManifest.xml", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Permission error - check activity declarations")
                }
                else -> {
                    Toast.makeText(this, "Navigation error: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Unknown navigation error", e)
                }
            }

            Log.d(TAG, "Staying on completion screen due to navigation error")
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "Back pressed - returning to previous screen")
        super.onBackPressed()

        try {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        } catch (e: Exception) {
            Log.w(TAG, "Back animation resources not found", e)
        }
    }
}