package com.example.conectavagas.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

class DetalhesVagaScreen {

    @Composable
    fun DetalhesVagaScreenContent(navController: NavController, vaga: Vaga){
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp), verticalArrangement = Arrangement.Center,) {

        Text(text = "Detalhes da Vaga", fontSize = 24.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(24.dp))


        Text(text = "Título: ${vaga.titulo}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Empresa: ${vaga.empresa}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Local: ${vaga.local}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Horário: ${vaga.horario}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Benefícios: ${vaga.beneficios}", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Atividades", style = MaterialTheme.typography.titleMedium)
        Text(text = vaga.atividades)

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Requisitos", style = MaterialTheme.typography.titleMedium)
        Text(text = vaga.requisitos)

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Contato", style = MaterialTheme.typography.titleMedium)
        Text(text = vaga.contato)

        Spacer(modifier = Modifier.height(24.dp))

        //Button(onClick = { navController.popBackStack() }, modifier = Modifier.align(CenterHorizontally)) {
         //   Icon(Icons.Default.ArrowBack, contentDescription = null)
         //   Spacer(Modifier.width(8.dp))
         //   Text("Voltar")
      //  }


    }
    }
}