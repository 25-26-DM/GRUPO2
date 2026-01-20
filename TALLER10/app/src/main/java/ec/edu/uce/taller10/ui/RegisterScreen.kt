package ec.edu.uce.taller10.ui

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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.security.MessageDigest
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// Actualizar imports a paquete local
import ec.edu.uce.taller10.data.AppDatabase
import ec.edu.uce.taller10.data.User
import ec.edu.uce.taller10.R

fun hashPassword(password: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

@Composable
fun RegisterScreen(
    context: Context,
    onRegisterSuccess: () -> Unit,
    showError: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

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
                    // Verifica que el recurso exista en res/drawable como logo_personal.png, logo_personal.webp o logo_personal.xml
                    // Si el recurso no existe, usa un logo alternativo o muestra un placeholder
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
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        shape = CardDefaults.shape,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )
                    // Elimino el botón de mostrar/ocultar contraseña y el uso de los iconos Visibility/VisibilityOff
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
                                val db = AppDatabase.getDatabase(context)
                                val userDao = db.userDao()
                                // Verifica si el usuario ya existe en la base de datos
                                val exists = userDao.validateUser(username, hashPassword(password))
                                if (exists != null) {
                                    showError("El usuario ya existe")
                                } else {
                                    // Inserta el usuario con la contraseña cifrada en SQLite (Room)
                                    userDao.insertUser(User(username, hashPassword(password)))
                                    onRegisterSuccess()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = CardDefaults.shape
                    ) {
                        Text("Registrar", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
