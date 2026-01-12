package ec.edu.uce.taller7.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalScreen(
    photoCount: Int,
    username: String,
    loginTime: String,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Total de Fotos")
                        Text("Usuario: $username - Inicio: $loginTime", style = MaterialTheme.typography.bodySmall)
                    }
                }
            )
        }
    ) { padding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(text = "Éxito: Se recuperaron $photoCount fotos de Marte")
        }
    }
}