    package com.example.efficilog

    import androidx.fragment.app.Fragment
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.util.Log
    import androidx.recyclerview.widget.RecyclerView
    import android.view.View
    import android.view.ViewGroup
    import java.text.SimpleDateFormat
    import java.util.Date
    import java.util.Locale
    import com.google.firebase.firestore.FirebaseFirestore
    import androidx.recyclerview.widget.LinearLayoutManager
    import android.widget.Toast
    import com.example.efficilog.model.ProductionActivity
    import com.google.firebase.firestore.Query

    class ProductionFragment : Fragment() {
        private lateinit var recentActivitiesRecyclerView: RecyclerView
        private lateinit var adapter: ProductionActivityAdapter
        private val activityList = mutableListOf<ProductionActivity>()
        private val db = FirebaseFirestore.getInstance()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val binding = inflater.inflate(R.layout.fragment_production, container, false)

            // Initialize RecyclerView
            recentActivitiesRecyclerView = binding.findViewById(R.id.recent_activities_recyclerView)
            recentActivitiesRecyclerView.layoutManager = LinearLayoutManager(context)

            // Initialize Adapter
            adapter = ProductionActivityAdapter(activityList)
            recentActivitiesRecyclerView.adapter = adapter

            // Fetch production activities
            fetchRecentProductionActivities()

            return binding
        }
        private fun fetchRecentProductionActivities() {
            Log.d("ProductionFragment", "Fetching from productionActivity collection")

            db.collection("productionActivity")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnSuccessListener { snapshot ->
                    activityList.clear()

                    for (doc in snapshot.documents) {
                        val userId = doc.getString("userId") // Extract the correct user ID
                        val task = doc.getString("task") ?: "Unknown Task"
                        val timestamp = doc.getLong("timestamp") ?: 0L
                        val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(timestamp))

                        Log.d("ProductionFragment", "Retrieved userId: $userId")

                        if (userId != null) {
                            db.collection("users").document(userId)
                                .get()
                                .addOnSuccessListener { userDoc ->
                                    val userName = userDoc.getString("name") ?: "Unknown User"
                                    Log.d("ProductionFragment", "Mapped userId: $userId to userName: $userName")

                                    val activity = ProductionActivity(userName, task, formattedTime)
                                    activityList.add(activity)
                                    adapter.notifyDataSetChanged()
                                }
                                .addOnFailureListener { e ->
                                    Log.e("ProductionFragment", "Failed to fetch user name for ID: $userId, Error: ${e.message}")
                                }
                        } else {
                            Log.e("ProductionFragment", "Missing userId in productionActivity document.")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ProductionFragment", "Error fetching activities: ${e.message}")
                    Toast.makeText(context, "Failed to load activities", Toast.LENGTH_SHORT).show()
                }
        }

//        private fun fetchRecentProductionActivities() {
//            Log.d("ProductionFragment", "Fetching from productionActivity collection")
//
//            db.collection("productionActivity")
//                .orderBy("timestamp", Query.Direction.DESCENDING)
//                .limit(5)
//                .get()
//                .addOnSuccessListener { snapshot ->
//                    activityList.clear()
//
//                    for (doc in snapshot.documents) {
//                        val userId = doc.getString("userId") ?: "Unknown ID" // Correct way to reference stored user ID
//                        val task = doc.getString("task") ?: "Unknown Task"
//                        val timestamp = doc.getLong("timestamp") ?: 0L
//                        val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(timestamp))
//
//                        // Fetch the actual username from the users collection
//                        db.collection("users").document(userId)
//                            .get()
//                            .addOnSuccessListener { userDoc ->
//                                val userName = userDoc.getString("name") ?: "Unknown User"
//
//                                Log.d("ProductionFragment", "Job: $task | User: $userName | Timestamp: $formattedTime")
//
//                                val activity = ProductionActivity(userName, task, formattedTime)
//                                activityList.add(activity)
//
//                                adapter.notifyDataSetChanged() // Update UI once names are retrieved
//                            }
//                            .addOnFailureListener { e ->
//                                Log.e("ProductionFragment", "Failed to fetch user name for ID: $userId, Error: ${e.message}")
//                            }
//                    }
//                }
//                .addOnFailureListener { e ->
//                    Log.e("ProductionFragment", "Error fetching activities: ${e.message}")
//                    Toast.makeText(context, "Failed to load activities", Toast.LENGTH_SHORT).show()
//                }
//        }


//        private fun fetchRecentProductionActivities() {
//            Log.d("ProductionFragment", "Fetching from productionActivity collection")
//
//            db.collection("productionActivity")
//                .orderBy("timestamp", Query.Direction.DESCENDING)
//                .limit(5)
//                .get()
//                .addOnSuccessListener { snapshot ->
//                    activityList.clear()
//                    for (doc in snapshot.documents) {
//                        val userName = doc.getString("userName") ?: "Unknown"
//                        val task = doc.getString("task") ?: "Unknown Task"
//                        val timestamp = doc.getLong("timestamp") ?: 0L
//                        val formattedTime =
//                            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(
//                                Date(timestamp)
//                            )
//                        val activity = ProductionActivity(userName, task, formattedTime)
//                        activityList.add(activity)
//                    }
//                    adapter.notifyDataSetChanged()
//                }
//                .addOnFailureListener { e ->
//                    Log.e("ProductionFragment", "Error fetching activities: ${e.message}")
//                    Toast.makeText(context, "Failed to load activities", Toast.LENGTH_SHORT).show()
//                }
//        }
    }