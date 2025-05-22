package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.widget.Toast
import android.net.Uri
import android.widget.EditText
import com.google.firebase.firestore.SetOptions
import android.content.Intent
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.efficilog.repository.FirestoreRepo
import com.squareup.picasso.Picasso

class EditProfileActivity : AppCompatActivity() {
    // Initialize Firebase instances
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var firestoreRepository: FirestoreRepo

    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    // Request code for the image picker
    private val PICK_IMAGE_REQUEST = 1
    // Variable to store the selected image URI
    private var imageUri: Uri? = null
    // Reference to the profile image view
    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firestoreRepository = FirestoreRepo()

        // Initialize the views
        nameEditText = findViewById(R.id.edit_name)
        phoneEditText = findViewById(R.id.edit_phone)
        addressEditText = findViewById(R.id.edit_address)
        profileImageView = findViewById(R.id.profile_picture)
        emailEditText = findViewById(R.id.edit_email)
        val changeImageButton = findViewById<Button>(R.id.edit_picture_button)
        val saveButton: Button = findViewById(R.id.btn_save)
        val cancelButton: Button = findViewById(R.id.btn_cancel)

        // Set on click listener for the change image button
        changeImageButton.setOnClickListener {
            openFileChooser()
        }

        // Set on click listener for the save button
        saveButton.setOnClickListener {
            uploadImage()
        }

        // Set on click listener for the cancel button
        cancelButton.setOnClickListener {
            finish() // Close the activity
        }

        // Load the user's current profile picture
        loadProfileImage()

        // Load the user's current profile data
        val userId = auth.currentUser?.uid ?: return
        fetchUserProfile(userId)
    }

    private lateinit var progressDialog: AlertDialog

    // show progress bar to let the user know that somthing is going on
    private fun showProgressDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setView(R.layout.progress_dialog)
        builder.setCancelable(false)

        progressDialog = builder.create()
        progressDialog.show()
    }

    // hide progress bar
    private fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    private fun fetchUserProfile(userId: String) {
        firestoreRepository.fetchUserProfileById(
            userId,
            onSuccess = { user ->
                if (user != null) {
                    nameEditText.setText(user.name)
                    emailEditText.setText(user.email)
                    phoneEditText.setText(user.phone)
                    addressEditText.setText(user.address)
                }
            },
            onFailure = { exception ->
                Toast.makeText(this, "Error loading profile: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }


    // Function to open the file chooser for selecting an image
    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Handle the result of the image selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            // Display the selected image in the ImageView
            profileImageView.setImageURI(imageUri)
        }
    }

    // Function to upload the selected image to Firebase Storage

    private fun uploadImage() {
        showProgressDialog()
        if (imageUri != null) {
            val userId = auth.currentUser?.uid ?: return
            val fileReference = storage.reference.child("profile_images/$userId.jpg")
            fileReference.putFile(imageUri!!)
                .addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        hideProgressDialog()
                        // Save the image URL to Firestore
                        saveImageUrlToFirestore(imageUrl)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                hideProgressDialog()
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()

            saveUserInfo(null)
            hideProgressDialog()
        }
    }

    private fun saveUserInfo(imageUrl: String?) {
        val userId = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(userId)

        val name = findViewById<EditText>(R.id.edit_name).text.toString().trim()
        val phone = findViewById<EditText>(R.id.edit_phone).text.toString().trim()
        val address = findViewById<EditText>(R.id.edit_address).text.toString().trim()


        val userInfo = hashMapOf<String, Any?>(
            "profileImageUrl" to imageUrl
        )
        if (name.isNotEmpty()) userInfo["name"] = name
        if (phone.isNotEmpty()) userInfo["phone"] = phone
        if (address.isNotEmpty()) userInfo["address"] = address


        userRef.set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to save the image URL to Firestore
    private fun saveImageUrlToFirestore(imageUrl: String) {
        val userId = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(userId)
        userRef.update("profileImageUrl", imageUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish() // Close the activity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to load the user's current profile picture from Firestore
    private fun loadProfileImage() {
        val userId = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(userId)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val imageUrl = document.getString("profileImageUrl")
                    if (imageUrl != null) {
                        // Use Picasso to load the image into the ImageView
                        Picasso.get().load(imageUrl).into(profileImageView)
                    } else {
                        // Set a default image if no URL is found
                        profileImageView.setImageResource(R.drawable.profile_image)
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading profile picture: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
