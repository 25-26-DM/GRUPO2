package ec.edu.uce.final_amsilvac1.controller

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ec.edu.uce.final_amsilvac1.data.AppDatabase
import ec.edu.uce.final_amsilvac1.data.User
import ec.edu.uce.final_amsilvac1.sync.SyncWorker
import ec.edu.uce.final_amsilvac1.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class AuthController(private val context: Context) {
    private val db by lazy { AppDatabase.getDatabase(context) }

    suspend fun login(username: String, password: String): User? = withContext(Dispatchers.IO) {
        val normalized = username.trim().lowercase()
        val hash = hashPassword(password)
        db.userDao().validateUser(normalized, hash)
    }

    suspend fun register(username: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val original = username.trim()
        val normalized = original.lowercase()
        val userDao = db.userDao()
        val exists = userDao.getUserByUsername(normalized)
        if (exists != null) return@withContext false

        // Crear usuario marcado como pendiente de sincronización
        val newUser = User(
            username = normalized,
            password = hashPassword(password),
            syncStatus = "pending",
            lastModified = System.currentTimeMillis()
        )
        userDao.insertUser(newUser)
        // Lanzar sincronización inmediata si hay WiFi
        if (NetworkUtils.isNetworkAvailable(context)) {
            val workRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                "SyncOnUserInsert",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
        true
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
