package com.example.efficilog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.efficilog.model.RecentActivity
import com.example.efficilog.model.UserDetail
import com.example.efficilog.adapter.RecentActivitiesAdapter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.example.efficilog.adapter.UserDetailsAdapter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AnalyticsFragment : Fragment() {

    // UI components
    private lateinit var pieChart: PieChart
    private lateinit var totalTasksTextView: TextView
    private lateinit var activeUsersText: TextView
    private lateinit var loadMoreButton: Button
    private lateinit var recentActivitiesRecyclerView: RecyclerView
    private lateinit var userDetailsRecyclerView: RecyclerView

    private val db = FirebaseFirestore.getInstance()

    // Data lists
    private val recentActivitiesList = mutableListOf<RecentActivity>()
    private val userDetailsList = mutableListOf<UserDetail>()

    private lateinit var recentActivitiesAdapter: RecentActivitiesAdapter
    private lateinit var userDetailsAdapter: UserDetailsAdapter

    // Pagination
    private var lastVisibleDocument: DocumentSnapshot? = null
    private val USERS_PER_PAGE = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analytics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupRecyclerViews()
        setupPieChart()

        loadAnalyticsData()

        loadMoreButton.setOnClickListener { loadMoreUsers() }
    }

    private fun initializeViews(view: View) {
        pieChart = view.findViewById(R.id.pieChart)
        totalTasksTextView = view.findViewById(R.id.total_activities)
        activeUsersText = view.findViewById(R.id.active_users)
        loadMoreButton = view.findViewById(R.id.load_more)
        recentActivitiesRecyclerView = view.findViewById(R.id.recent_activities_recyclerview)
        userDetailsRecyclerView = view.findViewById(R.id.user_details_recyclerview)
    }

    private fun setupRecyclerViews() {
//        recentActivitiesRecyclerView.layoutManager = LinearLayoutManager(context)
//        recentActivitiesRecyclerView = view.findViewById(R.id.recent_activities_recyclerview)
//        recentActivitiesRecyclerView = view.findViewById(R.id.recent_activities_recyclerview)


        recentActivitiesRecyclerView.layoutManager = LinearLayoutManager(context)
        recentActivitiesAdapter = RecentActivitiesAdapter(recentActivitiesList)
        recentActivitiesRecyclerView.adapter = recentActivitiesAdapter

        userDetailsRecyclerView.layoutManager = LinearLayoutManager(context)
        userDetailsAdapter = UserDetailsAdapter(userDetailsList)
        userDetailsRecyclerView.adapter = userDetailsAdapter
    }

    private fun setupPieChart() {
        pieChart.apply {
//            description.isEnabled = false
//            isRotationEnabled = true
//            setUsePercentValues(true)
//            centerText = "Task Distribution"
//            setCenterTextSize(18f)
//            setDrawHoleEnabled(true)
//            setDrawEntryLabels(false)
//            holeRadius = 40f
//            setHoleColor(Color.WHITE)
//
//            legend.isEnabled = true
//            data?.setDrawValues(false)
//
//            animateY(1000)


            // TODO: remove the values from the pie  chart slices
            description.isEnabled = false
                isRotationEnabled = true
                setUsePercentValues(true)
//                centerText = "Task Distribution"
                setCenterTextSize(18f)
                setDrawHoleEnabled(true)
                setDrawEntryLabels(false)  // Ensure no labels on the slices
//                val percentFormatter = PercentFormatter(pieChart)
//                setValueFormatter(percentFormatter)
                holeRadius = 40f
                setHoleColor(Color.WHITE)

                legend.isEnabled = true
                data?.setDrawValues(false)  // Disable numbers/values on slices

                // Optional: Disable the percentage labels
//                setDrawPercentValues(false) // This specifically turns off percentage values

                animateY(1000)


        }
    }

    private fun loadAnalyticsData() {
        fetchTotalActivitiesCount()
        fetchActiveUsersCount()
        fetchActivityTypeDistribution()
        fetchRecentActivities()
        fetchUserAnalyticsWithPagination()
    }

    // ------------------------------------------------------------
    // 1. TOTAL ACTIVITIES
    // ------------------------------------------------------------

    private fun fetchTotalActivitiesCount() {
        db.collection("productionActivity")
            .get()
            .addOnSuccessListener { snapshot ->
                val total = snapshot.size()
                totalTasksTextView.text = total.toString()
            }
            .addOnFailureListener {
                totalTasksTextView.text = "0"
            }
    }

    // ------------------------------------------------------------
    // 2. ACTIVE USERS
    // ------------------------------------------------------------

    private fun fetchActiveUsersCount() {
        db.collection("productionActivity")
            .get()
            .addOnSuccessListener { snap ->
                val count = snap.documents.mapNotNull { it.getString("userId") }.toSet().size
                activeUsersText.text = count.toString()
            }
    }

    // ------------------------------------------------------------
    // 3. PIE CHART DATA
    // ------------------------------------------------------------

    private fun fetchActivityTypeDistribution() {
        db.collection("productionActivity").get()
            .addOnSuccessListener { snapshots ->

                val typeCounts = mutableMapOf<String, Int>()

                for (doc in snapshots.documents) {
                    val task = doc.getString("task") ?: "Unknown"
                    typeCounts[task] = typeCounts.getOrDefault(task, 0) + 1
                }

                val entries = typeCounts.map { PieEntry(it.value.toFloat(), it.key) }

                updatePieChart(entries)
            }
    }

    private fun updatePieChart(entries: List<PieEntry>) {
        val colors = listOf(
            Color.parseColor("#4CAF50"),
            Color.parseColor("#2196F3"),
            Color.parseColor("#FFC107"),
            Color.parseColor("#FF5722"),
            Color.parseColor("#9C27B0")
        )

        val dataSet = PieDataSet(entries, "Task Distribution") .apply {
//            sliceSpace = 3f
//            selectionShift = 5f
            this.colors = colors
//            valueTextColor = Color.BLACK
//            valueTextSize = 14f
//            valueFormatter = null
            setDrawValues(false)
        }

//        dataSet.colors = colors
//        dataSet.valueTextColor = Color.BLACK
//        dataSet.valueTextSize = 14f
//        dataSet.valueFormatter = null
//        data?.setDrawValues(false)


        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.invalidate()
    }

    // ------------------------------------------------------------
    // 4. RECENT ACTIVITIES (last 5)
    // ------------------------------------------------------------

    private fun fetchRecentActivities() {
        db.collection("productionActivity")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { snap ->

                recentActivitiesList.clear()

                for (doc in snap.documents) {
                    val name = doc.getString("name") ?: "Unknown"
                    val task = doc.getString("task") ?: "Unknown"
                    val time = doc.getLong("timestamp") ?: 0L

                    val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        .format(Date(time))

                    recentActivitiesList.add(
                        RecentActivity(name, task, formattedTime)
                    )
                }

                recentActivitiesAdapter.notifyDataSetChanged()
            }
    }

    // ------------------------------------------------------------
    // 5. USER ANALYTICS (COMPUTED, PAGINATED)
    // ------------------------------------------------------------

    private fun fetchUserAnalyticsWithPagination() {
        // First load = compute analytics fresh
        userDetailsList.clear()
        lastVisibleDocument = null

        computeUserAnalytics {
            // After computing analytics, paginate the first page
            loadMoreUsers()
        }
    }

    private fun computeUserAnalytics(onComplete: () -> Unit) {
        db.collection("productionActivity")
            .get()
            .addOnSuccessListener { snapshots ->

                val grouped = mutableMapOf<String, MutableList<Map<String, Any>>>()

                for (doc in snapshots.documents) {
                    val userId = doc.getString("userId") ?: continue
                    val task = doc.getString("task") ?: "Unknown"
                    val time = doc.getLong("timestamp") ?: 0L

                    if (!grouped.containsKey(userId))
                        grouped[userId] = mutableListOf()

                    grouped[userId]!!.add(
                        mapOf(
                            "task" to task,
                            "timestamp" to time
                        )
                    )
                }

                // Convert each user group into analytics
                allComputedUserDetails.clear()
                val totalUsers = grouped.size
                var done = 0

                for ((userId, activities) in grouped) {

                    db.collection("users").document(userId).get()
                        .addOnSuccessListener { userDoc ->

                            val name = userDoc.getString("name") ?: "Unknown"

                            val totalUnits = activities.size
                            val lastTimestamp = activities.maxOf { it["timestamp"] as Long }
                            val formatted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                .format(Date(lastTimestamp))

                            val breakdown = mutableMapOf<String, Int>()
                            for (a in activities) {
                                val t = a["task"] as String
                                breakdown[t] = breakdown.getOrDefault(t, 0) + 1
                            }

                            val userDetail = UserDetail(
                                username = name,
                                lastActivity = formatted,
                                totalUnits = totalUnits,
                                activities = breakdown
                            )

                            allComputedUserDetails.add(userDetail)

                            done++
                            if (done == totalUsers) {
                                allComputedUserDetails.sortByDescending { it.totalUnits }
                                onComplete()
                            }
                        }
                }
            }
    }

    // Storage for computed analytics before pagination
    private val allComputedUserDetails = mutableListOf<UserDetail>()

    private fun loadMoreUsers() {
        val startIndex = userDetailsList.size
        val endIndex = (startIndex + USERS_PER_PAGE).coerceAtMost(allComputedUserDetails.size)

        if (startIndex >= allComputedUserDetails.size) {
            Toast.makeText(context, "No more users", Toast.LENGTH_SHORT).show()
            return
        }

        val nextBatch = allComputedUserDetails.subList(startIndex, endIndex)

        userDetailsList.addAll(nextBatch)
        userDetailsAdapter.notifyDataSetChanged()
    }
}
