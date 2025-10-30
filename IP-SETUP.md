# 🌐 ProLink - Configuración Multi-IP

## 📋 Cambios Realizados

Se ha actualizado ProLink para que funcione desde cualquier IP. Solo necesitas cambiar **2 líneas** en el código.

## 🔧 Configuración Rápida

### **Paso 1: Configurar Frontend**
En `prolink-app.html` líneas 16-17, cambia:

```javascript
const SERVER_IP = 'localhost';     // ← Cambiar por tu IP
const SERVER_PORT = '8080';
```

### **Ejemplos:**
```javascript
// Para red local:
const SERVER_IP = '192.168.1.100';

// Para servidor remoto:
const SERVER_IP = 'mi-servidor.com';
```

### **Paso 2: Desplegar**
```bash
# Recompilar backend (solo una vez)
cd prolink-backend && mvn clean package -DskipTests

# Copiar frontend actualizado
sudo cp prolink-app.html /var/www/html/prolink/

# Iniciar backend (acepta conexiones desde cualquier IP)
java -jar target/prolink-backend-1.0.0.jar --spring.profiles.active=h2
```

## 🎯 URLs Resultantes

Después de cambiar `SERVER_IP` a tu IP:
- **App:** `http://TU_IP/prolink/prolink-app.html`
- **API:** `http://TU_IP:8080/api/v1/`
- **Base de datos:** `http://TU_IP:8080/api/v1/h2-console`

## ✅ Verificación

```bash
# Probar conectividad
curl http://TU_IP:8080/api/v1/auth/test
```

## 📱 Acceso Móvil

Ahora puedes acceder desde cualquier dispositivo en la red usando la IP configurada.

¡ProLink es completamente portable! 🚀