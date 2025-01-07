package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.firestore.FirebaseFirestore
import com.example.efficilog.FirestoreRepo
import com.example.efficilog.model.Users // Import Users class

class MainActivity : AppCompatActivity() {
    private val repository = FirestoreRepo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        // Initialize Firestore
//        val db = FirebaseFirestore.getInstance()
//
//        // Test Firestore connection
//        db.collection("test").add(hashMapOf("key" to "value"))
//            .addOnSuccessListener {
//                Log.d("Firebase", "Test document added successfully!")
//            }
//            .addOnFailureListener { e ->
//                Log.w("Firebase", "Error adding document", e)
//            }

        // Add navigation to the Sign-Up Page
        val signUpLink = findViewById<TextView>(R.id.signup_link)
        signUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


        // Get references to the UI elements
        val emailField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)

        // Set up click listener for the login button
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
               validateLogin(email, password)
            } else {
                Toast.makeText(this, "Please enter a email and password", Toast.LENGTH_SHORT).show()
            }

            // Navigate to the DashboardActivity
//            val intent = Intent(this, DashboardActivity::class.java)
//            startActivity(intent)
        }
    }

//    fun addUserToFirestore(user: Users) {
//        val db = FirebaseFirestore.getInstance()
//
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d("Firestore", "User added with ID: ${documentReference.id}")
//                Toast.makeText(this, "User added successfully!", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                Log.e("Firestore", "Error adding user", e)
//                Toast.makeText(this, "Failed to add user: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }

    private fun validateLogin(email: String, password: String) {
        repository.checkUserExists(
            email = email,
            password = password,
            onSuccess = { userExists ->
                if (userExists) {
                    // navigate to dashboard
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            },
            onFailure = { exception ->
                Log.e("Firestore", "Error checking user: ${exception.message}")
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

}
