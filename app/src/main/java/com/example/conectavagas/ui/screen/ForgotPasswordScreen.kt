package com.example.conectavagas.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ForgotPasswordScreen {
    @Composable
    fun ForgotPasswordScreenContent(onBackToLogin: () -> Unit) {
        val email = remember { mutableStateOf("") }
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Recuperar Senha",
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    label = { Text(text = "Email") }
                )
                Button(
                    onClick = {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email.value)
                            .addOnCompleteListener { task ->
                                coroutineScope.launch {
                                    if (task.isSuccessful) {
                                        snackbarHostState.showSnackbar("Email de recuperação enviado com sucesso!")
                                    } else {
                                        snackbarHostState.showSnackbar("Erro: ${task.exception?.message}")
                                    }
                                }
                            }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Enviar Email de Recuperação")
                }

                Text(
                    text = "Voltar ao Login",
                    color = Color.Blue,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onBackToLogin() }
                )
            }
        }
    }

    @Preview (showBackground = true)
    @Composable
    fun ForgotPasswordScreenPreview() {
        ForgotPasswordScreenContent(onBackToLogin = {})
    }

    }
