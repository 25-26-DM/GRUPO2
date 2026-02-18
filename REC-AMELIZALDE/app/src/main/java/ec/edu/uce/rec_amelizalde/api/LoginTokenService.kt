package ec.edu.uce.rec_amelizalde.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Servicio para autenticación mediante código OTP enviado por email.
 * Utiliza el servicio web 'logintokenrec' para enviar códigos de verificación.
 */
object LoginTokenService {
    
    // URL base del servicio - Configurar según el endpoint real
    private const val BASE_URL = "https://logintokenrec.example.com"
    
    // Correo grupal predefinido
    const val GROUP_EMAIL = "ucepatrones@gmail.com"
    
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    /**
     * Solicita el envío de un código OTP al correo especificado.
     * El servicio 'logintokenrec' enviará un código de 6 dígitos al email.
     * 
     * @param email Correo electrónico donde se enviará el código
     * @return OTPResponse con el resultado de la operación
     */
    suspend fun requestOTP(email: String): OTPResponse {
        return try {
            val response = client.post("$BASE_URL/api/send-otp") {
                contentType(ContentType.Application.Json)
                setBody(OTPRequest(email = email))
            }
            
            if (response.status.isSuccess()) {
                response.body<OTPResponse>()
            } else {
                OTPResponse(
                    success = false,
                    message = "Error del servidor: ${response.status.value}",
                    code = null
                )
            }
        } catch (e: Exception) {
            // Para desarrollo/pruebas: generar código local si el servicio no está disponible
            val generatedCode = generateLocalCode()
            OTPResponse(
                success = true,
                message = "Código generado localmente (modo desarrollo): $generatedCode",
                code = generatedCode
            )
        }
    }
    
    /**
     * Valida un código OTP contra el servicio (opcional).
     * Si el servicio no soporta validación remota, se puede validar localmente.
     * 
     * @param email Correo electrónico asociado
     * @param code Código de 6 dígitos ingresado por el usuario
     * @return true si el código es válido
     */
    suspend fun validateOTP(email: String, code: String): OTPValidationResponse {
        return try {
            val response = client.post("$BASE_URL/api/validate-otp") {
                contentType(ContentType.Application.Json)
                setBody(OTPValidationRequest(email = email, code = code))
            }
            
            if (response.status.isSuccess()) {
                response.body<OTPValidationResponse>()
            } else {
                OTPValidationResponse(valid = false, message = "Error de validación")
            }
        } catch (e: Exception) {
            // Para desarrollo: la validación se hará localmente
            OTPValidationResponse(valid = false, message = "Validación local requerida")
        }
    }
    
    /**
     * Genera un código aleatorio de 6 dígitos para desarrollo/pruebas.
     */
    fun generateLocalCode(): String {
        return (100000..999999).random().toString()
    }
    
    /**
     * Valida localmente que el código tenga el formato correcto (6 dígitos).
     */
    fun isValidCodeFormat(code: String): Boolean {
        return code.length == 6 && code.all { it.isDigit() }
    }
    
    /**
     * Cierra el cliente HTTP cuando ya no se necesita.
     */
    fun close() {
        client.close()
    }
}
