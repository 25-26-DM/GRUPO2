package ec.edu.uce.taller8.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uce.taller8.R
import ec.edu.uce.taller8.data.Item
import ec.edu.uce.taller8.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userId: Int,
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory) // Use the global factory
) {
    // Load user-specific data when the screen is shown for this user
    LaunchedEffect(userId) {
        viewModel.loadHomeData(userId)
    }

    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.inventory_title)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_item), tint = MaterialTheme.colorScheme.onSecondary)
            }
        },
        bottomBar = {
            InventorySummary(
                totalItems = homeUiState.totalItems, 
                totalValue = homeUiState.totalValue
            )
        },
        modifier = modifier
    ) { innerPadding ->
        HomeBody(
            itemList = homeUiState.itemList,
            onItemClick = navigateToItemUpdate,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun HomeBody(
    itemList: List<Item>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (itemList.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.no_items_in_inventory),
                style = MaterialTheme.typography.titleLarge
            )
        }
    } else {
        InventoryList(
            itemList = itemList,
            onItemClick = onItemClick,
            modifier = modifier
        )
    }
}

@Composable
private fun InventoryList(
    itemList: List<Item>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)) {
        items(items = itemList, key = { it.id }) { item ->
            InventoryItem(
                item = item, 
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { onItemClick(item.id) }
            )
        }
    }
}

@Composable
private fun InventorySummary(totalItems: Int, totalValue: Double, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(), 
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.total_items), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(totalItems.toString(), style = MaterialTheme.typography.headlineSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.total_value), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(String.format("$%.2f", totalValue), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun InventoryItem(
    item: Item, modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name, style = MaterialTheme.typography.titleLarge)
                Text(text = "${stringResource(R.string.quantity_short)} ${item.quantity}", style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = String.format("$%.2f", item.price), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}