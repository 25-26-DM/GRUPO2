package ec.edu.uce.final_aetroyab.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE isDeleted = 0 ORDER BY description")
    suspend fun getAll(): List<Product>

    @Query("SELECT * FROM products WHERE code = :code LIMIT 1")
    suspend fun getByCode(code: String): Product?

    @Query("SELECT * FROM products WHERE syncStatus = 'pending' AND isDeleted = 0")
    suspend fun getPendingSync(): List<Product>

    @Query("SELECT * FROM products WHERE syncStatus = 'pending' AND isDeleted = 1")
    suspend fun getPendingDeletion(): List<Product>

    @Query("UPDATE products SET syncStatus = :status WHERE code = :code")
    suspend fun updateSyncStatus(code: String, status: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Query("UPDATE products SET isDeleted = 1, syncStatus = 'pending', lastModified = :timestamp WHERE code = :code")
    suspend fun markAsDeleted(code: String, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM products WHERE isDeleted = 1 AND syncStatus = 'synced'")
    suspend fun cleanupSyncedDeletions()

    @Delete
    suspend fun delete(product: Product)
}

// No se requiere cambio en los métodos, Room soporta ByteArray como BLOB.
