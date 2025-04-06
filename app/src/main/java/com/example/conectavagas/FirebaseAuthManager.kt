package com.example.conectavagas

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseAuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, null)
            } else {
                onResult(false, task.exception?.message)
            }
        }
    }

    fun registerUser(email: String, password: String, tipo: String = "cliente", onResult: (Boolean, String?) -> Unit){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val uid = auth.currentUser?.uid
                val userData = hashMapOf(
                    "email" to email,
                    "tipo" to tipo
                )

                if(uid != null){
                    firestore.collection("usuarios").document(uid).set(userData).addOnSuccessListener {
                        onResult(true, null)
                    }
                        .addOnFailureListener{ e ->
                            onResult(false, "Erro ao salvar os dados do usuário: ${e.message}")
                        }
                } else {
                    onResult(false, "Erro ao obter o ID do usuário")
                }
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