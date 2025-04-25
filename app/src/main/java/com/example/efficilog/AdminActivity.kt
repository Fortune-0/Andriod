package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.efficilog.model.Staff
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {
    private lateinit var staffRecyclerView: RecyclerView
    private lateinit var adapter: StaffAdapter
    private val staffList = mutableListOf<Staff>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Initialize the RecyclerView
        staffRecyclerView = findViewById(R.id.staffRecyclerView)
        staffRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with the click listener
        adapter = StaffAdapter(staffList) { staff ->
            onStaffClicked(staff)
        }
        staffRecyclerView.adapter = adapter

        // Fetch staff data from Firestore
        fetchStaffData()
    }

    private fun fetchStaffData() {
        // Clear the existing list to avoid duplication
        staffList.clear()

        // Firestore collection "users" assumed to hold staff data
        db.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Toast.makeText(this, "No staff data found.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                for (document in querySnapshot.documents) {
                    val id = document.id
                    val name = document.getString("name") ?: "Unknown Name"
                    val role = document.getString("role") ?: "Unknown Role"
                    val status = document.getString("status") ?: "Inactive"
                    val lastActive = document.getString("lastActive") ?: "N/A"
                    val productionRate = document.getString("productionRate") ?: "N/A"

                    // Create a Staff object and add it to the list
                    val staff = Staff(id, name, role, status, lastActive, productionRate)
                    staffList.add(staff)
                }

                // Notify the adapter of data changes
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch staff data: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("AdminActivity", "Error fetching staff data", exception)
            }
    }

    private fun onStaffClicked(staff: Staff) {
        // Handle the click event on the "View Details" button
        // Example: Navigate to a staff profile or production history activity
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("staffId", staff.id)
        startActivity(intent)
    }
}
