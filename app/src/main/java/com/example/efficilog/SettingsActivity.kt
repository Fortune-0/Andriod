package com.example.efficilog

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var backButton: ImageView
    private lateinit var notificationsSwitch: Switch
    private lateinit var themeSwitch: Switch
    private lateinit var profileInfoLayout: LinearLayout
    private lateinit var securitySettingsLayout: LinearLayout
    private lateinit var clearJobHistory: LinearLayout
    private lateinit var deleteAccountLayout: LinearLayout
    private lateinit var privacyPolicyTextView: TextView
    private lateinit var termsOfServiceTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize SharedPreferences before setting content view
        sharedPreferences = getSharedPreferences("StaffSettings", MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Apply saved theme
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize UI components
        backButton = findViewById(R.id.backButton)
        notificationsSwitch = findViewById(R.id.switch_notifications)
        themeSwitch = findViewById(R.id.theme_switch)
        profileInfoLayout = findViewById(R.id.layout_profile_info)
        securitySettingsLayout = findViewById(R.id.layout_security_settings)
        clearJobHistory = findViewById(R.id.layout_clear_job_history)
        deleteAccountLayout = findViewById(R.id.layout_delete_account)
        privacyPolicyTextView = findViewById(R.id.tv_privacy_policy)
        termsOfServiceTextView = findViewById(R.id.tv_terms_of_service)

        // Load saved settings
        loadSettings()

        // Handle Back Button
        backButton.setOnClickListener {
            finish()
        }

        // Handle Notifications Toggle
        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("notifications", isChecked)
            Toast.makeText(this, "Notifications ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
        }

        // Handle Theme Switch (Dark Mode)
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("dark_mode", isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            recreate()
        }

        // Handle Profile Information Click
        profileInfoLayout.setOnClickListener {
            Toast.makeText(this, "Opening Profile Settings...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        // Handle Security Settings Click
        securitySettingsLayout.setOnClickListener {
            Toast.makeText(this, "Opening Security Settings...", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, SecuritySettingsActivity::class.java))
        }

        // Handle Clear Job History
        clearJobHistory.setOnClickListener {
            showConfirmationDialog(
                title = "Clear Job History",
                message = "Are you sure you want to clear your job history? This action cannot be undone.",
                onConfirm = {
                    Toast.makeText(this, "Clearing Job History...", Toast.LENGTH_SHORT).show()

                    clearUserJobHistory()
                }
            )
        }


        // Handle Delete Account
        deleteAccountLayout.setOnClickListener {
            showConfirmationDialog(
                title = "Delete Account",
                message = "Are you sure you want to delete your account? This action cannot be undone.",
                onConfirm = {
                    Toast.makeText(this, "Requesting Account Deletion...", Toast.LENGTH_SHORT).show()

                    deleteUserAccount()
                }
            )
        }

        // Open Privacy Policy
        privacyPolicyTextView.setOnClickListener {
            openWebPage("https://www.efficilog.com/privacy-policy")
        }

        // Open Terms of Service
        termsOfServiceTextView.setOnClickListener {
            openWebPage("https://www.efficilog.com/terms-of-service")
        }
    }

    private fun loadSettings() {
        notificationsSwitch.isChecked = sharedPreferences.getBoolean("notifications", true)
        themeSwitch.isChecked = sharedPreferences.getBoolean("dark_mode", false)
    }

    private fun saveSetting(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun clearUserJobHistory() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("jobs")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        document.reference.delete()
                    }
                    Toast.makeText(
                        this,
                        "Job history cleared successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        "Error clearing job history: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun deleteUserAccount() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId)
                .delete()
                .addOnSuccessListener {
                    auth.currentUser?.delete()?.addOnSuccessListener {
                        Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error deleting account: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showConfirmationDialog(title: String, message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ -> onConfirm() }
            .setNegativeButton("No", null)
            .show()
    }
}

//package com.example.efficilog
//
//import android.content.Intent
//import android.content.SharedPreferences
//import android.net.Uri
//import android.os.Bundle
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.Switch
//import android.widget.TextView
//import android.widget.Toast
//
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatDelegate
//
//
//class SettingsActivity : AppCompatActivity() {
//    private lateinit var backButton: ImageView
//    private lateinit var notificationsSwitch: Switch
//    private lateinit var themeSwitch: Switch
//    private lateinit var profileInfoLayout: LinearLayout
//    private lateinit var securitySettingsLayout: LinearLayout
//    private lateinit var clearJobHistory: LinearLayout
//    private lateinit var deleteAccountLayout: LinearLayout
//    private lateinit var privacyPolicyTextView: TextView
//    private lateinit var termsOfServiceTextView: TextView
//    private lateinit var sharedPreferences: SharedPreferences
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_settings)
//
//        // Initialize UI components
//        backButton = findViewById(R.id.backButton)
//        notificationsSwitch = findViewById(R.id.switch_notifications)
//        themeSwitch = findViewById(R.id.theme_switch)
//        profileInfoLayout = findViewById(R.id.layout_profile_info)
//        securitySettingsLayout = findViewById(R.id.layout_security_settings)
//        clearJobHistory = findViewById(R.id.layout_clear_job_history)
//        deleteAccountLayout = findViewById(R.id.layout_delete_account)
//        privacyPolicyTextView = findViewById(R.id.tv_privacy_policy)
//        termsOfServiceTextView = findViewById(R.id.tv_terms_of_service)
//
//        // Initialize SharedPreferences
//        sharedPreferences = getSharedPreferences("StaffSettings", MODE_PRIVATE)
//
//        // Apply saved theme before setting the content view
//        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
//        AppCompatDelegate.setDefaultNightMode(
//            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
//            else AppCompatDelegate.MODE_NIGHT_NO
//        )
//
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_settings)
//
//        // Load saved settings
//        loadSettings()
//
//        // Handle Back Button
//        backButton.setOnClickListener {
//            finish() // Close Settings Page
//        }
//
//        // Handle Notifications Toggle
//        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
//            saveSetting("notifications", isChecked)
//            Toast.makeText(this, "Notifications ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
//        }
//
////        // Handle Dark Mode Toggle
////        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
////            saveSetting("dark_mode", isChecked)
////            Toast.makeText(this, "Dark Mode ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
////            recreate() // Recreate Activity to apply theme changes
////        }
//        themeSwitch = findViewById(R.id.theme_switch)
//        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
//            saveSetting("dark_mode", isChecked)
//            AppCompatDelegate.setDefaultNightMode(
//                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
//                else AppCompatDelegate.MODE_NIGHT_NO
//            )
//            recreate()
//        }
//
//        // Handle Profile Information Click
//        profileInfoLayout.setOnClickListener {
//            Toast.makeText(this, "Opening Profile Settings...", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, EditProfileActivity::class.java))
//        }
//
//        // Handle Security Settings Click
//        securitySettingsLayout.setOnClickListener {
//            Toast.makeText(this, "Opening Security Settings...", Toast.LENGTH_SHORT).show()
////            startActivity(Intent(this, SecuritySettingsActivity::class.java))
//        }
//
//        // Handle Clear Job History
//        clearJobHistory.setOnClickListener {
//            // Show confirmation dialog
//            showConfirmationDialog(
//                title = "Clear Job History",
//                message = "Are you sure you want to clear your job history? This action cannot be undone.",
//                onConfirm = {
////                    clearJobHistory()
//                    Toast.makeText(this, "Clearing Job History...", Toast.LENGTH_SHORT).show()
//                }
//            )
//
//        }
//
//        // Handle Delete Account
//        deleteAccountLayout.setOnClickListener {
//            // Show confirmation dialog
//            showConfirmationDialog(
//                title = "Delete Account",
//                message = "Are you sure you want to delete your account? This action cannot be undone.",
//                onConfirm = {
//                    // Implement account deletion logic here
////                    deleteAccount()
//                    Toast.makeText(this, "Requesting Account Deletion...", Toast.LENGTH_SHORT).show()
//                }
//            )
//        }
//
//        // Open Privacy Policy
//        privacyPolicyTextView.setOnClickListener {
//            openWebPage("https://www.efficilog.com/privacy-policy")
//        }
//
//        // Open Terms of Service
//        termsOfServiceTextView.setOnClickListener {
//            openWebPage("https://www.efficilog.com/terms-of-service")
//        }
//    }
//
//    private fun loadSettings() {
//        notificationsSwitch.isChecked = sharedPreferences.getBoolean("notifications", true)
//        themeSwitch.isChecked = sharedPreferences.getBoolean("dark_mode", false)
//    }
//
//    private fun saveSetting(key: String, value: Boolean) {
//        val editor = sharedPreferences.edit()
//        editor.putBoolean(key, value)
//        editor.apply()
//    }
//
//    private fun openWebPage(url: String) {
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//        startActivity(intent)
//    }
//    private fun showConfirmationDialog(title: String, message: String, onConfirm: () -> Unit) {
//        AlertDialog.Builder(this)
//            .setTitle(title)
//            .setMessage(message)
//            .setPositiveButton("Yes") { _, _ -> onConfirm() }
//            .setNegativeButton("No", null)
//            .show()
//    }
//}
