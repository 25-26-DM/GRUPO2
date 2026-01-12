package ec.edu.uce.taller8.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uce.taller8.R
import ec.edu.uce.taller8.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryScreen(
    userId: Int,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(userId) {
        viewModel.loadItemForEdit(userId)
    }

    val coroutineScope = rememberCoroutineScope()
    val isEditing = viewModel.itemUiState.id != 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(if (isEditing) R.string.edit_item_title else R.string.item_entry_title)) },
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(onClick = onNavigateUp) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary, navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        }
    ) { innerPadding ->
        ItemEntryBody(
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem(userId)
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ItemEntryBody(
    itemUiState: ItemUiState,
    onItemValueChange: (ItemUiState) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ItemInputForm(itemUiState = itemUiState, onValueChange = onItemValueChange, modifier = Modifier.fillMaxWidth())
        Button(
            onClick = onSaveClick,
            enabled = itemUiState.actionEnabled,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@Composable
fun ItemInputForm(
    itemUiState: ItemUiState,
    onValueChange: (ItemUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = itemUiState.name,
                onValueChange = { onValueChange(itemUiState.copy(name = it)) },
                label = { Text(stringResource(R.string.item_name_req)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = itemUiState.price,
                onValueChange = { onValueChange(itemUiState.copy(price = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text(stringResource(R.string.item_price_req)) },
                leadingIcon = { Text("$") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = itemUiState.quantity,
                onValueChange = { onValueChange(itemUiState.copy(quantity = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(R.string.quantity_req)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}