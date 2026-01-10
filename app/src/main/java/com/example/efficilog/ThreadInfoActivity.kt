package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.app.Activity
import android.widget.ArrayAdapter
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ThreadInfoActivity : AppCompatActivity() {

    // Firestore instance
    private val db = FirebaseFirestore.getInstance()

    private lateinit var currentFeatureName: String
    private var isFlange = false // Flag to determine if we're handling flanges

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_info)

        // Set header text based on the feature name passed from the previous activity
        currentFeatureName = intent.getStringExtra("FEATURE_NAME") ?: "Thread Information"
        isFlange = currentFeatureName.equals("Flanges", ignoreCase = true)

        // Set appropriate header text
        findViewById<TextView>(R.id.headerText).text = if (isFlange) {
            "$currentFeatureName Information"
        } else {
            "$currentFeatureName Thread Information"
        }

        // Find UI elements
        val pinSpinner = findViewById<Spinner>(R.id.pinSpinner)
        val threadTypeSpinner = findViewById<Spinner>(R.id.threadTypeSpinner)
        val sizeSpinner = findViewById<Spinner>(R.id.sizeSpinner)
        val numberEditText = findViewById<EditText>(R.id.numberEditText)
        val entriesContainer = findViewById<LinearLayout>(R.id.entriesContainer)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val addButton = findViewById<Button>(R.id.addButton)

        // IMPORTANT: Set up the spinners FIRST, then configure UI
        setupSpinners(pinSpinner, threadTypeSpinner, sizeSpinner)

        // Configure UI based on feature type AFTER spinners are set up
        configureUIForFeature(pinSpinner, threadTypeSpinner, numberEditText)

        // Set up button click listener for adding new entries
        addButton.setOnClickListener {
            // Validate inputs based on feature type
            if (validateInputs(pinSpinner, threadTypeSpinner, sizeSpinner, numberEditText)) {
                // Create a new entry based on user input
                val entry = if (isFlange) {
                    // For flanges, we don't use pinSpinner, so we pass the flange type in threadType field
                    Entry(
                        type = "Flange", // Fixed type for flanges
                        threadType = threadTypeSpinner.selectedItem.toString(), // This is actually flange type
                        size = sizeSpinner.selectedItem.toString(),
                        number = numberEditText.text.toString().toIntOrNull() ?: 0
                    )
                } else {
                    // Regular thread entry
                    Entry(
                        type = pinSpinner.selectedItem.toString(),
                        threadType = threadTypeSpinner.selectedItem.toString(),
                        size = sizeSpinner.selectedItem.toString(),
                        number = numberEditText.text.toString().toIntOrNull() ?: 0
                    )
                }

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
                saveEntriesToFirestore(currentFeatureName, entries)
            } else {
                Toast.makeText(this, "No entries to submit.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Configures the UI elements based on whether we're dealing with flanges or regular threads
     */
    private fun configureUIForFeature(
        pinSpinner: Spinner,
        threadTypeSpinner: Spinner,
        numberEditText: EditText
    ) {
        if (isFlange) {
            // Hide pin/box spinner and its label for flanges
            pinSpinner.visibility = View.GONE

            // Find and hide the pin label
            val pinLabel = findViewById<TextView>(R.id.pinLabel)
            pinLabel?.visibility = View.GONE

            // Update thread type label to "Flange Type"
            val threadTypeLabel = findViewById<TextView>(R.id.threadTypeLabel)
            threadTypeLabel?.text = "Flange Type:"

            // Update number field hint
            numberEditText.hint = "Enter number of flanges"

            // Debug logging
            android.util.Log.d("ThreadInfo", "Flange mode: Pin spinner hidden, labels updated")
        } else {
            // Show pin/box spinner for regular threads
            pinSpinner.visibility = View.VISIBLE

            val pinLabel = findViewById<TextView>(R.id.pinLabel)
            pinLabel?.visibility = View.VISIBLE

            // Keep original labels
            val threadTypeLabel = findViewById<TextView>(R.id.threadTypeLabel)
            threadTypeLabel?.text = "Thread Type:"

            numberEditText.hint = "Enter number of threads"

            // Debug logging
            android.util.Log.d("ThreadInfo", "Thread mode: Pin spinner visible, original labels")
        }
    }

    private fun setupSpinners(
        pinSpinner: Spinner,
        threadTypeSpinner: Spinner,
        sizeSpinner: Spinner
    ) {
        if (isFlange) {
            // Setup spinners for flange configuration
            val flangeTypeOptions = listOf(
                "Select Flange Type",
                "Raised Face (RF)",
                "Ring-Type Joint (RTJ)",
                "Flat Face (FF)",
                "Weld Neck (WN)",
                "Slip-On (SO)",
                "Socket Weld (SW)",
                "Threaded (TH)",
                "Blind Flange",
                "Lap Joint (LJ)"
            )

            // Use the size options passed from dashboard
            val sizeOptions = intent.getStringArrayListExtra("SIZE_OPTIONS") ?: listOf("Select Size")

            // Set adapters
            threadTypeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, flangeTypeOptions)
            sizeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sizeOptions)
        } else {
            // Setup spinners for regular thread configuration
            val pinOptions = listOf("Select Configuration", "Pin", "Box")
            val threadTypeOptions = listOf(
                "Select Thread Type", "VAM TOP", "VAM FJL",
                "NEW VAM", "Stub-Acme", "CS", "PH-6",
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
    }

    private fun addEntryCard(entry: Entry, container: LinearLayout) {
        // Inflate the entry card layout
        val entryView = LayoutInflater.from(this).inflate(R.layout.entry_card, container, false) as CardView

        if (isFlange) {
            // For flanges, show different labels
            entryView.findViewById<TextView>(R.id.typeText).text = "Type: ${entry.type}"
            entryView.findViewById<TextView>(R.id.threadTypeText).text = "Flange Type: ${entry.threadType}"
            entryView.findViewById<TextView>(R.id.sizeText).text = "Size: ${entry.size}"
            entryView.findViewById<TextView>(R.id.numberText).text = "Number: ${entry.number}"
        } else {
            // For regular threads, show original labels
            entryView.findViewById<TextView>(R.id.typeText).text = "Type: ${entry.type}"
            entryView.findViewById<TextView>(R.id.threadTypeText).text = "Thread Type: ${entry.threadType}"
            entryView.findViewById<TextView>(R.id.sizeText).text = "Size: ${entry.size}"
            entryView.findViewById<TextView>(R.id.numberText).text = "Number: ${entry.number}"
        }

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
        return if (isFlange) {
            // For flanges, don't validate pin spinner
            threadTypeSpinner.selectedItem.toString() != "Select Flange Type" &&
                    sizeSpinner.selectedItem.toString() != "Select Size" &&
                    !numberEditText.text.isNullOrEmpty()
        } else {
            // For regular threads, validate all fields
            pinSpinner.selectedItem.toString() != "Select Configuration" &&
                    threadTypeSpinner.selectedItem.toString() != "Select Thread Type" &&
                    sizeSpinner.selectedItem.toString().isNotEmpty() &&
                    !numberEditText.text.isNullOrEmpty()
        }
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
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
            return
        }

        val userRef = db.collection("users").document(userId)
        val jobsRef = userRef.collection("jobs").document(featureName)

        // Retrieve the user's display name from their document
        userRef.get().addOnSuccessListener { userDoc ->
            val userName = userDoc.getString("name") ?: "Unknown User"

            // First, retrieve existing jobs for merging
            jobsRef.get().addOnSuccessListener { jobDoc ->
                val existingEntries = mutableListOf<Map<String, Any>>()

                if (jobDoc.exists()) {
                    val currentEntries = jobDoc.get("entries") as? List<Map<String, Any>>
                    if (currentEntries != null) {
                        existingEntries.addAll(currentEntries)
                    }
                }

                // Add new entries
                existingEntries.addAll(entries.map { entry ->
                    mapOf(
                        "type" to entry.type,
                        "threadType" to entry.threadType,
                        "size" to entry.size,
                        "number" to entry.number
                    )
                })

                val updatedData = mapOf(
                    "entries" to existingEntries,
                    "timestamp" to System.currentTimeMillis()
                )

                // Save under user's jobs
                jobsRef.set(updatedData, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Entries submitted successfully!", Toast.LENGTH_SHORT).show()

                        // Push a simplified summary to global productionActivity collection
                        val summaryEntry = mapOf(
                            "userId" to userId,
                            "name" to userName,
                            "task" to featureName,
                            "timestamp" to System.currentTimeMillis()
                        )

                        db.collection("productionActivity")
                            .add(summaryEntry)
                            .addOnSuccessListener {
                                // Send result back to dashboard activity
                                val resultIntent = Intent()
                                resultIntent.putExtra("SUBMITTED_FEATURE", featureName)
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Saved job but failed to log activity: ${e.message}", Toast.LENGTH_SHORT).show()
                                val resultIntent = Intent()
                                resultIntent.putExtra("SUBMITTED_FEATURE", featureName)
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to submit entries: ${e.message}", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_CANCELED)
                    }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to fetch user info: ${e.message}", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_CANCELED)
        }
    }
}