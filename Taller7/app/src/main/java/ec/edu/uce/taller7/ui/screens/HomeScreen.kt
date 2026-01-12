package ec.edu.uce.taller7.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ec.edu.uce.taller7.ui.MarsUiState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun HomeScreen(marsUiState: MarsUiState, modifier: Modifier = Modifier) {
    when (marsUiState) {
        is MarsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is MarsUiState.Success -> {
            val json = Json { prettyPrint = true }
            val jsonString = json.encodeToString(marsUiState.photos)
            ResultScreen(
                photos = jsonString, modifier = modifier.fillMaxSize()
            )
        }
        is MarsUiState.Error -> ErrorScreen( modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text("Loading...")
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text("Error")
    }
}

@Composable
fun ResultScreen(photos: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            item {
                Text(text = photos)
            }
        }
    }
}