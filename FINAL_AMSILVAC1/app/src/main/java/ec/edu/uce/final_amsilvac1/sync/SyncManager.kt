package ec.edu.uce.final_amsilvac1.sync

import android.content.Context
import android.util.Log
import ec.edu.uce.final_amsilvac1.data.AppDatabase
import ec.edu.uce.final_amsilvac1.data.Product
import ec.edu.uce.final_amsilvac1.data.User
import ec.edu.uce.final_amsilvac1.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncManager(private val context: Context) {
    private val TAG = "SyncManager"
    private val db by lazy { AppDatabase.getDatabase(context) }
    private val dynamoHelper = DynamoDBHelper

    init {
        dynamoHelper.initialize(context)
    }

    data class SyncResult(
        val success: Boolean,
        val productsSynced: Int,
        val usersSynced: Int,
        val productsDeleted: Int,
        val errors: Int,
        val message: String
    )

    /**
     * Sincroniza todos los datos pendientes de SQLite a DynamoDB
     * Solo se ejecuta si hay conexión a Internet
     */
    suspend fun syncToCloud(): SyncResult = withContext(Dispatchers.IO) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            return@withContext SyncResult(
                success = false,
                productsSynced = 0,
                usersSynced = 0,
                productsDeleted = 0,
                errors = 0,
                message = "No hay conexión a Internet"
            )
        }

        Log.d(TAG, "Iniciando sincronización a DynamoDB...")

        var productsSynced = 0
        var usersSynced = 0
        var productsDeleted = 0
        var errors = 0

        try {
            // Crear tablas si no existen
            dynamoHelper.createTablesIfNotExist()

            // 1. Sincronizar usuarios pendientes
            val pendingUsers = db.userDao().getPendingSync()
            Log.d(TAG, "Usuarios pendientes de sincronización: ${pendingUsers.size}")

            for (user in pendingUsers) {
                if (dynamoHelper.putUser(user)) {
                    db.userDao().updateSyncStatus(user.username, "synced")
                    usersSynced++
                } else {
                    db.userDao().updateSyncStatus(user.username, "error")
                    errors++
                }
            }

            // 2. Sincronizar productos pendientes
            val pendingProducts = db.productDao().getPendingSync()
            Log.d(TAG, "Productos pendientes de sincronización: ${pendingProducts.size}")

            for (product in pendingProducts) {
                if (dynamoHelper.putProduct(product)) {
                    db.productDao().updateSyncStatus(product.code, "synced")
                    productsSynced++
                } else {
                    db.productDao().updateSyncStatus(product.code, "error")
                    errors++
                }
            }

            // 3. Sincronizar eliminaciones de productos
            val pendingDeletions = db.productDao().getPendingDeletion()
            Log.d(TAG, "Productos pendientes de eliminación: ${pendingDeletions.size}")

            for (product in pendingDeletions) {
                if (dynamoHelper.deleteProduct(product.code)) {
                    db.productDao().updateSyncStatus(product.code, "synced")
                    productsDeleted++
                } else {
                    db.productDao().updateSyncStatus(product.code, "error")
                    errors++
                }
            }

            // 4. Limpiar registros eliminados que ya fueron sincronizados
            db.productDao().cleanupSyncedDeletions()

            val message = buildString {
                append("Sincronización completada: ")
                append("$productsSynced productos, ")
                append("$usersSynced usuarios, ")
                append("$productsDeleted eliminaciones")
                if (errors > 0) append(", $errors errores")
            }

            Log.d(TAG, message)

            SyncResult(
                success = errors == 0,
                productsSynced = productsSynced,
                usersSynced = usersSynced,
                productsDeleted = productsDeleted,
                errors = errors,
                message = message
            )

        } catch (e: Exception) {
            Log.e(TAG, "Error durante la sincronización", e)
            SyncResult(
                success = false,
                productsSynced = productsSynced,
                usersSynced = usersSynced,
                productsDeleted = productsDeleted,
                errors = errors + 1,
                message = "Error: ${e.message}"
            )
        }
    }

    /**
     * Descarga todos los datos de DynamoDB y los guarda en SQLite
     * Útil para sincronización inicial o recuperación de datos
     */
    suspend fun syncFromCloud(): SyncResult = withContext(Dispatchers.IO) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            return@withContext SyncResult(
                success = false,
                productsSynced = 0,
                usersSynced = 0,
                productsDeleted = 0,
                errors = 0,
                message = "No hay conexión a Internet"
            )
        }

        Log.d(TAG, "Descargando datos desde DynamoDB...")

        var productsDownloaded = 0
        var errors = 0

        try {
            // Descargar productos desde DynamoDB
            val cloudProducts = dynamoHelper.getAllProducts()
            Log.d(TAG, "Productos descargados: ${cloudProducts.size}")

            for (product in cloudProducts) {
                try {
                    // Verificar si el producto local es más reciente
                    val localProduct = db.productDao().getByCode(product.code)

                    if (localProduct == null || product.lastModified > localProduct.lastModified) {
                        // Insertar o actualizar con el producto de la nube
                        db.productDao().insert(product.copy(syncStatus = "synced"))
                        productsDownloaded++
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error al insertar producto ${product.code}", e)
                    errors++
                }
            }

            val message = "Descargados $productsDownloaded productos desde la nube"
            Log.d(TAG, message)

            SyncResult(
                success = errors == 0,
                productsSynced = productsDownloaded,
                usersSynced = 0,
                productsDeleted = 0,
                errors = errors,
                message = message
            )

        } catch (e: Exception) {
            Log.e(TAG, "Error descargando datos", e)
            SyncResult(
                success = false,
                productsSynced = 0,
                usersSynced = 0,
                productsDeleted = 0,
                errors = 1,
                message = "Error: ${e.message}"
            )
        }
    }

    /**
     * Sincronización bidireccional completa:
     * 1. Envía datos locales pendientes a DynamoDB
     * 2. Descarga datos nuevos de DynamoDB
     */
    suspend fun fullSync(): SyncResult = withContext(Dispatchers.IO) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            return@withContext SyncResult(
                success = false,
                productsSynced = 0,
                usersSynced = 0,
                productsDeleted = 0,
                errors = 0,
                message = "No hay conexión a Internet"
            )
        }

        Log.d(TAG, "Iniciando sincronización bidireccional completa...")

        // Primero sincronizar a la nube
        val uploadResult = syncToCloud()

        // Luego descargar de la nube
        val downloadResult = syncFromCloud()

        SyncResult(
            success = uploadResult.success && downloadResult.success,
            productsSynced = uploadResult.productsSynced + downloadResult.productsSynced,
            usersSynced = uploadResult.usersSynced,
            productsDeleted = uploadResult.productsDeleted,
            errors = uploadResult.errors + downloadResult.errors,
            message = "Sincronización completa: ${uploadResult.message}. ${downloadResult.message}"
        )
    }

    /**
     * Retorna el número de elementos pendientes de sincronización
     */
    suspend fun getPendingSyncCount(): Int = withContext(Dispatchers.IO) {
        val pendingProducts = db.productDao().getPendingSync().size
        val pendingUsers = db.userDao().getPendingSync().size
        val pendingDeletions = db.productDao().getPendingDeletion().size
        pendingProducts + pendingUsers + pendingDeletions
    }

    /**
     * Verifica si hay conexión a Internet
     */
    fun isNetworkAvailable(): Boolean {
        return NetworkUtils.isNetworkAvailable(context)
    }
}
