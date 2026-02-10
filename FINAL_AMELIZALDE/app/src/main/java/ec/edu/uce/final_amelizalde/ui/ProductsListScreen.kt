package ec.edu.uce.final_erenriquezp.ui

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
import ec.edu.uce.final_erenriquezp.controller.ProductController
import ec.edu.uce.final_erenriquezp.data.Product
import ec.edu.uce.final_erenriquezp.R
import ec.edu.uce.final_erenriquezp.notification.NotificationHelper
import ec.edu.uce.final_erenriquezp.sync.SyncManager
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.draw.alpha
import ec.edu.uce.final_erenriquezp.service.MsgDropService

@Composable
fun ProductsListScreen(
    context: Context,
    onLogout: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (Product) -> Unit,
    onDelete: (Product) -> Unit = {}
) {
    val controller = remember { ProductController(context) }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var pendingCount by remember { mutableStateOf(0) }
    var networkAvailable by remember { mutableStateOf(false) }
    var isSyncing by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<Product?>(null) }
    val scope = rememberCoroutineScope()
    val syncManager = remember { SyncManager(context) }
    val notifier = remember { NotificationHelper(context) }

    // Función para manejar edición con validación
    fun handleEdit(product: Product) {
        if (!controller.canEdit(product)) {
            scope.launch {
                val message = MsgDropService.getActionMessage("edit")
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        } else {
            onEdit(product)
        }
    }

    // Función para manejar eliminación con validación
    fun handleDelete(product: Product) {
        if (!controller.canDelete(product)) {
            scope.launch {
                val message = MsgDropService.getActionMessage("delete")
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        } else {
            showDeleteDialog = product
        }
    }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
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
            androidx.compose.material3.IconButton(
                onClick = onLogout,
                modifier = Modifier.size(36.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Logout,
                    contentDescription = "Cerrar sesión",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(28.dp)
                )
            }
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
                // Indicador visual: productos no disponibles se ven atenuados
                val cardAlpha = if (product.available) 1f else 0.6f
                val cardBackground = if (product.available) Color(0xFFF8F8F8) else Color(0xFFE0E0E0)
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .alpha(cardAlpha),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackground)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val imageBitmap: ImageBitmap? = remember(product.photo) {
                                product.photo?.let {
                                    try {
                                        BitmapFactory.decodeByteArray(it, 0, it.size)?.asImageBitmap()
                                    } catch (_: Exception) { null }
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE0E0E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (imageBitmap != null) {
                                    Image(
                                        bitmap = imageBitmap,
                                        contentDescription = "Foto del producto",
                                        modifier = Modifier.size(56.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text("Sin foto", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    product.description,
                                    fontSize = 16.sp,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = Color(0xFF222222),
                                    maxLines = 2
                                )
                                Text(
                                    "Código: ${product.code}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF757575),
                                    maxLines = 1
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            // Botones de acción: editar y eliminar
                            Row {
                                androidx.compose.material3.IconButton(
                                    onClick = { handleEdit(product) },
                                    modifier = Modifier.size(32.dp),
                                    enabled = product.available
                                ) {
                                    androidx.compose.material3.Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar producto",
                                        tint = if (product.available) Color(0xFF1976D2) else Color(0xFF9E9E9E)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                androidx.compose.material3.IconButton(
                                    onClick = { handleDelete(product) },
                                    modifier = Modifier.size(32.dp),
                                    enabled = product.available
                                ) {
                                    androidx.compose.material3.Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar producto",
                                        tint = if (product.available) Color(0xFFD32F2F) else Color(0xFF9E9E9E)
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Costo:", fontSize = 12.sp, color = Color(0xFF757575))
                            Text("$${product.cost}", fontSize = 12.sp, color = Color(0xFF388E3C), fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Fecha fabricación:", fontSize = 12.sp, color = Color(0xFF757575))
                            Text(
                                java.text.SimpleDateFormat("dd/MM/yyyy").format(java.util.Date(product.manufactureDate)),
                                fontSize = 12.sp,
                                color = Color(0xFF616161)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Disponibilidad:", fontSize = 12.sp, color = Color(0xFF757575))
                            val dispText = if (product.available) "Disponible" else "Agotado"
                            val dispColor = if (product.available) Color(0xFF1976D2) else Color(0xFFD32F2F)
                            Text(dispText, fontSize = 12.sp, color = dispColor, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar
    showDeleteDialog?.let { product ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Está seguro que desea eliminar el producto '${product.description}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            controller.delete(product)
                            showDeleteDialog = null
                            load()
                            Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
