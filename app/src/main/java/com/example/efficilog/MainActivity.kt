package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp
import com.example.efficilog.repository.FirestoreRepo

class MainActivity : AppCompatActivity() {
    private val repository = FirestoreRepo()
    private lateinit var auth: FirebaseAuth

    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var signUpLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        emailField = findViewById(R.id.username)
        passwordField = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        progressBar = findViewById(R.id.login_progress)
        signUpLink = findViewById(R.id.signup_link)

        // Sign-up navigation
        signUpLink.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // Login button click
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE  // Show progress bar
                loginButton.isEnabled = false // Disable button to prevent multiple clicks
                validateLogin(email, password)
            } else {
                Toast.makeText(this, "Please enter an email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                repository.checkUserExists(
                    email = email,
                    onSuccess = { userExists ->
                        progressBar.visibility = View.GONE  // Hide progress bar
                        loginButton.isEnabled = true // Enable button

                        if (userExists) {
                            startActivity(Intent(this, DashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "User profile not found", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onFailure = { exception ->
                        progressBar.visibility = View.GONE  // Hide progress bar
                        loginButton.isEnabled = true // Enable button
                        Log.e("Firestore", "Error checking user: ${exception.message}")
                        Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .addOnFailureListener { exception ->
                progressBar.visibility = View.GONE  // Hide progress bar
                loginButton.isEnabled = true // Enable button
                Toast.makeText(this, "Authentication failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
