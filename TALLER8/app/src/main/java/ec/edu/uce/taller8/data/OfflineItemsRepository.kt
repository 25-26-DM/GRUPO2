package ec.edu.uce.taller8.data

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {
    override fun getAllItemsStream(userId: Int): Flow<List<Item>> = itemDao.getAllItems(userId)

    override fun getItemStream(id: Int, userId: Int): Flow<Item?> = itemDao.getItem(id, userId)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)
}