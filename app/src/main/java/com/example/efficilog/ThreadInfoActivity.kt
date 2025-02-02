package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.*
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ThreadInfoActivity : AppCompatActivity() {

    // Firestore instance
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_info)

        // Set header text based on the feature name passed from the previous activity
        val featureName = intent.getStringExtra("FEATURE_NAME") ?: "Thread Information"
        findViewById<TextView>(R.id.headerText).text = "$featureName Thread Information"

        // Find UI elements
        val pinSpinner = findViewById<Spinner>(R.id.pinSpinner)
        val threadTypeSpinner = findViewById<Spinner>(R.id.threadTypeSpinner)
        val sizeSpinner = findViewById<Spinner>(R.id.sizeSpinner)
        val numberEditText = findViewById<EditText>(R.id.numberEditText)
        val entriesContainer = findViewById<LinearLayout>(R.id.entriesContainer)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val addButton = findViewById<Button>(R.id.addButton)

        // Set up button click listener for adding new entries
        addButton.setOnClickListener {
            // Validate inputs
            if (validateInputs(pinSpinner, threadTypeSpinner, sizeSpinner, numberEditText)) {
                // Create a new entry based on user input
                val entry = Entry(
                    type = pinSpinner.selectedItem.toString(),
                    threadType = threadTypeSpinner.selectedItem.toString(),
                    size = sizeSpinner.selectedItem.toString(),
                    number = numberEditText.text.toString().toIntOrNull() ?: 0
                )

                addEntryCard(entry, entriesContainer)
                numberEditText.text.clear()
            } else {
                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle the Submit button to save all entries to Firestore
        submitButton.setOnClickListener {
            val entries = collectEntries(entriesContainer)
            if (entries.isNotEmpty()) {
                saveEntriesToFirestore(featureName, entries)
            } else {
                Toast.makeText(this, "No entries to submit.", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up the spinners with options
        setupSpinners(pinSpinner, threadTypeSpinner, sizeSpinner)
    }

    private fun setupSpinners(
        pinSpinner: Spinner,
        threadTypeSpinner: Spinner,
        sizeSpinner: Spinner
    ) {
        // Define options for each spinner
        val pinOptions = listOf("Select Configuration", "Pin", "Box")
        val threadTypeOptions = listOf(
            "Select Thread Type", "VAM TOP", "VAM FJL",
            "NEW VAM",
            "Stub-Acme", "CS", "PH-6",
            "PH-4", "B.T.C", "L.T.C", "SLX",
            "S.T.C", "NUE", "EUE", "T.BLUE",
            "HT-511", "IF", "REG.", "HUNTING"
        )
        val sizeOptions = intent.getStringArrayListExtra("SIZE_OPTIONS") ?: emptyList()

        // Set adapters for each spinner
        pinSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, pinOptions)
        threadTypeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, threadTypeOptions)
        sizeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sizeOptions)
    }

    private fun addEntryCard(entry: Entry, container: LinearLayout) {
        // Inflate the entry card layout
        val entryView = LayoutInflater.from(this).inflate(R.layout.entry_card, container, false) as CardView

        entryView.findViewById<TextView>(R.id.typeText).text = "Type: ${entry.type}"
        entryView.findViewById<TextView>(R.id.threadTypeText).text = "Thread Type: ${entry.threadType}"
        entryView.findViewById<TextView>(R.id.sizeText).text = "Size: ${entry.size}"
        entryView.findViewById<TextView>(R.id.numberText).text = "Number: ${entry.number}"

        // Close button to remove card
        val closeButton = entryView.findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            container.removeView(entryView)
        }

        // Add the entry layout to the main container
        container.addView(entryView)
    }

    private fun validateInputs(
        pinSpinner: Spinner,
        threadTypeSpinner: Spinner,
        sizeSpinner: Spinner,
        numberEditText: EditText
    ): Boolean {
        return pinSpinner.selectedItem.toString() != "Select Configuration" &&
                threadTypeSpinner.selectedItem.toString() != "Select Thread Type" &&
                sizeSpinner.selectedItem.toString().isNotEmpty() &&
                !numberEditText.text.isNullOrEmpty()
    }

    private fun collectEntries(container: LinearLayout): List<Entry> {
        val entries = mutableListOf<Entry>()
        for (i in 0 until container.childCount) {
            val entryView = container.getChildAt(i) as CardView
            val type = entryView.findViewById<TextView>(R.id.typeText).text.toString().substringAfter(": ").trim()
            val threadType = entryView.findViewById<TextView>(R.id.threadTypeText).text.toString().substringAfter(": ").trim()
            val size = entryView.findViewById<TextView>(R.id.sizeText).text.toString().substringAfter(": ").trim()
            val number = entryView.findViewById<TextView>(R.id.numberText).text.toString().substringAfter(": ").trim().toIntOrNull() ?: 0

            entries.add(Entry(type, threadType, size, number))
        }
        return entries
    }

    private fun saveEntriesToFirestore(featureName: String, entries: List<Entry>) {
        // Get the user ID from Firebase Authentication
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
            return
        }

        // Firestore path: users/{userId}/jobs/{featureName}
        val userRef = db.collection("users").document(userId)
        val jobsRef = userRef.collection("jobs").document(featureName)

        // Prepare data to save under the job
        val data = mapOf(
            "entries" to entries.map { entry ->
                mapOf(
                    "type" to entry.type,
                    "threadType" to entry.threadType,
                    "size" to entry.size,
                    "number" to entry.number
                )
            },
            "timestamp" to System.currentTimeMillis() // Optional: Add a timestamp
        )

        // Save the data to Firestore
        jobsRef.set(data, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Entries submitted successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to submit entries: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
//
//// Data class for an entry
//data class Entry(
//    val type: String,
//    val threadType: String,
//    val size: String,
//    val number: Int
//)
