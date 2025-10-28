# 🤝 Contribuyendo a ProLink

¡Gracias por tu interés en contribuir a ProLink! Este documento proporciona pautas para contribuir al proyecto.

## 📋 Código de Conducta

Este proyecto se adhiere a un código de conducta. Al participar, se espera que mantengas este código.

## 🚀 ¿Cómo contribuir?

### Reportar Bugs

1. Usa la plantilla de issue para bugs
2. Incluye pasos detallados para reproducir
3. Especifica tu entorno (OS, Java version, etc.)
4. Adjunta logs relevantes

### Sugerir Mejoras

1. Usa la plantilla de issue para features
2. Explica claramente el caso de uso
3. Proporciona mockups si aplica
4. Discute la implementación propuesta

### Enviar Pull Requests

#### Preparación
1. Fork el repositorio
2. Crea una rama desde `main`:
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```
3. Configura tu entorno de desarrollo local

#### Desarrollo
1. Sigue las convenciones de código existentes
2. Escribe tests para nuevas funcionalidades
3. Actualiza la documentación si es necesario
4. Ejecuta los tests localmente:
   ```bash
   cd prolink-backend
   mvn test
   ```

#### Commit Guidelines
Usa conventional commits:
- `feat:` nueva funcionalidad
- `fix:` corrección de bug
- `docs:` cambios en documentación
- `style:` formateo, espacios, etc.
- `refactor:` refactorización de código
- `test:` agregar o modificar tests
- `chore:` cambios en build process, etc.

Ejemplo:
```
feat: agregar sistema de notificaciones push

- Implementar WebSocket para notificaciones en tiempo real
- Agregar endpoints REST para gestión de notificaciones
- Actualizar UI con componente de notificaciones
```

#### Envío
1. Push a tu fork:
   ```bash
   git push origin feature/nueva-funcionalidad
   ```
2. Crear Pull Request desde GitHub
3. Llenar la plantilla de PR completamente
4. Enlazar issues relacionados

## 🏗️ Estructura de Desarrollo

### Backend (Spring Boot)
```
src/main/java/com/prolink/
├── controller/     # REST endpoints
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos
├── entity/         # Modelos JPA
├── security/       # JWT y autenticación
├── config/         # Configuraciones
└── dto/           # Data Transfer Objects
```

### Frontend
- Vanilla JavaScript modular
- Bootstrap para componentes UI
- Fetch API para comunicación backend

### Base de Datos
- Oracle 21c Express Edition
- Scripts SQL en `/sql/`
- Migraciones versionadas

## 🧪 Testing

### Backend Tests
```bash
# Unit tests
mvn test

# Integration tests
mvn verify

# Test específico
mvn test -Dtest=UserServiceTest
```

### Frontend Tests
```bash
# Servir frontend para testing
python3 -m http.server 3000

# Testing manual con test-buttons.html
```

## 📝 Estándares de Código

### Java (Backend)
- Seguir Java Code Conventions
- Usar Lombok para reducir boilerplate
- Documentar métodos públicos con Javadoc
- Máximo 120 caracteres por línea

### JavaScript (Frontend)
- Usar ES6+ features
- Funciones arrow cuando sea apropiado
- Nombres descriptivos para variables/funciones
- Comentarios para lógica compleja

### SQL
- Nombres de tabla en snake_case
- Columnas descriptivas
- Índices para queries frecuentes
- Constraints y foreign keys apropiadas

## 🔧 Configuración de Desarrollo

### Prerrequisitos
- Java 21+
- Maven 3.6+
- Docker Desktop
- Git
- IDE (IntelliJ IDEA recomendado)

### Setup Local
```bash
# Clonar tu fork
git clone https://github.com/tu-usuario/prolink.git
cd prolink

# Configurar upstream
git remote add upstream https://github.com/usuario-original/prolink.git

# Configurar Oracle Database
docker-compose up oracle-db

# Compilar backend
cd prolink-backend
mvn clean compile

# Ejecutar tests
mvn test
```

## 📚 Recursos Útiles

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Oracle Database Documentation](https://docs.oracle.com/en/database/)
- [Bootstrap Documentation](https://getbootstrap.com/docs/)
- [JWT Best Practices](https://tools.ietf.org/html/rfc7519)

## ❓ Preguntas Frecuentes

### ¿Cómo configuro el entorno de desarrollo?
Sigue la sección "Setup Local" arriba.

### ¿Puedo usar una base de datos diferente?
Actualmente solo soportamos Oracle, pero aceptamos PRs para otros motores.

### ¿Hay roadmap del proyecto?
Revisa los issues con label "enhancement" y los milestones.

### ¿Cómo reporto vulnerabilidades de seguridad?
Envía un email privado en lugar de crear un issue público.

## 🎉 Reconocimientos

Los contribuyentes serán listados en:
- README.md principal
- Releases notes
- Contributors page (próximamente)

¡Gracias por hacer ProLink mejor! 🚀