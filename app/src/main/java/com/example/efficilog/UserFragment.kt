package com.example.efficilog

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.efficilog.model.User
import com.google.firebase.firestore.FirebaseFirestore

class UsersFragment : Fragment() {
    private lateinit var staffRecyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private val staffList = mutableListOf<User>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var searchEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        // Initialize the RecyclerView
        staffRecyclerView = view.findViewById(R.id.users_recycler_view)
        staffRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize search edit text
        searchEditText = view.findViewById(R.id.search_users)

        // Setup search functionality
        setupSearch()

        // Initialize the adapter with the click listener
        adapter = UserAdapter(requireContext(), staffList) { user ->
            onUserClicked(user)
        }
        staffRecyclerView.adapter = adapter

        // Fetch staff data from Firestore
        fetchStaffData()

        return view
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener { text ->
            filterUsers(text.toString())
        }
    }

    private fun filterUsers(query: String) {
        if (query.isEmpty()) {
            // If search query is empty, fetch all users again
            fetchStaffData()
            return
        }

        // Filter users based on name or role
        db.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                staffList.clear()

                for (document in querySnapshot.documents) {
                    val name = document.getString("name") ?: "Unknown Name"
                    val role = document.getString("role") ?: "Unknown Role"

                    // Check if name or role contains the search query (case insensitive)
                    if (name.contains(query, ignoreCase = true) ||
                        role.contains(query, ignoreCase = true)) {

                        val id = document.id
                        val status = document.getString("status") ?: "Inactive"
                        val lastActive = document.getString("lastActive") ?: "N/A"
                        val productionRate = document.getString("productionRate") ?: "N/A"

                        // Create a User object and add it to the list
                        val user = User(id, name, role, status, lastActive, productionRate)
                        staffList.add(user)
                    }
                }

                // Notify the adapter of data changes
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to search users: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("UsersFragment", "Error searching users", exception)
            }
    }

    private fun fetchStaffData() {
        // Clear the existing list to avoid duplication
        staffList.clear()

        // Firestore collection "users" assumed to hold staff data
        db.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Toast.makeText(context, "No staff data found.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                for (document in querySnapshot.documents) {
                    val id = document.id
                    val name = document.getString("name") ?: "Unknown Name"
                    val role = document.getString("role") ?: "Unknown Role"
                    val status = document.getString("status") ?: "Inactive"
                    val lastActive = document.getString("lastActive") ?: "N/A"
                    val productionRate = document.getString("productionRate") ?: "N/A"

                    // Create a User object and add it to the list
                    val user = User(id, name, role, status, lastActive, productionRate)
                    staffList.add(user)
                }

                // Notify the adapter of data changes
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to fetch staff data: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("UsersFragment", "Error fetching staff data", exception)
            }
    }

    private fun onUserClicked(user: User) {
        // Toggle the expansion state of the clicked user
        user.isExpanded = !user.isExpanded

        // Notify the adapter that the item has changed
        val position = staffList.indexOf(user)
        adapter.notifyItemChanged(position)
    }
}
//package com.example.efficilog
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import android.content.Intent
//import androidx.appcompat.widget.Toolbar
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.efficilog.model.Staff
//import com.google.firebase.firestore.FirebaseFirestore
//
//class UsersFragment : Fragment() {
//    private lateinit var staffRecyclerView: RecyclerView
//    private lateinit var adapter: UserAdapter
//    private val staffList = mutableListOf<User>()
//    private val db = FirebaseFirestore.getInstance()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the fragment layout
//        val view = inflater.inflate(R.layout.fragment_user, container, false)
//
//        // Initialize the RecyclerView
//        staffRecyclerView =
//            view.findViewById(R.id.users_recycler_view)
//
//        staffRecyclerView.layoutManager = LinearLayoutManager(context)
//
//        // Initialize the adapter with the click listener
//        adapter = UserAdapter(requireContext(), staffList) { user ->
//            onStaffClicked(user)
//        }
//        staffRecyclerView.adapter = adapter
//
//        // Fetch staff data from Firestore
//        fetchStaffData()
//
//        return view
//    }
//
//    private fun fetchStaffData() {
//        // Clear the existing list to avoid duplication
//        staffList.clear()
//
//        // Firestore collection "users" assumed to hold staff data
//        db.collection("users")
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                if (querySnapshot.isEmpty) {
//                    Toast.makeText(context, "No staff data found.", Toast.LENGTH_SHORT).show()
//                    return@addOnSuccessListener
//                }
//
//                for (document in querySnapshot.documents) {
//                    val id = document.id
//                    val name = document.getString("name") ?: "Unknown Name"
//                    val role = document.getString("role") ?: "Unknown Role"
//                    val status = document.getString("status") ?: "Inactive"
//                    val lastActive = document.getString("lastActive") ?: "N/A"
//                    val productionRate = document.getString("productionRate") ?: "N/A"
//
//                    // Create a Staff object and add it to the list
//                    val user = User(id, name, role, status, lastActive, productionRate)
//                    staffList.add(user)
//                }
//
//                // Notify the adapter of data changes
//                adapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(context, "Failed to fetch staff data: ${exception.message}", Toast.LENGTH_SHORT).show()
//                Log.e("UsersFragment", "Error fetching staff data", exception)
//            }
//    }
//
//    private fun onStaffClicked(user: User) {
//        // Handle the click event on the "View Details" button
//        // Example: Navigate to a staff profile or production history activity
//        val intent = Intent(context, ProfileActivity::class.java)
//        intent.putExtra("staffId", user.id)
//        startActivity(intent)
//    }
//}
