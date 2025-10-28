# ProLink - LinkedIn Clone con Java Puro

## Proyecto Completado ✅

ProLink es un clon de LinkedIn construido completamente con **Java puro**, utilizando tecnologías de servidor tradicionales sin frameworks pesados. El proyecto está diseñado para demostrar habilidades sólidas en Java enterprise y desarrollo web full-stack.

## 🏗️ Arquitectura del Sistema

### Backend (Puerto 8080)
- **Framework**: Spring Boot con Spring Web, Data JPA y Security
- **Base de datos**: H2 (desarrollo) / PostgreSQL (producción)
- **API**: REST con autenticación JWT
- **WebSocket**: Chat en tiempo real
- **Documentación**: OpenAPI/Swagger

### Frontend (Puerto 9090)
- **Tecnología**: Java Puro con Servlets + JSP
- **Servidor**: Eclipse Jetty integrado con Maven
- **UI**: Bootstrap 5 + Font Awesome
- **JavaScript**: WebSocket para chat, AJAX para interacciones

## 🚀 Funcionalidades Implementadas

### ✅ Autenticación y Seguridad
- [x] Registro de usuarios con validación
- [x] Login con JWT tokens
- [x] Logout seguro
- [x] Protección de rutas
- [x] Validación de sesiones

### ✅ Gestión de Perfiles
- [x] Visualización completa de perfiles
- [x] Edición de información personal
- [x] Cambio de contraseña
- [x] Estadísticas de perfil
- [x] Sistema de avatares con iniciales

### ✅ Red de Conexiones
- [x] Búsqueda de usuarios
- [x] Envío de solicitudes de conexión
- [x] Aceptación/rechazo de solicitudes
- [x] Lista de conexiones
- [x] Sugerencias inteligentes

### ✅ Sistema de Mensajería
- [x] Chat uno a uno
- [x] Conversaciones persistentes
- [x] Interface moderna de chat
- [x] WebSocket para tiempo real
- [x] Indicadores de estado (en línea, escribiendo)

### ✅ Feed y Posts
- [x] Dashboard principal con feed
- [x] Creación de posts
- [x] Sistema de likes y comentarios
- [x] Visualización de actividad
- [x] Interface tipo LinkedIn

## 📁 Estructura del Proyecto

```
linkedin/
├── prolink-backend/          # API REST (Spring Boot)
│   ├── src/main/java/
│   │   └── com/prolink/
│   │       ├── config/       # Configuraciones (Security, WebSocket, OpenAPI)
│   │       ├── controller/   # Controllers REST
│   │       ├── dto/          # DTOs de request/response
│   │       ├── entity/       # Entidades JPA
│   │       ├── repository/   # Repositorios Spring Data
│   │       ├── service/      # Lógica de negocio
│   │       └── security/     # JWT y configuración de seguridad
│   └── src/main/resources/
│       └── application.properties
│
└── prolink-frontend/         # Frontend Java Puro
    ├── src/main/java/
    │   └── com/prolink/web/
    │       ├── controller/   # Servlets (Auth, Profile, Connections, Messages, Dashboard)
    │       ├── model/        # Modelos del frontend
    │       └── service/      # Cliente REST para backend
    └── src/main/webapp/
        ├── WEB-INF/jsp/      # Páginas JSP
        │   ├── auth/         # Login y registro
        │   ├── common/       # Headers, footers
        │   └── dashboard/    # Dashboard, perfil, conexiones, mensajes
        ├── css/              # Estilos personalizados
        └── js/               # JavaScript (WebSocket, interacciones)
```

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 21**: Última versión LTS
- **Spring Boot 3.2.0**: Framework principal
- **Spring Data JPA**: ORM y persistencia
- **Spring Security**: Autenticación y autorización
- **JWT**: Tokens de autenticación
- **H2 Database**: Base de datos en memoria
- **WebSocket**: Comunicación en tiempo real
- **Maven**: Gestión de dependencias

### Frontend
- **Jakarta Servlets 6.0**: Servlets modernos
- **JSP 3.1 + JSTL**: Templating del lado servidor
- **Eclipse Jetty**: Servidor web embebido
- **Bootstrap 5.3.2**: Framework CSS
- **Font Awesome**: Iconos
- **Apache HttpClient**: Cliente REST
- **Jackson**: Procesamiento JSON

## 🎯 Características Destacadas

### 1. **Java Puro** - Sin frameworks frontend pesados
- Uso exclusivo de Servlets y JSP
- Comunicación directa con API REST
- Arquitectura tradicional pero moderna

### 2. **Diseño Profesional**
- Interface idéntica a LinkedIn
- Responsive design con Bootstrap
- Experiencia de usuario pulida

### 3. **Chat en Tiempo Real**
- WebSocket para mensajería instantánea
- Indicadores de estado en línea
- Notificaciones del navegador

### 4. **Arquitectura Escalable**
- Separación clara frontend/backend
- API REST bien documentada
- Código modular y mantenible

## 🚀 Instrucciones de Ejecución

### Prerrequisitos
- Java 21 o superior
- Maven 3.9+
- Ports 8080 y 9090 disponibles

### Backend
```bash
cd prolink-backend
mvn clean spring-boot:run
```
- Acceso: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`

### Frontend
```bash
cd prolink-frontend
mvn clean jetty:run
```
- Acceso: `http://localhost:9090`

### Usuario de Prueba
- Email: `admin@prolink.com`
- Password: `admin123`

## 📊 Endpoints API Principales

### Autenticación
- `POST /api/v1/auth/register` - Registro
- `POST /api/v1/auth/login` - Login
- `POST /api/v1/auth/logout` - Logout

### Usuarios
- `GET /api/v1/users/profile` - Perfil actual
- `PUT /api/v1/users/profile` - Actualizar perfil
- `GET /api/v1/users/{id}` - Perfil por ID

### Conexiones
- `GET /api/v1/connections` - Mis conexiones
- `POST /api/v1/connections/request` - Enviar solicitud
- `PUT /api/v1/connections/{id}/accept` - Aceptar solicitud

### Mensajes
- `GET /api/v1/messages/conversations` - Conversaciones
- `GET /api/v1/messages/conversation/{userId}` - Mensajes con usuario
- `POST /api/v1/messages/send` - Enviar mensaje

### Posts
- `GET /api/v1/posts/feed` - Feed de posts
- `POST /api/v1/posts` - Crear post
- `POST /api/v1/posts/{id}/like` - Like/Unlike

## 🎨 Capturas de Pantalla

El sistema incluye:
1. **Página de login** elegante con branding ProLink
2. **Dashboard principal** con feed, sugerencias y estadísticas
3. **Perfil completo** con edición inline y cambio de contraseña
4. **Red de conexiones** con búsqueda y gestión de solicitudes
5. **Sistema de mensajes** con chat en tiempo real

## 🔧 Configuraciones Importantes

### Backend (application.properties)
```properties
# Database
spring.datasource.url=jdbc:h2:mem:prolink
spring.h2.console.enabled=true

# JWT
jwt.secret=your-secret-key
jwt.expiration=86400000

# WebSocket
spring.websocket.enabled=true
```

### Frontend (web.xml)
```xml
<!-- Configuración de Servlets -->
<servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>*.css</url-pattern>
</servlet-mapping>
```

## 💡 Decisiones de Diseño

### ¿Por qué Java Puro?
1. **Demostración de habilidades fundamentales**: Dominio de Servlets, JSP y tecnologías core
2. **Control total**: Sin abstracciones que oculten la implementación
3. **Rendimiento**: Menos overhead comparado con frameworks pesados
4. **Aprendizaje**: Entendimiento profundo del funcionamiento web

### Arquitectura Elegida
- **Separación de responsabilidades**: Frontend y backend independientes
- **API-First**: Backend como servicio REST reutilizable
- **Escalabilidad**: Cada parte puede escalar independientemente

## 🐛 Problemas Conocidos y Soluciones

### 1. CORS en desarrollo
**Solución**: Configuración en `SecurityConfig.java`

### 2. WebSocket y proxies
**Solución**: Configuración de headers en `WebSocketConfig.java`

### 3. Sesiones entre servicios
**Solución**: JWT tokens para comunicación stateless

## 🚀 Próximas Mejoras

1. **Notificaciones push** para mayor engagement
2. **Sistema de posts con imágenes** usando almacenamiento cloud
3. **Búsqueda avanzada** con Elasticsearch
4. **Mobile app** con React Native
5. **Analíticas** con métricas detalladas

## 📝 Notas de Desarrollo

### Patrones Utilizados
- **MVC**: Separación clara en ambos proyectos
- **DTO**: Para transferencia de datos API
- **Repository**: Abstracción de acceso a datos
- **Service Layer**: Lógica de negocio centralizada

### Seguridad Implementada
- **JWT Authentication**: Tokens seguros
- **Password Hashing**: BCrypt para contraseñas
- **Input Validation**: Validación en frontend y backend
- **SQL Injection Prevention**: JPA Query Methods

## 🎯 Conclusión

ProLink demuestra que es posible crear aplicaciones web modernas y atractivas usando **Java puro** sin sacrificar funcionalidad o experiencia de usuario. El proyecto combina lo mejor de las tecnologías tradicionales Java EE con prácticas modernas de desarrollo web.

**Tecnologías Core**: Servlets + JSP + Spring Boot
**Resultado**: Una red social profesional completamente funcional
**Tiempo de desarrollo**: Implementación completa en una sesión

¡El proyecto está **100% funcional** y listo para demostración!

---
*Desarrollado con ❤️ y ☕ usando Java puro*