package ec.edu.uce.taller8.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uce.taller8.R
import ec.edu.uce.taller8.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    userId: Int,
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    viewModel: ItemDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(userId) {
        viewModel.loadItemData(userId)
    }

    val uiState by viewModel.uiState.collectAsState()
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(ItemDetailsDestination.titleRes)) },
                navigationIcon = { 
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back_button))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary, navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        }
    ) { innerPadding ->
        ItemDetailsBody(
            itemDetailsUiState = uiState,
            onEdit = { navigateToEditItem(uiState.itemDetails.id) },
            onDelete = { showDeleteConfirmation = true },
            modifier = Modifier.padding(innerPadding)
        )

        if (showDeleteConfirmation) {
            DeleteConfirmationDialog(
                onConfirm = {
                    coroutineScope.launch {
                        viewModel.deleteItem(userId)
                        navigateBack()
                    }
                },
                onDismiss = { showDeleteConfirmation = false }
            )
        }
    }
}

@Composable
private fun ItemDetailsBody(
    itemDetailsUiState: ItemDetailsUiState,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val item = itemDetailsUiState.itemDetails
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ItemDetailsRow(label = stringResource(R.string.item_name_label), value = item.name, isHeader = true)
                ItemDetailsRow(label = stringResource(R.string.quantity_label), value = item.quantity)
                ItemDetailsRow(label = stringResource(R.string.price_label), value = "$${item.price}", isPrice = true)
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = onEdit, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                Text(stringResource(R.string.edit))
            }
            Button(onClick = onDelete, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Text(stringResource(R.string.delete))
            }
        }
    }
}

@Composable
private fun ItemDetailsRow(label: String, value: String, modifier: Modifier = Modifier, isHeader: Boolean = false, isPrice: Boolean = false) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = value, 
            modifier = Modifier.weight(1f).padding(start = 8.dp), 
            style = if (isHeader) MaterialTheme.typography.headlineSmall else if (isPrice) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge, 
            fontWeight = if(isHeader || isPrice) FontWeight.Bold else FontWeight.Normal,
            color = if(isPrice) MaterialTheme.colorScheme.primary else Color.Unspecified
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.no)) } },
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(R.string.yes)) } }
    )
}