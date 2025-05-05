package com.example.conectavagas.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun EditScreen(
    navController: NavController,
    vagaId: String? = null,
    initialTitulo: String? = "",
    initialEmpresa: String? = "",
    initialBeneficios: String? = "",
    initialHorario: String? = "",
    initialLocal: String? = "",
    initialAtividades: String? = "",
    initialRequisitos: String? = "",
    initialContato: String? = ""
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()

    val titulo = remember { mutableStateOf(initialTitulo ?: "") }
    val empresa = remember { mutableStateOf(initialEmpresa ?: "") }
    val beneficios = remember { mutableStateOf(initialBeneficios ?: "") }
    val horario = remember { mutableStateOf(initialHorario ?: "") }
    val local = remember { mutableStateOf(initialLocal ?: "") }
    val atividades = remember { mutableStateOf(initialAtividades ?: "") }
    val requisitos = remember { mutableStateOf(initialRequisitos ?: "") }
    val contato = remember { mutableStateOf(initialContato ?: "") }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Editar Vaga",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                        vagaId?.let { id ->
                            val vagaAtualizada = hashMapOf(
                                "titulo" to titulo.value,
                                "empresa" to empresa.value,
                                "beneficios" to beneficios.value,
                                "horario" to horario.value,
                                "local" to local.value,
                                "atividades" to atividades.value,
                                "requisitos" to requisitos.value,
                                "contato" to contato.value
                            )

                            db.collection("vagas").document(id)
                                .update(vagaAtualizada as Map<String, Any>)
                                .addOnSuccessListener {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Vaga atualizada com sucesso!")
                                    }
                                }
                                .addOnFailureListener {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Erro ao atualizar vaga: ${it.message}")
                                    }
                                }
                        } ?: run {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("ID da vaga não encontrado.")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Alterações")
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar / Voltar")
            }

            Spacer(modifier = Modifier.height(6.dp))


        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditVagaScreenPreview() {
    EditScreen(
        navController = rememberNavController()
    )
}
