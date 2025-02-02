package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View
import android.view.LayoutInflater
import com.google.firebase.auth.FirebaseAuth



class HistoryActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()


    private val db = FirebaseFirestore.getInstance()
    private lateinit var historyLinearLayout: LinearLayout
    private lateinit var noHistoryText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Initialize Views
        historyLinearLayout = findViewById(R.id.history_linear_layout)
        noHistoryText = findViewById(R.id.no_history_text)

        loadProductionHistory()
    }

    private fun loadProductionHistory() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
            return
        }

        val userRef = db.collection("users").document(userId)
        val jobRef = userRef.collection("job")

        jobRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    noHistoryText.visibility = View.VISIBLE
                    historyLinearLayout.visibility = View.GONE
                    return@addOnSuccessListener

                }

                // Process each document
                for (doc in querySnapshot.documents) {
                    val featureName = doc.id // use feature name as document id
                    val timestamp = doc.getLong("timestamp") ?: 0L
                    val entries = doc.get("entries") as List<Map<String, Any>> ?: emptyList()

                    addJobCard(
                        featureName,
                        timestamp.toString(),
                        entries,
                        historyLinearLayout
                    )
                }
            }

            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error loading history, ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun addJobCard(
        featureName: String,
        timestamp: String,
        entries: List<Map<String, Any>>,
        parentLayout: LinearLayout
    ) {
        // inflate card layout
        val cardView = LayoutInflater.from(this)
            .inflate(R.layout.entry_card, parentLayout, false) as androidx.cardview.widget.CardView

        // Set feature name and timestamp
        val titleTextView = cardView.findViewById<TextView>(R.id.titleText)
        val timestampTextView = cardView.findViewById<TextView>(R.id.timestampTextView)
        val entriesTextView = cardView.findViewById<TextView>(R.id.threadTypeText)

        titleTextView.text = featureName
        timestampTextView.text = timestamp

        // Format entries as a string
        val entriesText = entries.joinToString(separator = "\n") { entry ->
            val type = entry["type"] as? String ?: "unknown"
            val threadType = entry["threadType"] as? String ?: "unknown"
            val size = entry["size"] as? String ?: "unknown"
            val number = entry["number"] as? Long ?: "unknown"
            "Type: $type, Thread Type: $threadType, Size: $size, Number: $number"
        }

        entriesTextView.text = entriesText

        parentLayout.addView(cardView)
    }
}


// TODO: Debug this history page to out where the error is coming from
// ERROR: History Cards are not being displayed







//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            val userId = currentUser.uid
//
//
//            // Fetch history items from Firestore
//            db.collection("users")
//                .document(userId)
//                .collection("job")
//                .get()
//                .addOnSuccessListener { querySnapshot ->
//                    handleSuccess(querySnapshot, historyLinearLayout, noHistoryText)
//                }
//                .addOnFailureListener { exception ->
//                    Toast.makeText(
//                        this,
//                        "Error loading history, ${exception.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//        } else {
//            Toast.makeText(this, "User not Logged in", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun handleSuccess(
//        querySnapshot: QuerySnapshot,
//        historyLinearLayout: LinearLayout,
//        noHistoryText: TextView
//    ) {
//        noHistoryText.visibility = View.GONE
//        querySnapshot.forEach { document ->
//            val jobData = document.data
//            val featureName = jobData["featureName"] as? String ?: "Unknown"
//            val timestamp = jobData["timestamp"] as? String ?: "Unknown"
//            val entries = jobData["entries"] as? List<Map<String, Any>> ?: emptyList()
//
//            addJobCard(featureName, timestamp, entries, historyLinearLayout)
//        }
//    }
//
//    private fun addJobCard(
//        featureName: String,
//        timestamp: String,
//        entries: List<Map<String, Any>>,
//        parentLayout: LinearLayout
//    ) {
//        // inflate the card layout activity
//        val cardView = LayoutInflater.from(this)
//            .inflate(R.layout.entry_card, historyLinearLayout, false) as androidx.cardview.widget.CardView
//
//        // Set feature name and timestamp
//        val titleTextView = cardView.findViewById<TextView>(R.id.titleText)
//        val timestampTextView = cardView.findViewById<TextView>(R.id.timestampTextView)
//        val entriesTextView = cardView.findViewById<TextView>(R.id.threadTypeText)
//
//        titleTextView.text = "Job: $featureName"
//        timestampTextView.text = "Date: $timestamp"
//
//        // Formart entries as a string
//        val entriesText = entries.joinToString(separator = "\n") { entry ->
//            val type = entry["type"] as? String ?: "unknown"
//            val threadType = entry["threadType"] as? String ?: "unknown"
//            val size = entry["size"] as? String ?: "unknown"
//            val number = entry["number"] as? Long ?: "unknown"
//            "Type: $type, Thread Type: $threadType, Size: $size, Number: $number"
//        }
//
//        entriesTextView.text = entriesText
//
//        // Add the card to the parent layout
//        historyLinearLayout.addView(cardView)
//    }
//}



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
//        TODO: Fetch and populate history items from Firestore and update the UI accordingly
//        val mockData = listOf(
//            HistoryRecord("Config A", "Thread A", "Size A", "5")
//        )
//        populateHistory(mockData)
//    }
//
//    private fun populateHistory(records: List<HistoryRecord>) {
//        if (records.isEmpty()) {
//            noHistoryText.visibility = View.VISIBLE
//        } else {
//            noHistoryText.visibility = View.GONE
//            for (record in records) {
//                addHistoryCard(record)
//            }
//        }
//    }
//    private fun addHistoryCard(record: HistoryRecord) {
//        val inflater = LayoutInflater.from(this)
//        val cardView = inflater.inflate(R.layout.history_card, historyLinearLayout, false) as CardView
//        val titleText: TextView = cardView.findViewById(R.id.titleText)
//        val typeText: TextView = cardView.findViewById(R.id.typeText)
//        val threadTypeText: TextView = cardView.findViewById(R.id.threadTypeText)
//        val sizeText: TextView = cardView.findViewById(R.id.sizeText)
//        val numberText: TextView = cardView.findViewById(R.id.numberText)
//    }
//
//    titleText.text = "History"
//    typeText.text = "Config: ${record.config}"
//    threadTypeText.text = "Thread: ${record.threadType}"
//    sizeText.text = "Size: ${record.size}"
//    numberText.text = "Number: ${record.number}"

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
//}