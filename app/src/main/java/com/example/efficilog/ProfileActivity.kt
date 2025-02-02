package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Button
import android.content.Intent
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.util.Log
import com.example.efficilog.repository.FirestoreRepo

import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestoreRepository: FirestoreRepo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.title = getString(R.string.profile)

        // Initialize Views
        val profileImage: ImageView = findViewById(R.id.profileImage)
        val nameField: TextView = findViewById(R.id.nameField)
        val position: TextView = findViewById(R.id.positionField)
        val phoneField: TextView = findViewById(R.id.phoneField)
        val emailField: TextView = findViewById(R.id.emailField)
        val addressField: TextView = findViewById(R.id.addressField)
        val editButton: Button = findViewById(R.id.editButton)
        val settingsButton: ImageView = findViewById(R.id.settingButton)

        auth = FirebaseAuth.getInstance()
        firestoreRepository = FirestoreRepo()

        // Get current user id
        val currentUser = auth.currentUser
        Log.d("ProfileActivity", "Current user: ${currentUser?.email}")
        Log.d("ProfileActivity", "Is user logged in: ${currentUser != null}")


        if (currentUser != null) {
            // Fetch user profile using email
            firestoreRepository.fetchUserProfile(
                currentUser.email ?: "",
                onSuccess = { user ->  // Add explicit parameter name
                    if (user != null) {
                        // Update UI on main thread
                        runOnUiThread {
                            nameField.text = user.name
                            position.text = user.position  // Note: changed from position to profile to match your Users class
                            phoneField.text = user.phone ?: "N/A"
                            emailField.text = user.email
                            addressField.text = user.address ?: "N/A"

                            profileImage.setImageResource(R.mipmap.profile_holder_round)
                        }
                    } else {
                        Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show()
                    }
                },
                onFailure = { exception ->  // Add explicit parameter name
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }


        // Set initial data
//        profileImage.setImageResource(R.mipmap.profile_holder_round)
//        nameField.text = "John Doe"
//        position.text = "Software Engineer"
//        phoneField.text = "2348001235122"
//        emailField.text = "john.doe@gmail.com"
//        addressField.text = "Rivers, State"

        // Onclick Listeners for buttons
        editButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

//        // Initialize TabLayout
//        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
//        val viewPager = findViewById<ViewPager>(R.id.viewPager)
//        val adapter = ViewPagerAdapter(supportFragmentManager)
//        adapter.addFragment(InfoFragment(), "Info")
//        adapter.addFragment(ProjectsFragment(), "Projects")
//        adapter.addFragment(SettingsFragment(), "Settings")
//        viewPager.adapter = adapter
//        tabLayout.setupWithViewPager(viewPager)

    }
}