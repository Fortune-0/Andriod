package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View
import android.view.LayoutInflater



class HistoryActivity : AppCompatActivity() {
    private lateinit var historyLinearLayout: LinearLayout
    private lateinit var noHistoryText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Initialize Views
        historyLinearLayout = findViewById(R.id.history_linear_layout)
        noHistoryText = findViewById(R.id.no_history_text)

        // Add history items to the linear layout

//        val historyItems = listOf("Task 1", "Task 2", "Task 3", "Task 4", "Task 5")
//        for (item in historyItems) {
//            val historyItemView = layoutInflater.inflate(R.layout.history_item, null)
//            val historyItemText = historyItemView.findViewById<TextView>(R.id.history_item_text)
//            historyItemText.text = item
//            historyLinearLayout.addView(historyItemView)
//        }
//
//        // Hide the no history text if there are history items
//        if (historyItems.isEmpty()) {
//            noHistoryText.visibility = TextView.VISIBLE
//        }
        // TODO: Fetch and populate history items from Firestore and update the UI accordingly
        val mockData = listOf(
            HistoryRecord("Config A", "Thread A", "Size A", "5")
        )
        populateHistory(mockData)
    }

    private fun populateHistory(records: List<HistoryRecord>) {
        if (records.isEmpty()) {
            noHistoryText.visibility = View.VISIBLE
        } else {
            noHistoryText.visibility = View.GONE
            for (record in records) {
                addHistoryCard(record)
            }
        }
    }
    private fun addHistoryCard(record: HistoryRecord) {
        val inflater = LayoutInflater.from(this)
        val cardView = inflater.inflate(R.layout.history_card, historyLinearLayout, false) as CardView
        val titleText: TextView = cardView.findViewById(R.id.titleText)
        val typeText: TextView = cardView.findViewById(R.id.typeText)
        val threadTypeText: TextView = cardView.findViewById(R.id.threadTypeText)
        val sizeText: TextView = cardView.findViewById(R.id.sizeText)
        val numberText: TextView = cardView.findViewById(R.id.numberText)
    }

    titleText.text = "History"
    typeText.text = "Config: ${record.config}"
    threadTypeText.text = "Thread: ${record.threadType}"
    sizeText.text = "Size: ${record.size}"
    numberText.text = "Number: ${record.number}"

//    private fun populateHistory()records: List<HistoryRecord>) {
//        val db = FirebaseFirestore.getInstance()
//        val query = db.collection("users")
//           .document(FirebaseAuth.getInstance().currentUser?.uid?: "")
//           .collection("history")
//
//        query.addSnapshotListener { snapshots, e ->
//            if (e!= null) {
//                return@addSnapshotListener
//            }
//
//            val historyItems = mutableListOf<HistoryRecord>()
//            snapshots?.forEach { document ->
//                val featureName = document["featureName"] as String
//                val threadType = document["threadType"] as String
//                val size = document["size"] as String
//                val number = document["number"] as String
//                historyItems.add(HistoryRecord(featureName, threadType, size, number))
//            }
//
//            populateHistory(historyItems)
//        }
//    }

//    private fun populateHistory()records: List<HistoryRecord>) {
//        historyLinearLayout.removeAllViews()
//        if (records.isEmpty()) {
//            noHistoryText.visibility = TextView.VISIBLE
//            return
//        }
//        noHistoryText.visibility = TextView.GONE
//
//    }
}