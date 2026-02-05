package ec.edu.uce.final_aetroyab.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ec.edu.uce.final_aetroyab.controller.ProductController
import ec.edu.uce.final_aetroyab.data.Product
import ec.edu.uce.final_aetroyab.util.FileUtils
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange

@Composable
fun ProductFormScreen(
    context: Context,
    productToEdit: Product? = null,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val controller = remember { ProductController(context) }
    var code by remember { mutableStateOf(productToEdit?.code ?: "") }
    var description by remember { mutableStateOf(productToEdit?.description ?: "") }
    var cost by remember { mutableStateOf(productToEdit?.cost?.toString() ?: "") }
    var available by remember { mutableStateOf(productToEdit?.available ?: true) }
    var manufactureDate by remember { mutableStateOf(productToEdit?.manufactureDate ?: System.currentTimeMillis()) }
    var photo by remember { mutableStateOf<ByteArray?>(productToEdit?.photo) }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(productToEdit?.photo?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }) }
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            photoBitmap = bitmap
            // Convertir a ByteArray
            val stream = java.io.ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            photo = stream.toByteArray()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Código") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = cost,
                    onValueChange = { cost = it },
                    label = { Text("Costo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    androidx.compose.material3.Checkbox(
                        checked = available,
                        onCheckedChange = { available = it }
                    )
                    Text("Disponible", modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Selector de fecha de fabricación en bloque separado
                val context = LocalContext.current
                val calendar = java.util.Calendar.getInstance().apply { timeInMillis = manufactureDate }
                val year = calendar.get(java.util.Calendar.YEAR)
                val month = calendar.get(java.util.Calendar.MONTH)
                val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
                var showDatePicker by remember { mutableStateOf(false) }
                Text(
                    "Fecha de Fabricación:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text(
                        java.text.SimpleDateFormat("dd/MM/yyyy").format(java.util.Date(manufactureDate)),
                        color = Color(0xFF757575),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    androidx.compose.material3.IconButton(onClick = { showDatePicker = true }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Cambiar fecha",
                            tint = Color(0xFF1976D2)
                        )
                    }
                }
                if (showDatePicker) {
                    android.app.DatePickerDialog(
                        context,
                        { _, y, m, d ->
                            val cal = java.util.Calendar.getInstance()
                            cal.set(y, m, d)
                            manufactureDate = cal.timeInMillis
                            showDatePicker = false
                        },
                        year, month, day
                    ).show()
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { launcher.launch(null) }) { Text("Tomar foto") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onCancel) { Text("Cancelar") }
                }
                Spacer(modifier = Modifier.height(16.dp))
                photoBitmap?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.size(140.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            val product = Product(
                                code = code,
                                description = description,
                                manufactureDate = manufactureDate,
                                cost = cost.toDoubleOrNull() ?: 0.0,
                                available = available,
                                photo = photo,
                                syncStatus = productToEdit?.syncStatus ?: "pending",
                                lastModified = System.currentTimeMillis(),
                                isDeleted = productToEdit?.isDeleted ?: false
                            )
                            if (productToEdit == null) controller.insert(product) else controller.update(product)
                            onSave()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Guardar") }
            }
        }
    }
}
