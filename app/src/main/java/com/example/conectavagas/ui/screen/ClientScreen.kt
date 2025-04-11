package com.example.conectavagas.ui.screen

import android.net.Uri
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.example.conectavagas.ui.components.VagaCard
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment.Companion.CenterHorizontally


@Keep
data class Vaga(
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
        val vagas = remember { mutableStateListOf<Vaga>()}


        LaunchedEffect(Unit){
            val db = FirebaseFirestore.getInstance()
            db.collection("vagas").get().addOnSuccessListener { result ->
                vagas.clear()
                for (document in result){
                    val vaga = document.toObject(Vaga::class.java)
                    vagas.add(vaga)

                    println("Vaga carregada: $vaga")
                }
            }.addOnFailureListener{
                println("Erro ao carregar vagas: ${it.message}")
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Vagas Disponíveis",style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(vagas) { vaga ->
                    VagaCard(
                        titulo = vaga.titulo,
                        empresa = vaga.empresa,
                        local = vaga.local,
                        onClick = {
                            val encodedTitulo = Uri.encode(vaga.titulo)
                            val encodedEmpresa = Uri.encode(vaga.empresa)
                            val encodedLocal = Uri.encode(vaga.local)
                            val encodedBeneficios = Uri.encode(vaga.beneficios)
                            val encodedHorario = Uri.encode(vaga.horario)
                            val encodedAtividades = Uri.encode(vaga.atividades)
                            val encodedRequisitos = Uri.encode(vaga.requisitos)
                            val encodedContato = Uri.encode(vaga.contato)

                            navController.navigate("detalhes/$encodedTitulo/$encodedEmpresa/$encodedLocal/$encodedBeneficios/" +
                                    "$encodedHorario/$encodedAtividades/$encodedRequisitos/$encodedContato")
                        }

                    )

                }
            }

            Button(onClick = { navController.popBackStack() }, modifier = Modifier.align(
                CenterHorizontally
            )) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Voltar")
            }


        }
        }




