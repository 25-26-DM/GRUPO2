package com.example.tarea07_u2_g2.controller

import androidx.compose.runtime.mutableStateListOf
import com.example.tarea07_u2_g2.model.Producto
import com.example.tarea07_u2_g2.model.Usuario

object Controlador {

    // LISTA DE USUARIOS
    // Actualizado: Ahora guarda Nombre, Apellido y Clave.
    val usuarios = mutableStateListOf<Usuario>(
        Usuario("Edison", "Enriquez", "1234"),    // Ejemplo Integrante 1
        Usuario("Kelly", "Ledesma", "1234"),   // Ejemplo Integrante 2
        Usuario("Stalin", "Acurio", "1234"),
        Usuario("Ariel", "Elizalde", "1234"),
        Usuario("Mauricio", "Lopez", "1234"),
        Usuario("Angelo", "Silva", "1234"),
        Usuario("Alexis", "Troya", "1234")
    )

    // LISTA DE PRODUCTOS (Igual que antes)
    val productos = mutableStateListOf<Producto>(
        Producto("001", "Laptop HP", 850.00, "01/01/2024", true),
        Producto("002", "Mouse Logitech", 25.50, "15/02/2024", true),
        Producto("003", "Teclado Gamer", 45.00, "10/03/2024", false)
    )

    // --- LÓGICA REFACTORIZADA ---

    // 1. Validar Login con Nombre y Apellido
    fun validarLogin(nombre: String, apellido: String, clave: String): Boolean {
        // Busca coincidencias exactas ignorando mayúsculas/minúsculas en el nombre
        return usuarios.any {
            it.nombre.equals(nombre, ignoreCase = true) &&
                    it.apellido.equals(apellido, ignoreCase = true) &&
                    it.clave == clave
        }
    }

    // 2. Registrar Usuario con los nuevos campos
    fun registrarUsuario(nombre: String, apellido: String, clave: String) {
        usuarios.add(Usuario(nombre, apellido, clave))
    }

    // Las funciones de productos se mantienen igual
    fun agregarProducto(producto: Producto) { productos.add(producto) }

    fun editarProducto(productoEditado: Producto) {
        val index = productos.indexOfFirst { it.codigo == productoEditado.codigo }
        if (index != -1) productos[index] = productoEditado
    }

    fun eliminarProducto(producto: Producto) { productos.remove(producto) }
}