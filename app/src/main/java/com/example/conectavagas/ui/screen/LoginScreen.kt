package com.example.conectavagas.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conectavagas.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginScreen {

    @Composable
    fun LoginScreenContent(onLoginClick: () -> Unit, onRegisterClick: () -> Unit, onForgotPasswordClick: () -> Unit) {
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val auth =  remember { FirebaseAuth.getInstance() }
        val uriHandler = LocalUriHandler.current
        val privacyPolicyUrl = "https://victorurich.github.io/App_ConectaVagas/privacidade/"

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
                Image(
                    painter = painterResource(id = R.drawable.logo), // substitua "logo" pelo nome da sua imagem
                    contentDescription = "Logo ConectaVagas",
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .fillMaxWidth(0.5f) // Ajuste o tamanho conforme necessário
                )

                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    label = { Text(text = "Email") }
                )

                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    label = { Text(text = "Senha") },
                    visualTransformation = PasswordVisualTransformation()
                )

                Button(onClick = {
                    auth.signInWithEmailAndPassword(email.value, password.value).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            onLoginClick()
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Erro: ${task.exception?.message}")
                            }
                        }
                    }}, modifier = Modifier.padding(16.dp)) {
                    Text(text = "Login")
                }

                Text(
                    text = "Não tem uma conta? Registre-se.",
                    modifier = Modifier.padding(top = 16.dp).clickable { onRegisterClick() },
                    style = TextStyle(color = Color.Blue)
                )

                Text(
                    text = "Esqueceu a senha?", modifier = Modifier.padding(16.dp).clickable {onForgotPasswordClick()},
                    style = TextStyle(color = Color.Blue)
                )
                Text(
                    text = "Política de Privacidade",
                    modifier = Modifier.padding(top = 16.dp).clickable { uriHandler.openUri(privacyPolicyUrl) },
                    style = TextStyle(color = Color.Blue, fontSize = 14.sp)
                )
            }
        }
    }

    fun validateLogin(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() && email.contains("@") && !email.contains(" ") && !password.contains(" ")
    }

    @Preview (showBackground = true)
    @Composable
    fun LoginScreenPreview() {
        LoginScreenContent(onLoginClick = {}, onRegisterClick = {}, onForgotPasswordClick = {})
    }
}