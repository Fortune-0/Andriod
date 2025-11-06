//package com.example.efficilog
//
//
//import android.os.Bundle
//import android.view.View
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import android.graphics.Color
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.RecyclerView
//import com.github.mikephil.charting.charts.BarChart
//import com.github.mikephil.charting.charts.PieChart
//import com.google.firebase.firestore.FirebaseFirestore
//import androidx.recyclerview.widget.LinearLayoutManager
//
//
//class AnalyticsFragment : Fragment() {
//
////    private lateinit var analyticsViewModel: AnalyticsViewModel
//
//    // Ui Components
//    private lateinit var pieChart: PieChart
//    private lateinit var barChart: BarChart
//    private lateinit var totalTasksTextView: TextView
//    private lateinit var userDetailsRecyclerView: RecyclerView
//    private lateinit var recentActivitiesRecyclerView: RecyclerView
//    private lateinit var loadMoreButton: Button
//    private lateinit var activeUsersText: TextView
////    private lateinit var growthText: TextView
//
//    private val db = FirebaseFirestore.getInstance()
//
//    // Data holders
//    private val recentActivitiesList = mutableListOf<RecentActivity>()
//    private val userDetailsList = mutableListOf<UserDetail>()
//    private lateinit var recentActivitiesAdapter: RecentActivitiesAdapter
//    private lateinit var userDetailsAdapter: UserDetailsAdapter
//
//    // Pagination
//    private var lastVisibleDocument: com.google.firebase.firestore.DocumentSnapshot? = null
//    private val USERS_PER_PAGE = 3
//
//
////    override fun onCreateView(
////        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
////        savedInstanceState: Bundle?
////    ): android.view.View? {
////        return inflater.inflate(R.layout.fragment_analytics, container, false)
////    }
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_analytics, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        initializeViews(view)
//
//        // Setup recycler views with adapter
//        setupRecyclerViews()
//
//        // Setup charts
//        setupPieChart()
//
//        // Load data from firestore
//        loadAnalyticsData()
//
//        loadMoreButton.setOnClickListener {
//            loadMoreUsers()
//        }
//    }
//
//    private fun initializeViews(view: View) {
//        recentActivitiesRecyclerView = view.findViewById(R.id.recent_activities_recyclerView)
//        userDetailsRecyclerView = view.findViewById(R.id.user_details_recyclerview)
//        pieChart = view.findViewById(R.id.pieChart)
//        loadMoreButton = view.findViewById(R.id.load_more)
////        totalActivitiesText = view.findViewById(R.id.total_activities)
//        activeUsersText = view.findViewById(R.id.active_users)
////        growthText = view.findViewById(R.id.tv_growth)
//        totalTasksTextView = view.findViewById(R.id.total_activities)
//
//    }
//
//    /** Setup RecyclerViews with their adapters
//     */
//    private fun setupRecyclerViews() {
//
//        // Recent Activities RecyclerView
//        recentActivitiesRecyclerView.layoutManager = LinearLayoutManager(context)
//        recentActivitiesAdapter = RecentActivitiesAdapter(recentActivitiesList)
//        recentActivitiesRecyclerView.adapter = recentActivitiesAdapter
////        recentActivitiesRecyclerView.setHasFixedSize(true)
//
//        // User Details RecyclerView
//        userDetailsRecyclerView.layoutManager = LinearLayoutManager(context)
//        userDetailsAdapter = UserDetailsAdapter(userDetailsList)
//        userDetailsRecyclerView.adapter = userDetailsAdapter
//    }
//
//    /**
//     *  Setup Pie Chart with basic configurations
//     */
//
////    private fun setupPieChart() {
////        pieChart.description.isEnabled = false
////        pieChart.isRotationEnabled = true
////        pieChart.setUsePercentValues(true)
////        pieChart.setEntryLabelColor(android.graphics.Color.BLACK)
////        pieChart.centerText = "Task Distribution"
////        pieChart.setCenterTextSize(18f)
////        pieChart.legend.isEnabled = true
////    }
//
//    private fun setupPieChart() {
//        pieChart.apply{
//            description.isEnabled = false
//            isRotationEnabled = true
////            setUsePercentValues(true)
//            setEntryLabelColor(android.graphics.Color.BLACK)
//            centerText = "Task Distribution"
//            setCenterTextSize(18f)
//            legend.isEnabled = true
//            legend.textColor = Color.BLACK
//            legend.textSize = 12f
//            setUsePercentValues(false)
//            setDrawHoleEnabled(true)
//            setHoleColor(Color.WHITE)
//            setTransparentCircleRadius(61f)
//        }
//    }
//
//    /**
//     * CRITICAL: Main Data Loading Method
//     *
//     * This is the entry point for all data fetching. We call multiple
//     * Firebase queries here to populate different sections of the UI.
//     *
//     * Order matters:
//     * 1. Fetch total activities count (for the stats card)
//     * 2. Fetch active users count
//     * 3. Fetch activity type distribution (for pie chart)
//     * 4. Fetch recent activities (for the list)
//     * 5. Fetch user details (for the user cards)
//     */
//
//    private fun loadAnalyticsData() {
//        fetchTotalActivitiesCount()
//        fetchActiveUsersCount()
//        ffetchActivityTypeDistribution()
//        fetchRecentActivities()
//        fetchUserDetails()
//    }
//
//    // 1. Fetch total activities count
//    private fun fetchTotalActivitiesCount() {
//        db.collection("productionActivity")
//            .get()
//            .addOnSuccessListener { snapshot ->
//                val totalActivities = snapshot.size()
//                totalTasksTextView.text = totalActivities.toString()
//
//                calculateGrowthPercentage(totalActivities)
//            }
//            .addOnFailureListener { e ->
//                Log.e("Firestore", "Error fetching total activities: ${e.message}")
//                totalActivitiesText.text = "0"
//            }
//    }
//
////    private fun loadMoreUsers() {
////        // Load user details with pagination
////        loadMoreUsers()
////    }
//
//
//}