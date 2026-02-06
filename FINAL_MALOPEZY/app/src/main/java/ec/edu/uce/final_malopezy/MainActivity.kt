package ec.edu.uce.final_malopezy

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import ec.edu.uce.final_malopezy.ui.theme.FINAL_MALOPEZYTheme
import ec.edu.uce.final_malopezy.ui.LoginScreen
import ec.edu.uce.final_malopezy.ui.RegisterScreen
import ec.edu.uce.final_malopezy.ui.ProductsListScreen
import ec.edu.uce.final_malopezy.ui.ProductFormScreen
import ec.edu.uce.final_malopezy.data.Product
import ec.edu.uce.final_malopezy.data.AppDatabase
import ec.edu.uce.final_malopezy.session.SessionManager
import ec.edu.uce.final_malopezy.sync.DynamoDBHelper
import ec.edu.uce.final_malopezy.sync.SyncScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Pre-populate DB if needed
        AppDatabase.prepopulateIfEmpty(this)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                DynamoDBHelper.initialize(this@MainActivity)
                Log.d("MainActivity", "DynamoDB initialized successfully")
            } catch (e: Exception) {
                Log.e("MainActivity", "Error initializing DynamoDB", e)
            }
        }

        // Programar sincronización automática en background
        SyncScheduler.schedulePeriodic(this)

        // Request POST_NOTIFICATIONS on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                // no-op, NotificationHelper hará la comprobación en tiempo de ejecución
            }
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Lanzar sincronización inmediata si hay WiFi al iniciar la app
        lifecycleScope.launch(Dispatchers.IO) {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            val network = cm.activeNetwork
            val capabilities = network?.let { cm.getNetworkCapabilities(it) }
            val hasWifi = capabilities?.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) == true
            val hasInternet = capabilities?.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            if (hasWifi && hasInternet) {
                val workRequest = androidx.work.OneTimeWorkRequestBuilder<ec.edu.uce.final_malopezy.sync.SyncWorker>().build()
                androidx.work.WorkManager.getInstance(this@MainActivity).enqueueUniqueWork(
                    "SyncOnAppStart",
                    androidx.work.ExistingWorkPolicy.REPLACE,
                    workRequest
                )
            }
        }

        setContent {
            FINAL_MALOPEZYTheme {
                val context = LocalContext.current
                val session = SessionManager(context)

                var screen by rememberSaveable { mutableStateOf("login") }
                var currentUser by rememberSaveable { mutableStateOf<String?>(null) }
                var pendingUsername by rememberSaveable { mutableStateOf<String?>(null) }
                var editingProduct by rememberSaveable { mutableStateOf<Product?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    when (screen) {
                        "login" -> {
                            LoginScreen(
                                context = context,
                                initialUsername = pendingUsername,
                                onLoginSuccess = { normalized, display ->
                                    // iniciar sesión y navegar
                                    session.startSession(normalized)
                                    currentUser = display
                                    pendingUsername = null
                                    screen = "main"
                                },
                                showError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_LONG).show() },
                                onNavigateToRegister = { screen = "register" }
                            )
                        }
                        "register" -> {
                            RegisterScreen(
                                context = context,
                                onRegisterSuccess = { username ->
                                    pendingUsername = username
                                    screen = "login"
                                },
                                showError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_LONG).show() },
                                onNavigateToLogin = { screen = "login" }
                            )
                        }
                        "main" -> {
                            // comprobar sesión válida
                            if (!session.isSessionValid()) {
                                session.clearSession()
                                screen = "login"
                            } else {
                                ProductsListScreen(
                                    context = context,
                                    onLogout = { session.clearSession(); screen = "login" },
                                    onAdd = { editingProduct = null; screen = "product_form" },
                                    onEdit = { product -> editingProduct = product; screen = "product_form" }
                                )
                            }
                        }
                        "product_form" -> {
                            ProductFormScreen(
                                context = context,
                                productToEdit = editingProduct,
                                onSave = { screen = "main" },
                                onCancel = { screen = "main" }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FINAL_MALOPEZYTheme {
        Greeting("Android")
    }
}
