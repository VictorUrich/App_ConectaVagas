package com.example.conectavagas.ui.screen

import android.net.Uri
import androidx.annotation.Keep
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.conectavagas.ui.components.VagaCard
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch






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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientScreenContent(navController: NavController) {
    val vagas = remember { mutableStateListOf<Vaga>() }
    var isAdmin by remember { mutableStateOf(false) }
    var vagaSelecionadaParaExclusao by remember { mutableStateOf<Vaga?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        userId?.let { id ->
            db.collection("usuarios").document(id).get()
                .addOnSuccessListener { document ->
                    val tipo = document.getString("tipo")
                    isAdmin = tipo == "admin"
                }
        }

        db.collection("vagas").get()
            .addOnSuccessListener { result ->
                vagas.clear()
                for (document in result) {
                    val vaga = document.toObject(Vaga::class.java).copy(id = document.id)
                    vagas.add(vaga)
                }
            }
    }

    // Host para exibir Snackbar somente quando chamado
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End){
               IconButton(onClick = {
                   FirebaseAuth.getInstance().signOut()
                   navController.navigate("LoginScreen"){
                       popUpTo(0){inclusive = true}
                   }
               }
               ) { Icon(
                   imageVector = Icons.Default.ExitToApp,
                   contentDescription = "Sair"
               )
               }

            }

            Text(
                text = "Vagas Disponíveis",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(vagas) {   vaga ->
                    VagaCard(
                        titulo = vaga.titulo,
                        empresa = vaga.empresa,
                        local = vaga.local,
                        onCardClick = {
                            navController.navigate(
                                "detalhes/${Uri.encode(vaga.titulo)}/${Uri.encode(vaga.empresa)}/${Uri.encode(vaga.local)}/" +
                                        "${Uri.encode(vaga.beneficios)}/${Uri.encode(vaga.horario)}/${Uri.encode(vaga.atividades)}/" +
                                        "${Uri.encode(vaga.requisitos)}/${Uri.encode(vaga.contato)}"
                            )
                        },
                        onEditClick = {
                            navController.navigate(
                                "edit/${vaga.id}/${Uri.encode(vaga.titulo)}/${Uri.encode(vaga.empresa)}/" +
                                        "${Uri.encode(vaga.local)}/${Uri.encode(vaga.beneficios)}/${Uri.encode(vaga.horario)}/" +
                                        "${Uri.encode(vaga.atividades)}/${Uri.encode(vaga.requisitos)}/${Uri.encode(vaga.contato)}"
                            )
                        },
                        onDeleteClick = {
                            vagaSelecionadaParaExclusao = vaga
                        },
                        isAdmin = isAdmin
                    )
                }
            }

            vagaSelecionadaParaExclusao?.let { vaga ->
                AlertDialog(
                    onDismissRequest = { vagaSelecionadaParaExclusao = null },
                    title = { Text("Confirmar exclusão") },
                    text = { Text("Deseja excluir a vaga \"${vaga.titulo}\"?") },
                    confirmButton = {
                        TextButton(onClick = {
                            deletarVaga(
                                vaga = vaga,
                                onSuccess = {
                                    vagas.remove(vaga)
                                    vagaSelecionadaParaExclusao = null
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Vaga excluída com sucesso!")
                                    }
                                },
                                onFailure = { exception ->
                                    vagaSelecionadaParaExclusao = null
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Erro ao excluir vaga: ${exception.message}")
                                    }
                                }
                            )
                        }) {
                            Text("Confirmar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            vagaSelecionadaParaExclusao = null
                        }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

        }

        if (isAdmin) {
          //  Button(
              //  onClick = {
              //      navController.popBackStack()
              //  },
               // modifier = Modifier
               //     .align(Alignment.BottomCenter)
               //     .padding(16.dp)
          //  ) {
             //   Text("Voltar")
           // }
        }

        if (!isAdmin) {
            BannerAdView(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 56.dp) // Deixa espaço para o snackbar não sobrepor
            )
        }

        // SnackbarHost visível no layout, mas usado apenas nas exclusões
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// Função auxiliar de exclusão
fun deletarVaga(
    vaga: Vaga,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    FirebaseFirestore.getInstance()
        .collection("vagas")
        .document(vaga.id)
        .delete()
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { exception -> onFailure(exception) }
}

@Composable
fun BannerAdView(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp), // Altura padrão do banner
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-4380355603416042/6743796304"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}









