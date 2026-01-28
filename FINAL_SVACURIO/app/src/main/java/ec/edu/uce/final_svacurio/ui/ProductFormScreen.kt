package ec.edu.uce.final_svacurio.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ec.edu.uce.final_svacurio.controller.ProductController
import ec.edu.uce.final_svacurio.data.Product
import ec.edu.uce.final_svacurio.util.FileUtils
import androidx.compose.ui.graphics.asImageBitmap

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

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Código") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = cost, onValueChange = { cost = it }, label = { Text("Costo") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = { launcher.launch(null) }) { Text("Tomar foto") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onCancel) { Text("Cancelar") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        photoBitmap?.let { Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.size(120.dp)) }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
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
        }) { Text("Guardar") }
    }
}
