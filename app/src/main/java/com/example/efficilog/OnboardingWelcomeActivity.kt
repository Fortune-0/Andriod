package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnboardingWelcomeActivity : AppCompatActivity() {

    private lateinit var tvSkip: TextView
    private lateinit var btnContinue: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_welcome)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        tvSkip = findViewById(R.id.tv_skip)
        btnContinue = findViewById(R.id.btn_continue)
    }

    private fun setupClickListeners() {
        // Skip button - jumps directly to main activity
        tvSkip.setOnClickListener {
            navigateToMainActivity()
        }

        // Continue button - proceeds to role selection
        btnContinue.setOnClickListener {
            navigateToRoleSelection()
        }
    }

    private fun navigateToRoleSelection() {
        val intent = Intent(this, OnboardingRoleSelectionActivity::class.java)
        startActivity(intent)
        // Apply slide transition animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun navigateToMainActivity() {

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}