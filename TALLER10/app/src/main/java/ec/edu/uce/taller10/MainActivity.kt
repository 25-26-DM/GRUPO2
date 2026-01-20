package ec.edu.uce.taller10

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import ec.edu.uce.taller10.ui.theme.TALLER10Theme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.view.PreviewView
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.video.Recording
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.VideoRecordEvent
import androidx.camera.video.QualitySelector
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.Quality

// Importar los composables y utilidades existentes
import ec.edu.uce.taller10.ui.LoginScreen
import ec.edu.uce.taller10.ui.RegisterScreen
import ec.edu.uce.taller10.ui.hashPassword
import ec.edu.uce.taller10.ui.PhotoScreen
import ec.edu.uce.taller10.ui.VideoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TALLER10Theme {
                // Estado de pantalla: "login", "register", "camera", "photo", "video"
                var screen by rememberSaveable { mutableStateOf("login") }
                var photoUri by remember { mutableStateOf<Uri?>(null) }
                var videoUri by remember { mutableStateOf<Uri?>(null) }
                val context = LocalContext.current
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (screen) {
                        "login" -> {
                            // Usar LoginScreen existente
                            LoginScreen(
                                context = context,
                                onLoginSuccess = { screen = "camera" },
                                showError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_LONG).show() },
                                hashPassword = { pwd -> hashPassword(pwd) },
                                onNavigateToRegister = { screen = "register" }
                            )
                        }
                        "register" -> {
                            RegisterScreen(
                                context = context,
                                onRegisterSuccess = { screen = "camera" },
                                showError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_LONG).show() }
                            )
                        }
                        "camera" -> {
                            CameraScreen(
                                modifier = Modifier.padding(innerPadding),
                                onPhotoTaken = { uri ->
                                    photoUri = uri
                                    screen = "photo"
                                },
                                onVideoTaken = { uri ->
                                    videoUri = uri
                                    screen = "video"
                                }
                            )
                        }
                        "photo" -> {
                            photoUri?.let { uri ->
                                PhotoScreen(uri = uri, onBack = {
                                    // volver a la cámara
                                    screen = "camera"
                                    photoUri = null
                                })
                            }
                        }
                        "video" -> {
                            videoUri?.let { uri ->
                                VideoScreen(uri = uri, onBack = {
                                    // volver a la cámara
                                    screen = "camera"
                                    videoUri = null
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    onPhotoTaken: (Uri) -> Unit = {},
    onVideoTaken: (Uri) -> Unit = {}
) {
    val context = LocalContext.current

    // Pedimos permisos CAMERA + RECORD_AUDIO (para video)
    val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    var permissionsGranted by remember {
        mutableStateOf(
            requiredPermissions.all { perm ->
                ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsGranted = perms.values.all { it }
            if (permissionsGranted) {
                Toast.makeText(context, "Permisos concedidos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permisos denegados: la cámara no funcionará", Toast.LENGTH_LONG).show()
            }
        }
    )

    // Estado para ImageCapture
    val imageCapture = remember { ImageCapture.Builder().build() }
    // Estado para ImageAnalysis
    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }
    // Estado para VideoCapture
    val videoCapture = remember {
        VideoCapture.withOutput(
            Recorder.Builder()
                .setQualitySelector(
                    QualitySelector.from(
                        Quality.HIGHEST,
                        FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
                    )
                )
                .build()
        )
    }
    var recording by remember { mutableStateOf<Recording?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    // Ejemplo de análisis: contar fotogramas
    var frameCount by remember { mutableStateOf(0) }
    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { image ->
        frameCount++
        image.close()
    }

    LaunchedEffect(true) {
        if (!permissionsGranted) permissionLauncher.launch(requiredPermissions)
    }

    if (permissionsGranted) {
        // UI con estilo similar a LoginScreen
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color(0xFFF5F6FA)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Cámara",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1976D2),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Vista previa y captura",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Preview dentro de un contenedor con altura fija
                        CameraPreview(modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(bottom = 12.dp), imageCapture = imageCapture, imageAnalysis = imageAnalysis, videoCapture = videoCapture)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(bottom = 8.dp)) {
                            IconButton(onClick = {
                                // Tomar foto y guardarla en MediaStore
                                val filename = "photo_${System.currentTimeMillis()}"
                                val contentValues = ContentValues().apply {
                                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Taller10")
                                }
                                val resolver = context.contentResolver
                                val imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                val outputOptions = ImageCapture.OutputFileOptions.Builder(resolver, imageCollection, contentValues).build()

                                imageCapture.takePicture(
                                    outputOptions,
                                    ContextCompat.getMainExecutor(context),
                                    object : ImageCapture.OnImageSavedCallback {
                                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                            // Obtener el Uri guardado y notificar
                                            output.savedUri?.let { uri ->
                                                Toast.makeText(context, "Foto guardada: $uri", Toast.LENGTH_SHORT).show()
                                                onPhotoTaken(uri)
                                            } ?: run {
                                                Toast.makeText(context, "Foto guardada", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onError(exc: ImageCaptureException) {
                                            Toast.makeText(context, "Error al guardar foto: ${exc.message}", Toast.LENGTH_LONG).show()
                                            Log.e("CameraScreen", "Error al guardar foto", exc)
                                        }
                                    }
                                )
                            }) {
                                Icon(Icons.Filled.Camera, contentDescription = "Tomar foto")
                            }
                            IconButton(onClick = {
                                if (!isRecording) {
                                    // Iniciar grabación de video
                                    val filename = "video_${System.currentTimeMillis()}"
                                    val contentValues = ContentValues().apply {
                                        put(MediaStore.Video.Media.DISPLAY_NAME, filename)
                                        put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                                        put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Taller10")
                                    }
                                    val mediaStoreOutput = MediaStoreOutputOptions.Builder(
                                        context.contentResolver,
                                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                    ).setContentValues(contentValues).build()
                                    val pendingRecording = videoCapture.output.prepareRecording(context, mediaStoreOutput)
                                    val activeRecording = pendingRecording.start(ContextCompat.getMainExecutor(context)) { event ->
                                        when (event) {
                                            is VideoRecordEvent.Start -> {
                                                isRecording = true
                                                Toast.makeText(context, "Grabando video...", Toast.LENGTH_SHORT).show()
                                            }
                                            is VideoRecordEvent.Finalize -> {
                                                isRecording = false
                                                recording = null
                                                if (event.outputResults.outputUri != Uri.EMPTY) {
                                                    Toast.makeText(context, "Video guardado: ${event.outputResults.outputUri}", Toast.LENGTH_SHORT).show()
                                                    onVideoTaken(event.outputResults.outputUri)
                                                } else {
                                                    Toast.makeText(context, "Error al guardar video", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        }
                                    }
                                    recording = activeRecording
                                } else {
                                    // Detener grabación
                                    recording?.stop()
                                }
                            }) {
                                Icon(
                                    if (!isRecording) Icons.Filled.Videocam else Icons.Filled.Videocam,
                                    contentDescription = if (!isRecording) "Grabar video" else "Detener grabación",
                                    tint = if (!isRecording) Color.Unspecified else Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        // Mostrar explicación y botón para pedir permisos o abrir ajustes
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Se requieren permisos de Cámara y Micrófono para continuar")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { permissionLauncher.launch(requiredPermissions) }) {
                Text("Solicitar permisos")
            }
            Spacer(modifier = Modifier.height(8.dp))
            val activity = context as? Activity
            Button(onClick = {
                // Abrir ajustes de la app
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", activity?.packageName ?: "", null)
                }
                activity?.startActivity(intent)
            }) {
                Text("Abrir ajustes")
            }
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    imageCapture: ImageCapture,
    imageAnalysis: ImageAnalysis,
    videoCapture: VideoCapture<Recorder>? = null
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    val useCases = mutableListOf(
                        preview,
                        imageCapture,
                        imageAnalysis
                    )
                    videoCapture?.let { useCases.add(it) }
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        *useCases.toTypedArray()
                    )
                } catch (e: Exception) {
                    Log.e("CameraPreview", "Error al iniciar la cámara", e)
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        }
    )
}
