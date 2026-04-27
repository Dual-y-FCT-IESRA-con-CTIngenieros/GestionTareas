# Guía: App Móvil Android para Empleado

## 1. ¿Qué tiene actualmente la parte de empleado en la web?

Cuando un usuario inicia sesión con rol **Empleado** (idRol ≠ 1), la app web le muestra únicamente **una pantalla**: `MyTimeView` (Registro de Horas). La barra lateral de navegación no aparece y el acceso queda restringido a sus propios datos.

### Pantallas / secciones visibles para el empleado

| Sección | Descripción |
|---|---|
| **Formulario de registro de horas** | Formulario inline (sin botón de abrir modal) para introducir: Fecha, Horas, Orden de Trabajo, Código de Tiempo, Actividad y Comentario. |
| **Resumen anual** | Muestra las horas totales del año agrupadas por mes. Tiene toggle Trimestral / Anual. |
| **Historial de registros** | Tabla con todos sus registros ordenados de más reciente a más antiguo, con acciones de editar y eliminar. |

### Datos que maneja el empleado

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | Long | ID del registro de horas |
| `idEmployee` | Long | ID del empleado (viene del token JWT) |
| `idWorkOrder` | String | Código de la Orden de Trabajo (opcional) |
| `idTimeCode` | Integer | ID del Código de Tiempo (opcional) |
| `idActivity` | Integer | ID de la Actividad (opcional) |
| `time` | Float | Horas trabajadas (ej: 4.5) |
| `date` | String | Fecha en formato `YYYY-MM-DD` |
| `comment` | String | Comentario libre (opcional) |

---

## 2. Arquitectura recomendada para Android

```
app/
 ├── data/
 │    ├── model/          ← Data classes (TimeRecord, WorkOrder, TimeCode, Activity, User)
 │    ├── remote/         ← Retrofit API interfaces + DTOs
 │    └── repository/     ← Repositorios (TimeRecordRepository, AuthRepository)
 ├── ui/
 │    ├── login/          ← LoginActivity / LoginFragment + ViewModel
 │    ├── mytime/         ← MyTimeFragment + ViewModel (pantalla principal empleado)
 │    │    ├── form/      ← Fragmento del formulario de registro
 │    │    ├── summary/   ← Fragmento del resumen anual
 │    │    └── history/   ← Fragmento del historial
 │    └── shared/         ← Componentes comunes (LoadingView, ErrorView)
 ├── utils/
 │    ├── TokenManager    ← Gestión del JWT en SharedPreferences / EncryptedSharedPreferences
 │    └── DateUtils       ← Formateo de fechas
 └── di/                  ← Hilt modules (si usas inyección de dependencias)
```

**Stack tecnológico recomendado:**
- Lenguaje: **Kotlin**
- UI: **Jetpack Compose** (o XML + ViewBinding)
- Networking: **Retrofit 2 + OkHttp**
- Autenticación: **JWT en EncryptedSharedPreferences**
- State management: **ViewModel + StateFlow/LiveData**
- Navegación: **Navigation Component**
- DI: **Hilt** (opcional pero recomendado)

---

## 3. Autenticación y JWT

### Endpoint
```
POST {BASE_URL}/api/auth/login
Content-Type: application/json

{
  "email": "empleado@empresa.com",
  "password": "contraseña"
}
```

### Respuesta del backend
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "idEmployee": 2,
  "email": "empleado@empresa.com",
  "nombre": "Ana",
  "apellidos": "García",
  "idRol": 3
}
```

### Cómo guardar el token en Android
```kotlin
// TokenManager.kt
class TokenManager(context: Context) {
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) = prefs.edit().putString("jwt_token", token).apply()
    fun getToken(): String? = prefs.getString("jwt_token", null)
    fun clearToken() = prefs.edit().remove("jwt_token").apply()

    fun saveUser(user: UserDto) {
        prefs.edit()
            .putInt("idEmployee", user.idEmployee)
            .putString("nombre", user.nombre)
            .putString("apellidos", user.apellidos)
            .putString("email", user.email)
            .putInt("idRol", user.idRol)
            .apply()
    }
    fun getIdEmployee(): Int = prefs.getInt("idEmployee", -1)
    fun getNombre(): String? = prefs.getString("nombre", null)
}
```

### Interceptor OkHttp para incluir el token automáticamente
```kotlin
// AuthInterceptor.kt
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = tokenManager.getToken()
        if (token != null) {
            request.addHeader("Authorization", "Bearer $token")
        }
        val response = chain.proceed(request.build())
        if (response.code == 401 || response.code == 403) {
            tokenManager.clearToken()
            // Lanzar evento para redirigir al login
        }
        return response
    }
}
```

---

## 4. Modelos de datos (Data Classes Kotlin)

```kotlin
// TimeRecord.kt
data class TimeRecord(
    val id: Long? = null,
    val idEmployee: Int,
    val idWorkOrder: String? = null,
    val idTimeCode: Int? = null,
    val idActivity: Int? = null,
    val time: Float,
    val date: String,           // "YYYY-MM-DD"
    val comment: String? = null
)

// WorkOrder.kt
data class WorkOrder(
    val idWorkOrder: String,
    val desc: String
)

// TimeCode.kt
data class TimeCode(
    val idTimeCode: Int,
    val desc: String,
    val color: Long,
    val chkProd: Boolean
)

// Activity.kt
data class Activity(
    val idActivity: Int,
    val desc: String
)

// User.kt
data class User(
    val idEmployee: Int,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val idRol: Int
)
```

---

## 5. Interfaz Retrofit (API)

```kotlin
// ApiService.kt
interface ApiService {

    // Auth
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // Registros de horas
    @GET("employee-activities")
    suspend fun getAllActivities(): List<TimeRecord>

    @GET("employee-activities/by-employee/{id}")
    suspend fun getActivitiesByEmployee(@Path("id") idEmployee: Int): List<TimeRecord>

    @POST("employee-activities")
    suspend fun createActivity(@Body record: TimeRecord): TimeRecord

    @PUT("employee-activities")
    suspend fun updateActivity(@Body record: TimeRecord): TimeRecord

    @HTTP(method = "DELETE", path = "employee-activities", hasBody = true)
    suspend fun deleteActivity(@Body record: TimeRecord)

    // Órdenes de trabajo
    @GET("workorders")
    suspend fun getWorkOrders(): List<WorkOrder>

    // Códigos de tiempo
    @GET("timecodes")
    suspend fun getTimeCodes(): List<TimeCode>

    // Actividades
    @GET("activities")
    suspend fun getActivities(): List<Activity>
}

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(
    val token: String,
    val idEmployee: Int,
    val email: String,
    val nombre: String,
    val apellidos: String,
    val idRol: Int
)
```

---

## 6. Pantallas a implementar en Android

### 6.1 Pantalla de Login

- Dos campos: email + contraseña
- Botón "Iniciar Sesión" con estado de carga
- Al hacer login exitoso: guardar JWT + datos de usuario, navegar a pantalla principal
- Si error 401: mostrar mensaje de credenciales incorrectas
- Si el usuario ya tiene token guardado: saltar el login directamente

**Vista sugerida (Compose):**
```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(), onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(24.dp)) {
        // Logo de empresa
        Image(painterResource(R.drawable.logo_con_texto), contentDescription = "Logo")

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") })
        OutlinedTextField(value = password, onValueChange = { password = it },
            label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation())

        Button(onClick = { viewModel.login(email, password) }) {
            Text(if (uiState.loading) "Cargando..." else "Iniciar Sesión")
        }

        if (uiState.error != null) Text(uiState.error!!, color = Color.Red)
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) onLoginSuccess()
    }
}
```

---

### 6.2 Pantalla Principal del Empleado (MyTime)

Organizar en **tabs o scroll vertical** con tres secciones:

#### Sección 1: Formulario de Registro

Campos del formulario:
- **Fecha** → `DatePickerDialog` nativo de Android
- **Horas** → `TextField` numérico con decimales (step 0.5)
- **Orden de Trabajo** → `DropdownMenu` / `ExposedDropdownMenuBox`
- **Código de Tiempo** → `DropdownMenu`
- **Actividad** → `DropdownMenu`
- **Comentario** → `TextField` multilínea

Botón: **"Registrar Horas"** (o "Guardar Cambios" en modo edición)
Botón cancelar edición visible solo en modo edición.

#### Sección 2: Resumen Anual

- Toggle entre vista **Trimestral** y **Anual**
- Vista Anual: grid de 12 tarjetas (una por mes) con horas totales
- Vista Trimestral: 4 tarjetas (T1–T4) con desglose mensual y total

```kotlin
// Cálculo equivalente al de la web
data class MonthSummary(val name: String, val hours: Float, val count: Int)

fun computeAnnualSummary(records: List<TimeRecord>, year: Int): List<MonthSummary> {
    val months = listOf("Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic")
    return months.mapIndexed { idx, name ->
        val prefix = "$year-${String.format("%02d", idx + 1)}"
        val recs = records.filter { it.date.startsWith(prefix) }
        MonthSummary(name, recs.sumOf { it.time.toDouble() }.toFloat(), recs.size)
    }
}
```

#### Sección 3: Historial de Registros

- `LazyColumn` con las filas ordenadas de más reciente a más antiguo
- Cada fila muestra: Fecha, Horas (chip de color), OT, Actividad, Código de Tiempo, Comentario
- **Swipe to delete** o botones de editar/eliminar visibles con long-press o en la propia fila
- Al pulsar editar: rellenar el formulario y hacer scroll hacia arriba

---

## 7. Navegación

```
LoginScreen
    └── MyTimeScreen   ← pantalla única del empleado
          ├── Tab/Sección 1: Formulario
          ├── Tab/Sección 2: Resumen
          └── Tab/Sección 3: Historial
```

Botón de **Cerrar Sesión** en la barra superior (icono de usuario pulsable), igual que en la web.

---

## 8. Variable de entorno / configuración

En Android, la URL base de la API se puede gestionar con `BuildConfig`:

```kotlin
// build.gradle.kts (app)
android {
    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8081/api/\"")
        }
        release {
            buildConfigField("String", "API_BASE_URL", "\"https://tu-api-produccion.com/api/\"")
        }
    }
}
```

> ⚠️ En el emulador de Android, `localhost` del PC es `10.0.2.2`. En dispositivo físico, usa la IP local de tu máquina o la URL de producción.

```kotlin
// RetrofitClient.kt
val retrofit = Retrofit.Builder()
    .baseUrl(BuildConfig.API_BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()
    )
    .build()
```

---

## 9. Dependencias Gradle recomendadas

```kotlin
// build.gradle.kts (app)
dependencies {
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ViewModel + LiveData / StateFlow
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // Jetpack Compose (si usas Compose)
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Seguridad (token cifrado)
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Hilt (DI - opcional)
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-android-compiler:2.51")
}
```

---

## 10. Flujo completo de la app Android

```
App arranca
   │
   ├─ ¿Token guardado? ──SÍ──► MyTimeScreen (carga datos del empleado)
   │
   └─ NO ──► LoginScreen
               │
               └─ Login OK ──► Guardar JWT + User ──► MyTimeScreen
                                                         │
                                                         ├─ Carga paralela:
                                                         │    GET /employee-activities (filtrado por idEmployee)
                                                         │    GET /workorders
                                                         │    GET /timecodes
                                                         │    GET /activities
                                                         │
                                                         ├─ Formulario visible directamente
                                                         ├─ Resumen anual calculado en local
                                                         └─ Historial en LazyColumn
```

---

## 11. Diferencias clave web → móvil

| Aspecto | Web (Vue) | Android |
|---|---|---|
| Selector de fecha | `<input type="date">` | `DatePickerDialog` o `DatePicker` de Material3 |
| Selects/Dropdowns | `<select>` HTML | `ExposedDropdownMenuBox` (Compose) |
| Token JWT | `localStorage` | `EncryptedSharedPreferences` |
| Formulario en pantalla | Siempre visible (inline) | Sección al inicio con scroll |
| Tabla de historial | `<table>` HTML con hover | `LazyColumn` con cards o filas |
| Carga inicial | `onMounted` + `Promise.all` | `viewModelScope.launch` + `async/await` |
| Manejo de errores 401 | Interceptor Axios | Interceptor OkHttp |
| Cierre de sesión | Icono superior derecha | Icono en TopAppBar |

