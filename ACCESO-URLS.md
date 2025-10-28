# 🌐 URLs de Acceso a ProLink

## 📍 **Tu IP del servidor:** `192.168.0.14`

## 🔗 **URLs Locales (desde el mismo servidor):**
- **Aplicación Principal:** http://localhost/prolink/prolink-app.html
- **Página de Inicio:** http://localhost/prolink/index.html  
- **API Backend:** http://localhost:8080/
- **Swagger API Docs:** http://localhost:8080/swagger-ui.html

## 🌍 **URLs desde Internet/Red Local:**
- **Aplicación Principal:** http://192.168.0.14/prolink/prolink-app.html
- **Página de Inicio:** http://192.168.0.14/prolink/index.html
- **Carpeta del Proyecto:** http://192.168.0.14/prolink/

## 📱 **Acceso desde otros dispositivos en la misma red:**
Cualquier dispositivo conectado a tu misma red WiFi/LAN puede acceder usando:
```
http://192.168.0.14/prolink/
```

## 🔐 **Credenciales de Acceso:**
- **Usuario:** `admin@prolink.com`
- **Contraseña:** `admin123`

## 📁 **Estructura de Archivos:**
```
/var/www/html/prolink/
├── prolink-app.html        # Aplicación principal
├── index.html              # Página de bienvenida  
├── css/                    # Estilos CSS
├── js/                     # JavaScript
├── WEB-INF/               # Configuración JSP
└── .htaccess              # Configuración Apache
```

## ⚙️ **Comandos de Gestión:**
```bash
# Ver estado de todos los servicios
./prolink-lamp.sh status

# Iniciar ProLink
./prolink-lamp.sh start

# Detener ProLink  
./prolink-lamp.sh stop

# Reiniciar ProLink
./prolink-lamp.sh restart

# Ver logs del backend
./prolink-lamp.sh logs
```

## 🔧 **Configuración del Servidor:**
- **Apache2:** Puerto 80
- **Spring Boot Backend:** Puerto 8080  
- **Oracle Database:** Puerto 1521
- **Oracle SQL Developer Web:** Puerto 5500

## 📋 **Notas Importantes:**
1. **Acceso desde Internet:** Para acceso desde internet necesitarías configurar port forwarding en tu router
2. **Firewall:** Asegúrate que el puerto 80 esté abierto en el firewall
3. **Red Local:** Funciona automáticamente en tu red local
4. **HTTPS:** Para producción real, configura SSL/HTTPS

---
**¡ProLink está funcionando correctamente en tu servidor LAMP!** 🚀