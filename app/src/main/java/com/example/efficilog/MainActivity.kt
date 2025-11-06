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
import com.example.efficilog.utils.SecurePreferencesHelper
//import com.example.efficilog.utils.SecurePreferencesHelper.Companion.securePreferences


class MainActivity : AppCompatActivity() {
    private val repository = FirestoreRepo()
    private lateinit var auth: FirebaseAuth
    private lateinit var securePreferences: SecurePreferencesHelper


    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var signUpLink: TextView
    private lateinit var loginTypeRadioGroup: RadioGroup
    private lateinit var radioButtonStaff: RadioButton
    private lateinit var radioAdmin: RadioButton
    private lateinit var remember_me: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        securePreferences = SecurePreferencesHelper(this)

        // Initialize UI components
        emailField = findViewById(R.id.username)
        passwordField = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        progressBar = findViewById(R.id.login_progress)
        signUpLink = findViewById(R.id.signup_link)
        loginTypeRadioGroup = findViewById(R.id.loginTypeRadioGroup)
        radioButtonStaff = findViewById(R.id.radioButtonStaff)
        radioAdmin = findViewById(R.id.radioAdmin)
        remember_me = findViewById(R.id.remember_me)




//        val googleLogin = findViewById<ImageView>(R.id.googleLogin)
//        val facebookLogin = findViewById<ImageView>(R.id.facebookLogin)

        // Check if remember me is enabled and restore credentials
        if (securePreferences.isRememberMeEnabled()) {
            securePreferences.getSavedEmail()?.let { email ->
                emailField.setText(email)
            }
            securePreferences.getSavedPassword()?.let { password ->
                passwordField.setText(password)
            }

            // Set radio button based on saved user type
            if (securePreferences.isAdminUser()) {
                radioAdmin.isChecked = true
            } else {
                radioButtonStaff.isChecked = true
            }

            remember_me.isChecked = true

            // Auto login if credentials are available
            attemptAutoLogin()
        }

        // Sign-up navigation
        signUpLink.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

//        googleLogin.setOnClickListener {
//            Toast.makeText(this, "Google login not implemented yet", Toast.LENGTH_SHORT).show()
//            // You can integrate Firebase Google Sign-In here later
//        }
//
//        facebookLogin.setOnClickListener {
//            Toast.makeText(this, "Facebook login not implemented yet", Toast.LENGTH_SHORT).show()
//            // You can integrate Facebook Login here later
//        }

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


    private fun attemptAutoLogin() {
        if (securePreferences.isRememberMeEnabled()) {
            val email = securePreferences.getSavedEmail()
            val password = securePreferences.getSavedPassword()

            if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                progressBar.visibility = View.VISIBLE
                loginButton.isEnabled = false
                validateLogin(email, password)
            }
        }
    }

//    private fun attemptAutoLogin() {
//        val email = securePreferences.getSavedEmail()
//        val password = securePreferences.getSavedPassword()
//
//        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
//            progressBar.visibility = View.VISIBLE
//            loginButton.isEnabled = false
//            validateLogin(email, password)
//        }
//    }

    private fun validateLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                repository.checkUserExists(
                    email = email,
                    onSuccess = { userExists ->
                        if (userExists) {
                            repository.getUserRole(
                                email = email,
                                onSuccess = { role ->
                                    progressBar.visibility = View.GONE
                                    loginButton.isEnabled = true

                                    if (role != null) {
                                        val selectedRole = if (radioAdmin.isChecked) "Admin" else "Staff"
                                        if (role.equals(selectedRole, ignoreCase = true)) {
                                            // Save login details if Remember Me is checked
                                            if (remember_me.isChecked) {
                                                securePreferences.saveCredentials(
                                                    email = email,
                                                    password = password,
                                                    isAdmin = (role == "Admin"),
                                                    rememberMe = true
                                                )
                                            } else {
                                                securePreferences.clearCredentials()
                                            }

                                            // Navigate based on user role
                                            val intent = if (role.equals("Admin", ignoreCase = true)) {
                                                Intent(this, AdminActivity::class.java)
                                            } else {
                                                Intent(this, DashboardActivity::class.java)
                                            }
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Unauthorized access: You are not $selectedRole",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(this, "Unable to retrieve user role", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onFailure = { exception ->
                                    progressBar.visibility = View.GONE
                                    loginButton.isEnabled = true
                                    Log.e("Firestore", "Error getting user role: ${exception.message}")
                                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            progressBar.visibility = View.GONE
                            loginButton.isEnabled = true
                            Toast.makeText(this, "User profile not found", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onFailure = { exception ->
                        progressBar.visibility = View.GONE
                        loginButton.isEnabled = true
                        Log.e("Firestore", "Error checking user: ${exception.message}")
                        Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .addOnFailureListener { exception ->
                progressBar.visibility = View.GONE
                loginButton.isEnabled = true
                Toast.makeText(this, "Authentication failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


//    private fun validateLogin(email: String, password: String) {
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnSuccessListener {
//                repository.checkUserExists(
//                    email = email,
//                    onSuccess = { userExists ->
//                        if (userExists) {
//                            // Now fetch the user's role
//                            repository.getUserRole(
//                                email = email,
//                                onSuccess = { role ->
//                                    progressBar.visibility = View.GONE
//                                    loginButton.isEnabled = true
//                                    if (role != null) {
//                                        // Determine what was selected in the UI
//                                        val selectedRole = if (radioAdmin.isChecked) "Admin" else "Staff"
//                                        if (role.equals(selectedRole, ignoreCase = true)) {
//                                            val intent = if (role.equals("Admin", ignoreCase = true)) {
//                                                Intent(this, AdminActivity::class.java)
//                                            } else {
//                                                Intent(this, DashboardActivity::class.java)
//                                            }
//                                            startActivity(intent)
//                                            finish()
//                                        } else {
//                                            Toast.makeText(
//                                                this,
//                                                "Unauthorized access: You are not $selectedRole",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    } else {
//                                        Toast.makeText(this, "Unable to retrieve user role", Toast.LENGTH_SHORT).show()
//                                    }
//                                },
//                                onFailure = { exception ->
//                                    progressBar.visibility = View.GONE
//                                    loginButton.isEnabled = true
//                                    Log.e("Firestore", "Error getting user role: ${exception.message}")
//                                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
//                                }
//                            )
//                        } else {
//                            progressBar.visibility = View.GONE
//                            loginButton.isEnabled = true
//                            Toast.makeText(this, "User profile not found", Toast.LENGTH_SHORT).show()
//                        }
//                    },
//                    onFailure = { exception ->
//                        progressBar.visibility = View.GONE
//                        loginButton.isEnabled = true
//                        Log.e("Firestore", "Error checking user: ${exception.message}")
//                        Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
//                    }
//                )
//            }
//            .addOnFailureListener { exception ->
//                progressBar.visibility = View.GONE
//                loginButton.isEnabled = true
//                Toast.makeText(this, "Authentication failed: ${exception.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
}
