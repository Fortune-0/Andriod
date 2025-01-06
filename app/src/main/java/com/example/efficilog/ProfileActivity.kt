package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Button
import android.content.Intent
import android.widget.TextView
import com.google.android.material.tabs.TabLayout

class ProfileActivity : AppCompatActivity() {
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

        // Set initial data
        profileImage.setImageResource(R.mipmap.profile_holder_round)
        nameField.text = "John Doe"
        position.text = "Software Engineer"
        phoneField.text = "2348001235122"
        emailField.text = "john.doe@gmail.com"
        addressField.text = "Rivers, State"

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