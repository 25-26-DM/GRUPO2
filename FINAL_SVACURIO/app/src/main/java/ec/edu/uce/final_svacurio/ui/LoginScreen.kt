package ec.edu.uce.final_svacurio.ui


import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.Surface
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect

import ec.edu.uce.final_svacurio.R
import ec.edu.uce.final_svacurio.controller.AuthController


@Composable
fun LoginScreen(
    context: Context,
    initialUsername: String? = null,
    onLoginSuccess: (normalizedUsername: String, displayName: String) -> Unit,
    showError: (String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val controller = remember { AuthController(context) }

    LaunchedEffect(initialUsername) {
        if (!initialUsername.isNullOrBlank()) {
            name = initialUsername
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F6FA)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_personal),
                        contentDescription = "Logo del grupo",
                        modifier = Modifier.size(240.dp).padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Ingresa tus credenciales",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        shape = CardDefaults.shape,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        shape = CardDefaults.shape,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                val original = name.trim()
                                val normalized = original.lowercase()
                                val user = controller.login(normalized, password)
                                if (user != null) {
                                    onLoginSuccess(normalized, original)
                                } else {
                                    showError("Usuario o contraseña incorrectos")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = CardDefaults.shape
                    ) {
                        Text("Ingresar", fontSize = 18.sp)
                    }
                    TextButton(
                        onClick = { onNavigateToRegister() },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("¿No tienes cuenta? Regístrate", fontSize = 16.sp, color = Color(0xFF1976D2))
                    }
                }
            }
        }
    }
}
