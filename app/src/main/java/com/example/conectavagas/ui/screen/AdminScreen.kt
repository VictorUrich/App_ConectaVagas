package com.example.conectavagas.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AdminScreen {

    @Composable
    fun adminScreenContent(navController: NavController) {
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val db = FirebaseFirestore.getInstance()

        val titulo = remember { mutableStateOf("") }
        val empresa = remember { mutableStateOf("") }
        val beneficios = remember { mutableStateOf("") }
        val horario = remember { mutableStateOf("") }
        val local = remember { mutableStateOf("") }
        val atividades = remember { mutableStateOf("") }
        val requisitos = remember { mutableStateOf("") }
        val contato = remember { mutableStateOf("") }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Cadastrar Vaga", style = MaterialTheme.typography.headlineSmall)

                TextField(
                    value = titulo.value,
                    onValueChange = { titulo.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Título") }
                )

                TextField(
                    value = empresa.value,
                    onValueChange = { empresa.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Empresa") }
                )

                TextField(
                    value = beneficios.value,
                    onValueChange = { beneficios.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Benefícios") }
                )

                TextField(
                    value = horario.value,
                    onValueChange = { horario.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Horário") }
                )

                TextField(
                    value = local.value,
                    onValueChange = { local.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Local") }
                )

                TextField(
                    value = atividades.value,
                    onValueChange = { atividades.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Atividades") }
                )

                TextField(
                    value = requisitos.value,
                    onValueChange = { requisitos.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Requisitos") }
                )

                TextField(
                    value = contato.value,
                    onValueChange = { contato.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contato") }
                )

                Button(
                    onClick = {
                        if (titulo.value.isBlank() || empresa.value.isBlank() || beneficios.value.isBlank() ||
                            horario.value.isBlank() || local.value.isBlank() || atividades.value.isBlank() ||
                            requisitos.value.isBlank() || contato.value.isBlank()
                        ) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Preencha todos os campos antes de salvar")
                            }
                        } else {
                            val vaga = hashMapOf(
                                "Título" to titulo.value,
                                "Empresa" to empresa.value,
                                "Benefícios" to beneficios.value,
                                "Horário" to horario.value,
                                "Local" to local.value,
                                "Atividades" to atividades.value,
                                "Requisitos" to requisitos.value,
                                "Contato" to contato.value
                            )

                            db.collection("vagas").add(vaga)
                                .addOnSuccessListener {
                                    titulo.value = ""
                                    empresa.value = ""
                                    beneficios.value = ""
                                    horario.value = ""
                                    local.value = ""
                                    atividades.value = ""
                                    requisitos.value = ""
                                    contato.value = ""

                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Vaga cadastrada com sucesso!")

                                    }
                                }
                                .addOnFailureListener {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Erro ao cadastrar vaga: ${it.message}")
                                    }
                                }
                        }
                    }
                ) {
                    Text("Salvar Vaga")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun adminScreenPreview() {
        adminScreenContent(navController = NavController(LocalContext.current))
    }
}
