package ec.edu.uce.taller8.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uce.taller8.data.Item
import ec.edu.uce.taller8.data.ItemsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ItemEntryViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: Int? = savedStateHandle[ItemEditDestination.ITEM_ID_ARG]

    fun loadItemForEdit(userId: Int) {
        if (itemId != null) {
            viewModelScope.launch {
                itemUiState = itemsRepository.getItemStream(itemId, userId)
                    .filterNotNull()
                    .first()
                    .toItemUiState(true)
            }
        }
    }

    fun updateUiState(newItemUiState: ItemUiState) {
        itemUiState = newItemUiState.copy(actionEnabled = newItemUiState.isValid())
    }

    suspend fun saveItem(userId: Int) {
        if (itemUiState.isValid()) {
            if (itemId == null) {
                itemsRepository.insertItem(itemUiState.toItem(userId))
            } else {
                itemsRepository.updateItem(itemUiState.toItem(userId))
            }
        }
    }
}

data class ItemUiState(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val actionEnabled: Boolean = false
)

fun ItemUiState.isValid(): Boolean {
    return name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
}

fun Item.toItemUiState(actionEnabled: Boolean = false): ItemUiState = ItemUiState(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString(),
    actionEnabled = actionEnabled
)

fun ItemUiState.toItem(userId: Int): Item = Item(
    id = id,
    userId = userId,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0
)
