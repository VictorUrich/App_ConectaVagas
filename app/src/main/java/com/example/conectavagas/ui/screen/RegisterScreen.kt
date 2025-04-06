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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.conectavagas.FirebaseAuthManager
import kotlinx.coroutines.launch

class RegisterScreen {
    private val authManager = FirebaseAuthManager()

    @Composable
    fun RegisterScreenContent(onBackToLogin: () -> Unit, onRegisterClick: () -> Unit) {
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val confirmPassword = remember { mutableStateOf("") }
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text (
                    text = "Registre-se",
                    modifier = Modifier.padding(bottom = 24.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = email.value,
                    onValueChange = {email.value = it},
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    label = { Text(text = "Email") }
                )
                TextField (
                    value = password.value,
                    onValueChange = {password.value = it},
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    label = {Text(text = "Senha")},
                    visualTransformation = PasswordVisualTransformation()
                )
                TextField(
                    value = confirmPassword.value,
                    onValueChange = {confirmPassword.value = it},
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    label = {Text(text = "Confirme a senha")},
                    visualTransformation = PasswordVisualTransformation()
                )
                Button (
                    onClick = {
                        if (password.value == confirmPassword.value && email.value.isNotEmpty() && password.value.isNotEmpty()) {
                            authManager.registerUser(email.value, password.value) {sucess, message ->
                                if(sucess) {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Cadastro realizado com sucesso!")
                                    }
                                    onRegisterClick()
                                } else {
                                    coroutineScope.launch{
                                        snackbarHostState.showSnackbar("Erro: $message")
                                    }
                                }
                            }

                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Erro: As senhas não coincidem ou email inválido.")
                            }
                        }
                    }, modifier = Modifier.padding(16.dp)){
                    Text(text = "Registrar")
                }
                Text(
                    text = "Já tem uma conta? Faça login.",
                    modifier = Modifier.padding(top = 16.dp).clickable { onBackToLogin() },
                    style = TextStyle(color = Color.Blue)
                )

            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun RegisterScreenPreview() {
        RegisterScreenContent(onBackToLogin = {}, onRegisterClick = {})
    }

}


