package com.example.tarea07_u2_g2.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarea07_u2_g2.controller.Controlador

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit, // Cambiado para recibir el nombre del usuario
    onNavigateToRegister: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // Variable para mostrar el hash en tiempo real
    val hashVisual = if (clave.isNotEmpty()) Controlador.hashString(clave) else ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppLogo()
        Spacer(modifier = Modifier.height(16.dp))
        AppName()
        Spacer(modifier = Modifier.height(32.dp))

        // Campo 1: Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo 2: Apellido
        OutlinedTextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo 3: Contraseña
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                val claveHash = Controlador.hashString(clave)
                if (Controlador.validarLogin(nombre, apellido, claveHash)) {
                    Toast.makeText(context, "¡Bienvenido $nombre!", Toast.LENGTH_SHORT).show()
                    onLoginSuccess("$nombre $apellido") // Pasamos el nombre completo
                } else {
                    Toast.makeText(context, "Nombre, Apellido o Clave incorrectos", Toast.LENGTH_SHORT).show()
                }
            })
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val claveHash = Controlador.hashString(clave)
                if (Controlador.validarLogin(nombre, apellido, claveHash)) {
                    Toast.makeText(context, "¡Bienvenido $nombre!", Toast.LENGTH_SHORT).show()
                    onLoginSuccess("$nombre $apellido") // Pasamos el nombre completo
                } else {
                    Toast.makeText(context, "Nombre, Apellido o Clave incorrectos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("INGRESAR")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onNavigateToRegister() }) {
            Text("¿Nuevo usuario? Regístrate aquí")
        }

        // --- MOSTRAR USUARIO Y HASH EN PANTALLA ---
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Usuario: $nombre $apellido", fontSize = 14.sp)
        Text(text = "Hash generado: $hashVisual", fontSize = 12.sp)
    }
}
