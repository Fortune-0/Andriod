package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnboardingStaffActivity : AppCompatActivity() {

    private lateinit var tvSkip: TextView
    private lateinit var btnBack: Button
    private lateinit var btnContinue: Button
    private var selectedRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_staff)

        selectedRole = intent.getStringExtra("SELECTED_ROLE")
        Log.d("OnboardingStaff", "OnboardingStaffActivity created with role: $selectedRole")

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        tvSkip = findViewById(R.id.tv_skip)
        btnBack = findViewById(R.id.btn_back)
        btnContinue = findViewById(R.id.btn_continue)
        Log.d("OnboardingStaff", "Views initialized successfully")
    }

    private fun setupClickListeners() {
        tvSkip.setOnClickListener {
            Log.d("OnboardingStaff", "Skip clicked - navigating to MainActivity")
            navigateToMainActivity()
        }

        btnBack.setOnClickListener {
            Log.d("OnboardingStaff", "Back clicked - returning to role selection")
            onBackPressed()
        }

        btnContinue.setOnClickListener {
            Log.d("OnboardingStaff", "Continue clicked - navigating to completion")
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
        } catch (e: Exception) {
            Log.e("OnboardingStaff", "Error navigating to completion", e)
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        try {
            val sharedPref = getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean("onboarding_completed", true)
                putString("user_role", selectedRole)
                apply()
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        } catch (e: Exception) {
            Log.e("OnboardingStaff", "Error navigating to MainActivity", e)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}