package com.example.conectavagas.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview


class LoadingScreen {
    @Composable
    fun LoadingScreenContent (navController: NavController){
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        LaunchedEffect (Unit){
            val currentUser = auth.currentUser;
            if(currentUser != null){
                val uid = currentUser.uid;
                firestore.collection("usuarios").document(uid).get().addOnSuccessListener { document ->
                    if(document.exists()){
                        val tipo = document.getString("tipo");

                        when(tipo){
                            "admin" -> navController.navigate("AdminScreen"){
                                popUpTo("LoadingScreen") {inclusive = true}
                            }
                            "cliente" -> navController.navigate("ClientScreen"){
                                popUpTo("LoadingScreen") {inclusive = true}
                            }
                            else -> {
                                navController.navigate("LoginScreen") {
                                    popUpTo("LoadingScreen") {inclusive = true}
                                }
                            }

                        }
                    } else {
                        navController.navigate("LoginScreen"){
                            popUpTo("LoadingScreen") {inclusive = true}
                        }

                    }

                    } .addOnFailureListener {
                        navController.navigate("LoginScreen"){
                            popUpTo("LoadingScreen") {inclusive = true}
                        }
                }

                    } else {
                        navController.navigate("LoginScreen"){
                            popUpTo("LoadingScreen") {inclusive = true}
                        }
            }
            }

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Carregando...")
            }
        }

    }

    @Preview (showBackground = true)
    @Composable
    fun LoadingScreenPreview(){
        LoadingScreenContent(navController = NavController(LocalContext.current))

    }
}