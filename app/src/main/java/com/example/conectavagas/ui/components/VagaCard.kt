package com.example.conectavagas.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

    @Composable
    fun VagaCard(
        titulo: String,
        empresa: String,
        local: String,
        onClick: () -> Unit
    ){
        Card(
           modifier = Modifier.fillMaxWidth()
               .padding(vertical = 8.dp)
               .clickable{ onClick()},
                elevation = CardDefaults.cardElevation(4.dp)
        ){
            Column (modifier = Modifier.padding(16.dp)){
                Text(text = titulo, style = MaterialTheme.typography.titleMedium)
                Text(text = empresa, style = MaterialTheme.typography.bodyMedium)
                Text(text = local, style = MaterialTheme.typography.bodySmall)
            }

        }
    }
