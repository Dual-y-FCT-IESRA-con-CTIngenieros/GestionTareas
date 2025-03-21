## Desarrollo de la aplicación móvil

## 🛠️ Herramientas de desarrollo:

### Kotlin Multiplatform (KMP) + Jetpack Compose + SwiftUI
🔹 **Descripción:**
Permite compartir la lógica de negocio entre Android e iOS mientras se desarrollan interfaces nativas para cada plataforma.

✅ **Ventajas:**
- Código compartido (~60-70%) entre Android e iOS.
- Mejor rendimiento que soluciones híbridas.
- Acceso directo a APIs nativas de cada plataforma.

❌ **Desventajas:**
- Requiere configuración adicional para compilación y pruebas en iOS.

🔗 **Conexión a BD en la nube:** ✅ Fácil mediante API REST o SDK de Supabase.

### Backend: Spring Boot (Kotlin) / ASP.NET 8 (Si la necesitamos)
🔹 **Descripción:**
Frameworks robustos para la construcción de APIs REST con alta escalabilidad.

✅ **Ventajas de Spring Boot:**
- Compatible con Kotlin y Java.
- Soporte para bases de datos escalables como PostgreSQL.
- Fácil integración con Docker para despliegue en la nube.
- ORM potente: JPA/Hibernate.

✅ **Ventajas de ASP.NET 8:**
- Alto rendimiento y escalabilidad.
- Soporte para C# y F#.
- Integración nativa con Azure y servicios en la nube.
- Uso de Entity Framework Core como ORM.

### Base de Datos: Supabase (PostgreSQL)
🔹 **Descripción:**
Supabase es una alternativa de código abierto a Firebase basada en PostgreSQL.

✅ **Ventajas:**
- Autenticación integrada.
- APIs generadas automáticamente para la base de datos.
- Soporte para WebSockets y suscripciones en tiempo real.
- Fácil integración con Spring Boot y Kotlin.

## Lenguajes Utilizados
- Para la lógica de la app se utilizará Kotlin
- En cuanto a las interfaces se utilizará Jetpack Compose para Android y SwiftUI para iOS
- Para la api en caso de utilizar SpringBoot utilizaremos Kotlin, y en caso de hacerlo con .Net será con C#.

### Infraestructura:
- **Docker** para empaquetar el backend.
- **Cloud Services** (Railway, Render, AWS) para el backend y la base de datos.

## ⚠️ Requisitos para el Desarrollo
- **Para Android:**
    - Windows, macOS o Linux.
    - Android Studio instalado.
- **Para iOS:**
    - Una Mac con macOS e Xcode instalado.
    - O una máquina virtual con macOS para ejecutar Xcode y el emulador de iPhone.
    - Alternativamente, puedes usar servicios en la nube como **MacStadium** o **MacinCloud**.

## 🚀 Despliegue
1. **Backend** → Railway, Render o AWS.
2. **Base de Datos** → Supabase (PostgreSQL).
