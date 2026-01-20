package ec.edu.uce.taller10.ui

import android.content.Intent
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoScreen(uri: Uri, onBack: () -> Unit) {
    // Mantener referencia al VideoView fuera del composable para poder limpiarlo
    val videoViewRef = remember { mutableListOf<VideoView>() }
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F6FA)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = CardDefaults.shape
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Video grabado",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                            .padding(8.dp)
                            .clip(CardDefaults.shape)
                            .border(2.dp, Color(0xFF1976D2), CardDefaults.shape),
                        factory = { ctx ->
                            VideoView(ctx).apply {
                                setBackgroundColor(0xFFF5F6FA.toInt())

                                // Crear y configurar MediaController
                                val mediaController = MediaController(ctx)
                                mediaController.setAnchorView(this)
                                setMediaController(mediaController)

                                // Configurar listeners
                                setOnPreparedListener { mp ->
                                    mp.isLooping = false
                                    android.util.Log.d("VideoScreen", "Video preparado y listo")
                                    // Iniciar reproducción automáticamente
                                    start()
                                }

                                setOnErrorListener { _, what, extra ->
                                    android.util.Log.e("VideoScreen", "Error: what=$what, extra=$extra")
                                    true
                                }

                                setOnCompletionListener {
                                    android.util.Log.d("VideoScreen", "Video completado")
                                }

                                // Cargar el video
                                setVideoURI(uri)
                                requestFocus()

                                // Guardar referencia
                                videoViewRef.clear()
                                videoViewRef.add(this)
                            }
                        },
                        update = { view ->
                            // Recargar si el URI cambia
                            if (view.isPlaying) {
                                view.stopPlayback()
                            }
                            view.setVideoURI(uri)
                            view.start()
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Usa los controles del reproductor para pausar, adelantar o reproducir",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Botón para abrir el video en la galería/explorador
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, "video/*")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            try {
                                context.startActivity(Intent.createChooser(intent, "Abrir video con"))
                            } catch (e: Exception) {
                                android.util.Log.e("VideoScreen", "Error al abrir video: ${e.message}")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = CardDefaults.shape,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF1976D2)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Folder,
                            contentDescription = "Abrir en galería",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Abrir en galería", fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            // Detener el video antes de salir
                            videoViewRef.firstOrNull()?.let {
                                if (it.isPlaying) {
                                    it.stopPlayback()
                                }
                            }
                            onBack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = CardDefaults.shape
                    ) {
                        Text("Volver a la cámara", fontSize = 16.sp)
                    }
                }
            }
        }
    }

    // Limpiar recursos cuando se desmonte
    DisposableEffect(Unit) {
        onDispose {
            videoViewRef.firstOrNull()?.let {
                if (it.isPlaying) {
                    it.stopPlayback()
                }
                it.suspend()
            }
            android.util.Log.d("VideoScreen", "VideoView limpiado")
        }
    }
}

