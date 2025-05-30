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
    import com.github.mikephil.charting.data.BarEntry
    import com.github.mikephil.charting.charts.BarChart
    import com.github.mikephil.charting.data.BarData
    import com.github.mikephil.charting.data.BarDataSet
//    import com.example.efficilog.adapter.ProductionActivityAdapter
    import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
    import com.github.mikephil.charting.components.XAxis
    import android.graphics.Color
    class ProductionFragment : Fragment() {
        private lateinit var recentActivitiesRecyclerView: RecyclerView
        private lateinit var adapter: ProductionActivityAdapter
        private val activityList = mutableListOf<ProductionActivity>()
        private val db = FirebaseFirestore.getInstance()
        private lateinit var barChart: BarChart

        private var pendingUserQueries = 0
        private val userActivityCount = mutableMapOf<String, Int>()


        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val binding = inflater.inflate(R.layout.fragment_production, container, false)

            // Initialize RecyclerView
            recentActivitiesRecyclerView = binding.findViewById(R.id.recent_activities_recyclerView)
            recentActivitiesRecyclerView.layoutManager = LinearLayoutManager(context)

            barChart = binding.findViewById(R.id.production_bar_chart)
            setupBarchart()

            // Initialize Adapter
            adapter = ProductionActivityAdapter(activityList)
            recentActivitiesRecyclerView.adapter = adapter

            // Fetch production activities
            fetchRecentProductionActivities()

            return binding
        }

        /**
         * Bar Chart Setup
         * This method configures the visual appearance and behavior of our bar chart.
         * We're setting up the chart BEFORE we have data to ensure proper initialization.
         */
//        private fun setupBarchart() {
//            barChart.apply {
//                setDrawBarShadow(false)
//                setDrawValueAboveBar(true)
//                description.isEnabled = false
//                setFitBars(true)
//                axisLeft.apply {
//                    axisMinimum = 0f
//                    setDrawGridLines(false)
//                }
//                axisRight.isEnabled = false
//                xAxis.apply {
//                    position = XAxis.XAxisPosition.BOTTOM
//                    setDrawGridLines(false)
//                    valueFormatter = IndexAxisValueFormatter(listOf("Cross-Over", "Pup-Joint", "Blank-Joint", "Heavy-Weight", "Casing Pipe", "Drill Pipe", "Bull-Plug", "Test-Cap", "Flanges"))
//                }
//                legend.isEnabled = false
//            }
//        }

        private fun setupBarchart(){
            barChart.apply {
                description.isEnabled=false

                // touch gestures
                setTouchEnabled(true)
                setDragEnabled(true)
                setScaleEnabled(false)

                // x-axis settings to show user names
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    textColor = Color.BLACK
                    granularity = 1f // Only show one label per user
//                    valueFormatter = IndexAxisValueFormatter(userActivitiesCount.keys.toList())
                    textSize = 10f
                }

                // y-axis configuration for activity counts
                axisLeft.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    axisMinimum = 0f // Start y-axis at 0
                    textColor = Color.BLACK
                    granularity = 1f // Only show whole numbers
                }

                // Disable right Y-axis to clean up appearance
                axisRight.isEnabled = false

                // Configure legend
                legend.isEnabled = true
                legend.textColor = Color.BLACK
            }
        }


        private fun fetchRecentProductionActivities() {
            Log.d("ProductionFragment", "Fetching from productionActivity collection")

            db.collection("productionActivity")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnSuccessListener { snapshot ->
                    activityList.clear()
                    userActivityCount.clear()

                    pendingUserQueries = snapshot.documents.size

                    if (snapshot.documents.isEmpty()) {
                        updateBarChart()
                        return@addOnSuccessListener
                    }

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

                                    // Update user activity count
                                    val activity = ProductionActivity(userName, task, formattedTime)
                                    activityList.add(activity)
                                    userActivityCount[userName] = userActivityCount.getOrDefault(userName, 0) + 1

                                    pendingUserQueries--
                                    if (pendingUserQueries == 0) {
                                        Log.d("ProductionFragment", "All user queries completed, updating chart")
                                        adapter.notifyDataSetChanged()
                                        updateBarChart()
                                    }

                                }
                                .addOnFailureListener { e ->
                                    Log.e("ProductionFragment", "Failed to fetch user name for ID: $userId, Error: ${e.message}")
                                }
                        } else {
                            Log.e("ProductionFragment", "Missing userId in productionActivity document.")

                            pendingUserQueries--
                            if (pendingUserQueries == 0) {
                                adapter.notifyDataSetChanged()
                                updateBarChart()
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ProductionFragment", "Error fetching activities: ${e.message}")
                    Toast.makeText(context, "Failed to load activities", Toast.LENGTH_SHORT).show()
                }
        }

        /**
         * Chart Data Update
         * This method converts our user activity count data into chart-compatible format.
         * We're using indexed entries because MPAndroidChart requires numeric X-values.
         */

        private fun updateBarChart() {
            if (userActivityCount.isEmpty()) {
                barChart.clear()
                barChart.invalidate()
                return
            }

            val entries = mutableListOf<BarEntry>()
            val userNames = mutableListOf<String>()

            userActivityCount.entries.forEachIndexed { index, (userName, count) ->
                entries.add(BarEntry(index.toFloat(), count.toFloat()))
                userNames.add(userName)
                }

            // Create dataset with visual styling
            val dataSet = BarDataSet(entries, "Production Activities").apply {
                // Color scheme - you can customize these colors
                colors = listOf(
                    Color.parseColor("#FF6B35"), // Orange
                    Color.parseColor("#004E89"), // Dark Blue
                    Color.parseColor("#009639"), // Green
                    Color.parseColor("#FFD23F"), // Yellow
                    Color.parseColor("#EE5A52")  // Red
                )
                valueTextColor = Color.BLACK
                valueTextSize = 12f
            }

            // Create BarData and apply to chart
            val barData = BarData(dataSet)
            barChart.data = barData

            // CRITICAL: Set custom labels for X-axis
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(userNames)
            barChart.xAxis.labelCount = userNames.size

            // Refresh the chart
            barChart.animateY(1000) // Animate for better UX
            barChart.invalidate()

            Log.d("ProductionFragment", "Bar chart updated with ${entries.size} entries")
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
//    }