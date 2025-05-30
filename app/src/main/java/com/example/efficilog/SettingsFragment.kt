package com.example.efficilog

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class SettingsFragment : Fragment() {

    private lateinit var darkModeSwitch: SwitchCompat
    private lateinit var notificationsSwitch: SwitchCompat
    private lateinit var dataSyncSwitch: SwitchCompat
    private lateinit var twoFactorSwitch: SwitchCompat
    private lateinit var logoutButton: MaterialButton
    private lateinit var btnChangePassword: MaterialButton
    private lateinit var btnUserPermissions: MaterialButton
    private lateinit var btnDataExport: MaterialButton

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        // Bind switches
        darkModeSwitch = view.findViewById(R.id.dark_mode_switch)
        notificationsSwitch = view.findViewById(R.id.notifications_switch)
        dataSyncSwitch = view.findViewById(R.id.data_sync_switch)
        twoFactorSwitch = view.findViewById(R.id.two_factor_switch)

        // Bind buttons
        logoutButton = view.findViewById(R.id.logout_button)
        btnChangePassword = view.findViewById(R.id.btn_change_password)
        btnUserPermissions = view.findViewById(R.id.btn_user_permissions)
        btnDataExport = view.findViewById(R.id.btn_data_export)

        // Load saved settings
        loadSettings()

        // Switch listeners
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("dark_mode", isChecked)
            ThemeHelper.setDarkMode(requireContext(), isChecked)
            activity?.recreate() // Apply theme immediately
        }

        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("notifications", isChecked)
            Toast.makeText(requireContext(), "Notifications ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
        }

        dataSyncSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("data_sync", isChecked)
            Toast.makeText(requireContext(), "Auto Sync ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
        }

        twoFactorSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("two_factor", isChecked)
            Toast.makeText(requireContext(), "2FA ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
        }

        // Button listeners
        logoutButton.setOnClickListener {
            logout()
        }

        btnChangePassword.setOnClickListener {
            Toast.makeText(requireContext(), "Change Password Clicked", Toast.LENGTH_SHORT).show()
            // TODO: startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }

        btnUserPermissions.setOnClickListener {
            Toast.makeText(requireContext(), "User Permissions Clicked", Toast.LENGTH_SHORT).show()
            // TODO: startActivity(Intent(requireContext(), UserPermissionsActivity::class.java))
        }

        btnDataExport.setOnClickListener {
            Toast.makeText(requireContext(), "Exporting data...", Toast.LENGTH_SHORT).show()
            // TODO: Implement export logic
        }
    }

    private fun loadSettings() {
        darkModeSwitch.isChecked = sharedPreferences.getBoolean("dark_mode", false)
        notificationsSwitch.isChecked = sharedPreferences.getBoolean("notifications", true)
        dataSyncSwitch.isChecked = sharedPreferences.getBoolean("data_sync", true)
        twoFactorSwitch.isChecked = sharedPreferences.getBoolean("two_factor", false)
    }

    private fun saveSetting(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    private fun logout() {
        // Clear any session-related data if needed
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}

//package com.example.efficilog

//
//import android.content.Intent
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.widget.Switch
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.material.button.MaterialButton
//
//class AdminSettingsActivity : AppCompatActivity() {
//    private lateinit var darkModeSwitch: Switch
//    private lateinit var notificationsSwitch: Switch
//    private lateinit var dataSyncSwitch: Switch
//    private lateinit var twoFactorSwitch: Switch
//    private lateinit var btnChangePassword: MaterialButton
//    private lateinit var btnUserPermissions: MaterialButton
//    private lateinit var btnDataExport: MaterialButton
//    private lateinit var logoutButton: MaterialButton
//    private lateinit var languageSelector: TextView
//
//    private lateinit var sharedPreferences: SharedPreferences
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_settings)
//
//        // Initialize UI components
//        darkModeSwitch = findViewById(R.id.dark_mode_switch)
//        notificationsSwitch = findViewById(R.id.notifications_switch)
//        dataSyncSwitch = findViewById(R.id.data_sync_switch)
//        twoFactorSwitch = findViewById(R.id.two_factor_switch)
//        btnChangePassword = findViewById(R.id.btn_change_password)
//        btnUserPermissions = findViewById(R.id.btn_user_permissions)
//        btnDataExport = findViewById(R.id.btn_data_export)
//        logoutButton = findViewById(R.id.logout_button)
//        languageSelector = findViewById(R.id.language_selector)
//
//        // Initialize SharedPreferences for storing settings
//        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
//
//        // Load saved settings
//        loadSettings()
//
//        // Handle switch toggles
//        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
//            saveSetting("dark_mode", isChecked)
//            Toast.makeText(this, "Dark Mode ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
//        }
//
//        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
//            saveSetting("notifications", isChecked)
//            Toast.makeText(this, "Notifications ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
//        }
//
//        dataSyncSwitch.setOnCheckedChangeListener { _, isChecked ->
//            saveSetting("data_sync", isChecked)
//            Toast.makeText(this, "Auto Data Sync ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
//        }
//
//        twoFactorSwitch.setOnCheckedChangeListener { _, isChecked ->
//            saveSetting("two_factor", isChecked)
//            Toast.makeText(this, "Two-Factor Authentication ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
//        }
//
////        // Handle navigation buttons
////        btnChangePassword.setOnClickListener {
////            startActivity(Intent(this, ChangePasswordActivity::class.java))
////        }
////
////        btnUserPermissions.setOnClickListener {
////            startActivity(Intent(this, UserPermissionsActivity::class.java))
////        }
////
////        btnDataExport.setOnClickListener {
////            exportData()
////        }
////
////        languageSelector.setOnClickListener {
////            startActivity(Intent(this, LanguageSelectionActivity::class.java))
////        }
//
//        // Handle logout
//        logoutButton.setOnClickListener {
//            logout()
//        }
//    }
//
//    private fun loadSettings() {
//        darkModeSwitch.isChecked = sharedPreferences.getBoolean("dark_mode", false)
//        notificationsSwitch.isChecked = sharedPreferences.getBoolean("notifications", true)
//        dataSyncSwitch.isChecked = sharedPreferences.getBoolean("data_sync", true)
//        twoFactorSwitch.isChecked = sharedPreferences.getBoolean("two_factor", false)
//    }
//
//    private fun saveSetting(key: String, value: Boolean) {
//        val editor = sharedPreferences.edit()
//        editor.putBoolean(key, value)
//        editor.apply()
//    }
//
//    private fun exportData() {
//        Toast.makeText(this, "Exporting Data...", Toast.LENGTH_SHORT).show()
//        // Implement actual data export logic here
//    }
//
//    private fun logout() {
//        // Clear session, go back to login screen
//        val intent = Intent(this, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//        finish()
//    }
//}
