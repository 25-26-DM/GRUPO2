package ec.edu.uce.taller8.di

import ec.edu.uce.taller8.data.ItemsRepository
import ec.edu.uce.taller8.data.UsersRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
    val usersRepository: UsersRepository
}