
package ec.edu.uce.taller6

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.core.graphics.createBitmap
import ec.edu.uce.taller6.ui.theme.TALLER6Theme
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TALLER6Theme {
                val context = LocalContext.current
                val prefs = remember { context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE) }
                var isLoggedIn by remember {
                    mutableStateOf(prefs.getBoolean("is_logged_in", false))
                }

                if (isLoggedIn) {
                    MainScreen(onLogout = {
                        clearCurrentSession(context)
                        isLoggedIn = false
                    })
                } else {
                    LoginScreen(onLoginSuccess = { isLoggedIn = true })
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_personal),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text("Bienvenido", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Inicia sesión para continuar", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        saveLoginInfo(context, username)
                        onLoginSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text("Ingresar")
            }
        }
    }
}

fun saveLoginInfo(context: Context, username: String) {
    val prefs = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.US)
    val currentDate = sdf.format(Date())

    val history = prefs.getStringSet("login_history", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
    history.add("$username - $currentDate")

    prefs.edit {
        putString("username", username)
        putString("login_date", currentDate)
        putBoolean("is_logged_in", true)
        putStringSet("login_history", history)
    }
}

fun clearCurrentSession(context: Context) {
    val prefs = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
    prefs.edit {
        remove("username")
        remove("login_date")
        putBoolean("is_logged_in", false)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onLogout: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE) }
    val username = prefs.getString("username", "N/A") ?: "N/A"
    val loginDate = prefs.getString("login_date", "N/A") ?: "N/A"
    val history = prefs.getStringSet("login_history", emptySet())?.toList()?.sortedDescending() ?: emptyList()

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                bitmap = renderPdfPage(context, it)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Visor PDF") },
                actions = {
                    TextButton(onClick = onLogout) { Text("Salir") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { launcher.launch(arrayOf("application/pdf")) }) {
                Icon(Icons.Default.Add, contentDescription = "Abrir PDF")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Usuario: $username", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Último ingreso: $loginDate", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (bitmap == null) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Historial de Ingresos", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(history) {
                            Text(it, modifier = Modifier.padding(vertical = 4.dp))
                            Divider()
                        }
                    }
                }
            }
            PdfViewer(
                modifier = if (bitmap != null) Modifier.weight(1f) else Modifier,
                bitmap = bitmap
            )
        }
    }
}

@Composable
fun PdfViewer(modifier: Modifier = Modifier, bitmap: Bitmap?) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "PDF Page",
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (bitmap == null) {
                   // Text("Abre un PDF para empezar", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

private fun renderPdfPage(context: Context, uri: Uri): Bitmap? {
    return try {
        val pfd = context.contentResolver.openFileDescriptor(uri, "r")
        pfd?.let {
            val renderer = PdfRenderer(it)
            renderer.use { r ->
                val page = r.openPage(0)
                val bitmap = createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                bitmap
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    TALLER6Theme {
        LoginScreen(onLoginSuccess = {})
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TALLER6Theme {
        MainScreen(onLogout = {})
    }
}
