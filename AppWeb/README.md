# Proyecto API Web — Spring Boot + React + Supabase + JasperSoft

## Herramientas de desarrollo

### Backend: Spring Boot

**Descripción:**  
Spring Boot es un framework de desarrollo en Java que facilita la creación de aplicaciones web y APIs REST de forma rápida, estructurada y escalable.

**Ventajas:**
- Rápido desarrollo gracias a su configuración automática.  
- Integración nativa con **Spring Security** (autenticación y autorización).  
- Compatible con **Supabase (PostgreSQL)** mediante JDBC o JPA.  
- Alta escalabilidad y rendimiento.  
- Amplia comunidad y documentación.

**Inconvenientes:**
- Curva de aprendizaje inicial moderada.  
- Mayor consumo de memoria que frameworks ligeros.  
- Configuraciones avanzadas requieren experiencia.

---

### Frontend: React

**Descripción:**  
React es una biblioteca de JavaScript desarrollada por Meta para construir interfaces de usuario dinámicas e interactivas.

**Ventajas:**
- Renderizado eficiente mediante Virtual DOM.  
- Componentes reutilizables.  
- Integración sencilla con APIs REST.

**Inconvenientes:**
- Gestión de estado compleja en proyectos grandes.  
- SEO limitado sin SSR (Server Side Rendering).  
- Actualizaciones frecuentes del ecosistema.

---

### Herramienta de informes: JasperSoft

**Descripción:**  
JasperSoft (JasperReports) permite generar reportes empresariales personalizados en múltiples formatos (PDF, Excel, HTML, CSV).

**Ventajas:**
- Informes dinámicos y exportables.  
- Integración directa con Spring Boot.  
- Diseño visual con JasperSoft Studio.  

**Inconvenientes:**
- Requiere tiempo para dominar la creación de plantillas.  
- Informes grandes pueden afectar el rendimiento.  
- Mantenimiento adicional si cambian los modelos de datos.

---

## Seguridad en Spring Boot

La seguridad del backend se gestionará mediante **Spring Security** y **JWT (JSON Web Tokens)**.

**Medidas de seguridad:**
1. **Autenticación con JWT:**  
   - Token firmado que protege cada solicitud.
2. **Autorización basada en roles:**  
   - Roles: `ADMIN`, `USER`, `REPORT_VIEWER`.
3. **Cifrado de contraseñas:**  
   - Uso de `BCryptPasswordEncoder`.
4. **Protecciones adicionales:**  
   - Prevención de inyecciones SQL mediante JPA.  
   - Validación de datos de entrada.  

---

## Base de datos: Supabase

**Descripción:**  
Supabase es una plataforma **BaaS (Backend as a Service)** basada en **PostgreSQL**, que proporciona autenticación, almacenamiento y APIs automáticas.

**Ventajas:**
- Totalmente compatible con PostgreSQL.  
- Configuración rápida y sencilla.  
- Seguridad mediante políticas **Row-Level Security (RLS)**.  
- Entorno cloud administrado.  
- Panel de control visual.

**Inconvenientes:**
- Dependencia total de la conexión a internet.  
- Coste si se escala a planes superiores.

**Integración:**  
Conexión desde Spring Boot mediante **JDBC o JPA**, configurando las credenciales en el archivo `application.properties`.

---

## Despliegue

El proyecto se desplegará modularmente:

| Componente | Plataforma | Descripción |
|-------------|-------------|-------------|
| **Backend (Spring Boot)** | Render / Railway | API desplegada con integración a Supabase y JasperSoft. |
| **Frontend (React)** | Vercel / Netlify | Aplicación web conectada al backend vía HTTPS. |
| **Base de datos** | Supabase Cloud | PostgreSQL gestionado con copias de seguridad y RLS. |

**Ventajas:**
- Accesibilidad global.  
- Escalabilidad automática.  
- Seguridad y respaldo en la nube.  
- Pago por uso según demanda.

---

## Propuesta de calendario de desarrollo

| **Fase** | **Duración** | **Objetivos principales** |
|-----------|--------------|---------------------------|
| **1. Análisis y diseño** | 15 – 22 octubre | Requisitos, arquitectura y modelo de datos. |
| **2. Configuración inicial** | 23 – 31 octubre | Creación del proyecto y conexión a Supabase. |
| **3. Backend (API REST)** | 1 – 15 noviembre | Endpoints CRUD, validaciones y seguridad JWT. |
| **4. Frontend (React)** | 16 – 30 noviembre | Interfaz, componentes e integración con API. |
| **5. JasperSoft** | 1 – 8 diciembre | Creación de reportes y exportaciones. |
| **6. Pruebas e integración** | 9 – 16 diciembre | Pruebas unitarias, test de carga y QA. |
| **7. Despliegue y documentación** | 17 – 25 diciembre | Despliegue en la nube y documentación final. |

---

## Conclusión

Este proyecto combina tecnologías modernas, seguras y escalables:

- **Spring Boot** → Backend robusto y modular.  
- **React** → Frontend dinámico e intuitivo.  
- **Supabase** → Base de datos cloud basada en PostgreSQL.  
- **JasperSoft** → Generación de informes empresariales.  

---
