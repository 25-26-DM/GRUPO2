package ec.edu.uce.final_amsivac1.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ec.edu.uce.final_amsilvac1.sync.SyncWorker

/**
 * BroadcastReceiver para detectar cambios de conectividad y lanzar la sincronización
 */
class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Validar que la acción recibida sea la esperada (aunque esté deprecado, es necesario para compatibilidad)
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent?.action && isConnectedToWifiOrInternet(context)) {
            val workRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                "SyncOnNetworkAvailable",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }

    private fun isConnectedToWifiOrInternet(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        // Solo sincronizar si la conexión es WiFi y tiene acceso a Internet
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) &&
               capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
