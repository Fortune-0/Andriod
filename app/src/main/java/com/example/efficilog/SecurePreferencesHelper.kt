package com.example.efficilog

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom

class SecurePrefrencesHelper(context: Context) {

    companion object{
        private const val PREFS_FILE_NAME = "secure_user_preferences"
        private const val MASTER_KEY_ALIAS = "master_key"
        private const val KEY_PASSWORD = "saved_password"
//        private const val KEY_USERNAME = "saved_username"
        private const val KEY_EMAIL = "saved_email"
        private const val KEY_USER_TYPE = "user_type"
        private const val KEY_REMEMBER_ME = "remember_me"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val securePrefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // save user credentials if "remember me" is checked
    fun saveUserCredentials(email: String, password: String, rememberMe: Boolean) {
        val editor = securePrefs.edit()
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_PASSWORD, password)
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe)
        editor.apply()
    }
    fun saveCredentials(email: String, password: String, isAdmin: Boolean, rememberMe: Boolean) {
        securePrefs.edit().apply {
            putBoolean(KEY_REMEMBER_ME, rememberMe)

            if (rememberMe) {
                putString(KEY_EMAIL, email)
                putString(KEY_PASSWORD, password)
                putBoolean(KEY_USER_TYPE, isAdmin)
            } else {
                // Clear credentials if remember me is disabled
                remove(KEY_EMAIL)
                remove(KEY_PASSWORD)
                remove(KEY_USER_TYPE)
            }
        }.apply()
    }

    /**
     * Check if remember me is enabled
     */
    fun isRememberMeEnabled(): Boolean {
        return securePrefs.getBoolean(KEY_REMEMBER_ME, false)
    }

    /**
     * Get saved email address
     */
    fun getSavedEmail(): String? {
        return securePrefs.getString(KEY_EMAIL, null)
    }

    /**
     * Get saved password
     */
    fun getSavedPassword(): String? {
        return securePrefs.getString(KEY_PASSWORD, null)
    }

    /**
     * Get saved user type (admin or staff)
     */
    fun isAdminUser(): Boolean {
        return securePrefs.getBoolean(KEY_USER_TYPE, false)
    }

    /**
     * Clear all saved credentials
     */
    fun clearCredentials() {
        securePrefs.edit().apply {
            remove(KEY_EMAIL)
            remove(KEY_PASSWORD)
            remove(KEY_USER_TYPE)
            putBoolean(KEY_REMEMBER_ME, false)
        }.apply()
    }
}