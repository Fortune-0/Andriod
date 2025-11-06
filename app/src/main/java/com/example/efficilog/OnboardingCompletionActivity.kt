package com.example.efficilog

import android.content.Intent
import android.content.SharedPreferences
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

    private var selectedRole: String? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_completion)

        // ✅ ENHANCED: Added detailed logging for debugging
        Log.d("OnboardingCompletion", "OnboardingCompletionActivity started")

        // Get the selected role from previous activity
        selectedRole = intent.getStringExtra("SELECTED_ROLE")
        Log.d("OnboardingCompletion", "Received selected role: $selectedRole")

        // Initialize SharedPreferences for storing user preferences
        sharedPreferences = getSharedPreferences("EfficiLogPrefs", MODE_PRIVATE)

        initializeViews()
        setupDynamicContent()
        setupClickListeners()

        // ✅ NEW: Log successful initialization
        Log.d("OnboardingCompletion", "Activity initialization completed successfully")
    }

    private fun initializeViews() {
        try {
            tvRoleMessage = findViewById(R.id.tv_role)
            btnBack = findViewById(R.id.btn_back)
            btnGetStarted = findViewById(R.id.btn_get_started)
            Log.d("OnboardingCompletion", "Views initialized successfully")
        } catch (e: Exception) {
            // ✅ NEW: Error handling for missing views
            Log.e("OnboardingCompletion", "Error initializing views", e)
            Toast.makeText(this, "Layout error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupDynamicContent() {
        try {
            // Update the message based on selected role
            val roleMessage = when (selectedRole) {
                "Administrator" -> "You're ready to use EfficiLog as an Administrator"
                "Staff" -> "You're ready to use EfficiLog as a Staff Member"
                else -> "You're ready to use EfficiLog"
            }

            tvRoleMessage.text = roleMessage
            Log.d("OnboardingCompletion", "Dynamic content set: $roleMessage")
        } catch (e: Exception) {
            // ✅ NEW: Error handling for content setup
            Log.e("OnboardingCompletion", "Error setting up dynamic content", e)
        }
    }

    private fun setupClickListeners() {
        try {
            btnBack.setOnClickListener {
                Log.d("OnboardingCompletion", "Back button clicked")
                onBackPressed()
            }

            btnGetStarted.setOnClickListener {
                Log.d("OnboardingCompletion", "Get Started button clicked")
                completeOnboarding()
            }
            Log.d("OnboardingCompletion", "Click listeners set up successfully")
        } catch (e: Exception) {
            // ✅ NEW: Error handling for click listeners
            Log.e("OnboardingCompletion", "Error setting up click listeners", e)
        }
    }

    private fun completeOnboarding() {
        try {
            Log.d("OnboardingCompletion", "Starting onboarding completion process")

            // Save onboarding completion and user role to SharedPreferences
            with(sharedPreferences.edit()) {
                putBoolean("ONBOARDING_COMPLETED", true)
                putString("USER_ROLE", selectedRole)
                putLong("ONBOARDING_COMPLETION_TIME", System.currentTimeMillis())
                apply()
            }

            Log.d("OnboardingCompletion", "Onboarding data saved to SharedPreferences")

            // Navigate to main activity
            navigateToMainActivity()

        } catch (e: Exception) {
            // ✅ NEW: Error handling for onboarding completion
            Log.e("OnboardingCompletion", "Error completing onboarding", e)
            Toast.makeText(this, "Error completing onboarding: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToMainActivity() {
        try {
            Log.d("OnboardingCompletion", "Attempting to navigate to MainActivity")

            // ✅ ENHANCED: Check if MainActivity exists before navigating
            val intent = Intent(this, MainActivity::class.java)

            // ✅ MODIFIED: Simplified intent flags - remove potential stack issues
            // OLD: intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

            // Pass the selected role to MainActivity for any role-specific setup
            intent.putExtra("USER_ROLE", selectedRole)

            Log.d("OnboardingCompletion", "Intent created with flags: ${intent.flags}")
            Log.d("OnboardingCompletion", "Starting MainActivity with role: $selectedRole")

            startActivity(intent)

            // ✅ NEW: Add transition animation with error handling
            try {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } catch (e: Exception) {
                Log.w("OnboardingCompletion", "Animation resources not found, using default transition", e)
            }

            finish()
            Log.d("OnboardingCompletion", "Navigation to MainActivity completed")

        } catch (e: Exception) {
            // ✅ NEW: Comprehensive error handling for navigation
            Log.e("OnboardingCompletion", "Critical error navigating to MainActivity", e)

            // ✅ NEW: Fallback navigation strategy
            when {
                e.message?.contains("Unable to find explicit activity class") == true -> {
                    // MainActivity doesn't exist
                    Toast.makeText(this, "MainActivity not found. Please check your project setup.", Toast.LENGTH_LONG).show()
                    Log.e("OnboardingCompletion", "MainActivity class not found in AndroidManifest.xml")
                }
                e.message?.contains("Permission") == true -> {
                    // Permission issues
                    Toast.makeText(this, "Permission error. Check AndroidManifest.xml", Toast.LENGTH_LONG).show()
                    Log.e("OnboardingCompletion", "Permission error - check activity declarations")
                }
                else -> {
                    // Generic error
                    Toast.makeText(this, "Navigation error: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("OnboardingCompletion", "Unknown navigation error", e)
                }
            }

            // ✅ NEW: Alternative action - stay on current screen instead of crashing
            Log.d("OnboardingCompletion", "Staying on completion screen due to navigation error")
        }
    }

    override fun onBackPressed() {
        Log.d("OnboardingCompletion", "Back pressed - returning to previous screen")
        super.onBackPressed()

        // ✅ NEW: Error handling for back navigation animation
        try {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        } catch (e: Exception) {
            Log.w("OnboardingCompletion", "Back animation resources not found", e)
        }
    }
}