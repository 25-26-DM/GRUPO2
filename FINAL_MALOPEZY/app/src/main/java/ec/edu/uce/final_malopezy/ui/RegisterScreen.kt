package ec.edu.uce.final_malopezy.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.TextButton
import androidx.compose.runtime.setValue

import ec.edu.uce.final_malopezy.R
import ec.edu.uce.final_malopezy.controller.AuthController

@Composable
fun RegisterScreen(
    context: Context,
    onRegisterSuccess: (String) -> Unit,
    showError: (String) -> Unit,
    onNavigateToLogin: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val controller = remember { AuthController(context) }

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
                    .fillMaxWidth()
                    .padding(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_personal),
                        contentDescription = "Logo personal",
                        modifier = Modifier.size(240.dp).padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Crear cuenta",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Registra un nuevo usuario",
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
                                val registered = controller.register(original, password)
                                if (registered) {
                                    onRegisterSuccess(original)
                                } else {
                                    showError("El usuario ya existe")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = CardDefaults.shape
                    ) {
                        Text("Registrar", fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Nuevo botón para ir al login si ya se conoce usuario/contraseña
                    TextButton(
                        onClick = { onNavigateToLogin() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("¿Ya tienes cuenta? Ir a Iniciar sesión")
                    }
                }
            }
        }
    }
}
