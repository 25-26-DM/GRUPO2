package ec.edu.uce.final_erenriquezp.controller

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ec.edu.uce.final_erenriquezp.data.AppDatabase
import ec.edu.uce.final_erenriquezp.data.Product
import ec.edu.uce.final_erenriquezp.sync.SyncWorker
import ec.edu.uce.final_erenriquezp.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductController(private val context: Context) {
    private val db by lazy { AppDatabase.getDatabase(context) }

    suspend fun getAll(): List<Product> = withContext(Dispatchers.IO) {
        db.productDao().getAll()
    }

    suspend fun getByCode(code: String): Product? = withContext(Dispatchers.IO) {
        db.productDao().getByCode(code)
    }

    suspend fun insert(product: Product) = withContext(Dispatchers.IO) {
        // Marcar como pendiente de sincronización
        val productToInsert = product.copy(
            syncStatus = "pending",
            lastModified = System.currentTimeMillis()
        )
        db.productDao().insert(productToInsert)
        // Lanzar sincronización inmediata si hay WiFi
        if (NetworkUtils.isNetworkAvailable(context)) {
            val workRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                "SyncOnProductInsert",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }

    suspend fun update(product: Product) = withContext(Dispatchers.IO) {
        // Marcar como pendiente de sincronización
        val productToUpdate = product.copy(
            syncStatus = "pending",
            lastModified = System.currentTimeMillis()
        )
        db.productDao().update(productToUpdate)
        // Lanzar sincronización inmediata si hay WiFi
        if (NetworkUtils.isNetworkAvailable(context)) {
            val workRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                "SyncOnProductUpdate",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }

    suspend fun delete(product: Product) = withContext(Dispatchers.IO) {
        // Marcar como eliminado pendiente de sincronización
        db.productDao().markAsDeleted(product.code, System.currentTimeMillis())
    }

    suspend fun getPendingSyncCount(): Int = withContext(Dispatchers.IO) {
        db.productDao().getPendingSync().size + db.productDao().getPendingDeletion().size
    }

    /**
     * Verifica si un producto puede ser editado.
     * No se puede editar si available = false (disponibilidad 0)
     */
    fun canEdit(product: Product): Boolean {
        return product.available
    }

    /**
     * Verifica si un producto puede ser eliminado.
     * No se puede eliminar si available = false (disponibilidad 0)
     */
    fun canDelete(product: Product): Boolean {
        return product.available
    }
}
