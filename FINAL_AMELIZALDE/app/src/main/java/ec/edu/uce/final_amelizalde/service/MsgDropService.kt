package ec.edu.uce.final_erenriquezp.service

import kotlinx.coroutines.delay

/**
 * Servicio simulado que proporciona mensajes de error para acciones bloqueadas.
 * Simula una llamada a un servicio web "msgdrop".
 * 
 * En el futuro, esta implementación puede ser reemplazada por una llamada HTTP real.
 */
object MsgDropService {
    
    private const val SIMULATED_NETWORK_DELAY_MS = 300L
    
    /**
     * Obtiene el mensaje de error para una acción bloqueada.
     * 
     * @param action Tipo de acción: "edit" o "delete"
     * @return Mensaje de error proporcionado por el servicio
     */
    suspend fun getActionMessage(action: String): String {
        // Simular latencia de red
        delay(SIMULATED_NETWORK_DELAY_MS)
        
        return when (action.lowercase()) {
            "edit" -> "imposible editar Producto"
            "delete" -> "imposible eliminar Producto"
            else -> "imposible realizar acción en Producto"
        }
    }
    
    /**
     * Verifica si el servicio está disponible.
     * En una implementación real, haría un health check al endpoint.
     */
    suspend fun isServiceAvailable(): Boolean {
        delay(100)
        return true
    }
}
