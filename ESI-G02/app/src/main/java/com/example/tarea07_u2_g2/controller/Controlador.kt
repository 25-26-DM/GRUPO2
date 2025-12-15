package com.example.tarea07_u2_g2.controller

import androidx.compose.runtime.mutableStateListOf
import com.example.tarea07_u2_g2.model.Producto
import com.example.tarea07_u2_g2.model.Usuario
import java.security.MessageDigest

object Controlador {

    // LISTA DE USUARIOS
    // Inicialmente vacía para registro en memoria
    val usuarios = mutableStateListOf<Usuario>()

    // LISTA DE PRODUCTOS
    val productos = mutableStateListOf<Producto>(
        Producto("001", "Laptop HP", 850.00, "01/01/2024", true),
        Producto("002", "Mouse Logitech", 25.50, "15/02/2024", true),
        Producto("003", "Teclado Gamer", 45.00, "10/03/2024", false)
    )

    // --- LÓGICA DE USUARIOS ---

    // Valida si existe el usuario con el hash de la clave proporcionada
    fun validarLogin(nombre: String, apellido: String, claveHash: String): Boolean {
        return usuarios.any {
            it.nombre.equals(nombre, ignoreCase = true) &&
                    it.apellido.equals(apellido, ignoreCase = true) &&
                    it.clave == claveHash
        }
    }

    // Registra un nuevo usuario hasheando la clave antes de guardarla
    fun registrarUsuario(nombre: String, apellido: String, clave: String) {
        val claveHash = hashString(clave)
        usuarios.add(Usuario(nombre, apellido, claveHash))
    }

    // Función utilitaria para hashear texto (SHA-256)
    fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // --- LÓGICA DE PRODUCTOS ---
    fun agregarProducto(producto: Producto) { productos.add(producto) }

    fun editarProducto(productoEditado: Producto) {
        val index = productos.indexOfFirst { it.codigo == productoEditado.codigo }
        if (index != -1) productos[index] = productoEditado
    }

    fun eliminarProducto(producto: Producto) { productos.remove(producto) }
}