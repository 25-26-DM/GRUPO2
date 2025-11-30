App de Postres
=================================

Esta aplicación Android permite a los usuarios realizar pedidos de postres de manera intuitiva, siguiendo un flujo paso a paso que guía desde la selección de cantidad hasta la confirmación final del pedido. Desarrollada con Jetpack Compose y siguiendo el patrón MVVM (Model-View-ViewModel), la app ofrece una experiencia moderna y fluida.

## Índice
1. [Características Principales](#características-principales)
2. [Arquitectura de la Aplicación](#arquitectura-de-la-aplicación)
3. [Funcionamiento Detallado](#funcionamiento-detallado)
4. [Estructura del Proyecto](#estructura-del-proyecto)
5. [Requisitos Previos](#requisitos-previos)
6. [Instalación y Configuración](#instalación-y-configuración)

---

## Características Principales

### 🛒 Flujo de Pedido Multi-Paso
La aplicación implementa un proceso de pedido en cuatro pantallas secuenciales:
1. **Pantalla de Inicio**: Selección de cantidad
2. **Pantalla de Artículos**: Elección del tipo de postre
3. **Pantalla de Fecha**: Selección de fecha de recogida
4. **Pantalla de Resumen**: Confirmación y envío del pedido

### 🍰 Catálogo de Postres
Los usuarios pueden elegir entre tres tipos de postres, cada uno con precio diferente:
- **Cupcake**: $2.00 USD
- **Coffee**: $1.50 USD
- **Cake Pop**: $2.50 USD

### 📊 Sistema de Cantidades
Opciones predefinidas de cantidad:
- 1 unidad
- 6 unidades
- 12 unidades

### 💰 Cálculo de Precio Dinámico
El sistema calcula automáticamente el precio total considerando:
- **Cantidad de artículos** seleccionados
- **Tipo de postre** elegido (precio unitario variable)
- **Fecha de recogida** (cargo adicional de $3.00 USD para entrega el mismo día)

**Fórmula de cálculo:**
```
Precio Total = (Cantidad × Precio Unitario) + Cargo Mismo Día (si aplica)
```

### 🎨 Interfaz Moderna con Material Design 3
- **Tema personalizado**: Esquema de colores azul (#0000FF) y blanco (#FFFFFF)
- **Modo claro y oscuro**: Soporte completo para ambos temas
- **Componentes Material 3**: Botones, radio buttons, dividers y AppBar modernos
- **Navegación fluida**: Integración con Jetpack Navigation Component

### 🌍 Soporte Multilingüe
La aplicación está completamente traducida a tres idiomas:
- **Español (es)**: Idioma por defecto
- **Inglés (en)**: Traducción completa
- **Francés (fr)**: Traducción completa

El cambio de idioma se realiza automáticamente según la configuración del dispositivo.

### 📧 Envío de Pedidos
Funcionalidad de compartir pedido mediante Intent implícito:
- **Destinatario predefinido**: amelizalde@uce.edu.ec
- **Formatos soportados**: Email, WhatsApp, Telegram, etc.
- **Contenido del pedido**: Cantidad, artículo, fecha y precio total

---

## Arquitectura de la Aplicación

### Patrón MVVM (Model-View-ViewModel)

```
┌─────────────────────────────────────────────────────────┐
│                         VIEW                            │
│  (Composable Functions - UI Layer)                      │
│  - StartOrderScreen                                     │
│  - SelectOptionScreen                                   │
│  - OrderSummaryScreen                                   │
└────────────────┬────────────────────────────────────────┘
                 │
                 │ Observa StateFlow
                 │
┌────────────────▼────────────────────────────────────────┐
│                      VIEWMODEL                          │
│  OrderViewModel                                         │
│  - Gestiona el estado de la UI                          │
│  - Lógica de negocio (cálculo de precios)               │
│  - Manejo de eventos del usuario                        │
└────────────────┬────────────────────────────────────────┘
                 │
                 │ Accede a datos
                 │
┌────────────────▼────────────────────────────────────────┐
│                        MODEL                            │
│  - OrderUiState (data class)                            │
│  - DataSource (opciones estáticas)                      │
└─────────────────────────────────────────────────────────┘
```

### Componentes Clave

#### 1. **MainActivity.kt**
- **Función**: Punto de entrada de la aplicación
- **Responsabilidad**: 
  - Inicializa el tema `CupcakeTheme`
  - Configura edge-to-edge display
  - Lanza el composable principal `CupcakeApp()`

#### 2. **CupcakeScreen.kt** (Navegación)
- **Función**: Controlador de navegación y estructura general
- **Componentes**:
  - **Enum `CupcakeScreen`**: Define las 4 rutas de navegación
    ```kotlin
    Start    → Pantalla inicial
    Item     → Selección de artículo
    Pickup   → Selección de fecha
    Summary  → Resumen del pedido
    ```
  - **`CupcakeAppBar`**: Barra superior con título dinámico y botón "Atrás"
  - **`CupcakeApp`**: Composable principal que configura:
    - NavHost para navegación entre pantallas
    - Scaffold con TopAppBar
    - Scroll vertical automático
    - Gestión del estado con ViewModel

#### 3. **OrderViewModel.kt** (Lógica de Negocio)
- **Función**: Gestión del estado del pedido y cálculo de precios
- **StateFlow**: 
  ```kotlin
  private val _uiState = MutableStateFlow(OrderUiState())
  val uiState: StateFlow<OrderUiState>
  ```
- **Métodos principales**:
  - `setQuantity(Int)`: Actualiza cantidad y recalcula precio
  - `setItem(String)`: Actualiza artículo seleccionado y recalcula precio
  - `setDate(String)`: Actualiza fecha y aplica cargo por mismo día si aplica
  - `resetOrder()`: Reinicia el estado a valores por defecto
  - `calculatePrice()`: Función privada que implementa la lógica de precios
  - `pickupOptions()`: Genera lista de 4 fechas disponibles (hoy + 3 días)

**Lógica de Precio por Mismo Día:**
```kotlin
if (pickupOptions()[0] == pickupDate) {
    calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP // +$3.00
}
```

#### 4. **OrderUiState.kt** (Modelo de Datos)
- **Función**: Data class que representa el estado completo del pedido
- **Propiedades**:
  ```kotlin
  data class OrderUiState(
      val quantity: Int = 0,           // Cantidad seleccionada
      val item: String = "",            // Tipo de postre
      val date: String = "",            // Fecha de recogida
      val price: String = "",           // Precio formateado
      val pickupOptions: List<String>   // Fechas disponibles
  )
  ```

#### 5. **DataSource.kt** (Fuente de Datos Estática)
- **Función**: Proporciona datos predefinidos
- **Contenido**:
  ```kotlin
  object DataSource {
      // Pares de (String Resource ID, Cantidad)
      val quantityOptions = listOf(
          Pair(R.string.one_cupcake, 1),
          Pair(R.string.six_cupcakes, 6),
          Pair(R.string.twelve_cupcakes, 12)
      )
      
      // Lista de IDs de recursos para tipos de postres
      val itemOptions = listOf(
          R.string.cupcake,   // "Cupcake"
          R.string.coffee,    // "Coffee"
          R.string.cake_pop   // "Cake Pop"
      )
  }
  ```

---

## Funcionamiento Detallado

### 🎯 Pantalla 1: Inicio del Pedido (StartOrderScreen)

**Archivo**: `StartOrderScreen.kt`

**Composables**:
- `StartOrderScreen`: Pantalla principal de inicio
- `SelectOptionButton`: Componente reutilizable de botón

**Funcionalidad**:
1. Muestra imagen decorativa del cupcake (300dp de ancho)
2. Presenta título "Order Desserts" / "Pedir Postres"
3. Renderiza tres botones dinámicamente desde `DataSource.quantityOptions`:
   - "One Cupcake" / "Un Cupcake"
   - "Six Cupcakes" / "Seis Cupcakes"
   - "Twelve Cupcakes" / "Doce Cupcakes"

**Flujo de Interacción**:
```
Usuario toca botón → onNextButtonClicked(cantidad) 
                   → viewModel.setQuantity(it)
                   → Navegación a pantalla Item
```

**Código clave**:
```kotlin
quantityOptions.forEach { item ->
    SelectOptionButton(
        labelText = stringResource(item.first),
        onClick = { onNextButtonClicked(item.second) }
    )
}
```

---

### 🍰 Pantalla 2: Selección de Artículo (SelectOptionScreen - Items)

**Archivo**: `SelectOptionScreen.kt`

**Funcionalidad**:
1. Muestra lista de radio buttons con los tres tipos de postres
2. Permite seleccionar **solo uno** mediante `rememberSaveable`
3. Muestra subtotal calculado dinámicamente
4. Botones de navegación:
   - **Cancel**: Cancela pedido y vuelve a inicio
   - **Next**: Solo habilitado cuando hay selección

**Componentes**:
- `RadioButton`: Selección exclusiva de opciones
- `Divider`: Separador visual
- `FormattedPriceLabel`: Componente que muestra "Subtotal $X.XX"

**Estado Local**:
```kotlin
var selectedValue by rememberSaveable { mutableStateOf("") }
```

**Validación**:
```kotlin
Button(
    enabled = selectedValue.isNotEmpty(), // Botón deshabilitado si no hay selección
    onClick = onNextButtonClicked
)
```

**Flujo de Interacción**:
```
Usuario selecciona radio button → onSelectionChanged(item)
                                → viewModel.setItem(it)
                                → Actualiza precio en tiempo real
Usuario toca "Next"             → Navegación a pantalla Pickup
```

---

### 📅 Pantalla 3: Selección de Fecha (SelectOptionScreen - Pickup)

**Archivo**: `SelectOptionScreen.kt` (mismo componente reutilizado)

**Funcionalidad**:
1. Reutiliza `SelectOptionScreen` con diferentes datos
2. Muestra 4 fechas disponibles generadas dinámicamente:
   - Formato: "E MMM d" (ejemplo: "Fri Nov 29")
   - Fecha actual + 3 días siguientes
3. Aplica cargo adicional si se selecciona la primera opción (hoy)

**Generación de Fechas**:
```kotlin
private fun pickupOptions(): List<String> {
    val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
    val calendar = Calendar.getInstance()
    repeat(4) {
        dateOptions.add(formatter.format(calendar.time))
        calendar.add(Calendar.DATE, 1)
    }
}
```

**Lógica de Cargo Extra**:
```kotlin
// En OrderViewModel
if (pickupOptions()[0] == pickupDate) {
    calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP // +$3.00
}
```

**Flujo de Interacción**:
```
Usuario selecciona fecha → onSelectionChanged(date)
                        → viewModel.setDate(it)
                        → Recalcula precio con cargo si es mismo día
Usuario toca "Next"     → Navegación a pantalla Summary
```

---

### 📋 Pantalla 4: Resumen del Pedido (OrderSummaryScreen)

**Archivo**: `SummaryScreen.kt`

**Funcionalidad**:
1. Muestra resumen completo del pedido en tres secciones:
   - **QUANTITY**: Cantidad con pluralización correcta
   - **ITEM**: Tipo de postre seleccionado
   - **PICKUP DATE**: Fecha de recogida formateada
2. Muestra precio total final
3. Botones de acción:
   - **Send Order to Another App**: Comparte pedido
   - **Cancel**: Cancela y vuelve al inicio

**Pluralización Inteligente**:
```kotlin
val numberOfCupcakes = resources.getQuantityString(
    R.plurals.cupcakes,
    orderUiState.quantity,
    orderUiState.quantity
)
// Resultado: "1 cupcake" o "6 cupcakes"
```

**Formato del Mensaje**:
```kotlin
val orderSummary = stringResource(
    R.string.order_details,
    numberOfCupcakes,    // %1$s
    orderUiState.item,   // %2$s
    orderUiState.date,   // %3$s
    orderUiState.price   // %4$s
)
```

**Funcionalidad de Envío**:
```kotlin
Button(onClick = { 
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_EMAIL, arrayOf("amelizalde@uce.edu.ec"))
        putExtra(Intent.EXTRA_SUBJECT, newOrder)
        putExtra(Intent.EXTRA_TEXT, orderSummary)
    }
    context.startActivity(Intent.createChooser(intent, "Send"))
})
```

**Intent Implícito**:
- **ACTION_SEND**: Permite al usuario elegir la app (Gmail, WhatsApp, etc.)
- **EXTRA_EMAIL**: Preestablece destinatario
- **EXTRA_SUBJECT**: Asunto del mensaje
- **EXTRA_TEXT**: Cuerpo con detalles del pedido

---

### 🎨 Sistema de Temas (Theme.kt y Color.kt)

**Archivos**: `ui/theme/Theme.kt`, `ui/theme/Color.kt`

**Paleta de Colores Personalizada**:
```kotlin
val Blue = Color(0xFF0000FF)  // Azul primario puro
val White = Color(0xFFFFFFFF) // Blanco para contraste
```

**Aplicación del Tema**:
- **Modo Claro**: Fondo blanco, elementos azules
- **Modo Oscuro**: Fondo blanco (personalizado), elementos azules
- **Material Design 3**: Esquema completo con 30+ variaciones de color
- **Dynamic Color**: Deshabilitado para mantener esquema personalizado

**Características Visuales**:
- TopAppBar con `primaryContainer` (azul)
- Botones con color primario azul
- Texto en color blanco sobre azul para contraste
- Divisores y elementos secundarios en tonos neutros

---

### 🌐 Internacionalización (i18n)

**Archivos de Recursos**:
- `res/values/strings.xml` (inglés - por defecto)
- `res/values-es/strings.xml` (español)
- `res/values-fr/strings.xml` (francés)

**Características**:
1. **Strings parametrizados**:
   ```xml
   <string name="subtotal_price">Subtotal %s</string>
   <string name="order_details">Quantity: %1$s \nItem: %2$s...</string>
   ```

2. **Plurales**:
   ```xml
   <plurals name="cupcakes">
       <item quantity="one">%d cupcake</item>
       <item quantity="other">%d cupcakes</item>
   </plurals>
   ```

3. **Detección automática**: La app usa `Locale.getDefault()` para fechas y formato de moneda

**Ejemplo de Formato de Moneda**:
```kotlin
val formattedPrice = NumberFormat.getCurrencyInstance().format(calculatedPrice)
// Resultado: "$12.00" (US), "12,00 €" (Francia), "$12.00" (México)
```

---

### 🔄 Gestión de Navegación

**Implementación con Jetpack Navigation**:

```kotlin
NavHost(
    navController = navController,
    startDestination = CupcakeScreen.Start.name
) {
    composable(route = CupcakeScreen.Start.name) { /* StartOrderScreen */ }
    composable(route = CupcakeScreen.Item.name) { /* SelectOptionScreen */ }
    composable(route = CupcakeScreen.Pickup.name) { /* SelectOptionScreen */ }
    composable(route = CupcakeScreen.Summary.name) { /* OrderSummaryScreen */ }
}
```

**Funciones de Navegación**:
1. **Hacia adelante**: `navController.navigate(route)`
2. **Hacia atrás**: `navController.navigateUp()`
3. **Cancelar pedido**:
   ```kotlin
   private fun cancelOrderAndNavigateToStart(
       viewModel: OrderViewModel,
       navController: NavHostController
   ) {
       viewModel.resetOrder()
       navController.popBackStack(CupcakeScreen.Start.name, inclusive = false)
   }
   ```

**Back Stack Management**:
- El botón "Cancel" limpia el stack hasta la pantalla de inicio
- El botón "Atrás" del sistema navega normalmente
- AppBar muestra flecha solo si hay pantallas anteriores

---

### 🧩 Componentes Reutilizables

**Archivo**: `ui/components/CommonUi.kt`

**FormattedPriceLabel**:
```kotlin
@Composable
fun FormattedPriceLabel(subtotal: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.subtotal_price, subtotal),
        style = MaterialTheme.typography.headlineSmall
    )
}
```

**Propósito**: 
- Centraliza formato de precios
- Consistencia visual en toda la app
- Facilita cambios futuros

---

## Estructura del Proyecto

```
app/src/main/
├── java/com/example/cupcake/
│   ├── MainActivity.kt                    # Actividad principal
│   ├── CupcakeScreen.kt                   # Navegación y estructura
│   │
│   ├── data/
│   │   ├── DataSource.kt                  # Datos estáticos
│   │   └── OrderUiState.kt                # Modelo de estado
│   │
│   └── ui/
│       ├── OrderViewModel.kt              # Lógica de negocio
│       ├── StartOrderScreen.kt            # Pantalla de inicio
│       ├── SelectOptionScreen.kt          # Pantallas de selección
│       ├── SummaryScreen.kt               # Pantalla de resumen
│       │
│       ├── components/
│       │   └── CommonUi.kt                # Componentes reutilizables
│       │
│       └── theme/
│           ├── Color.kt                   # Paleta de colores
│           ├── Theme.kt                   # Configuración del tema
│           └── Type.kt                    # Tipografía
│
├── res/
│   ├── drawable/                          # Imágenes (cupcake.png)
│   ├── values/
│   │   ├── strings.xml                    # Textos en inglés
│   │   ├── dimens.xml                     # Dimensiones
│   │   └── themes.xml                     # Temas XML
│   ├── values-es/
│   │   └── strings.xml                    # Textos en español
│   └── values-fr/
│       └── strings.xml                    # Textos en francés
│
└── AndroidManifest.xml                    # Configuración de la app
```

---

## Requisitos Previos

### Conocimientos Técnicos
- ✅ Kotlin: Sintaxis básica e intermedia
- ✅ Jetpack Compose: Funciones composables
- ✅ Android Studio: Creación y ejecución de proyectos
- ✅ MVVM: Comprensión del patrón arquitectónico
- ✅ StateFlow: Manejo de estado reactivo

### Herramientas de Desarrollo
- Android Studio (versión recomendada: Hedgehog o superior)
- JDK 17 o superior
- SDK de Android: API 21+ (Android 5.0) como mínimo
- Gradle: Configurado automáticamente

### Dependencias Principales
```kotlin
// build.gradle.kts (app)
dependencies {
    // Jetpack Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    
    // Activity
    implementation("androidx.activity:activity-compose")
}
```

---

## Instalación y Configuración

### Paso 1: Clonar o Descargar el Proyecto
```bash
git clone https://github.com/google-developer-training/basic-android-kotlin-compose-training-cupcake.git
cd basic-android-kotlin-compose-training-cupcake
```

### Paso 2: Abrir en Android Studio
1. Abre Android Studio
2. Selecciona "Open an Existing Project"
3. Navega a la carpeta del proyecto
4. Espera a que Gradle sincronice las dependencias

### Paso 3: Configurar Dispositivo
**Opción A - Emulador:**
1. Tools → Device Manager
2. Create Virtual Device
3. Selecciona un dispositivo (ej: Pixel 6)
4. Descarga una imagen del sistema (API 34 recomendado)

**Opción B - Dispositivo Físico:**
1. Habilita "Opciones de Desarrollador" en el dispositivo
2. Activa "Depuración USB"
3. Conecta el dispositivo por USB

### Paso 4: Compilar y Ejecutar
1. Selecciona el dispositivo en el selector
2. Haz clic en "Run" (▶️) o presiona `Shift + F10`
3. Espera a que la app se instale y se ejecute

### Paso 5: Probar Funcionalidades
**Escenario de Prueba Completo**:
1. ✅ Selecciona "Six Cupcakes" (6 unidades)
2. ✅ Elige "Coffee" ($1.50 × 6 = $9.00)
3. ✅ Selecciona la primera fecha (hoy) → +$3.00 de cargo
4. ✅ Verifica que el total sea $12.00
5. ✅ Toca "Send Order to Another App"
6. ✅ Comprueba que el email tiene destinatario preestablecido

**Prueba de Multiidioma**:
1. Cambia el idioma del dispositivo a español
2. Reinicia la app
3. Verifica que todos los textos estén en español
4. Repite con francés

---

## Notas Técnicas Adicionales

### Gestión del Estado
- **Fuente única de verdad**: `OrderViewModel` mantiene el estado
- **Inmutabilidad**: `OrderUiState` es una data class inmutable
- **Reactividad**: Uso de `StateFlow` para observar cambios
- **Persistencia durante rotación**: StateFlow sobrevive a cambios de configuración

### Optimizaciones
- **Scroll eficiente**: `verticalScroll` con `rememberScrollState`
- **Recomposición inteligente**: Solo se recomponen los composables afectados
- **Carga de recursos**: Strings e imágenes cargados eficientemente

### Seguridad y Validación
- **Botones condicionalmente habilitados**: Evita estados inválidos
- **Validación de selección**: `enabled = selectedValue.isNotEmpty()`
- **Reset seguro**: `resetOrder()` restaura estado por defecto

---

## Posibles Mejoras Futuras
- 🔄 Persistencia de pedidos con Room Database
- 🎨 Más opciones de personalización (sabores, decoraciones)
- 💳 Integración con pasarelas de pago
- 📊 Historial de pedidos
- 🔔 Notificaciones de recordatorio de recogida
- 🌙 Mejora del tema oscuro
- ♿ Accesibilidad mejorada (TalkBack, descripciones)

---

## Contacto y Soporte
**Email de pedidos**: amelizalde@uce.edu.ec

**Repositorio Original**: [Google Developer Training - Cupcake](https://github.com/google-developer-training/basic-android-kotlin-compose-training-cupcake)

**Licencia**: Apache 2.0
