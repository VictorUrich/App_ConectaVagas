package com.example.conectavagas.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton


@Composable
fun VagaCard(
    titulo: String,
    empresa: String,
    local: String,
    onCardClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isAdmin: Boolean // <-- Novo parâmetro
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onCardClick() }, // Clicar no card
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = titulo, style = MaterialTheme.typography.titleMedium)
            Text(text = empresa, style = MaterialTheme.typography.bodyMedium)
            Text(text = local, style = MaterialTheme.typography.bodySmall)

            if (isAdmin) { // <-- Mostra só se for admin
                Row {
                    IconButton(onClick = { onEditClick() }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar"
                        )
                    }
                    IconButton(onClick = { onDeleteClick() }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Excluir"
                        )
                    }
                }
            }
        }
    }
}

