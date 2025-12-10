package com.example.tarea07_u2_g2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme // Importamos el tema genérico
import androidx.compose.runtime.*
import com.example.tarea07_u2_g2.model.Producto
import com.example.tarea07_u2_g2.view.HomeScreen
import com.example.tarea07_u2_g2.view.LoginScreen
import com.example.tarea07_u2_g2.view.ProductoFormScreen
import com.example.tarea07_u2_g2.view.RegistroScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // USAMOS MaterialTheme GENÉRICO PARA EVITAR ERRORES DE NOMBRE
            MaterialTheme {
                AppNavegacion()
            }
        }
    }
}

@Composable
fun AppNavegacion() {
    var pantallaActual by remember { mutableStateOf("login") }
    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }

    when (pantallaActual) {
        "login" -> {
            LoginScreen(
                onLoginSuccess = { pantallaActual = "home" },
                onNavigateToRegister = { pantallaActual = "registro" }
            )
        }
        "registro" -> {
            RegistroScreen(
                onRegistroSuccess = { pantallaActual = "login" }
            )
        }
        "home" -> {
            HomeScreen(
                onLogout = { pantallaActual = "login" },
                onAddProduct = {
                    productoSeleccionado = null
                    pantallaActual = "formulario"
                },
                onEditProduct = { producto ->
                    productoSeleccionado = producto
                    pantallaActual = "formulario"
                }
            )
        }
        "formulario" -> {
            ProductoFormScreen(
                productoAEditar = productoSeleccionado,
                onSaveSuccess = { pantallaActual = "home" },
                onCancel = { pantallaActual = "home" }
            )
        }
    }
}