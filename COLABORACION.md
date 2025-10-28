# 🤝 Guía de Colaboración - ProLink

## 📋 Requisitos del Sistema

### Software necesario:
- **Java 21 o superior**
- **Maven 3.6+**
- **Docker & Docker Compose**
- **Git**
- **IDE recomendado:** IntelliJ IDEA o VS Code
- **Base de datos:** Oracle Database 21c XE (via Docker)

### Verificar instalaciones:
```bash
java --version
mvn --version
docker --version
docker-compose --version
git --version
```

## 🔧 Setup del Proyecto

### 1. Clonar el repositorio
```bash
# Clonar el proyecto
git clone https://github.com/Jefer2510/prolink.git
cd prolink
```

### 2. Configurar la base de datos
```bash
# Levantar Oracle Database con Docker
docker-compose up -d

# Verificar que esté corriendo
docker ps
```

### 3. Configurar variables de entorno
Crear archivo `.env` en la raíz del proyecto:
```env
# Database Configuration
DB_HOST=localhost
DB_PORT=1521
DB_SERVICE=XE
DB_USERNAME=prolink_user
DB_PASSWORD=prolink123

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-here-make-it-long-and-secure
JWT_EXPIRATION=86400000

# Server Configuration
SERVER_PORT=8080
```

### 4. Compilar el backend
```bash
cd prolink-backend
mvn clean install
```

### 5. Ejecutar el proyecto
```bash
# Opción 1: Usar script de inicio
./start-prolink.sh

# Opción 2: Manual
cd prolink-backend
mvn spring-boot:run

# En otra terminal para frontend
cd prolink-frontend
python3 -m http.server 3000
```

## 🌐 Acceso a la aplicación
- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html

### Credenciales de prueba:
- **Usuario:** admin@prolink.com
- **Contraseña:** admin123

## 🔄 Workflow de Desarrollo

### Configuración inicial de Git
```bash
# Configurar datos del usuario
git config user.name "Tu Nombre"
git config user.email "tu.email@ejemplo.com"

# Agregar upstream (repositorio original)
git remote add upstream https://github.com/Jefer2510/prolink.git
```

### Workflow recomendado (Git Flow)

#### 1. Crear una nueva rama para cada feature
```bash
# Actualizar rama main
git checkout main
git pull upstream main

# Crear nueva rama para feature
git checkout -b feature/nombre-de-la-feature
```

#### 2. Desarrollar y commitear
```bash
# Hacer cambios...
git add .
git commit -m "feat: descripción clara del cambio

- Detalle específico 1
- Detalle específico 2"
```

#### 3. Subir cambios y crear Pull Request
```bash
# Subir rama al fork
git push origin feature/nombre-de-la-feature
```

Luego crear **Pull Request** en GitHub desde tu fork al repositorio principal.

## 📁 Estructura del Proyecto

```
prolink/
├── prolink-backend/          # Spring Boot API
│   ├── src/main/java/        # Código Java
│   ├── src/main/resources/   # Configuraciones
│   └── pom.xml               # Dependencias Maven
├── prolink-frontend/         # Frontend web
│   └── src/main/webapp/      # HTML, CSS, JS
├── docker-compose.yml        # Configuración Docker
├── README.md                 # Documentación principal
├── DEPLOYMENT.md             # Guía de despliegue
└── COLABORACION.md           # Esta guía
```

## 🎯 Convenciones del Proyecto

### Commits (Conventional Commits):
- `feat:` Nueva funcionalidad
- `fix:` Corrección de bugs
- `docs:` Documentación
- `style:` Formato/estilo
- `refactor:` Refactorización
- `test:` Tests
- `chore:` Tareas de mantenimiento

### Naming:
- **Ramas:** `feature/nombre`, `bugfix/nombre`, `hotfix/nombre`
- **Clases Java:** PascalCase
- **Métodos:** camelCase
- **Variables:** camelCase
- **Constantes:** UPPER_SNAKE_CASE

## 🐛 Solución de Problemas Comunes

### Base de datos no conecta:
```bash
# Reiniciar containers
docker-compose down
docker-compose up -d

# Verificar logs
docker-compose logs oracle-db
```

### Puerto ocupado:
```bash
# Ver qué usa el puerto 8080
lsof -i :8080
kill -9 <PID>
```

### Maven problemas:
```bash
# Limpiar y reinstalar dependencias
mvn clean
rm -rf ~/.m2/repository
mvn install
```

## 📞 Contacto y Soporte

- **Issues:** Usar GitHub Issues para reportar bugs
- **Discusiones:** GitHub Discussions para preguntas
- **Code Review:** Mandatory para todos los PRs
- **Comunicación:** Comentar en commits y PRs

## 🔒 Permisos de Repositorio

### Para colaboradores directos:
1. El owner debe agregar como colaborador en GitHub
2. Permisos de `Write` para push directo a ramas

### Para contribuidores externos:
1. Fork del repositorio
2. Pull Request workflow
3. Code review obligatorio

## 🚀 Próximos pasos después del setup

1. **Revisar Issues abiertos** en GitHub
2. **Leer la documentación** completa en README.md
3. **Ejecutar tests** existentes
4. **Familiarizarse** con la arquitectura del proyecto
5. **Crear tu primera branch** y hacer un pequeño cambio de prueba

---

¡Bienvenido al equipo de desarrollo de ProLink! 🌟