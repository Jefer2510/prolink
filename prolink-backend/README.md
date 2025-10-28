# 🚀 ProLink Backend - LinkedIn Clone

## 📋 Descripción
ProLink es un clon de LinkedIn desarrollado con **Spring Boot** y **Java 21**, diseñado para ser desplegado en **Google Cloud Platform**.

## 🏗️ Arquitectura del Proyecto

```
prolink-backend/
├── src/main/java/com/prolink/
│   ├── config/          # Configuraciones de Spring
│   ├── controller/      # Controladores REST
│   ├── dto/            # Data Transfer Objects
│   ├── entity/         # Entidades JPA
│   ├── exception/      # Manejo de excepciones
│   ├── repository/     # Repositorios JPA
│   ├── service/        # Lógica de negocio
│   ├── security/       # Configuración de seguridad JWT
│   └── util/           # Utilidades
└── src/main/resources/
    ├── application.properties          # Configuración local (Oracle)
    └── application-gcp.properties      # Configuración producción (PostgreSQL)
```

## 🛠️ Tecnologías Utilizadas

### Backend Core
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.2.0** - Framework principal
- **Maven** - Gestión de dependencias

### Base de Datos
- **Oracle Database XE** - Desarrollo local (Docker)
- **PostgreSQL** - Producción en GCP Cloud SQL

### Seguridad
- **Spring Security** - Autenticación y autorización
- **JWT (JSON Web Tokens)** - Manejo de sesiones
- **BCrypt** - Encriptación de contraseñas

### APIs y Comunicación
- **Spring Web** - APIs REST
- **WebSocket** - Chat en tiempo real
- **OpenAPI/Swagger** - Documentación de APIs

### Cloud & DevOps
- **Docker** - Contenedores
- **Google Cloud Storage** - Almacenamiento de archivos
- **Google Cloud SQL** - Base de datos en la nube

## 🚀 Funcionalidades Principales

### 👤 Gestión de Usuarios
- [x] Registro y login seguro
- [x] Perfiles de usuario completos
- [x] Roles y permisos
- [x] Autenticación JWT

### 📱 Red Social
- [x] Timeline de publicaciones
- [x] Crear, editar y eliminar posts
- [x] Sistema de likes y comentarios
- [x] Conexiones entre usuarios
- [x] Solicitudes de conexión (enviar, aceptar, rechazar)

### 💬 Mensajería
- [x] Chat privado entre usuarios
- [x] Mensajes en tiempo real (WebSocket/STOMP)
- [x] Historial de conversaciones
- [x] Indicadores de escritura
- [x] Marcado de mensajes como leídos

### 🔍 Búsqueda y Filtros
- [x] Búsqueda de usuarios
- [x] Paginación en resultados
- [x] API REST completa para todos los endpoints

## 🔧 Configuración del Entorno

### Prerequisitos
- Java 21+
- Maven 3.9+
- Docker
- Oracle Database XE (Docker)

### Configuración Local

1. **Oracle Database** (Ya configurado con Docker):
```bash
docker ps  # Verificar que oracle-xe-prolink esté corriendo
```

2. **Configuración Base de Datos**:
- **Host**: localhost:1521
- **Usuario**: system  
- **Contraseña**: ProLink2024!
- **SID**: XE

### Variables de Entorno para Producción

```bash
# Base de datos
DB_USERNAME=tu_usuario_gcp
DB_PASSWORD=tu_password_seguro

# JWT
JWT_SECRET=tu_jwt_secret_muy_seguro

# Google Cloud
GCP_STORAGE_BUCKET=prolink-files-bucket

# CORS
CORS_ORIGINS=https://tu-dominio.com
```

## 🚀 Ejecución

### Desarrollo Local
```bash
cd prolink-backend
mvn clean install
mvn spring-boot:run
```

### Perfil de Producción
```bash
mvn spring-boot:run -Dspring.profiles.active=gcp
```

## 📚 APIs Principales

### 🔐 Autenticación
- `POST /api/v1/auth/register` - Registro de usuario
- `POST /api/v1/auth/login` - Login
- `POST /api/v1/auth/refresh` - Refresh token

### 👤 Usuarios  
- `GET /api/v1/users/profile` - Perfil del usuario
- `PUT /api/v1/users/profile` - Actualizar perfil
- `GET /api/v1/users/search` - Buscar usuarios

### 📝 Posts
- `GET /api/v1/posts/timeline` - Timeline de posts
- `POST /api/v1/posts` - Crear post
- `POST /api/v1/posts/{id}/like` - Like a post

### 💬 Mensajes
- `GET /messages/conversations` - Lista de conversaciones
- `GET /messages/conversation/{userId}` - Mensajes con usuario específico
- `PUT /messages/conversation/{userId}/read` - Marcar mensajes como leídos
- `WebSocket /ws` - Conexión WebSocket para chat en tiempo real

## 📚 Documentación API (Swagger)

Una vez que la aplicación esté corriendo, puedes acceder a la documentación interactiva:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Características de la Documentación:
- ✅ Todos los endpoints documentados
- ✅ Autenticación JWT integrada
- ✅ Ejemplos de peticiones y respuestas  
- ✅ Esquemas de datos detallados
- ✅ Posibilidad de probar endpoints directamente

## 🧪 Testing

```bash
# Ejecutar todos los tests
mvn test

# Tests específicos
mvn test -Dtest=UserServiceTest
```

## 🐳 Docker (Para despliegue)

```dockerfile
# Dockerfile estará aquí para GCP deployment
FROM openjdk:21-jdk-slim
COPY target/prolink-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🌐 Despliegue en GCP

### Cloud Run
```bash
gcloud run deploy prolink-backend \
  --source . \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated
```

## 📈 Próximos Pasos

1. **Implementar entidades JPA** ✅ (Siguiente)
2. **Configurar Spring Security + JWT** 
3. **Crear APIs REST básicas**
4. **Implementar WebSocket para chat**
5. **Añadir tests unitarios**
6. **Configurar CI/CD para GCP**

## 🤝 Contribución

Este proyecto está en desarrollo activo. Las contribuciones son bienvenidas.

## 📄 Licencia

MIT License - Ver archivo LICENSE para más detalles.

---

**Estado del Proyecto**: 🚧 En Desarrollo Activo  
**Versión**: 1.0.0  
**Última actualización**: Octubre 2024