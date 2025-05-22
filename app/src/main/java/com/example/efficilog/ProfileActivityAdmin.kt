package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
class ProfileActivityAdmin : AppCompatActivity() {
    private lateinit var staffNameTextView: TextView
    private lateinit var staffRoleTextView: TextView
    private lateinit var staffStatusTextView: TextView
    private lateinit var staffLastActiveTextView: TextView
    private lateinit var staffProductionRateTextView: TextView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_admin)

        // Initialize Views
        staffNameTextView = findViewById(R.id.staff_name)
        staffRoleTextView = findViewById(R.id.staff_role)
        staffStatusTextView = findViewById(R.id.staff_status)
        staffLastActiveTextView = findViewById(R.id.staff_last_activeAdmin)
        staffProductionRateTextView = findViewById(R.id.staff_production_rate)
        val backButton = findViewById<ImageButton>(R.id.back_buttonAdmin)

        // Set back button click listener
        backButton.setOnClickListener { finish() }

        // Get staff ID from passed from the AdminActivity
        val staffId = intent.getStringExtra("staffId")
        if (staffId != null) {
            fetchStaffDetails(staffId)
        } else {
            Toast.makeText(this, "Staff ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchStaffDetails(staffId: String) {
        db.collection("users").document(staffId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val staffName = document.getString("name") ?: "Unknown"
                    val staffRole = document.getString("role") ?: "Unknown"
                    val staffStatus = document.getString("status") ?: "Unknown"
                    val staffLastActive = document.getString("lastActive") ?: "N/A"
                    val staffProductionRate = document.getDouble("productionRate") ?: "Unknown"

                    // Set the values to the TextViews
                    staffNameTextView.text = staffName
                    staffRoleTextView.text = "Role: $staffRole"
                    staffStatusTextView.text = "Status: $staffStatus"
                    staffLastActiveTextView.text = "Last Active: $staffLastActive"
                    staffProductionRateTextView.text = "Production Rate: ${staffProductionRate ?: 0.0}"
                } else {
                    Toast.makeText(this, "No such document", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching details: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}