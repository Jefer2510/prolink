# 🖥️ Configuración LAMP Server - ProLink

## 📋 Resumen de la Configuración

**ProLink** está ahora desplegado en un servidor LAMP (Linux + Apache2 + MySQL/Oracle + PHP) con las siguientes características:

### 🏗️ **Arquitectura del Despliegue**
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Apache2       │    │   Spring Boot    │    │   Oracle DB     │
│   :80           │───▶│   Backend        │───▶│   Container     │
│   Frontend      │    │   :8080          │    │   :1521         │
│   Static Files  │    │   REST API       │    │   XE Database   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

### 📁 **Estructura de Archivos**
```
/var/www/html/                    # Apache Document Root
├── index.html                    # Página principal de ProLink
├── prolink-app.html             # Aplicación principal
├── prolink-demo.html            # Demo interactivo
├── css/                         # Estilos CSS
├── js/                          # JavaScript + Configuración
│   ├── config.js               # Config para producción
│   └── websocket-chat.js       # WebSocket chat
└── WEB-INF/                     # Recursos Java Web

/etc/apache2/sites-available/
└── prolink.conf                 # Virtual Host configuration
```

## ⚙️ **Configuración de Apache2**

### **Virtual Host (prolink.conf)**
```apache
<VirtualHost *:80>
    ServerName prolink.local
    ServerAlias www.prolink.local localhost
    DocumentRoot /var/www/html
    
    # Directorio principal
    <Directory /var/www/html>
        Options Indexes FollowSymLinks
        AllowOverride All
        Require all granted
        DirectoryIndex index.html prolink-app.html
    </Directory>
    
    # Proxy reverso para API del backend
    ProxyPreserveHost On
    ProxyPass /api/ http://localhost:8080/
    ProxyPassReverse /api/ http://localhost:8080/
    
    # Headers para CORS
    Header always set Access-Control-Allow-Origin "*"
    Header always set Access-Control-Allow-Methods "GET, POST, PUT, DELETE, OPTIONS"
    Header always set Access-Control-Allow-Headers "Content-Type, Authorization"
    
    # Logs
    ErrorLog ${APACHE_LOG_DIR}/prolink_error.log
    CustomLog ${APACHE_LOG_DIR}/prolink_access.log combined
</VirtualHost>
```

### **Módulos habilitados:**
- `mod_proxy` - Proxy reverso al backend
- `mod_proxy_http` - Proxy HTTP
- `mod_headers` - Headers personalizados
- `mod_rewrite` - URL rewriting

## 🚀 **Gestión del Servidor**

### **Script de gestión: `prolink-lamp.sh`**
```bash
# Iniciar todos los servicios
./prolink-lamp.sh start

# Ver estado de servicios
./prolink-lamp.sh status

# Detener ProLink (mantiene Apache)
./prolink-lamp.sh stop

# Reiniciar ProLink
./prolink-lamp.sh restart

# Ver logs del backend
./prolink-lamp.sh logs
```

### **Comandos Apache básicos:**
```bash
# Reiniciar Apache
sudo systemctl restart apache2

# Ver estado
sudo systemctl status apache2

# Ver logs de error
sudo tail -f /var/log/apache2/prolink_error.log

# Ver logs de acceso
sudo tail -f /var/log/apache2/prolink_access.log
```

## 🗄️ **Base de Datos Oracle**

### **Docker Container:**
```bash
# Estado del container
docker ps | grep oracle

# Logs de la base de datos
docker logs oracle-xe-prolink

# Iniciar/detener Oracle
docker compose -f docker-compose-simple.yml up -d
docker compose -f docker-compose-simple.yml down
```

### **Conexión a la BD:**
```
Host: localhost
Puerto: 1521
Servicio: XE
Usuario: prolink_user
Contraseña: prolink123
```

## 🌐 **URLs de Acceso**

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Frontend Principal** | http://localhost/ | Página de inicio |
| **App ProLink** | http://localhost/prolink-app.html | Aplicación completa |
| **Demo** | http://localhost/prolink-demo.html | Demo interactivo |
| **Backend API** | http://localhost:8080/ | REST API directa |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentación API |
| **H2 Console** | http://localhost:8080/h2-console | Console BD (desarrollo) |
| **Oracle EM** | http://localhost:5500/em | Oracle Enterprise Manager |

## 🔐 **Credenciales por Defecto**

### **Aplicación ProLink:**
- **Usuario:** admin@prolink.com
- **Contraseña:** admin123

### **Oracle Database:**
- **SYS:** ProLink123
- **Usuario App:** prolink_user / prolink123

## 📊 **Monitoreo y Logs**

### **Logs principales:**
```bash
# Backend Spring Boot
tail -f prolink-backend.log

# Apache Access
sudo tail -f /var/log/apache2/prolink_access.log

# Apache Error
sudo tail -f /var/log/apache2/prolink_error.log

# Oracle Database
docker logs -f oracle-xe-prolink
```

### **Verificación de servicios:**
```bash
# Estado completo
./prolink-lamp.sh status

# Procesos activos
ps aux | grep -E "(apache2|java|oracle)"

# Puertos en uso
netstat -tulpn | grep -E "(80|8080|1521)"
```

## 🛠️ **Troubleshooting**

### **Problemas comunes:**

**1. Apache no sirve archivos de ProLink:**
```bash
# Verificar permisos
sudo chown -R www-data:www-data /var/www/html/
sudo chmod -R 755 /var/www/html/

# Reiniciar Apache
sudo systemctl restart apache2
```

**2. Backend no responde:**
```bash
# Verificar si está corriendo
ps aux | grep spring-boot

# Reiniciar backend
./prolink-lamp.sh restart

# Ver logs
./prolink-lamp.sh logs
```

**3. Error de conexión a BD:**
```bash
# Verificar Oracle container
docker ps | grep oracle

# Reiniciar Oracle
docker compose -f docker-compose-simple.yml restart
```

**4. Error 502 Bad Gateway:**
```bash
# El backend no está corriendo
./prolink-lamp.sh start

# Verificar configuración del proxy
sudo apache2ctl configtest
```

## 🔄 **Actualizaciones y Mantenimiento**

### **Actualizar código:**
```bash
# 1. Hacer pull del repositorio
git pull origin main

# 2. Recompilar backend
cd prolink-backend
mvn clean install

# 3. Copiar frontend actualizado
sudo cp -r prolink-frontend/src/main/webapp/* /var/www/html/

# 4. Reiniciar servicios
./prolink-lamp.sh restart
```

### **Backup de la configuración:**
```bash
# Backup virtual host
sudo cp /etc/apache2/sites-available/prolink.conf ~/prolink-backup/

# Backup archivos web
sudo tar -czf ~/prolink-web-backup.tar.gz /var/www/html/

# Backup base de datos
docker exec oracle-xe-prolink expdp system/ProLink123@XE \
  directory=DATA_PUMP_DIR dumpfile=prolink_backup.dmp \
  schemas=prolink_user
```

## 🚀 **Optimizaciones de Producción**

### **Performance:**
```apache
# En prolink.conf
<Directory /var/www/html>
    # Cache estático
    ExpiresActive On
    ExpiresByType text/css "access plus 1 month"
    ExpiresByType application/javascript "access plus 1 month"
    ExpiresByType image/png "access plus 1 year"
    
    # Compresión
    SetOutputFilter DEFLATE
</Directory>
```

### **Seguridad:**
```apache
# Headers de seguridad
Header always set X-Frame-Options "SAMEORIGIN"
Header always set X-Content-Type-Options "nosniff"
Header always set X-XSS-Protection "1; mode=block"
```

---

**📝 Nota:** Esta configuración proporciona un entorno completo de desarrollo/producción para ProLink usando un stack LAMP tradicional con integración de Spring Boot y Oracle Database.