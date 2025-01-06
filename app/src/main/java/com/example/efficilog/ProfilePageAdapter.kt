package com.example.efficilog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfilePageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
//            0 -> InfoFragment()
            1 -> MachineFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
