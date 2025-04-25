package com.example.efficilog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.TextView
import com.example.efficilog.repository.FirestoreRepo
import com.example.efficilog.model.Users // Import Users class
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.RadioGroup
import android.widget.RadioButton



class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val repository = FirestoreRepo()

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButtonStaff: RadioButton
    private lateinit var radioButtonAdmin: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        nameEditText = findViewById(R.id.name)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        signUpButton = findViewById(R.id.signup_button)
        radioGroup = findViewById(R.id.loginTypeRadioGroup)
        radioButtonStaff = findViewById(R.id.radioButtonStaff)
        radioButtonAdmin = findViewById(R.id.radioAdmin)

        // Add navigation to the login Page
        val signUpLink = findViewById<TextView>(R.id.login_link)
        signUpLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            val selectedSignUpType = when (radioGroup.checkedRadioButtonId) {
                R.id.radioButtonStaff -> "staff"
                R.id.radioAdmin -> "admin"
                else -> "staff"
            }

            if (name.isEmpty() && email.isEmpty() && password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUpButton.isEnabled = false

            // Create authentication user
            auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener { authResult ->
                    val user = Users(
                        name = name,
                        email = email,
                        position = "operator",
                        role = selectedSignUpType,
//                        passcode = password
                    )

                    repository.addUser(
                        userId = authResult.user?.uid ?: "",
                        user = user,
                        onSuccess = {
                            Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
                            finish() // Close the sign-up activity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        },
                        onFailure = { exception ->
                            signUpButton.isEnabled = true
                            Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                .addOnFailureListener { exception ->
                    signUpButton.isEnabled = true
                    Toast.makeText(this, "Authencation Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }




//        // Get references to the UI elements
//        val nameField = findViewById<EditText>(R.id.name)
//        val emailField = findViewById<EditText>(R.id.email)
//        val passwordField = findViewById<EditText>(R.id.password)
//        val positonField = findViewById<EditText>(R.id.position)
//        val signUpButton = findViewById<Button>(R.id.signup_button)
//
//        // Add navigation to the login Page
//        val signUpLink = findViewById<TextView>(R.id.login_link)
//        signUpLink.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//
//        signUpButton.setOnClickListener {
//            val name = nameField.text.toString().trim()
//            val email = emailField.text.toString().trim()
//            val password = passwordField.text.toString().trim()
//
//            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
//                val newUser = Users(name = name, email = email, passcode = password)
//                registerUser(newUser)
//            } else {
//                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun registerUser(user: Users) {
//        repository.addUser(
//            user = user,
//            onSuccess = {
//                Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
//                finish() // Close the sign-up activity
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//            },
//            onFailure = { exception ->
//                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
//            }
//        )
    }

//                // Add user to Firestore
//                db.collection("users").add(newUser)
//                    .addOnSuccessListener {
//                        Toast.makeText(this, "Sign-Up Successful! Please log in.", Toast.LENGTH_SHORT).show()
//                        // Navigate back to login page
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                    .addOnFailureListener { e ->
//                        Toast.makeText(this, "Sign-Up Failed: ${e.message}", Toast.LENGTH_SHORT).show()
//                    }

}
