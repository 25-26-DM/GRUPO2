package ec.edu.uce.taller9

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ec.edu.uce.taller9.ui.LoginScreen
import ec.edu.uce.taller9.ui.SensorListScreen
import ec.edu.uce.taller9.ui.RegisterScreen
import java.security.MessageDigest
import ec.edu.uce.taller9.ui.theme.TALLER9Theme
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign

fun hashPassword(password: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var requestingLocationUpdates = false
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private var lastLocation = mutableStateOf<Pair<Double, Double>?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location: Location? = locationResult.lastLocation
                location?.let {
                    lastLocation.value = Pair(it.latitude, it.longitude)
                }
            }
        }
        setContent {
            val navController = rememberNavController()
            val loginError = remember { mutableStateOf("") }
            val registerError = remember { mutableStateOf("") }
            TALLER9Theme {
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            context = this@MainActivity,
                            onLoginSuccess = { navController.navigate("main") },
                            showError = { loginError.value = it },
                            hashPassword = ::hashPassword,
                            onNavigateToRegister = { navController.navigate("register") }
                        )

                        if (loginError.value.isNotEmpty()) {
                            Toast.makeText(this@MainActivity, loginError.value, Toast.LENGTH_SHORT).show()
                            loginError.value = ""
                        }
                    }
                    composable("register") {
                        RegisterScreen(
                            context = this@MainActivity,
                            onRegisterSuccess = { navController.popBackStack() },
                            showError = { registerError.value = it }
                        )
                        if (registerError.value.isNotEmpty()) {
                            Toast.makeText(this@MainActivity, registerError.value, Toast.LENGTH_SHORT).show()
                            registerError.value = ""
                        }
                    }
                    composable("main") {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            LocationScreen(
                                location = lastLocation.value,
                                onRequestLocation = { checkLocationPermissionAndStartUpdates() },
                                onListSensors = { navController.navigate("sensors") },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                    composable("sensors") {
                        SensorListScreen(context = this@MainActivity)
                    }
                }
            }
        }
        checkLocationPermissionAndStartUpdates()
    }

    private fun checkLocationPermissionAndStartUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLocationUpdates() {
        if (!requestingLocationUpdates) {
            val locationRequest = LocationRequest.Builder(10000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build()
            try {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
                requestingLocationUpdates = true
            } catch (e: SecurityException) {
                Toast.makeText(this, "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopLocationUpdates() {
        if (requestingLocationUpdates) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            requestingLocationUpdates = false
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermissionAndStartUpdates()
    }
}

@Composable
fun LocationScreen(location: Pair<Double, Double>?, onRequestLocation: () -> Unit, onListSensors: () -> Unit, modifier: Modifier) {
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
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ubicación actual",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = if (location != null) {
                            "Latitud: ${location.first}\nLongitud: ${location.second}"
                        } else {
                            "Ubicación no disponible"
                        },
                        fontSize = 18.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    // Botones uno debajo del otro, centrados y con diseño moderno
                    Button(
                        onClick = onRequestLocation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = CardDefaults.shape
                    ) {
                        Text("Obtener ubicación", fontSize = 16.sp)
                    }
                    Button(
                        onClick = onListSensors,
                        modifier = Modifier.fillMaxWidth(),
                        shape = CardDefaults.shape
                    ) {
                        Text("Listar sensores", fontSize = 16.sp)
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
    TALLER9Theme {
        Greeting("Android")
    }
}