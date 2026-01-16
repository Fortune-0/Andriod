package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnboardingStaffActivity : AppCompatActivity() {

    // ✅ STEP 1: DECLARE all variables at class level
    private lateinit var tvSkip: TextView
    private lateinit var btnBack: Button
    private lateinit var btnContinue: Button

    // ✅ DECLARE OnboardingManager (DO NOT initialize here)
    private lateinit var onboardingManager: OnboardingManager

    private var selectedRole: String? = null

    companion object {
        private const val TAG = "OnboardingStaff"
    }

    // ✅ STEP 2: INITIALIZE everything in onCreate()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_staff)

        // ✅ Initialize OnboardingManager HERE (inside onCreate)
        onboardingManager = OnboardingManager(this)

        selectedRole = intent.getStringExtra("SELECTED_ROLE")
        Log.d(TAG, "OnboardingStaffActivity created with role: $selectedRole")

        initializeViews()
        setupClickListeners()

        Log.d(TAG, "OnboardingStaffActivity initialization complete")
    }

    private fun initializeViews() {
        tvSkip = findViewById(R.id.tv_skip)
        btnBack = findViewById(R.id.btn_back)
        btnContinue = findViewById(R.id.btn_continue)
        Log.d(TAG, "Views initialized successfully")
    }

    private fun setupClickListeners() {
        tvSkip.setOnClickListener {
            Log.d(TAG, "Skip clicked - completing onboarding and navigating to MainActivity")
            skipToMainActivity()
        }

        btnBack.setOnClickListener {
            Log.d(TAG, "Back clicked - returning to role selection")
            onBackPressed()
        }

        btnContinue.setOnClickListener {
            Log.d(TAG, "Continue clicked - navigating to completion")
            navigateToCompletion()
        }
    }

    private fun navigateToCompletion() {
        try {
            val intent = Intent(this, OnboardingCompletionActivity::class.java)
            intent.putExtra("SELECTED_ROLE", selectedRole)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
            Log.d(TAG, "Navigated to completion successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error navigating to completion", e)
            skipToMainActivity()
        }
    }

    /**
     * ✅ FIXED: Use OnboardingManager instead of direct SharedPreferences
     * This method is called when user clicks "Skip"
     */
    private fun skipToMainActivity() {
        try {
            Log.d(TAG, "Saving onboarding completion with role: $selectedRole")

            // ✅ CRITICAL FIX: Use OnboardingManager to save completion
            // This saves to the correct SharedPreferences file with correct keys
            onboardingManager.skipOnboarding()

            Log.d(TAG, "Onboarding completed successfully via OnboardingManager")


            // Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()

            Log.d(TAG, "Navigated to MainActivity successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error navigating to MainActivity", e)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}