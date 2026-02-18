package ec.edu.uce.rec_amelizalde.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.Surface
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedButton

import ec.edu.uce.rec_amelizalde.R
import ec.edu.uce.rec_amelizalde.api.LoginTokenService
import ec.edu.uce.rec_amelizalde.controller.AuthController
import ec.edu.uce.rec_amelizalde.controller.OTPResult

/**
 * Estados del flujo de login OTP
 */
private enum class LoginState {
    EMAIL_INPUT,    // Ingreso de correo electrónico
    CODE_INPUT,     // Ingreso del código OTP
    LOADING         // Cargando (enviando código o validando)
}

@Composable
fun LoginScreen(
    context: Context,
    initialUsername: String? = null,
    onLoginSuccess: (normalizedUsername: String, displayName: String) -> Unit,
    showError: (String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Estado del flujo de login
    var loginState by remember { mutableStateOf(LoginState.EMAIL_INPUT) }
    
    // Correo electrónico (pre-llenado con el correo grupal)
    var email by remember { mutableStateOf(LoginTokenService.GROUP_EMAIL) }
    
    // Código OTP ingresado por el usuario
    var otpCode by remember { mutableStateOf("") }
    
    // Código OTP esperado (recibido del servicio en modo desarrollo)
    var expectedCode by remember { mutableStateOf<String?>(null) }
    
    // Mensaje informativo
    var infoMessage by remember { mutableStateOf("") }
    
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
                    .fillMaxWidth(1f)
                    .padding(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.logo_personal),
                        contentDescription = "Logo del grupo",
                        modifier = Modifier.size(200.dp).padding(bottom = 16.dp)
                    )
                    
                    // Título
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Subtítulo según estado
                    Text(
                        text = when (loginState) {
                            LoginState.EMAIL_INPUT -> "Ingresa tu correo para recibir el código"
                            LoginState.CODE_INPUT -> "Ingresa el código de 6 dígitos"
                            LoginState.LOADING -> "Procesando..."
                        },
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    
                    when (loginState) {
                        LoginState.EMAIL_INPUT -> {
                            // Campo de correo electrónico
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Correo electrónico") },
                                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                                shape = CardDefaults.shape,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                singleLine = true
                            )
                            
                            // Botón enviar código
                            Button(
                                onClick = {
                                    if (email.isBlank() || !email.contains("@")) {
                                        showError("Ingresa un correo válido")
                                        return@Button
                                    }
                                    
                                    loginState = LoginState.LOADING
                                    scope.launch {
                                        when (val result = controller.requestLoginCode(email)) {
                                            is OTPResult.Success -> {
                                                expectedCode = result.code
                                                infoMessage = result.message
                                                loginState = LoginState.CODE_INPUT
                                            }
                                            is OTPResult.Error -> {
                                                showError(result.message)
                                                loginState = LoginState.EMAIL_INPUT
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = CardDefaults.shape
                            ) {
                                Text("Enviar código", fontSize = 18.sp)
                            }
                        }
                        
                        LoginState.CODE_INPUT -> {
                            // Mostrar el correo al que se envió el código
                            Text(
                                text = "Código enviado a: $email",
                                fontSize = 14.sp,
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            // Mostrar mensaje informativo (incluye código en modo desarrollo)
                            if (infoMessage.isNotEmpty()) {
                                Text(
                                    text = infoMessage,
                                    fontSize = 12.sp,
                                    color = Color(0xFFFF9800),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }
                            
                            // Campo para código OTP
                            OutlinedTextField(
                                value = otpCode,
                                onValueChange = { 
                                    // Solo permitir dígitos y máximo 6 caracteres
                                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                                        otpCode = it
                                    }
                                },
                                label = { Text("Código de 6 dígitos") },
                                leadingIcon = { Icon(Icons.Filled.Pin, contentDescription = null) },
                                shape = CardDefaults.shape,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                singleLine = true
                            )
                            
                            // Botón verificar
                            Button(
                                onClick = {
                                    if (otpCode.length != 6) {
                                        showError("El código debe tener 6 dígitos")
                                        return@Button
                                    }
                                    
                                    loginState = LoginState.LOADING
                                    scope.launch {
                                        val isValid = controller.validateCode(otpCode, expectedCode)
                                        if (isValid) {
                                            controller.clearOTPCode()
                                            // Usar el email como identificador de usuario
                                            onLoginSuccess(email, email.substringBefore("@"))
                                        } else {
                                            showError("Código inválido o expirado")
                                            loginState = LoginState.CODE_INPUT
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = CardDefaults.shape,
                                enabled = otpCode.length == 6
                            ) {
                                Text("Verificar", fontSize = 18.sp)
                            }
                            
                            // Botón reenviar código
                            OutlinedButton(
                                onClick = {
                                    otpCode = ""
                                    loginState = LoginState.LOADING
                                    scope.launch {
                                        when (val result = controller.requestLoginCode(email)) {
                                            is OTPResult.Success -> {
                                                expectedCode = result.code
                                                infoMessage = result.message
                                                loginState = LoginState.CODE_INPUT
                                            }
                                            is OTPResult.Error -> {
                                                showError(result.message)
                                                loginState = LoginState.CODE_INPUT
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                shape = CardDefaults.shape
                            ) {
                                Icon(
                                    Icons.Filled.Refresh, 
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp).padding(end = 4.dp)
                                )
                                Text("Reenviar código")
                            }
                            
                            // Botón cambiar correo
                            TextButton(
                                onClick = {
                                    otpCode = ""
                                    expectedCode = null
                                    infoMessage = ""
                                    loginState = LoginState.EMAIL_INPUT
                                },
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                            ) {
                                Text("Cambiar correo", color = Color.Gray)
                            }
                        }
                        
                        LoginState.LOADING -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp).padding(16.dp),
                                color = Color(0xFF1976D2)
                            )
                        }
                    }
                    
                    // Enlace a registro (solo visible en estado EMAIL_INPUT)
                    if (loginState == LoginState.EMAIL_INPUT) {
                        TextButton(
                            onClick = { onNavigateToRegister() },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Text(
                                "¿No tienes cuenta? Regístrate", 
                                fontSize = 16.sp, 
                                color = Color(0xFF1976D2)
                            )
                        }
                    }
                }
            }
        }
    }
}