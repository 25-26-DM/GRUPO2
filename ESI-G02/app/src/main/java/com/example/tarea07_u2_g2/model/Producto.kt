package com.example.tarea07_u2_g2.model

data class Producto(
    val codigo: String,           // Requerimiento: Código
    val descripcion: String,      // Requerimiento: Descripción
    val precio: Double,           // Requerimiento: Costo
    val fechaFabricacion: String, // Requerimiento: Fecha fabricación (Usamos String para hacerlo fácil)
    val disponibilidad: Boolean   // Requerimiento: Disponibilidad
)