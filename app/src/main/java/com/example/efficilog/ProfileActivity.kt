package com.example.efficilog

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var jobTitle: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profilePhone: TextView
    private lateinit var editImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initializing the views
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        profileImage = findViewById(R.id.profileImage)
        profileName = findViewById(R.id.usernameText)
        jobTitle = findViewById(R.id.jobSection)
        profileEmail = findViewById(R.id.profileEmail)
            ?: run {
                Log.w("ProfileActivity", "profileEmail not found")
                TextView(this)  // Provide a dummy TextView
            }
        profilePhone = findViewById(R.id.profilePhone)
            ?: run {
                Log.w("ProfileActivity", "profilePhone not found")
                TextView(this)  // Provide a dummy TextView
            }

        val profilePageAdapter = ProfilePageAdapter(this)
        viewPager.adapter = profilePageAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Info"
                1 -> "Machine"
                else -> null
            }
        }.attach()

        setupDummyData()
    }

    private fun setupDummyData() {
        // Placeholder for now, replace with data fetched from the database
        profileImage.setImageResource(R.drawable.ic_profile_placeholder)
        profileName.text = "John Doe"
        jobTitle.text = "Field Engineer"
        profileEmail.text = "test@test.com"
        profilePhone.text = "0800 123 456"
    }
}



//        // Add this logging to help diagnose the issue
//        Log.d("ProfileActivity", "Starting onCreate")
//
//        try {
//            setContentView(R.layout.activity_profile)
//            Log.d("ProfileActivity", "Layout inflated successfully")
//
//            // Null checks with descriptive logging
//            tabLayout = findViewById(R.id.tabLayout)
//                ?: throw IllegalStateException("Could not find tabLayout")
//            viewPager = findViewById(R.id.viewPager)
//                ?: throw IllegalStateException("Could not find viewPager")
//            editImage = findViewById(R.id.editImage)
//                ?: throw IllegalStateException("Could not find editImage")
//            profileImage = findViewById(R.id.profileImage)
//                ?: throw IllegalStateException("Could not find profileImage")
//            profileName = findViewById(R.id.usernameText)
//                ?: throw IllegalStateException("Could not find usernameText")
//            jobTitle = findViewById(R.id.jobSection)
//                ?: throw IllegalStateException("Could not find jobSection")
//
//            // Optional views - only log if they're critical
//            profileEmail = findViewById(R.id.profileEmail)
//                ?: run {
//                    Log.w("ProfileActivity", "profileEmail not found")
//                    TextView(this)  // Provide a dummy TextView
//                }
//            profilePhone = findViewById(R.id.profilePhone)
//                ?: run {
//                    Log.w("ProfileActivity", "profilePhone not found")
//                    TextView(this)  // Provide a dummy TextView
//                }
//
//            // Setup ViewPager and TabLayout
//            val profilePageAdapter = ProfilePageAdapter(this)
//            viewPager.adapter = profilePageAdapter
//
//            // Link TabLayout and ViewPager
//            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//                tab.text = when (position) {
//                    0 -> "Info"
//                    1 -> "Machine"
//                    else -> null
//                }
//            }.attach()
//
//            // Set up placeholder data
//            setupDummyData()
//
//        } catch (e: Exception) {
//            Log.e("ProfileActivity", "Error in onCreate", e)
//            // Optionally, show an error to the user
//            // Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun setupDummyData() {
//        // Placeholder for now, replace with data fetched from the database
//        profileImage.setImageResource(R.drawable.ic_profile_placeholder) // Replace with dynamic image when implemented
//        profileName.text = "John Doe"
//        jobTitle.text = "Field Engineer"
//
//        // Only set if the views were found
//        profileEmail.text = "test@test.com"
//        profilePhone.text = "0800 123 456"
//    }
//}