package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()

        // Get references to the UI elements
        val usernameField = findViewById<EditText>(R.id.newUser)
        val emailField = findViewById<EditText>(R.id.newEmail)
        val passwordField = findViewById<EditText>(R.id.newPassword)
        val signUpButton = findViewById<Button>(R.id.signup_button)

        // Add navigation to the Sign-Up Page
        val signUpLink = findViewById<TextView>(R.id.login_link)
        signUpLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                val newUser = hashMapOf(
                    "username" to username,
                    "email" to email,
                    "password" to password
                )

                // Add user to Firestore
                db.collection("users").add(newUser)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Sign-Up Successful! Please log in.", Toast.LENGTH_SHORT).show()
                        // Navigate back to login page
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Sign-Up Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
