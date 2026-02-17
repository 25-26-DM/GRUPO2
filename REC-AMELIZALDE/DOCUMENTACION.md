# Documentación del Proyecto REC-AMELIZALDE

## 📋 Descripción General

**REC-AMELIZALDE** es una aplicación Android desarrollada con Kotlin y Jetpack Compose que implementa un sistema de gestión de productos con autenticación de usuarios, persistencia local mediante Room y sincronización bidireccional con AWS DynamoDB.

### Características principales:
- Autenticación de usuarios (login/registro)
- Gestión de productos (CRUD)
- Sincronización automática con DynamoDB
- Manejo de sesiones con timeout
- Notificaciones de sincronización
- Soporte offline-first con sincronización cuando hay conexión

---

## 📁 Estructura del Proyecto

```
app/src/main/java/ec/edu/uce/rec_amelizalde/
├── MainActivity.kt              # Punto de entrada de la aplicación
├── controller/                  # Capa de controladores
│   ├── AuthController.kt        # Lógica de autenticación
│   └── ProductController.kt     # Lógica de gestión de productos
├── data/                        # Capa de datos (Room)
│   ├── AppDatabase.kt           # Configuración de Room Database
│   ├── Product.kt               # Entidad de Producto
│   ├── ProductDao.kt            # DAO de Productos
│   ├── User.kt                  # Entidad de Usuario
│   └── UserDao.kt               # DAO de Usuarios
├── notification/                # Sistema de notificaciones
│   └── NotificationHelper.kt    # Helper para notificaciones
├── session/                     # Manejo de sesiones
│   └── SessionManager.kt        # Gestión de sesión de usuario
├── sync/                        # Sincronización con la nube
│   ├── DynamoLocalHelper.kt     # Cliente AWS DynamoDB
│   ├── NetworkChangeReceiver.kt # Receptor de cambios de conectividad
│   ├── SyncManager.kt           # Orquestador de sincronización
│   ├── SyncScheduler.kt         # Programador de tareas de sincronización
│   └── SyncWorker.kt            # Worker de WorkManager para sync en background
├── ui/                          # Capa de presentación (Compose)
│   ├── LoginScreen.kt           # Pantalla de inicio de sesión
│   ├── RegisterScreen.kt        # Pantalla de registro
│   ├── ProductListScreen.kt     # Pantalla de lista de productos
│   ├── ProductFormScreen.kt     # Formulario de producto (crear/editar)
│   └── theme/                   # Tema de la aplicación
│       ├── Color.kt             # Definición de colores
│       ├── Theme.kt             # Configuración del tema
│       └── Type.kt              # Tipografía
└── util/                        # Utilidades
    ├── FileUtils.kt             # Utilidades para archivos
    └── NetworkUtils.kt          # Utilidades de red
```

---

## 🏗️ Arquitectura

La aplicación sigue una arquitectura de capas simplificada:

```
┌─────────────────────────────────────────────────────────────┐
│                    UI Layer (Compose)                       │
│  LoginScreen | RegisterScreen | ProductListScreen | Form    │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                   Controller Layer                           │
│           AuthController | ProductController                 │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                     Data Layer                               │
│    Room Database (SQLite) ←→ DAOs ←→ Entities               │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                   Sync Layer                                 │
│   SyncManager | SyncWorker | DynamoDBHelper | Scheduler     │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
                    ┌───────────────┐
                    │ AWS DynamoDB  │
                    └───────────────┘
```

---

## 📦 Dependencias

### Archivo `libs.versions.toml`

#### Versiones principales:
| Dependencia | Versión |
|-------------|---------|
| Android Gradle Plugin | 8.13.2 |
| Kotlin | 2.3.0 |
| Compose BOM | 2026.01.00 |
| AWS SDK | 2.41.15 |

#### Bibliotecas utilizadas:

**AndroidX Core & Lifecycle:**
- `androidx-core-ktx` (1.17.0) - Extensiones Kotlin para Android
- `androidx-lifecycle-runtime-ktx` (2.10.0) - Lifecycle con coroutines

**Jetpack Compose:**
- `androidx-compose-bom` - Bill of Materials de Compose
- `androidx-activity-compose` (1.12.2) - Integración Activity-Compose
- `androidx-compose-ui` - UI core
- `androidx-compose-material3` - Material Design 3
- `androidx-compose-material-icons-extended` - Iconos extendidos

**AWS SDK:**
- `aws-sdk-dynamodb` - Cliente DynamoDB Kotlin
- `aws.sdk.kotlin:dynamodb:1.+` - SDK AWS Kotlin

**Room Database:**
- `room-runtime` (2.8.4) - Persistencia local
- `room-ktx` - Extensiones Kotlin para Room
- `room-compiler` - Procesador de anotaciones

**WorkManager:**
- `work-runtime-ktx` (2.9.0) - Tareas en background

**Networking:**
- `ktor-client-okhttp` (2.3.4) - Cliente HTTP para AWS SDK

**Testing:**
- `junit` (4.13.2) - Testing unitario
- `androidx-junit` (1.3.0) - AndroidX JUnit
- `androidx-espresso-core` (3.7.0) - Testing de UI

---

## 🔄 Flujo de Trabajo

### 1. Inicio de la Aplicación

```
MainActivity.onCreate()
    │
    ├── AppDatabase.prepopulateIfEmpty() → Datos iniciales
    │
    ├── DynamoDBHelper.initialize() → Configurar cliente AWS
    │
    ├── SyncScheduler.schedulePeriodic() → Sincronización cada 15 min
    │
    ├── Solicitar permiso POST_NOTIFICATIONS (Android 13+)
    │
    └── Si hay WiFi → Sincronización inmediata
```

### 2. Flujo de Autenticación

```
LoginScreen
    │
    ├── Usuario ingresa credenciales
    │
    ├── AuthController.login()
    │   └── UserDao.validateUser() → Hash SHA-256
    │
    ├── SessionManager.startSession()
    │   └── Guarda timestamp en SharedPreferences
    │
    └── Navega a ProductsListScreen
```

```
RegisterScreen
    │
    ├── Usuario crea cuenta
    │
    ├── AuthController.register()
    │   ├── Verificar usuario existente
    │   ├── Crear con syncStatus = "pending"
    │   └── Si hay WiFi → Trigger sincronización
    │
    └── Navega a LoginScreen
```

### 3. Gestión de Productos

```
ProductsListScreen
    │
    ├── ProductController.getAll() → Lista productos
    │
    ├── Sincronización automática si hay pendientes + WiFi
    │
    ├── Agregar producto → ProductFormScreen
    │
    └── Editar producto → ProductFormScreen (con datos)
```

```
ProductFormScreen
    │
    ├── Campos: código, descripción, costo, disponible, fecha, foto
    │
    ├── Tomar foto (ActivityResultContracts.TakePicturePreview)
    │
    └── Guardar → ProductController.insert()/update()
        └── Marcar syncStatus = "pending" + trigger sync
```

### 4. Sincronización con DynamoDB

```
SyncManager.fullSync()
    │
    ├── syncToCloud()
    │   ├── Usuarios pendientes → DynamoDBHelper.putUser()
    │   ├── Productos pendientes → DynamoDBHelper.putProduct()
    │   └── Eliminaciones → DynamoDBHelper.deleteProduct()
    │
    └── syncFromCloud()
        └── Descarga productos de DynamoDB → Actualiza local
```

**Estrategia de sincronización:**
- **Offline-first**: Los datos se guardan primero localmente
- **Sync status**: `pending` | `synced` | `error`
- **Resolución de conflictos**: El más reciente gana (`lastModified`)
- **Eliminación lógica**: `isDeleted = true` antes de sincronizar

### 5. Gestión de Sesiones

```
SessionManager
    │
    ├── MAX_SESSION_MS = 15 minutos (sesión total)
    │
    ├── INACTIVITY_TIMEOUT_MS = 5 minutos (inactividad)
    │
    ├── isSessionValid() → Verifica ambos tiempos
    │
    └── clearSession() → Logout
```

---

## 📄 Descripción de Componentes

### MainActivity.kt
Punto de entrada que configura:
- Edge-to-edge display
- Inicialización de DynamoDB
- Programación de sincronización periódica
- Permisos de notificaciones
- Navegación entre pantallas (login → register → main → product_form)

### controller/AuthController.kt
- `login(username, password)`: Valida credenciales con hash SHA-256
- `register(username, password)`: Crea nuevo usuario con estado "pending"

### controller/ProductController.kt
- `getAll()`: Obtiene todos los productos no eliminados
- `getByCode(code)`: Busca producto por código
- `insert(product)`: Inserta producto con sync pendiente
- `update(product)`: Actualiza producto con sync pendiente
- `delete(product)`: Marca como eliminado (soft delete)

### data/AppDatabase.kt
Base de datos Room con:
- Entidades: `User`, `Product`
- Migración 1→2: Agrega campos de sincronización
- `prepopulateIfEmpty()`: Datos de ejemplo iniciales

### data/Product.kt
```kotlin
@Entity(tableName = "products")
data class Product(
    @PrimaryKey val code: String,
    val description: String,
    val manufactureDate: Long,     // Timestamp
    val cost: Double,
    val available: Boolean,
    val photo: ByteArray?,         // Imagen como BLOB
    val syncStatus: String,        // pending, synced, error
    val lastModified: Long,
    val isDeleted: Boolean
)
```

### data/User.kt
```kotlin
@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val password: String,          // Hash SHA-256
    val syncStatus: String,
    val lastModified: Long,
    val isDeleted: Boolean
)
```

### sync/DynamoDBHelper.kt
Cliente singleton para AWS DynamoDB:
- Inicializa cliente con credenciales
- CRUD de productos (put, get, getAll, delete)
- CRUD de usuarios (put, get)
- Creación automática de tablas si no existen

### sync/SyncManager.kt
Orquestador de sincronización:
- `syncToCloud()`: Sube datos locales pendientes
- `syncFromCloud()`: Descarga datos de la nube
- `fullSync()`: Sincronización bidireccional completa
- Devuelve `SyncResult` con estadísticas

### sync/SyncWorker.kt
CoroutineWorker para WorkManager:
- Se ejecuta en background cuando hay conexión
- Llama a `SyncManager.fullSync()`
- Envía notificación al completar

### sync/SyncScheduler.kt
Programador de tareas:
- `schedulePeriodic()`: Cada 15 minutos con conexión
- `scheduleImmediate()`: Sincronización inmediata
- Usa PeriodicWorkRequest con constraints de red

### session/SessionManager.kt
Gestión de sesión con SharedPreferences:
- Sesión máxima: 15 minutos
- Timeout por inactividad: 5 minutos
- `startSession()`, `touch()`, `isSessionValid()`, `clearSession()`

### notification/NotificationHelper.kt
Sistema de notificaciones:
- Canal "sync_channel" para Android O+
- Verifica permiso POST_NOTIFICATIONS en Android 13+
- `notifySync(count)`: Notifica sincronización completada

### ui/ (Pantallas Compose)
- **LoginScreen**: Formulario de login con logo, navegación a registro
- **RegisterScreen**: Formulario de registro, navegación a login
- **ProductsListScreen**: Lista de productos con cards, estado de sincronización, logout
- **ProductFormScreen**: Formulario con selector de fecha, captura de foto, validación

### util/
- **NetworkUtils**: Verifica conectividad usando NetworkCapabilities
- **FileUtils**: Guarda Bitmap como archivo PNG

---

## 🔐 Seguridad

- **Contraseñas**: Almacenadas como hash SHA-256
- **Sesiones**: Timeout automático por tiempo e inactividad
- **Credenciales AWS**: Configuradas en DynamoDBHelper (usar variables de entorno en producción)

---

## 📱 Requisitos

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Permisos**: POST_NOTIFICATIONS, ACCESS_NETWORK_STATE

---

## 🚀 Compilación

```bash
./gradlew assembleDebug
```

El proyecto usa:
- Kotlin DSL para Gradle
- Version Catalog (libs.versions.toml)
- Kapt para procesamiento de anotaciones Room
- Parcelize para serialización de Product

---

## 📝 Notas Adicionales

1. **DynamoDB**: El proyecto está configurado para conectarse a AWS DynamoDB real. Las tablas `Products` y `Users` se crean automáticamente si no existen.

2. **Sincronización**: La app sigue el patrón offline-first, guardando cambios localmente y sincronizando cuando hay conexión WiFi disponible.

3. **Imágenes**: Las fotos de productos se almacenan como `ByteArray` (BLOB) tanto en SQLite como en DynamoDB.

4. **Tema**: Utiliza Material Design 3 con soporte para colores dinámicos en Android 12+.
