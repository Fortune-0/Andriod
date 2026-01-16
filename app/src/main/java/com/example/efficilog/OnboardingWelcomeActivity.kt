package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnboardingWelcomeActivity : AppCompatActivity() {

    private lateinit var tvSkip: TextView
    private lateinit var btnContinue: Button

    // ✅ NEW: Add OnboardingManager
    private lateinit var onboardingManager: OnboardingManager

    companion object {
        private const val TAG = "OnboardingWelcome"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_welcome)

        // ✅ NEW: Initialize OnboardingManager
        onboardingManager = OnboardingManager(this)
        Log.d(TAG, "OnboardingWelcomeActivity started")

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        tvSkip = findViewById(R.id.tv_skip)
        btnContinue = findViewById(R.id.btn_continue)
        Log.d(TAG, "Views initialized")
    }

    private fun setupClickListeners() {
        // Skip button - jumps directly to main activity
        tvSkip.setOnClickListener {
            Log.d(TAG, "Skip clicked - completing onboarding without role selection")
            skipOnboarding()
        }

        // Continue button - proceeds to role selection
        btnContinue.setOnClickListener {
            Log.d(TAG, "Continue clicked - navigating to role selection")
            navigateToRoleSelection()
        }
    }

    private fun navigateToRoleSelection() {
        try {
            val intent = Intent(this, OnboardingRoleSelectionActivity::class.java)
            startActivity(intent)
            // Apply slide transition animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            Log.d(TAG, "Navigated to role selection successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error navigating to role selection", e)
        }
    }

    /**
     * ✅ FIXED: Now properly saves onboarding completion using OnboardingManager
     */
    private fun skipOnboarding() {
        try {
            onboardingManager.skipOnboarding()
            Log.d(TAG, "Onboarding marked as completed (skipped)")
            navigateToMainActivity()
        } catch (e: Exception) {
            Log.e(TAG, "Error during skip onboarding", e)
        }
    }


    private fun navigateToMainActivity() {
        try {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            Log.d(TAG, "Navigated to MainActivity successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error navigating to MainActivity", e)
        }
    }

    /**
     * ✅ NEW: Prevent back navigation during onboarding
     */
    override fun onBackPressed() {
        // Don't allow going back from welcome screen
        // User should either continue or skip
        Log.d(TAG, "Back button pressed - ignoring (can't go back from welcome)")
    }
}