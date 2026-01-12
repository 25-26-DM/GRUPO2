package ec.edu.uce.taller8.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uce.taller8.data.Item
import ec.edu.uce.taller8.data.ItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ItemDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemDetailsUiState())
    val uiState: StateFlow<ItemDetailsUiState> = _uiState.asStateFlow()

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.ITEM_ID_ARG])

    fun loadItemData(userId: Int) {
        viewModelScope.launch {
            itemsRepository.getItemStream(itemId, userId)
                .filterNotNull()
                .map { item -> ItemDetailsUiState(itemDetails = item.toItemDetails()) }
                .collect { newState -> _uiState.value = newState }
        }
    }

    fun deleteItem(userId: Int) {
        viewModelScope.launch {
            itemsRepository.deleteItem(uiState.value.itemDetails.toItem(userId))
        }
    }
}

data class ItemDetailsUiState(
    val itemDetails: ItemDetails = ItemDetails()
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = ""
)

fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString()
)

fun ItemDetails.toItem(userId: Int): Item = Item(
    id = id,
    userId = userId,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0
)