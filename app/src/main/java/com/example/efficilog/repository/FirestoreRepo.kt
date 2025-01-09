package com.example.efficilog
import android.R
import com.google.firebase.firestore.FirebaseFirestore
import com.example.efficilog.model.Users
import com.google.rpc.QuotaFailure


class FirestoreRepo {
    private val db = FirebaseFirestore.getInstance()

    // Add users to Firestore
    fun addUser(user: Users, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
           .add(user)
           .addOnSuccessListener {
               onSuccess()
           }
            .addOnFailureListener { exception ->
                onFailure(exception)
           }
    }

    // Check if user is already exist
    fun isUserExist(email: String, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
           .whereEqualTo("email", email)
           .get()
           .addOnSuccessListener { querySnapshot ->
               onSuccess(querySnapshot.isEmpty)
           }
           .addOnFailureListener {
               onFailure(it)
           }
    }

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
}