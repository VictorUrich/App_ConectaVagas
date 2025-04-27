package com.example.conectavagas.ui.screen

import android.net.Uri
import androidx.annotation.Keep
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.conectavagas.ui.components.VagaCard
import com.google.firebase.firestore.FirebaseFirestore

@Keep
data class Vaga(
    val id: String = "",
    val titulo: String = "",
    val empresa: String = "",
    val local: String = "",
    val beneficios: String = "",
    val horario: String = "",
    val atividades: String = "",
    val requisitos: String = "",
    val contato: String = ""
)

@Composable
fun ClientScreenContent(navController: NavController) {
    val vagas = remember { mutableStateListOf<Vaga>() }
    var isAdmin by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        // Primeiro: busca o tipo do usuário
        userId?.let { id ->
            db.collection("users").document(id).get()
                .addOnSuccessListener { document ->
                    val tipo = document.getString("tipo")
                    isAdmin = tipo == "admin"
                }
                .addOnFailureListener {
                    println("Erro ao buscar tipo do usuário: ${it.message}")
                    isAdmin = false
                }
        }

        // Depois: busca as vagas
        db.collection("vagas").get()
            .addOnSuccessListener { result ->
                vagas.clear()
                for (document in result) {
                    val vaga = document.toObject(Vaga::class.java)
                        .copy(id = document.id)
                    vagas.add(vaga)
                    println("Vaga carregada: $vaga")
                }
            }
            .addOnFailureListener {
                println("Erro ao carregar vagas: ${it.message}")
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Vagas Disponíveis",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(vagas) { vaga ->
                VagaCard(
                    titulo = vaga.titulo,
                    empresa = vaga.empresa,
                    local = vaga.local,
                    onCardClick = {
                        // seu código pra ver detalhes (ok)
                    },
                    onEditClick = {
                        navController.navigate(
                            "edit/${vaga.id}/${Uri.encode(vaga.titulo)}/${Uri.encode(vaga.empresa)}/" +
                                    "${Uri.encode(vaga.local)}/${Uri.encode(vaga.beneficios)}/${Uri.encode(vaga.horario)}/" +
                                    "${Uri.encode(vaga.atividades)}/${Uri.encode(vaga.requisitos)}/${Uri.encode(vaga.contato)}"
                        )
                    },
                    onDeleteClick = {
                        // depois implementar deletar
                    },
                    isAdmin = isAdmin // <-- Passa aqui se é admin!
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Voltar")
        }
    }
}
