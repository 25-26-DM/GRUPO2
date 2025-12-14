package com.example.tarea07_u2_g2.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tarea07_u2_g2.controller.Controlador
import com.example.tarea07_u2_g2.model.Producto

@Composable
fun ProductoFormScreen(
    productoAEditar: Producto?, // Puede ser nulo (si estamos creando uno nuevo)
    onSaveSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    // Si hay productoAEditar, rellenamos los campos, si no, vacíos.
    var codigo by remember { mutableStateOf(productoAEditar?.codigo ?: "") }
    var descripcion by remember { mutableStateOf(productoAEditar?.descripcion ?: "") }
    var precioStr by remember { mutableStateOf(productoAEditar?.precio?.toString() ?: "") }
    var fecha by remember { mutableStateOf(productoAEditar?.fechaFabricacion ?: "") }
    var disponible by remember { mutableStateOf(productoAEditar?.disponibilidad ?: true) }

    val context = LocalContext.current
    val esEdicion = productoAEditar != null
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (esEdicion) "EDITAR PRODUCTO" else "NUEVO PRODUCTO",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Formulario
        OutlinedTextField(
            value = codigo,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    codigo = newValue
                }
            },
            label = { Text("Código") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !esEdicion, // El código no se debería cambiar al editar
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = precioStr,
            onValueChange = { precioStr = it },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() })
        )

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(
            label = "Fecha de fabricación",
            selectedDate = fecha,
            onDateSelected = { fecha = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Switch para disponibilidad
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Disponible: ")
            Switch(checked = disponible, onCheckedChange = { disponible = it })
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botones
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    // Validar y Guardar
                    val precio = precioStr.toDoubleOrNull()
                    if (codigo.isNotEmpty() && descripcion.isNotEmpty() && precio != null && fecha.isNotEmpty()) {
                        val nuevoProd = Producto(codigo, descripcion, precio, fecha, disponible)

                        if (esEdicion) {
                            Controlador.editarProducto(nuevoProd)
                            Toast.makeText(context, "Producto editado", Toast.LENGTH_SHORT).show()
                        } else {
                            Controlador.agregarProducto(nuevoProd)
                            Toast.makeText(context, "Producto creado", Toast.LENGTH_SHORT).show()
                        }
                        onSaveSuccess()
                    } else {
                        Toast.makeText(context, "Revise los datos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("GUARDAR")
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("CANCELAR")
            }
        }
    }
}