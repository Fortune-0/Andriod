package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout

class ThreadInfoActivity : AppCompatActivity() {
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

        // Set up button click listener for adding new entries
        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            // Create a new entry based on user input
            val entry = Entry(
                type = pinSpinner.selectedItem.toString(),
                threadType = threadTypeSpinner.selectedItem.toString(),
                size = sizeSpinner.selectedItem.toString(),
                number = numberEditText.text.toString().toIntOrNull() ?: 0
            )

            addEntryCard(entry, entriesContainer)
            numberEditText.text.clear()
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
        val pinOptions = listOf("Pin", "Box")
        val threadTypeOptions = listOf(
            "Select Thread Type", "VAM TOP", "VAM FJL",
            "VAM TOP HT", "NEW VAM",
            "Stub-Acme", "CS", "PH-6",
            "PH-4", "B.T.C", "L.T.C", "SLX",
            "S.T.C", "NUE", "EUE", "T.BLUE",
            "HT-511", "IF", "REG.", "HUNTING"
        )
        val sizeOptions = listOf(
            "Select Size","2 3/8", "2 7/8", "3 1/2", "4", "4 1/2", "5", "5 1/2", "6",
            "6 5/8", "7", "7 5/8", "8 5/8", "9 5/8", "10 3/4", "11 3/4", "13 3/8"
        )

        // Set adapters for each spinner
        pinSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, pinOptions)
        threadTypeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, threadTypeOptions)
        sizeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sizeOptions)
    }

    private fun addEntryCard(entry: Entry, container: LinearLayout) {
        // Create a LinearLayout to hold the entry details in the specified format
        val entryLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(8, 8, 8, 8)
        }

        // Create TextViews for each entry field in the required format
        val configTextView = TextView(this).apply {
            text = "Thread Details:"
            textSize = 18f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        }
        val typeTextView = TextView(this).apply {
            text = "Type: ${entry.type}"
            textSize = 14f
        }
        val threadTypeTextView = TextView(this).apply {
            text = "Thread Type: ${entry.threadType}"
            textSize = 14f
        }
        val sizeTextView = TextView(this).apply {
            text = "Size: ${entry.size}"
            textSize = 14f
        }
        val numberTextView = TextView(this).apply {
            text = "Number: ${entry.number}"
            textSize = 14f
        }

        // Add each TextView to the entry layout
        entryLayout.addView(configTextView)
        entryLayout.addView(typeTextView)
        entryLayout.addView(threadTypeTextView)
        entryLayout.addView(sizeTextView)
        entryLayout.addView(numberTextView)

        // Add the entry layout to the main container
        container.addView(entryLayout)
    }
}

//// Data class for Entry
//data class Entry(
//    val type: String,
//    val threadType: String,
//    val size: String,
//    val number: Int
//)
