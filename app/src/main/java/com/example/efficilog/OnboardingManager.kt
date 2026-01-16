package com.example.efficilog

import android.content.Context
import android.content.SharedPreferences

/**
 * Utility class to manage onboarding state and user preferences
 */
class OnboardingManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("EfficiLogPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "ONBOARDING_COMPLETED"
        private const val KEY_USER_ROLE = "USER_ROLE"
        private const val KEY_ONBOARDING_COMPLETION_TIME = "ONBOARDING_COMPLETION_TIME"

        // Role constants
        const val ROLE_ADMINISTRATOR = "Administrator"
        const val ROLE_STAFF = "Staff"
    }

    /**
     * Check if user has completed onboarding
     */
    fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    /**
     * Get the user's selected role
     */
    fun getUserRole(): String? {
        return sharedPreferences.getString(KEY_USER_ROLE, null)
    }

    /**
     * Skip onboarding and mark as completed without a role
     */
    fun skipOnboarding() {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_ONBOARDING_COMPLETED, true)
            apply()
        }
    }

    /**
     * Save onboarding completion with user role
     */
    fun completeOnboarding(userRole: String) {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_ONBOARDING_COMPLETED, true)
            putString(KEY_USER_ROLE, userRole)
            putLong(KEY_ONBOARDING_COMPLETION_TIME, System.currentTimeMillis())
            apply()
        }
    }

    /**
     * Reset onboarding (useful for testing or allowing users to re-onboard)
     */
    fun resetOnboarding() {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_ONBOARDING_COMPLETED, false)
            remove(KEY_USER_ROLE)
            remove(KEY_ONBOARDING_COMPLETION_TIME)
            apply()
        }
    }

    fun saveSelectedRole(role: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_USER_ROLE, role)
            apply()
        }
    }

    /**
     * Check if user is an administrator
     */
    fun isUserAdmin(): Boolean {
        return getUserRole() == ROLE_ADMINISTRATOR
    }

    /**
     * Check if user is staff
     */
    fun isUserStaff(): Boolean {
        return getUserRole() == ROLE_STAFF
    }

    /**
     * Get when onboarding was completed
     */
    fun getOnboardingCompletionTime(): Long {
        return sharedPreferences.getLong(KEY_ONBOARDING_COMPLETION_TIME, 0L)
    }
}