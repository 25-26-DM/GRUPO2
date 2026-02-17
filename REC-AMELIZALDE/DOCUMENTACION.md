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
- Gestión segura de credenciales AWS

---

## 📑 Tabla de Contenidos

1. [Estructura del Proyecto](#-estructura-del-proyecto)
2. [Arquitectura](#-arquitectura)
3. [Dependencias](#-dependencias)
4. [Flujo de Trabajo](#-flujo-de-trabajo)
5. [Descripción de Componentes](#-descripción-de-componentes)
6. [Configuración de Credenciales AWS](#-configuración-de-credenciales-aws)
7. [Seguridad](#-seguridad)
8. [Requisitos](#-requisitos)
9. [Compilación](#-compilación)
10. [Notas Adicionales](#-notas-adicionales)
11. [Solución de Problemas](#-solución-de-problemas)
12. [Contribución](#-contribución)

---

## 📁 Estructura del Proyecto

### Estructura Raíz

```
REC-AMELIZALDE/
├── .gitignore                   # Archivos ignorados por Git
├── local.properties             # Credenciales AWS (NO en Git)
├── local.properties.example     # Plantilla de configuración
├── build.gradle.kts             # Configuración Gradle raíz
├── settings.gradle.kts          # Módulos del proyecto
├── gradle/
│   └── libs.versions.toml       # Catálogo de versiones
├── app/
│   ├── build.gradle.kts         # Configuración de la app
│   ├── proguard-rules.pro       # Reglas de ofuscación
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/ec/edu/uce/rec_amelizalde/
│           └── res/
└── DOCUMENTACION.md             # Este archivo
```

### Estructura del Código Fuente

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

## 🔐 Configuración de Credenciales AWS

### Estructura de Archivos
El proyecto utiliza `local.properties` para almacenar credenciales de AWS de forma segura, evitando que se suban al repositorio.

```
REC-AMELIZALDE/
├── local.properties           # Contiene credenciales (NO se sube a Git)
├── local.properties.example   # Plantilla para otros desarrolladores
└── .gitignore                 # Ignora archivos sensibles
```

### Configuración Inicial

**1. Copiar el archivo de ejemplo:**
```bash
cp local.properties.example local.properties
```

**2. Editar `local.properties` con tus credenciales:**
```properties
sdk.dir=/path/to/your/Android/Sdk

# AWS DynamoDB Credentials
AWS_ACCESS_KEY=tu_access_key_aqui
AWS_SECRET_KEY=tu_secret_key_aqui
AWS_REGION=us-east-1
```

**3. Compilar el proyecto:**
```bash
./gradlew assembleDebug
```

### Flujo de BuildConfig

```
local.properties
      ↓
app/build.gradle.kts (lee las propiedades)
      ↓
BuildConfig.java (generado automáticamente)
      ↓
DynamoDBHelper.kt (consume BuildConfig.AWS_ACCESS_KEY)
```

### Implementación en `build.gradle.kts`

```kotlin
// Load local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        // AWS Credentials from local.properties
        buildConfigField("String", "AWS_ACCESS_KEY", 
            "\"${localProperties.getProperty("AWS_ACCESS_KEY", "")}\"")
        buildConfigField("String", "AWS_SECRET_KEY", 
            "\"${localProperties.getProperty("AWS_SECRET_KEY", "")}\"")
        buildConfigField("String", "AWS_REGION", 
            "\"${localProperties.getProperty("AWS_REGION", "us-east-1")}\"")
    }
}
```

### Uso en `DynamoDBHelper.kt`

```kotlin
object DynamoDBHelper {
    // AWS Credentials loaded from BuildConfig
    private val AWS_ACCESS_KEY = BuildConfig.AWS_ACCESS_KEY
    private val AWS_SECRET_KEY = BuildConfig.AWS_SECRET_KEY
    private val AWS_REGION = BuildConfig.AWS_REGION

    fun initialize(context: Context) {
        client = DynamoDbClient {
            region = AWS_REGION
            credentialsProvider = StaticCredentialsProvider {
                accessKeyId = AWS_ACCESS_KEY
                secretAccessKey = AWS_SECRET_KEY
            }
        }
    }
}
```

### `.gitignore` - Archivos Excluidos

```ignore
# Local configuration file (SDK, credentials)
local.properties

# Secrets and credentials - NEVER commit these
secrets.properties
*.keystore
*.jks
google-services.json
```

---

## 🔐 Seguridad

### Mejores Prácticas Implementadas

1. **Contraseñas de Usuario**: 
   - Almacenadas como hash SHA-256
   - Nunca se almacenan en texto plano

2. **Sesiones**: 
   - Timeout automático por tiempo (15 min)
   - Timeout por inactividad (5 min)
   - Almacenadas en SharedPreferences privadas

3. **Credenciales AWS**: 
   - **✅ CORRECTO**: Almacenadas en `local.properties` (ignorado por Git)
   - **✅ CORRECTO**: Inyectadas vía BuildConfig en tiempo de compilación
   - **❌ INCORRECTO**: ~~Hardcoded en el código fuente~~ (solucionado)
   - Para producción: usar AWS Cognito o IAM Roles

4. **Control de Versiones**:
   - `local.properties` está en `.gitignore`
   - Se proporciona `local.properties.example` como plantilla
   - Credenciales nunca se suben al repositorio

### Advertencias de Seguridad

⚠️ **NUNCA** hagas lo siguiente:
- Subir `local.properties` a Git
- Hardcodear credenciales en el código
- Compartir credenciales en canales inseguros
- Usar credenciales de producción en desarrollo

---

## 📱 Requisitos

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Permisos**: POST_NOTIFICATIONS, ACCESS_NETWORK_STATE

---

## 🚀 Compilación

### Configuración Previa

**1. Clonar el repositorio:**
```bash
git clone <url-del-repositorio>
cd REC-AMELIZALDE
```

**2. Configurar credenciales AWS:**
```bash
# Copiar el archivo de ejemplo
cp local.properties.example local.properties

# Editar con tus credenciales
# Agregar: AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_REGION
```

**3. Sincronizar con Gradle:**
```bash
# En Android Studio
File → Sync Project with Gradle Files

# O desde línea de comandos
./gradlew build
```

### Comandos de Compilación

**Debug Build:**
```bash
./gradlew assembleDebug
```

**Release Build:**
```bash
./gradlew assembleRelease
```

**Limpiar y recompilar:**
```bash
./gradlew clean assembleDebug
```

**Ejecutar en dispositivo:**
```bash
./gradlew installDebug
```

### Tecnologías de Build

El proyecto usa:
- **Kotlin DSL** para Gradle (build.gradle.kts)
- **Version Catalog** (libs.versions.toml) para gestión de dependencias
- **Kapt** para procesamiento de anotaciones Room
- **Parcelize** para serialización de Product
- **BuildConfig** para inyección de configuración

### Estructura de Build

```
build.gradle.kts (root)          # Configuración global
├── settings.gradle.kts          # Módulos del proyecto
└── app/
    └── build.gradle.kts         # Configuración de la app
        ├── BuildConfig generation
        ├── Dependencias
        └── Packaging options
```

---

## 📝 Notas Adicionales

1. **DynamoDB**: El proyecto está configurado para conectarse a AWS DynamoDB real. Las tablas `Products` y `Users` se crean automáticamente si no existen.

2. **Sincronización**: La app sigue el patrón offline-first, guardando cambios localmente y sincronizando cuando hay conexión WiFi disponible.

3. **Imágenes**: Las fotos de productos se almacenan como `ByteArray` (BLOB) tanto en SQLite como en DynamoDB.

4. **Tema**: Utiliza Material Design 3 con soporte para colores dinámicos en Android 12+.

5. **Credenciales**: Las credenciales de AWS deben configurarse en `local.properties` antes de compilar. Ver sección "Configuración de Credenciales AWS".

---

## 🔧 Solución de Problemas

### Error: "Unresolved reference 'BuildConfig'"

**Problema:** El IDE no encuentra la clase `BuildConfig`.

**Solución:**
1. Asegúrate de tener `local.properties` configurado con las credenciales AWS
2. Compila el proyecto para generar BuildConfig:
   ```bash
   ./gradlew assembleDebug
   ```
3. Sincroniza el proyecto con Gradle:
   - **Android Studio**: File → Sync Project with Gradle Files
   - **VS Code**: Recarga la ventana (Ctrl+Shift+P → Reload Window)

### Error: AWS Credentials vacías

**Problema:** Las credenciales de AWS están vacías o no se cargan.

**Solución:**
1. Verifica que `local.properties` existe en la raíz del proyecto
2. Confirma que las propiedades están escritas correctamente:
   ```properties
   AWS_ACCESS_KEY=tu_clave_aqui
   AWS_SECRET_KEY=tu_secreto_aqui
   AWS_REGION=us-east-1
   ```
3. Recompila el proyecto completamente:
   ```bash
   ./gradlew clean assembleDebug
   ```

### Error: Room migration failed

**Problema:** La migración de la base de datos falla.

**Solución:**
1. Desinstala la app del dispositivo/emulador
2. Reinstala con:
   ```bash
   ./gradlew installDebug
   ```
3. O fuerza la recreación de la BD (solo desarrollo):
   ```kotlin
   Room.databaseBuilder(...).fallbackToDestructiveMigration().build()
   ```

### Error: DynamoDB connection timeout

**Problema:** No se puede conectar a DynamoDB.

**Solución:**
1. Verifica las credenciales en AWS IAM
2. Confirma que las tablas existen en la región correcta
3. Verifica permisos IAM (`dynamodb:PutItem`, `dynamodb:GetItem`, etc.)
4. Revisa la configuración de red del dispositivo

### Sincronización no se ejecuta

**Problema:** Los cambios no se sincronizan con DynamoDB.

**Solución:**
1. Verifica conectividad a Internet
2. Revisa los logs con:
   ```bash
   adb logcat -s SyncManager SyncWorker DynamoDBHelper
   ```
3. Fuerza una sincronización manual desde ProductsListScreen
4. Verifica que WorkManager esté habilitado

---

## 👥 Contribución

### Setup para Nuevos Desarrolladores

1. Clonar el repositorio
2. Copiar `local.properties.example` a `local.properties`
3. Solicitar credenciales de AWS al administrador
4. Ejecutar `./gradlew assembleDebug`
5. Sincronizar con Gradle en el IDE

### Commits de Seguridad

Antes de hacer commit, verifica que NO incluyas:
```bash
# Revisar cambios
git status

# Asegúrate de que local.properties NO aparezca
# Si aparece, está en .gitignore
git add .
git commit -m "Tu mensaje"
```

---

## 📄 Licencia

Este proyecto fue desarrollado como parte del examen de recuperación de Dispositivos Móviles, 8vo Semestre, Universidad Central del Ecuador.
