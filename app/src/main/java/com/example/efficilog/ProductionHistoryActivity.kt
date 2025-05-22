package com.example.efficilog

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ProductionHistoryActivity : AppCompatActivity() {
    private lateinit var productionRecyclerView: RecyclerView
    private lateinit var adapter: ProductionHistoryAdapter
    private val historyList = mutableListOf<ProductionHistoryCard>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_production_history)

        productionRecyclerView = findViewById(R.id.production_recycler_view)
        productionRecyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductionHistoryAdapter(historyList)
        productionRecyclerView.adapter = adapter

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener { finish() }

        val staffId = intent.getStringExtra("staffId")
        if (staffId != null) {
            fetchProductionHistory(staffId)
        } else {
            Toast.makeText(this, "No Staff ID received.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchProductionHistory(staffId: String) {
        db.collection("users").document(staffId).collection("production_history")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Toast.makeText(this, "No production history found.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                for (doc in querySnapshot.documents) {
                    val timestamp = doc.getString("timestamp") ?: "Unknown"
                    val type = doc.getString("type") ?: "Unknown"
                    val threadType = doc.getString("threadType") ?: "Unknown"
                    val size = doc.getString("size") ?: "Unknown"
                    val number = doc.getLong("number")?.toInt() ?: 0

                    historyList.add(
                        ProductionHistoryCard(
                            timestamp, type, threadType, size, number
                        )
                    )
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching production history: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
