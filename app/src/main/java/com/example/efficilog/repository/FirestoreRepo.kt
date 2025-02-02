package com.example.efficilog.repository


import com.google.firebase.firestore.FirebaseFirestore
import com.example.efficilog.model.Users


class FirestoreRepo {
    private val db = FirebaseFirestore.getInstance()

    // Add users to Firestore
    fun addUser(userId: String, user: Users, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userId = user.email ?: throw IllegalArgumentException("User email cannot be null")
        db.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Check if user is already exist
//    fun isUserExist(email: String, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("users")
//           .whereEqualTo("email", email)
//           .get()
//           .addOnSuccessListener { querySnapshot ->
//               onSuccess(querySnapshot.isEmpty)
//           }
//           .addOnFailureListener {
//               onFailure(it)
//           }
//    }

    fun checkUserExists(email: String, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                onSuccess(!querySnapshot.isEmpty) // Returns true if the user exists
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Fetch user data from Firestore
//    fun getUser(email: String, onSuccess: (Users?) -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("users")
//           .whereEqualTo("email", email)
//           .get()
//           .addOnSuccessListener { querySnapshot ->
//               if (querySnapshot.isEmpty) {
//                   onSuccess(null)
//               } else {
//                   val user = querySnapshot.documents[0].toObject(Users::class.java)
//                   onSuccess(user)
//               } else {
//                   onSuccess(null)
//               }
//           }
//           .addOnFailureListener {
//               onFailure(it)
//           }
//    }

    // Fetch user data from Firestore
    fun fetchUserProfile(email: String, onSuccess: (Users?) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty()) {
                val user = querySnapshot.documents[0].toObject(Users::class.java)
                onSuccess(user)
            } else {
                onSuccess(null)
            }
        }
        .addOnFailureListener {
            onFailure(it)
        }
    }
}
