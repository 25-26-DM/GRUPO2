package ec.edu.uce.rec_amelizalde.api

import kotlinx.serialization.Serializable

/**
 * Request para solicitar el envío de un código OTP.
 */
@Serializable
data class OTPRequest(
    val email: String
)

/**
 * Response del servicio al solicitar un OTP.
 * 
 * @param success Indica si la operación fue exitosa
 * @param message Mensaje descriptivo del resultado
 * @param code Código OTP (solo en modo desarrollo/pruebas, normalmente null en producción)
 */
@Serializable
data class OTPResponse(
    val success: Boolean,
    val message: String,
    val code: String? = null
)

/**
 * Request para validar un código OTP.
 */
@Serializable
data class OTPValidationRequest(
    val email: String,
    val code: String
)

/**
 * Response de la validación de OTP.
 */
@Serializable
data class OTPValidationResponse(
    val valid: Boolean,
    val message: String? = null
)
