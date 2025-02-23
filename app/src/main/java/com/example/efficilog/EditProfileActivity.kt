package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.widget.Toast
import android.net.Uri
import android.content.Intent
import android.widget.ImageView
import com.squareup.picasso.Picasso

class EditProfileActivity : AppCompatActivity() {
    // Initialize Firebase instances
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Request code for the image picker
    private val PICK_IMAGE_REQUEST = 1
    // Variable to store the selected image URI
    private var imageUri: Uri? = null
    // Reference to the profile image view
    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize the views
        profileImageView = findViewById(R.id.profile_picture)
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
        if (imageUri != null) {
            val userId = auth.currentUser?.uid ?: return
            val fileReference = storage.reference.child("profile_images/$userId.jpg")
            fileReference.putFile(imageUri!!)
                .addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        // Save the image URL to Firestore
                        saveImageUrlToFirestore(imageUrl)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to save the image URL to Firestore
    private fun saveImageUrlToFirestore(imageUrl: String) {
        val userId = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(userId)
        userRef.update("profileImageUrl", imageUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
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
