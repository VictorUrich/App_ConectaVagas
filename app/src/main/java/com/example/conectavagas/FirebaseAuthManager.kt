package com.example.conectavagas

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, null)
            } else {
                onResult(false, task.exception?.message)
            }
        }
    }

    fun registerUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                onResult(true, null)
            } else {
                onResult(false, task.exception?.message)
            }

        }
    }

    fun logoutUser() {
        auth.signOut()
    }

    fun getCurrentUser(): String?{
        return auth.currentUser?.email
    }





}