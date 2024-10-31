package com.example.efficilog

import android.os.Bundle
import android.widget.Toast
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitch = findViewById<SwitchMaterial>(R.id.theme_switch)
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // Set initial state based on current mode
        themeSwitch.isChecked = AppCompatDelegate.getDefaultNightMode() ==
                AppCompatDelegate.MODE_NIGHT_YES

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            sharedPref.edit().putBoolean("dark_mode", isChecked).apply()

            // Apply theme
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // System theme switch
        val systemThemeSwitch = findViewById< SwitchMaterial >(R.id.system_theme_switch)
        systemThemeSwitch.isChecked = sharedPref.getBoolean("system_theme", false)
        systemThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            sharedPref.edit().putBoolean("use_system_theme", isChecked).apply()
            Toast.makeText(this, "Restart the app to apply changes", Toast.LENGTH_SHORT).show()
        }

        // Notification switch
        val notificationSwitch = findViewById< SwitchMaterial >(R.id.notification_switch)
        val reminderSwitch = findViewById <SwitchMaterial>(R.id.reminder_switch)
        notificationSwitch.isChecked = sharedPref.getBoolean("notification", true)
        reminderSwitch.isChecked = sharedPref.getBoolean("reminder", false)

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            sharedPref.edit().putBoolean("notification", isChecked).apply()
            Toast.makeText(this, "Notification ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }

        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            sharedPref.edit().putBoolean("reminder", isChecked).apply()
            Toast.makeText(this, "Daily Reminder ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }

        // Data & Privacy Buttons
        val backupButton = findViewById<MaterialButton>(R.id.backup_button)
        val restoreButton = findViewById<MaterialButton>(R.id.restore_button)
        val clearDataButton = findViewById<MaterialButton>(R.id.clear_data_button)

        backupButton.setOnClickListener {
            // Add backup functionality here
            Toast.makeText(this, "Data backup initiated", Toast.LENGTH_SHORT).show()
        }

        restoreButton.setOnClickListener {
            // Add restore functionality here
            Toast.makeText(this, "Data restore initiated", Toast.LENGTH_SHORT).show()
        }

        clearDataButton.setOnClickListener {
            // Add clear data functionality here
            Toast.makeText(this, "All data cleared", Toast.LENGTH_SHORT).show()
        }

        // About Section Buttons
        val aboutButton = findViewById<MaterialButton>(R.id.about_button)
        val privacyPolicyButton = findViewById<MaterialButton>(R.id.privacy_policy_button)
        val termsOfServiceButton = findViewById<MaterialButton>(R.id.terms_of_service_button)

        aboutButton.setOnClickListener {
            // Navigate to About Activity or show a dialog with app info
            Toast.makeText(this, "About EfficiLog", Toast.LENGTH_SHORT).show()
        }

        privacyPolicyButton.setOnClickListener {
            // Navigate to Privacy Policy screen or open URL
            Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show()
        }

        termsOfServiceButton.setOnClickListener {
            // Navigate to Terms of Service screen or open URL
            Toast.makeText(this, "Terms of Service", Toast.LENGTH_SHORT).show()
        }
    }
}
