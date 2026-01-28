package ec.edu.uce.final_svacurio.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ec.edu.uce.final_svacurio.controller.ProductController
import ec.edu.uce.final_svacurio.data.Product
import ec.edu.uce.final_svacurio.R
import ec.edu.uce.final_svacurio.notification.NotificationHelper
import ec.edu.uce.final_svacurio.sync.SyncManager
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.sp

@Composable
fun ProductsListScreen(
    context: Context,
    onLogout: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (Product) -> Unit
) {
    val controller = remember { ProductController(context) }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var pendingCount by remember { mutableStateOf(0) }
    var networkAvailable by remember { mutableStateOf(false) }
    var isSyncing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val syncManager = remember { SyncManager(context) }
    val notifier = remember { NotificationHelper(context) }

    suspend fun load() {
        products = controller.getAll()
        pendingCount = syncManager.getPendingSyncCount()
        networkAvailable = syncManager.isNetworkAvailable()
    }

    LaunchedEffect(Unit) { load() }

    LaunchedEffect(pendingCount, networkAvailable) {
        if (pendingCount > 0 && networkAvailable) {
            isSyncing = true
            try {
                val result = syncManager.fullSync()
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                notifier.notifySync(result.productsSynced + result.usersSynced)
                load()
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                isSyncing = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("Productos")
                if (pendingCount > 0) {
                    Text(
                        "$pendingCount pendientes de sincronización",
                        color = Color(0xFFFF9800),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Text(
                    if (networkAvailable) "✓ Conectado" else "✗ Sin conexión",
                    color = if (networkAvailable) Color(0xFF4CAF50) else Color(0xFFF44336),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Button(onClick = onLogout) { Text("Cerrar sesión") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = onAdd) { Text("Agregar producto") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Imagen del producto
                        val imageBitmap: ImageBitmap? = remember(product.photo) {
                            product.photo?.let {
                                try {
                                    BitmapFactory.decodeByteArray(it, 0, it.size)?.asImageBitmap()
                                } catch (_: Exception) { null }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
                                .background(Color(0xFFE0E0E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (imageBitmap != null) {
                                Image(
                                    bitmap = imageBitmap,
                                    contentDescription = "Imagen del producto",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.logo_personal),
                                    contentDescription = "Sin imagen",
                                    modifier = Modifier.size(48.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(18.dp))
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = product.description,
                                    color = Color(0xFF222222),
                                    maxLines = 2,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                when (product.syncStatus) {
                                    "pending" -> Text("⏳", color = Color(0xFFFF9800))
                                    "synced" -> Text("✓", color = Color(0xFF4CAF50))
                                    "error" -> Text("✗", color = Color(0xFFF44336))
                                }
                            }
                            Text(
                                text = "Código: ${product.code}",
                                color = Color(0xFF666666),
                                style = TextStyle(fontSize = 13.sp)
                            )
                            Text(
                                text = "Costo: $${"%.2f".format(product.cost)}",
                                color = Color(0xFF666666),
                                style = TextStyle(fontSize = 13.sp)
                            )
                            Text(
                                text = if (product.available) "Disponible" else "No disponible",
                                color = if (product.available) Color(0xFF388E3C) else Color(0xFFD32F2F),
                                style = TextStyle(fontSize = 13.sp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = { onEdit(product) },
                            modifier = Modifier.height(36.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                        ) {
                            Text("Editar", style = TextStyle(fontSize = 14.sp))
                        }
                    }
                }
            }
        }
    }
}
