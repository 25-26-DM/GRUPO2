# Plan: Control de edición/eliminación por disponibilidad

**Estado: ✅ IMPLEMENTADO**

Este plan implementa validaciones para impedir editar o eliminar productos con available = false (disponibilidad 0). Cuando se intente la acción, se consultará un servicio local MsgDropService que retornará el mensaje "imposible editar/eliminar Producto". Se agregó el botón de eliminación en la lista de productos que anteriormente no existía.

Decisiones clave tomadas:

Disponibilidad 0: Interpretado como available = false según el modelo Boolean existente
Servicio msgdrop: Será una clase Kotlin simulada localmente (sin endpoint real)
Ubicación del botón: Se agregará icono de eliminar junto al de edición en cada item de ProductsListScreen.kt
Arquitectura del servicio: Seguirá el patrón singleton similar a NetworkUtils y FileUtils
Steps

Crear servicio MsgDropService

Crear nuevo archivo app/src/main/java/ec/edu/uce/final_amelizalde/service/MsgDropService.kt
Implementar como object (singleton) con función suspend fun getActionMessage(action: String): String
Retornar mensajes según el tipo de acción: "imposible editar Producto" o "imposible eliminar Producto"
Simular latencia de red con delay(300) para realismo
Opcionalmente: permitir configuración de mensajes para futura integración con API real
Agregar validación de disponibilidad en ProductController

En ProductController.kt, crear funciones:
suspend fun canEdit(product: Product): Boolean - retorna false si available == false
suspend fun canDelete(product: Product): Boolean - retorna false si available == false
Estas funciones encapsulan la lógica de negocio y pueden extenderse con más reglas futuras
Modificar ProductsListScreen para agregar botón de eliminar

En ProductsListScreen.kt, dentro del Card de cada producto:
Cambiar el IconButton de edición a un Row con dos botones
Agregar IconButton con Icons.Default.Delete junto al existente Icons.Default.Edit
Agregar callback onDelete: (Product) -> Unit a los parámetros del composable
Conectar el botón al callback
Implementar lógica de validación en ProductsListScreen

Agregar función suspend fun handleEdit(product: Product) en el scope del composable que:
Verifica controller.canEdit(product)
Si retorna false: llama a MsgDropService.getActionMessage("edit") y muestra Toast con el mensaje
Si retorna true: ejecuta onEdit(product) normalmente
Agregar función suspend fun handleDelete(product: Product) de forma similar:
Verifica controller.canDelete(product)
Si retorna false: muestra mensaje del servicio
Si retorna true: muestra AlertDialog de confirmación y ejecuta controller.delete(product)
Actualizar MainActivity para conectar el nuevo flujo

En MainActivity.kt, en la navegación de ProductsListScreen:
Agregar el callback onDelete que recibe un Product
Implementar dentro de una coroutine que llame a handleDelete con validación
Actualizar la lista después de la eliminación exitosa
Agregar validación preventiva en ProductFormScreen (opcional pero recomendado)

En ProductFormScreen.kt:
Al iniciar en modo edición, verificar controller.canEdit(productToEdit!!)
Si no puede editar, mostrar mensaje y deshabilitar el formulario (campos read-only)
Esto previene que usuarios accedan al formulario por otras vías (deep links, back stack, etc.)
Agregar indicador visual de productos no editables

En ProductsListScreen.kt:
Modificar el Card para cambiar su apariencia cuando available == false
Aplicar opacidad reducida (.alpha(0.6f)) y fondo grisáceo
Deshabilitar visualmente los botones de editar/eliminar (iconos en gris, sin funcionalidad click)
Esto proporciona feedback visual inmediato del estado del producto
Verification

Prueba 1 - Edición bloqueada:

Cambiar un producto a available = false mediante el checkbox
Intentar editarlo desde la lista
Verificar que aparece Toast con "imposible editar Producto"
Prueba 2 - Eliminación bloqueada:

Con un producto available = false, presionar botón de eliminar
Verificar mensaje "imposible eliminar Producto"
Prueba 3 - Productos disponibles:

Con producto available = true, editar y eliminar debe funcionar normalmente
El AlertDialog de confirmación debe aparecer antes de eliminar
Prueba 4 - Visual feedback:

Verificar que productos no disponibles se ven atenuados
Confirmar que botones están deshabilitados visualmente
Decisions

Servicio simulado vs real: Se optó por simular el servicio localmente para no depender de infraestructura externa. La implementación permite fácil migración a endpoint real mediante cambio de la función a una llamada HTTP con Ktor
Validación en Controller: Se centralizó la lógica en ProductController en lugar de la UI, siguiendo el principio de separación de responsabilidades. La UI solo consume las reglas de negocio
Feedback visual proactivo: Además de bloquear acciones, se deshabilitan visualmente los controles para reducir frustración del usuario al intentar acciones inválidas