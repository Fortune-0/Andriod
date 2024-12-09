package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.example.efficilog.model.Users // Import Users class

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()

        // Test Firestore connection
        db.collection("test").add(hashMapOf("key" to "value"))
            .addOnSuccessListener {
                Log.d("Firebase", "Test document added successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error adding document", e)
            }

        // Get references to the UI elements
        val usernameField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)

        // Set up click listener for the login button
        loginButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val user = Users(
                    name = username,
                    passcode = password
                )
                addUserToFirestore(user)
            } else {
                Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show()
            }

            // Navigate to the DashboardActivity
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    fun addUserToFirestore(user: Users) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "User added with ID: ${documentReference.id}")
                Toast.makeText(this, "User added successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding user", e)
                Toast.makeText(this, "Failed to add user: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
