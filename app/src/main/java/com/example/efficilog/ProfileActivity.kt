package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Button
import android.widget.ImageButton
import android.content.Intent
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.util.Log
import android.widget.LinearLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.example.efficilog.repository.FirestoreRepo
import com.google.android.material.button.MaterialButton
//import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
//    private lateinit var db: FirebaseFirestore
//    TODO: features not available in free version
//    private lateinit var storage : FirebaseStorage
    private lateinit var firestoreRepository: FirestoreRepo

    // View references
    private lateinit var profileImage: ImageView
    private lateinit var nameField: TextView
    private lateinit var position: TextView
    private lateinit var phoneField: TextView
    private lateinit var emailField: TextView
    private lateinit var addressField: TextView
    private lateinit var editButton: MaterialButton
    private lateinit var settingsButton: LinearLayout
    private lateinit var helpButton: LinearLayout
    private lateinit var logoutButton: MaterialButton

//    private lateinit var backButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        firestoreRepository = FirestoreRepo()

        // Initialize Views
        profileImage = findViewById(R.id.profileImage)
        nameField = findViewById(R.id.nameField)
        position = findViewById(R.id.positionField)
        phoneField = findViewById(R.id.phoneField)
        emailField = findViewById(R.id.emailField)
        addressField = findViewById(R.id.addressField)
        editButton = findViewById(R.id.editButton)
        logoutButton = findViewById(R.id.logoutButton)
        settingsButton = findViewById(R.id.settingButton)
        helpButton = findViewById(R.id.helpButton)

//        val backButton: ImageButton = findViewById(R.id.backButton)
//        val historyButton: ImageView = findViewById(R.id.historyButton)

        Log.d("ProfileActivity", "Views initialized")

        // Set click listeners first
        editButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        helpButton.setOnClickListener {
            val intent = Intent(this, UnderDevelopmentActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            handleLogout()
        }



        // Load user data
        loadUserProfile()
    }

    override fun onResume() {
        super.onResume()
        // Reload user profile every time the activity resumes
        // This ensures profile updates are shown after editing
        loadUserProfile()
    }

    /**
     * Handles user logout
     * Signs out from Firebase and redirects to login screen
     */
    private fun handleLogout() {
        try {
            // Sign out from Firebase
            auth.signOut()

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Redirect to MainActivity (login screen)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        } catch (e: Exception) {
            Log.e("ProfileActivity", "Logout error: ${e.message}")
            Toast.makeText(this, "Error logging out", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        Log.d("ProfileActivity", "Loading profile for user: ${currentUser?.email}")

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            // Redirect to login if not logged in
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Clear previous data to avoid flashing old content
        nameField.text = ""
        position.text = ""
        phoneField.text = ""
        emailField.text = ""
        addressField.text = ""

        // Set default profile image
        profileImage.setImageResource(R.drawable.ic_profile)

        // Fetch user profile by ID rather than email for more reliable results
        firestoreRepository.fetchUserProfileById(
            currentUser.uid,
            onSuccess = { user ->
                if (user != null) {
                    Log.d("ProfileActivity", "User data loaded: ${user.name}")

                    // Update UI on the main thread
                    runOnUiThread {
                        nameField.text = user.name
                        position.text = user.position ?: "N/A"
                        phoneField.text = user.phone ?: "N/A"
                        emailField.text = user.email
                        addressField.text = user.address ?: "N/A"

                        // Load profile image if available
                        if (!user.profileImageUrl.isNullOrEmpty()) {
                            Picasso.get()
                                .load(user.profileImageUrl)
                                .placeholder(R.mipmap.profile_holder_round)
                                .error(R.mipmap.profile_holder_round)
                                .into(profileImage)
                        }
                    }
                } else {
                    Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show()
                }
            },
            onFailure = { exception ->
                Log.e("ProfileActivity", "Error loading profile: ${exception.message}")
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
//package com.example.efficilog
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.ImageView
//import android.widget.Button
//import android.widget.ImageButton
//import android.content.Intent
//import android.widget.TextView
//import com.google.android.material.tabs.TabLayout
//import com.google.firebase.auth.FirebaseAuth
//import android.widget.Toast
//import android.util.Log
//import com.example.efficilog.repository.FirestoreRepo
//
//import com.google.firebase.firestore.FirebaseFirestore
//
//class ProfileActivity : AppCompatActivity() {
//
//    private lateinit var auth: FirebaseAuth
//    private lateinit var firestoreRepository: FirestoreRepo
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
////        supportActionBar?.setDisplayHomeAsUpEnabled(true)
////        supportActionBar?.title = getString(R.string.profile)
//
//        // Initialize Views
//        val profileImage: ImageView = findViewById(R.id.profileImage)
//        val nameField: TextView = findViewById(R.id.nameField)
//        val position: TextView = findViewById(R.id.positionField)
//        val phoneField: TextView = findViewById(R.id.phoneField)
//        val emailField: TextView = findViewById(R.id.emailField)
//        val addressField: TextView = findViewById(R.id.addressField)
//        val editButton: Button = findViewById(R.id.editButton)
//        val settingsButton: ImageView = findViewById(R.id.settingButton)
//        val backButton: ImageButton = findViewById(R.id.backButton)
//        val historyButton: ImageView = findViewById(R.id.historyButton)
//
//
//        auth = FirebaseAuth.getInstance()
//        firestoreRepository = FirestoreRepo()
//
//        // Get current user id
//        val currentUser = auth.currentUser
//        Log.d("ProfileActivity", "Current user: ${currentUser?.email}")
//        Log.d("ProfileActivity", "Is user logged in: ${currentUser != null}")
//
////        backButton.setOnClickListener{
////            finish()
////        }
//
//
//        if (currentUser != null) {
//            // Fetch user profile using email
//            firestoreRepository.fetchUserProfileById(currentUser.uid,
//                onSuccess = { user ->  // Add explicit parameter name
//                    if (user != null) {
//                        // Update UI on main thread
//                        runOnUiThread {
//                            nameField.text = user.name
//                            position.text = user.position  // Note: changed from position to profile to match your Users class
//                            phoneField.text = user.phone ?: "N/A"
//                            emailField.text = user.email
//                            addressField.text = user.address ?: "N/A"
//
//                            profileImage.setImageResource(R.mipmap.profile_holder_round)
//                        }
//                    } else {
//                        Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show()
//                    }
//                },
//                onFailure = { exception ->  // Add explicit parameter name
//                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
//                }
//            )
//        } else {
//            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
//        }
//
//
//        // Set initial data
////        profileImage.setImageResource(R.mipmap.profile_holder_round)
////        nameField.text = "John Doe"
////        position.text = "Software Engineer"
////        phoneField.text = "2348001235122"
////        emailField.text = "john.doe@gmail.com"
////        addressField.text = "Rivers, State"
//
//        // Onclick Listeners for buttons
//        editButton.setOnClickListener {
//            val intent = Intent(this, EditProfileActivity::class.java)
//            startActivity(intent)
//        }
//
//        settingsButton.setOnClickListener {
//            val intent = Intent(this, SettingsActivity::class.java)
//            startActivity(intent)
//        }
//
//        historyButton.setOnClickListener {
//            val intent = Intent(this, HistoryActivity::class.java)
//            startActivity(intent)
//        }
//
//        backButton.setOnClickListener{
//            val intent = Intent(this, DashboardActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//            startActivity(intent)
//            finish()
//        }
//
////        override fun onResume() {
////            super.onResume()
////            // Refresh the profile data when the activity resumes
////            fetchUserProfile()
////        }
//
////        // Initialize TabLayout
////        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
////        val viewPager = findViewById<ViewPager>(R.id.viewPager)
////        val adapter = ViewPagerAdapter(supportFragmentManager)
////        adapter.addFragment(InfoFragment(), "Info")
////        adapter.addFragment(ProjectsFragment(), "Projects")
////        adapter.addFragment(SettingsFragment(), "Settings")
////        viewPager.adapter = adapter
////        tabLayout.setupWithViewPager(viewPager)
//
//    }
//}