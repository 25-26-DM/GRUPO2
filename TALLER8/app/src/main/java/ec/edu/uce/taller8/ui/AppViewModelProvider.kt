package ec.edu.uce.taller8.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ec.edu.uce.taller8.InventoryApplication
import ec.edu.uce.taller8.ui.home.HomeViewModel
import ec.edu.uce.taller8.ui.item.ItemDetailsViewModel
import ec.edu.uce.taller8.ui.item.ItemEntryViewModel
import ec.edu.uce.taller8.ui.login.LoginViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(inventoryApplication().container.itemsRepository)
        }

        // Initializer for ItemEntryViewModel
        initializer {
            ItemEntryViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.itemsRepository
            )
        }

        // Initializer for ItemDetailsViewModel
        initializer {
            ItemDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.itemsRepository
            )
        }

        // Initializer for LoginViewModel
        initializer {
            LoginViewModel(inventoryApplication().container.usersRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.inventoryApplication(): InventoryApplication = 
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)