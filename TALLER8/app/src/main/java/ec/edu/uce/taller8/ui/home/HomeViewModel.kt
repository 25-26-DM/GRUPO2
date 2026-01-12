package ec.edu.uce.taller8.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uce.taller8.data.Item
import ec.edu.uce.taller8.data.ItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map

class HomeViewModel(
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    fun loadHomeData(userId: Int) {
        viewModelScope.launch {
            itemsRepository.getAllItemsStream(userId)
                .map { items -> 
                    HomeUiState(
                        itemList = items,
                        totalItems = items.sumOf { it.quantity },
                        totalValue = items.sumOf { it.price * it.quantity }
                    )
                }
                .collect { newState ->
                    _homeUiState.value = newState
                }
        }
    }
}

data class HomeUiState(
    val itemList: List<Item> = listOf(),
    val totalItems: Int = 0,
    val totalValue: Double = 0.0
)
