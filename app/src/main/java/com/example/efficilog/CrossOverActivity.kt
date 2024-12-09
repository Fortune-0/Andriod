package com.example.efficilog

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.efficilog.Entry
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity

class CrossOverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crossover)
        
        // Initialize Pin Spinner
        val pinSpinner = findViewById <Spinner> (R.id.pinSpinner)
        val pinOptions = listOf("Pin", "Box")
        val pinAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pinOptions)
        pinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pinSpinner.adapter = pinAdapter

        // Initialize Thread Type Spinner
        val threadTypeSpinner = findViewById<Spinner>(R.id.threadTypeSpinner)
            val threadTypeOptions = listOf("Select Thread Type","VAM TOP", "VAM FJL",
                "VAM TOP HT", "NEW VAM",
                "Stub-Acme", "CS", "PH-6",
                "PH-4",  "B.T.C", "L.T.C",
                "S.T.C", "NUE", "EUE",
                "HT-511", "IF")
        val threadTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, threadTypeOptions)
        threadTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        threadTypeSpinner.adapter = threadTypeAdapter

        // Initialize Size Spinner
        val sizeSpinner = findViewById<Spinner>(R.id.sizeSpinner)
        val sizeOptions = listOf("Select Size","2 3/8", "2 7/8", "3 1/2", "4 1/2")
        val sizeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sizeOptions)
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sizeSpinner.adapter = sizeAdapter

        val numberEditText = findViewById<EditText>(R.id.numberEditText)
        val selectedInfo = findViewById<TextView>(R.id.selectedInfo)
        val addButton = findViewById<Button>(R.id.addButton)


        findViewById<Button>(R.id.submitButton).setOnClickListener {
            val selectedThreadType = threadTypeSpinner.selectedItem as String
            val selectedPin = pinSpinner.selectedItem as String
            }

        // Clear button logic
        findViewById<Button>(R.id.clearButton).setOnClickListener {
            pinSpinner.setSelection(0)
            threadTypeSpinner.setSelection(0)
            sizeSpinner.setSelection(0)
//            selectedInfo.text =
//                "Thread Type: Not specified\nSize: Not specified\nNumber: Not specified"
        }
    }
}