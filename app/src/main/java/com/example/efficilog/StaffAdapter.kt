package com.example.efficilog

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
//import com.example.efficilog.model.User

class UserAdapter(
    private val context: Context,
    private val userList: List<User>,
    private val onUserClicked: (User) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_COLLAPSED = 0
        private const val VIEW_TYPE_EXPANDED = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_COLLAPSED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.staff_item, parent, false)
                CollapsedViewHolder(view)
            }
            VIEW_TYPE_EXPANDED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.staff_expanded_item, parent, false)
                ExpandedViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = userList[position]

        when (holder) {
            is CollapsedViewHolder -> holder.bind(user)
            is ExpandedViewHolder -> holder.bind(user)
        }
    }

    override fun getItemCount(): Int = userList.size

    override fun getItemViewType(position: Int): Int {
        return if (userList[position].isExpanded) {
            VIEW_TYPE_EXPANDED
        } else {
            VIEW_TYPE_COLLAPSED
        }
    }

    inner class CollapsedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName: TextView = itemView.findViewById(R.id.user_name)
        private val userRole: TextView = itemView.findViewById(R.id.user_role)
        private val userStatus: TextView = itemView.findViewById(R.id.user_status)
        private val expandArrow: ImageView = itemView.findViewById(R.id.expandArrow)
        private val container: ConstraintLayout = itemView as ConstraintLayout

        fun bind(user: User) {
            userName.text = user.name
            userRole.text = user.role
            userStatus.text = user.status

            // Set the status background color based on status value
            when (user.status.toLowerCase()) {
                "active" -> userStatus.setTextColor(context.getColor(android.R.color.holo_green_dark))
                "inactive" -> userStatus.setTextColor(context.getColor(android.R.color.holo_red_light))
                "on leave" -> userStatus.setTextColor(context.getColor(R.color.orange))
                else -> userStatus.setTextColor(context.getColor(android.R.color.darker_gray))
            }

            // Set click listener for the expandArrow
            expandArrow.setOnClickListener {
                toggleExpansion(user)
            }

            // Make the entire row clickable
            container.setOnClickListener {
                toggleExpansion(user)
            }
        }
    }

    inner class ExpandedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName: TextView = itemView.findViewById(R.id.expanded_user_name)
        private val userRole: TextView = itemView.findViewById(R.id.expanded_user_role)
//        private val userStatus: TextView = itemView.findViewById(R.id.user_detail_status)
        private val userLastActive: TextView = itemView.findViewById(R.id.user_detail_last_active)
//        private val userProductionRate: TextView = itemView.findViewById(R.id.user_detail_production_rate)
        private val btnViewFullProfile: Button = itemView.findViewById(R.id.btn_view_full_profile)
        private val btnProductionHistory: Button = itemView.findViewById(R.id.btn_production_history)
        private val expandedCard: CardView = itemView as CardView

        fun bind(user: User) {
            userName.text = user.name
            userRole.text = user.role
//            userStatus.text = user.status
            userLastActive.text = user.lastActive
//            userProductionRate.text = user.productionRate

            // Click listener for the full profile button
            btnViewFullProfile.setOnClickListener {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("staffId", user.id)
                intent.putExtra("staffName", user.name)
                intent.putExtra("staffRole", user.role)
                intent.putExtra("staffStatus", user.status)
                context.startActivity(intent)
            }

            // Click listener for the production history button
            btnProductionHistory.setOnClickListener {
                val intent = Intent(context, HistoryActivity::class.java)
                intent.putExtra("staffId", user.id)
                intent.putExtra("staffName", user.name)
                intent.putExtra("staffRole", user.role)
                intent.putExtra("staffStatus", user.status)
                context.startActivity(intent)
            }

            // Click listener for collapsing the expanded view
            expandedCard.setOnClickListener {
                toggleExpansion(user)
            }
        }
    }

    private fun toggleExpansion(user: User) {
        // Update the model
        user.isExpanded = !user.isExpanded

        // Find the position of the user in the list
        val position = userList.indexOf(user)

        // Notify adapter of the change to update the UI
        notifyItemChanged(position)
    }
}
//package com.example.efficilog
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.LinearLayout
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.core.content.ContextCompat
//import android.widget.Button
//import androidx.recyclerview.widget.RecyclerView
//
//
//class UserAdapter(
//    private val context: Context,
//    private val users: List<User>,
//    private val onUserClick: (User) -> Unit
//) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
//
//    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val userIcon: ImageView = itemView.findViewById(R.id.user_icon)
//        val userName: TextView = itemView.findViewById(R.id.user_name)
//        val userRole: TextView = itemView.findViewById(R.id.user_role)
//        val statusBadge: TextView = itemView.findViewById(R.id.user_status)
//        val expandArrow: ImageView = itemView.findViewById(R.id.expandArrow)
//
////        val expandedDetails: LinearLayout = itemView.findViewById(R.id.expanded_details)
////        val expandedStatus: TextView = itemView.findViewById(R.id.expanded_status)
////        val expandedLastActive: TextView = itemView.findViewById(R.id.expanded_last_active)
////        val expandedProductionRate: TextView = itemView.findViewById(R.id.expanded_production_rate)
////        val expandedContact: TextView = itemView.findViewById(R.id.expanded_contact)
////        val btnViewProfile: Button = itemView.findViewById(R.id.btn_view_profile)
////        val btnProductionHistory: Button = itemView.findViewById(R.id.btn_production_history)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.staff_item, parent, false)
//        return UserViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        val user = users[position]
//
//        holder.userName.text = user.name
//        holder.userRole.text = user.role
//
//        // Set status badge text and background
//        holder.statusBadge.text = user.status
//
////        holder.expandedStatus.text = user.status
////        holder.expandedLastActive.text = user.lastActive
////        holder.expandedProductionRate.text = user.productionRate
////        holder.expandedContact.text = user.contactInfo
//
//        when (user.status.lowercase()) {
//            "active" -> {
//                holder.statusBadge.setTextColor(ContextCompat.getColor(context, R.color.green))
//                holder.statusBadge.background = ContextCompat.getDrawable(context, R.drawable.status_background)
//            }
//            "inactive" -> {
//                holder.statusBadge.setTextColor(ContextCompat.getColor(context, R.color.red))
//                holder.statusBadge.background = ContextCompat.getDrawable(context, R.drawable.status_inactive_background)
//            }
//            "on leave" -> {
//                holder.statusBadge.setTextColor(ContextCompat.getColor(context, R.color.orange))
//                holder.statusBadge.background = ContextCompat.getDrawable(context, R.drawable.status_on_leave_background)
//            }
//        }
//
//        // Update arrow icon based on expanded state
//        holder.expandArrow.setImageResource(
//            if (user.isExpanded) R.drawable.ic_arrow_down else R.drawable.ic_arrow_right
//        )
//        // Set click listener for the expand/collapse arrow
//        holder.expandArrow.setOnClickListener {
//            user.isExpanded = !user.isExpanded
//            notifyItemChanged(position) // Notify the adapter to refresh the item
//        }
//
//        // Set click listener
//        holder.itemView.setOnClickListener {
//            onUserClick(user)
//        }
//    }
//
//    override fun getItemCount(): Int = users.size
//}
//
// User data class
data class User(
    val id: String,
    val name: String,
    val role: String,
    val status: String,
    val lastActive: String = "N/A",
    val productionRate: String,
    var isExpanded: Boolean = false,
    var contactInfo: String = "view details"
)