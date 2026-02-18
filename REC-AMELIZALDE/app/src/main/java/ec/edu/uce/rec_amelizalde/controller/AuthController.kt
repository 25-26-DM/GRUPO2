package ec.edu.uce.rec_amelizalde.controller

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ec.edu.uce.rec_amelizalde.api.LoginTokenService
import ec.edu.uce.rec_amelizalde.api.OTPResponse
import ec.edu.uce.rec_amelizalde.data.AppDatabase
import ec.edu.uce.rec_amelizalde.data.User
import ec.edu.uce.rec_amelizalde.sync.SyncWorker
import ec.edu.uce.rec_amelizalde.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

/**
 * Resultado de la solicitud de código OTP.
 */
sealed class OTPResult {
    data class Success(val message: String, val code: String?) : OTPResult()
    data class Error(val message: String) : OTPResult()
}

class AuthController (private val context: Context) {
    private val db by lazy { AppDatabase.getDatabase(context) }
    
    // Almacena el código OTP actual para validación local
    private var currentOTPCode: String? = null
    private var otpRequestTimestamp: Long = 0L
    
    // Tiempo de expiración del código OTP (5 minutos)
    private val OTP_EXPIRATION_MS = 5 * 60 * 1000L

    /**
     * Solicita el envío de un código OTP al correo especificado.
     * Llama al servicio web 'logintokenrec' para enviar el código.
     * 
     * @param email Correo electrónico donde se enviará el código
     * @return OTPResult con el resultado de la operación
     */
    suspend fun requestLoginCode(email: String): OTPResult = withContext(Dispatchers.IO) {
        try {
            // Verificar conectividad
            if (!NetworkUtils.isNetworkAvailable(context)) {
                return@withContext OTPResult.Error("No hay conexión a internet")
            }
            
            val response: OTPResponse = LoginTokenService.requestOTP(email)
            
            if (response.success) {
                // Guardar código para validación local (en modo desarrollo)
                currentOTPCode = response.code
                otpRequestTimestamp = System.currentTimeMillis()
                
                OTPResult.Success(
                    message = response.message,
                    code = response.code // En producción esto sería null
                )
            } else {
                OTPResult.Error(response.message)
            }
        } catch (e: Exception) {
            OTPResult.Error("Error al solicitar código: ${e.message}")
        }
    }
    
    /**
     * Valida el código OTP ingresado por el usuario.
     * 
     * @param inputCode Código de 6 dígitos ingresado por el usuario
     * @param expectedCode Código esperado (opcional, si se valida localmente)
     * @return true si el código es válido y no ha expirado
     */
    fun validateCode(inputCode: String, expectedCode: String? = null): Boolean {
        // Validar formato
        if (!LoginTokenService.isValidCodeFormat(inputCode)) {
            return false
        }
        
        // Usar código esperado proporcionado o el almacenado
        val codeToCompare = expectedCode ?: currentOTPCode
        
        if (codeToCompare == null) {
            return false
        }
        
        // Verificar expiración
        if (System.currentTimeMillis() - otpRequestTimestamp > OTP_EXPIRATION_MS) {
            currentOTPCode = null
            return false
        }
        
        return inputCode == codeToCompare
    }
    
    /**
     * Limpia el código OTP almacenado después de un login exitoso.
     */
    fun clearOTPCode() {
        currentOTPCode = null
        otpRequestTimestamp = 0L
    }
    
    /**
     * Verifica si hay un código OTP válido pendiente.
     */
    fun hasValidPendingOTP(): Boolean {
        return currentOTPCode != null && 
               (System.currentTimeMillis() - otpRequestTimestamp <= OTP_EXPIRATION_MS)
    }

    // ============ Métodos existentes (mantenidos para compatibilidad) ============

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