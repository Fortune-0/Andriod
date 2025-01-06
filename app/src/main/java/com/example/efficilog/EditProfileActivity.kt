package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize the views
        val changePictureButton = findViewById<Button>(R.id.edit_picture_button)
        val saveButton: Button = findViewById(R.id.btn_save)
        val cancelButton: Button = findViewById(R.id.btn_cancel)
    }
}