# 🔗 ProLink - Professional LinkedIn Clone

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Oracle](https://img.shields.io/badge/Oracle-21c%20XE-red.svg)](https://www.oracle.com/database/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3.2-purple.svg)](https://getbootstrap.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 🎯 **Descripción**

ProLink es un clon completo y funcional de LinkedIn desarrollado con tecnologías modernas. Incluye sistema de autenticación JWT, gestión de posts, networking profesional y una interfaz de usuario responsive que replica la experiencia de LinkedIn.

## ✨ **Características Principales**

- 🔐 **Autenticación JWT**: Sistema de login/registro seguro
- 📝 **Gestión de Posts**: Crear, editar, eliminar y dar likes
- 🌐 **Networking**: Sistema de conexiones profesionales
- 💬 **Mensajería**: Chat entre usuarios (UI implementada)
- 🔔 **Notificaciones**: Sistema de alertas y actividades
- 👤 **Perfiles**: Gestión completa de perfiles profesionales
- 📱 **Responsive**: Diseño adaptable a todos los dispositivos
- 🏢 **Base de Datos Empresarial**: Oracle Database 21c Express

## 🏗️ **Arquitectura Técnica**

### **Frontend**
- **HTML5 + CSS3 + Vanilla JavaScript**
- **Bootstrap 5.3.2** para UI responsiva
- **Font Awesome** para iconografía
- **Fetch API** para comunicación con backend

### **Backend**
- **Spring Boot 3.1.5** (Java 21)
- **Spring Security** con JWT
- **Spring Data JPA** con Hibernate
- **Oracle JDBC Driver**
- **Maven** para gestión de dependencias

### **Base de Datos**
- **Oracle Database 21c Express Edition**
- **Docker** para contenedorización
- **Esquema relacional** optimizado

## 🚀 **Inicio Rápido**

### **Prerrequisitos**
- Java 21+
- Maven 3.6+
- Docker Desktop
- Python 3.x (para servidor de desarrollo)

### **Instalación**

1. **Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/prolink.git
cd prolink
```

2. **Configurar Oracle Database**
```bash
# Iniciar contenedor Oracle
docker run --name oracle-xe-prolink \
  -p 1521:1521 -p 5500:5500 \
  -e ORACLE_PWD=ProLink123 \
  -d container-registry.oracle.com/database/express:21.3.0-xe

# Esperar a que inicie (2-3 minutos)
./setup-oracle-auto.sh
```

3. **Compilar y ejecutar backend**
```bash
cd prolink-backend
mvn clean compile
mvn spring-boot:run
```

4. **Iniciar frontend**
```bash
# En otra terminal, desde la raíz del proyecto
python3 -m http.server 3000
```

5. **Acceder a la aplicación**
- Frontend: http://localhost:3000/prolink-app.html
- Backend API: http://localhost:8080/api/v1/
- Health Check: http://localhost:8080/api/v1/auth/health

### **Demo Rápido**
1. Haz clic en "Demo Rápido" para prellenar credenciales
2. Credenciales: `testuser` / `password123`
3. ¡Explora todas las funcionalidades!

## 📡 **API Endpoints**

### **Autenticación**
- `POST /api/v1/auth/register` - Registro de usuario
- `POST /api/v1/auth/login` - Iniciar sesión
- `GET /api/v1/auth/health` - Estado del servicio

### **Posts**
- `GET /api/v1/posts` - Obtener posts
- `POST /api/v1/posts` - Crear post
- `DELETE /api/v1/posts/{id}` - Eliminar post
- `POST /api/v1/posts/{id}/like` - Like/Unlike

### **Usuarios**
- `GET /api/v1/users/profile` - Perfil actual
- `GET /api/v1/users/suggestions` - Sugerencias

## 🗂️ **Estructura del Proyecto**

```
prolink/
├── README.md                     # Este archivo
├── prolink-app.html             # Frontend principal
├── prolink-backend/             # Backend Spring Boot
│   ├── src/main/java/com/prolink/
│   │   ├── ProLinkApplication.java
│   │   ├── controller/          # REST Controllers
│   │   ├── entity/             # JPA Entities
│   │   ├── repository/         # Data Access
│   │   ├── service/            # Business Logic
│   │   └── security/           # JWT & Security
│   ├── pom.xml                 # Maven dependencies
│   └── src/main/resources/
│       └── application.properties
├── create-tables-oracle.sql    # Schema database
├── setup-oracle-auto.sh        # Script de configuración
└── start-prolink.sh           # Script de inicio
```

## 🎨 **Capturas de Pantalla**

### Dashboard Principal
![Dashboard](screenshots/dashboard.png)

### Sistema de Autenticación
![Login](screenshots/login.png)

### Gestión de Posts
![Posts](screenshots/posts.png)

## 🔧 **Configuración Avanzada**

### **Variables de Entorno**
```properties
# application.properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XEPDB1
spring.datasource.username=prolink
spring.datasource.password=ProLink123
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
jwt.secret=tu-clave-secreta-512-bits
jwt.expiration=86400000
```

### **Docker Compose** (Opcional)
```yaml
version: '3.8'
services:
  oracle-db:
    image: container-registry.oracle.com/database/express:21.3.0-xe
    ports:
      - "1521:1521"
      - "5500:5500"
    environment:
      - ORACLE_PWD=ProLink123
    volumes:
      - oracle_data:/opt/oracle/oradata
volumes:
  oracle_data:
```

## 🧪 **Testing**

### **Ejecutar Tests**
```bash
cd prolink-backend
mvn test
```

### **Usuarios de Prueba**
- Usuario: `testuser`
- Contraseña: `password123`
- Email: `testuser@prolink.com`

## 🚀 **Despliegue a Producción**

### **Compilar para Producción**
```bash
cd prolink-backend
mvn clean package -DskipTests
java -jar target/prolink-backend-1.0.0.jar
```

### **Consideraciones**
- Configurar variables de entorno para producción
- Usar certificados SSL/TLS
- Configurar proxy reverso (nginx)
- Monitoreo y logging

## 🤝 **Contribución**

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

## 📋 **Roadmap**

- [ ] WebSocket para chat en tiempo real
- [ ] Carga de archivos e imágenes
- [ ] Notificaciones push
- [ ] Sistema de recomendaciones
- [ ] Analytics dashboard
- [ ] Aplicación móvil

## 🐛 **Issues Conocidos**

- WebSocket chat está en desarrollo
- Subida de imágenes pendiente de implementar
- Optimización de queries para grandes volúmenes

## 📄 **Licencia**

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👨‍💻 **Autor**

**Tu Nombre**
- GitHub: [@tu-usuario](https://github.com/tu-usuario)
- LinkedIn: [Tu Perfil](https://linkedin.com/in/tu-perfil)

## 🙏 **Agradecimientos**

- [Spring Boot](https://spring.io/projects/spring-boot) por el framework backend
- [Bootstrap](https://getbootstrap.com/) por los componentes UI
- [Oracle](https://www.oracle.com/) por la base de datos
- [LinkedIn](https://linkedin.com) por la inspiración de diseño

---

**⭐ Si este proyecto te fue útil, ¡no olvides darle una estrella!**