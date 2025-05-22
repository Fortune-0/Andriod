package com.example.efficilog.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.example.efficilog.model.Users

class FirestoreRepo {
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "FirestoreRepo"

    // Add users to Firestore
    fun addUser(userId: String, user: Users, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userEmail = user.email ?: throw IllegalArgumentException("User email cannot be null")
        Log.d(TAG, "Adding user with email: $userEmail")

        db.collection("users")
            .document(userEmail)
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "User added successfully with email: $userEmail")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error adding user: ${exception.message}")
                onFailure(exception)
            }
    }

    fun addUserWithId(userId: String, user: Users, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        Log.d(TAG, "Adding user with ID: $userId")

        db.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "User added successfully with ID: $userId")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error adding user: ${exception.message}")
                onFailure(exception)
            }
    }

    fun checkUserExists(email: String, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
        Log.d(TAG, "Checking if user exists: $email")

        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val exists = !querySnapshot.isEmpty
                Log.d(TAG, "User exists check for $email: $exists")
                onSuccess(exists) // Returns true if the user exists
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error checking if user exists: ${exception.message}")
                onFailure(exception)
            }
    }

    // Fetch user data from Firestore using email
    fun fetchUserProfile(email: String, onSuccess: (Users?) -> Unit, onFailure: (Exception) -> Unit) {
        Log.d(TAG, "Fetching user profile by email: $email")

        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val user = querySnapshot.documents[0].toObject(Users::class.java)
                    Log.d(TAG, "User found by email: ${user?.name}")
                    onSuccess(user)
                } else {
                    Log.d(TAG, "No user found with email: $email")
                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching user by email: ${exception.message}")
                onFailure(exception)
            }
    }

    fun getUserRole(email: String, onSuccess: (String?) -> Unit, onFailure: (Exception) -> Unit) {
        Log.d(TAG, "Getting user role for email: $email")

        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val user = querySnapshot.documents[0].toObject(Users::class.java)
                    Log.d(TAG, "User role retrieved: ${user?.role}")
                    onSuccess(user?.role)
                } else {
                    Log.d(TAG, "No user found with email: $email")
                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting user role: ${exception.message}")
                onFailure(exception)
            }
    }

    // Update user profile method (already exists in your code)
    fun updateUserProfile(userId: String, userMap: Map<String, Any?>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        Log.d(TAG, "Updating user profile: $userId with data: $userMap")

        // Filter out null values to avoid Firestore errors
        val filteredMap = userMap.filterValues { it != null } as Map<String, Any>

        db.collection("users")
            .document(userId)
            .update(filteredMap)
            .addOnSuccessListener {
                Log.d(TAG, "User profile updated successfully")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error updating user profile: ${exception.message}")
                onFailure(exception)
            }
    }

    // Fetch user by ID method (already exists in your code)
    fun fetchUserProfileById(userId: String, onSuccess: (Users?) -> Unit, onFailure: (Exception) -> Unit) {
        Log.d(TAG, "Fetching user profile by ID: $userId")

        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(Users::class.java)
                    Log.d(TAG, "User found by ID: ${user?.name}")
                    onSuccess(user)
                } else {
                    Log.d(TAG, "No user found with ID: $userId")
                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching user by ID: ${exception.message}")
                onFailure(exception)
            }
    }
}
//package com.example.efficilog.repository
//
//
//import com.google.firebase.firestore.FirebaseFirestore
//import com.example.efficilog.model.Users
//
//
//class FirestoreRepo {
//    private val db = FirebaseFirestore.getInstance()
//
//    // Add users to Firestore
//    fun addUser(userId: String, user: Users, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//        val userId = user.email ?: throw IllegalArgumentException("User email cannot be null")
//        db.collection("users")
//            .document(userId)
//            .set(user)
//            .addOnSuccessListener {
//                onSuccess()
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }
//
//    fun addUserWithId(userId: String, user: Users, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("users")
//            .document(userId)
//            .set(user)
//            .addOnSuccessListener {
//                onSuccess()
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }
//
//    // Check if user is already exist
////    fun isUserExist(email: String, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
////        db.collection("users")
////           .whereEqualTo("email", email)
////           .get()
////           .addOnSuccessListener { querySnapshot ->
////               onSuccess(querySnapshot.isEmpty)
////           }
////           .addOnFailureListener {
////               onFailure(it)
////           }
////    }
//
//    fun checkUserExists(email: String, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("users")
//            .whereEqualTo("email", email)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                onSuccess(!querySnapshot.isEmpty) // Returns true if the user exists
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }
//
//    // Fetch user data from Firestore
////    fun getUser(email: String, onSuccess: (Users?) -> Unit, onFailure: (Exception) -> Unit) {
////        db.collection("users")
////           .whereEqualTo("email", email)
////           .get()
////           .addOnSuccessListener { querySnapshot ->
////               if (querySnapshot.isEmpty) {
////                   onSuccess(null)
////               } else {
////                   val user = querySnapshot.documents[0].toObject(Users::class.java)
////                   onSuccess(user)
////               } else {
////                   onSuccess(null)
////               }
////           }
////           .addOnFailureListener {
////               onFailure(it)
////           }
////    }
//
//    // Fetch user data from Firestore
//    fun fetchUserProfile(email: String, onSuccess: (Users?) -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("users")
//            .whereEqualTo("email", email)
//            .get()
//        .addOnSuccessListener { querySnapshot ->
//            if (!querySnapshot.isEmpty()) {
//                val user = querySnapshot.documents[0].toObject(Users::class.java)
//                onSuccess(user)
//            } else {
//                onSuccess(null)
//            }
//        }
//        .addOnFailureListener {
//            onFailure(it)
//        }
//    }
//    fun getUserRole(email: String, onSuccess: (String?) -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("users")
//            .whereEqualTo("email", email)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                if (!querySnapshot.isEmpty()) {
//                    val user = querySnapshot.documents[0].toObject(Users::class.java)
//                    onSuccess(user?.role)
//                } else {
//                    onSuccess(null)
//                }
//            }
//            .addOnFailureListener {
//                onFailure(it)
//            }
//    }
//
//    fun updateUserProfile(userId: String, userMap: Map<String, Any?>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("users")
//            .document(userId)
//            .update(userMap)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { exception -> onFailure(exception) }
//    }
//
//    fun fetchUserProfileById(userId: String, onSuccess: (Users?) -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("users")
//            .document(userId)
//            .get()
//            .addOnSuccessListener { documentSnapshot ->
//                if (documentSnapshot.exists()) {
//                    val user = documentSnapshot.toObject(Users::class.java)
//                    onSuccess(user)
//                } else {
//                    onSuccess(null)
//                }
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }
//
//}
