package com.example.conectavagas

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.conectavagas.ui.screen.AdminScreen
import com.example.conectavagas.ui.screen.ClientScreenContent
import com.example.conectavagas.ui.screen.DetalhesVagaScreen
import com.example.conectavagas.ui.screen.ForgotPasswordScreen
import com.example.conectavagas.ui.screen.LoadingScreen
import com.example.conectavagas.ui.screen.LoginScreen
import com.example.conectavagas.ui.screen.RegisterScreen
import com.example.conectavagas.ui.screen.Vaga
import com.example.conectavagas.ui.theme.ConectaVagasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConectaVagasTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "LoginScreen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("LoginScreen") {
                            LoginScreen().LoginScreenContent(
                                onRegisterClick = { navController.navigate("RegisterScreen") },
                                onForgotPasswordClick = { navController.navigate("ForgotPasswordScreen") },
                                onLoginClick = { navController.navigate("LoadingScreen") }
                            )
                        }
                        composable("RegisterScreen") {
                            RegisterScreen().RegisterScreenContent(
                                onBackToLogin = { navController.popBackStack() },
                                onRegisterClick = {navController.popBackStack()}
                            )
                        }

                        composable("ForgotPasswordScreen") {
                            ForgotPasswordScreen().ForgotPasswordScreenContent(
                                onBackToLogin = { navController.popBackStack() }
                            )
                        }
                        composable("LoadingScreen") {
                            LoadingScreen().LoadingScreenContent(navController)
                        }

                        composable("AdminScreen") {
                            AdminScreen().adminScreenContent(navController)
                        }

                        composable("ClientScreen") {
                            ClientScreenContent(navController)
                        }

                        composable(route = "detalhes/{titulo}/{empresa}/{local}/{beneficios}/{horario}/{atividades}/{requisitos}/{contato}") { backStackEntry ->
                            val titulo = Uri.decode(backStackEntry.arguments?.getString("titulo") ?: "")
                            val empresa = Uri.decode(backStackEntry.arguments?.getString("empresa") ?: "")
                            val local = Uri.decode(backStackEntry.arguments?.getString("local") ?: "")
                            val beneficios = Uri.decode(backStackEntry.arguments?.getString("beneficios") ?: "")
                            val horario = Uri.decode(backStackEntry.arguments?.getString("horario") ?: "")
                            val atividades = Uri.decode(backStackEntry.arguments?.getString("atividades") ?: "")
                            val requisitos = Uri.decode(backStackEntry.arguments?.getString("requisitos") ?: "")
                            val contato = Uri.decode(backStackEntry.arguments?.getString("contato") ?: "")

                            val vaga = Vaga(
                                titulo = titulo,
                                empresa = empresa,
                                local = local,
                                beneficios = beneficios,
                                horario = horario,
                                atividades = atividades,
                                requisitos = requisitos,
                                contato = contato
                            )

                            DetalhesVagaScreen().DetalhesVagaScreenContent(navController = navController, vaga = vaga)
                        }


                    }
                }
            }
        }
    }
}
