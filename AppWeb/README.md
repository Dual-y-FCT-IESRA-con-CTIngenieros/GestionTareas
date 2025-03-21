## Desarrollo de la aplicación web
### Lenguaje + Herramientas de desarrollo para la app web

### Herramientas de desarrollo:

#### React / Next.js / Vue / Angular (Single Page Applications - SPA)
🔹 **Descripción:**
Páginas dinámicas que se cargan en el navegador y actualizan contenido sin recargar.

✅ **Ventajas:**
- Excelente experiencia de usuario (rápido, sin recargas).
- Fácil conexión a APIs REST/GraphQL.
- Amplio ecosistema de librerías y herramientas.

❌ **Desventajas:**
- Puede ser complicado de configurar para principiantes.
- El SEO es más difícil en SPAs (excepto con Next.js o Nuxt.js).
- Mayor consumo de memoria en el navegador.

🔗 **Conexión a BD en la nube:** ✅ Fácil mediante API REST o GraphQL.

#### Next.js / Nuxt.js / Remix (SSR - Server Side Rendering)
🔹 **Descripción:**
Generan las páginas en el servidor antes de enviarlas al navegador.

✅ **Ventajas:**
- Mejor SEO que las SPAs.
- Renderizado más rápido y escalable.
- Soporte nativo para bases de datos en la nube (Prisma, Firebase, Supabase).

❌ **Desventajas:**
- Requiere conocimientos en backend y frontend.
- Puede ser más costoso en términos de infraestructura.

🔗 **Conexión a BD en la nube:** ✅ Fácil con Prisma, Supabase, Firebase o APIs personalizadas.

### Backend: Spring Boot (Java/Kotlin) o ASP .NET 8

✅ **Ventajas:**
- Alto rendimiento y soporte para concurrencia.
- Compatible con bases de datos escalables como PostgreSQL, MongoDB.
- Fácil integración con Docker y Kubernetes para escalabilidad.
- ORM potente: JPA/Hibernate (Spring Boot) o Entity Framework (ASP .NET).

#### Base de Datos:
- **SQL** (PostgreSQL, MySQL, SQL Server) si la estructura de datos es relacional.
- **NoSQL** (MongoDB, Firebase, DynamoDB) si se requieren consultas flexibles y escalabilidad horizontal.

#### Infraestructura:
- **Docker + Kubernetes** para despliegues escalables.
- **Cloud Services** (AWS, Azure, Google Cloud) con Load Balancing y Auto Scaling.
- **Mensajería** (Kafka, RabbitMQ) si necesitas procesar eventos de manera eficiente.

### React Native + React Native Web
Esta opción permite realizar en conjunto tanto la app web como la app móvil.

✅ **Ventajas:**
- Código compartido (~80%) entre web y móvil.
- Ecosistema unificado en JavaScript/TypeScript.
- Buen rendimiento en dispositivos móviles.
- Compatible con librerías como Redux, Zustand, TanStack Query.

❌ **Desventajas:**
- No todas las librerías de React Native funcionan bien en la web.
- Algunas optimizaciones requieren código nativo en Kotlin/Swift.
- La Web puede sentirse menos optimizada comparada con Next.js o Vite.

### Despliegue:
1. **Backend** → Railway o Render.
2. **Frontend** → Vercel.
3. **Base de Datos** → Supabase (PostgreSQL) o Neon.tech.

### Kubernetes:
1. **Orquestación de contenedores:** Administra aplicaciones que se ejecutan en contenedores, asegurando que se desplieguen y actualicen correctamente.
2. **Escalabilidad automática:** Puede escalar automáticamente las aplicaciones según la demanda de tráfico o uso de recursos.
3. **Despliegue y gestión de aplicaciones:** Permite estrategias de despliegue progresivo como Rolling Updates o Canary deployments.
4. **Auto-recuperación (self-healing):** Si un contenedor falla, Kubernetes puede reiniciarlo o reemplazarlo automáticamente.
5. **Balanceo de carga y enrutamiento de tráfico:** Gestiona el tráfico entre instancias de contenedores.
6. **Gestión de configuración y secretos:** Manejo seguro de contraseñas y claves API.
7. **Monitoreo y registros centralizados:** Integración con sistemas como Prometheus y ELK Stack.

---

### **Resumen:**

#### **Frontend:**
- React
- React Native
- React Native Web
- Nest.js
- Vue
- Angular
- Nuxt.js
- Remix

#### **Backend:**
- Spring Boot
- ASP .NET 8

#### **Base de Datos:**
- MongoDB
- Supabase

#### **Infraestructura:**
- Docker
- Kubernetes
- Cloud Services (AWS, Azure, Google Cloud)
- Kafka
- RabbitMQ

### **Lenguajes utilizados:**

#### **Backend (Spring Boot)**
✅ **Lenguajes:**
- Kotlin o Java (para la lógica del backend).
- SQL (si usas PostgreSQL, MySQL).
- YAML/JSON (para configuración en `application.yml` o `application.properties`).
- Dockerfile (si lo despliegas con contenedores).

#### **Frontend (Next.js con React)**
✅ **Lenguajes:**
- JavaScript o TypeScript (recomendado por seguridad y escalabilidad).
- HTML + CSS (para el diseño de la interfaz).
- TailwindCSS (opcional, para estilos más rápidos).

#### **Base de Datos (PostgreSQL o MySQL)**
✅ **Lenguajes:**
- SQL (para consultas y estructuras de datos).
- GraphQL (opcional si decides usarlo en lugar de REST).

#### **Infraestructura y Despliegue**
✅ **Lenguajes:**
- Dockerfile (si usas contenedores en Docker).
- YAML (si usas Kubernetes o CI/CD como GitHub Actions).
- Bash/Shell (para comandos de despliegue en la nube).

---

### **Conclusión:**
Debido a la familiarización con **Spring Boot** para el backend, se utilizarán los lenguajes **Kotlin** para el código y **SQL** para realizar consultas con la base de datos (en caso de usar Supabase).

Para la construcción de la app web, se utilizará **TypeScript** por su seguridad y escalabilidad en comparación con JavaScript. La interfaz se desarrollará con **HTML + TailwindCSS**, complementado con CSS convencional para mayor flexibilidad.

Tailwind cuenta con **PurgeCSS**, que elimina las clases CSS no utilizadas para optimizar el archivo final y mejorar la carga en el navegador. Si se usan clases dinámicamente, se deberá configurar manualmente para evitar su eliminación.

Para la infraestructura y despliegue, se utilizará **Docker** para la creación de contenedores y **Kubernetes** para su gestión. Esto permitirá escalar automáticamente la aplicación, agregando o eliminando instancias según la demanda de tráfico.

#### **Componente → Lenguajes principales**

| Componente | Lenguajes |
|------------|----------------------------|
| **Backend (Spring Boot)** | Kotlin, SQL, JSON/YAML |
| **Frontend (Next.js + React + TailwindCSS)** | JavaScript / TypeScript, HTML, CSS |
| **Base de Datos (PostgreSQL/MySQL)** | SQL |
| **Infraestructura (Docker, CI/CD, Cloud)** | Dockerfile, YAML, Bash |
