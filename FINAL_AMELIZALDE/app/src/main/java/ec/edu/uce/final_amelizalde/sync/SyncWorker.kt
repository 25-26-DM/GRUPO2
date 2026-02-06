package ec.edu.uce.final_erenriquezp.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ec.edu.uce.final_erenriquezp.notification.NotificationHelper

/**
 * Worker para sincronización automática en background
 * Se ejecuta periódicamente cuando hay conexión a Internet
 */
class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val TAG = "SyncWorker"
    private val syncManager = SyncManager(context)
    private val notificationHelper = NotificationHelper(context)

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Iniciando sincronización automática...")

            // Verificar si hay conexión a Internet
            if (!syncManager.isNetworkAvailable()) {
                Log.d(TAG, "No hay conexión a Internet, saltando sincronización")
                return Result.retry()
            }

            // Realizar sincronización completa
            val result = syncManager.fullSync()

            if (result.success) {
                Log.d(TAG, "Sincronización automática exitosa: ${result.message}")

                // Notificar solo si hubo cambios
                val totalSynced = result.productsSynced + result.usersSynced
                if (totalSynced > 0) {
                    notificationHelper.notifySync(totalSynced)
                }

                Result.success()
            } else {
                Log.e(TAG, "Error en sincronización automática: ${result.message}")
                Result.retry()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error en SyncWorker", e)
            Result.failure()
        }
    }
}
