package ec.edu.uce.taller8.di

import android.content.Context
import ec.edu.uce.taller8.data.InventoryDatabase
import ec.edu.uce.taller8.data.ItemsRepository
import ec.edu.uce.taller8.data.OfflineItemsRepository
import ec.edu.uce.taller8.data.OfflineUsersRepository
import ec.edu.uce.taller8.data.UsersRepository

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }

    /**
     * Implementation for [UsersRepository]
     */
    override val usersRepository: UsersRepository by lazy {
        OfflineUsersRepository(InventoryDatabase.getDatabase(context).userDao())
    }
}