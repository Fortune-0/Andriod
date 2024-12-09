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
        val pinOptions = listOf("Select Configuration", "Pin", "Box")
        val threadTypeOptions = listOf(
            "Select Thread Type", "VAM TOP", "VAM FJL",
            "VAM TOP HT", "NEW VAM",
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
        // inflate the entry card layout
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

        // Create TextViews for each entry field in the required format
//        val configTextView = TextView(this).apply {
//            text = "Thread Details:"
//            textSize = 18f
//            setTypeface(typeface, android.graphics.Typeface.BOLD)
//        }
//        val typeTextView = TextView(this).apply {
//            text = "Type: ${entry.type}"
//            textSize = 14f
//        }
//        val threadTypeTextView = TextView(this).apply {
//            text = "Thread Type: ${entry.threadType}"
//            textSize = 14f
//        }
//        val sizeTextView = TextView(this).apply {
//            text = "Size: ${entry.size}"
//            textSize = 14f
//        }
//        val numberTextView = TextView(this).apply {
//            text = "Number: ${entry.number}"
//            textSize = 14f
//        }

        // Add each TextView to the entry layout
//        entryLayout.addView(configTextView)
//        entryLayout.addView(typeTextView)
//        entryLayout.addView(threadTypeTextView)
//        entryLayout.addView(sizeTextView)
//        entryLayout.addView(numberTextView)

        // Add the entry layout to the main container
//        container.addView(entryLayout)
    }
}