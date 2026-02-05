package ec.edu.uce.final_aetroyab.sync

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit

/**
 * Scheduler para configurar la sincronización automática periódica
 */
object SyncScheduler {
    private const val TAG = "SyncScheduler"
    private const val SYNC_WORK_NAME = "periodic_sync_work"

    /**
     * Programa una sincronización periódica cada 15 minutos
     * Solo se ejecuta cuando hay conexión a Internet
     */
    fun schedulePeriodic(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                15, TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )

        Log.d(TAG, "Sincronización periódica programada")
    }

    /**
     * Programa una sincronización inmediata en cuanto haya Internet
     */
    fun scheduleImmediate(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(syncRequest)

        Log.d(TAG, "Sincronización inmediata programada")
    }

    /**
     * Cancela todas las sincronizaciones programadas
     */
    fun cancelAll(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(SYNC_WORK_NAME)
        Log.d(TAG, "Sincronización periódica cancelada")
    }
}
