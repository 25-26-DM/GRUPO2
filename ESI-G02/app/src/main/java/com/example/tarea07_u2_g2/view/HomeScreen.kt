package com.example.tarea07_u2_g2.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarea07_u2_g2.controller.Controlador
import com.example.tarea07_u2_g2.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    nombreUsuario: String,
    onLogout: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (Producto) -> Unit
) {
    val listaProductos = Controlador.productos

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Lista Productos")
                        Text(
                            text = "Hola, $nombreUsuario",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    // Botón SALIR (Texto en vez de ícono)
                    TextButton(onClick = onLogout) {
                        Text("Salir", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            // Botón AGREGAR (+)
            FloatingActionButton(onClick = onAddProduct) {
                Text("+", fontSize = 24.sp)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaProductos) { producto ->
                ProductoCard(
                    producto = producto,
                    onEdit = { onEditProduct(producto) },
                    onDelete = { Controlador.eliminarProducto(producto) }
                )
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título: Descripción y Precio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = producto.descripcion,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${producto.precio}",
                    fontSize = 18.sp,
                    color = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Código: ${producto.codigo}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fecha: ${producto.fechaFabricacion}", style = MaterialTheme.typography.bodySmall)

            val textoDisp = if (producto.disponibilidad) "Disponible" else "Agotado"
            val colorDisp = if (producto.disponibilidad) Color.Blue else Color.Red
            Text(text = textoDisp, color = colorDisp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(8.dp))

            // Botones de acción (Texto simple)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onEdit) {
                    Text("Editar")
                }
                TextButton(onClick = onDelete) {
                    Text("Eliminar", color = Color.Red)
                }
            }
        }
    }
}
