package com.example.efficilog

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UnderDevelopmentActivity : AppCompatActivity() {

    private lateinit var featureNameText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var goBackButton: Button

    companion object {
        const val EXTRA_FEATURE_NAME = "feature_name"
        const val EXTRA_FEATURE_TYPE = "feature_type" // Optional: for different styling
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_under_development)

        initViews()
        setupContent()
        setupClickListeners()
    }

    private fun initViews() {
        featureNameText = findViewById(R.id.feature_name)
        descriptionText = findViewById(R.id.feature_description)
        goBackButton = findViewById(R.id.btn_go_back)
    }

    private fun setupContent() {
        // Get the feature name from the intent
        val featureName = intent.getStringExtra(EXTRA_FEATURE_NAME) ?: "This Feature"
        val featureType = intent.getStringExtra(EXTRA_FEATURE_TYPE) ?: "feature"

        // Set the dynamic title
        featureNameText.text = "Under Development"

        // Create dynamic description based on feature name
        val description = when {
            featureName.contains("Analytics", ignoreCase = true) -> {
                "Analytics is still under development and will be released in the next update."
            }
            featureName.contains("Security", ignoreCase = true) -> {
                "Security Settings are still under development and will be released in the next update."
            }
            featureName.contains("Profile", ignoreCase = true) -> {
                "Profile Information is still under development and will be released in the next update."
            }
            featureName.contains("History", ignoreCase = true) -> {
                "Job History features are still under development and will be released in the next update."
            }
            else -> {
                "$featureName is still under development and will be released in the next update."
            }
        }

        descriptionText.text = description
    }

    private fun setupClickListeners() {
        goBackButton.setOnClickListener {
            finish() // Close this activity and return to previous screen
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}