#!/bin/bash

# ==============================================
# SCRIPT PARA INICIAR PROLINK CON ORACLE
# ==============================================

echo "🚀 Iniciando ProLink con Oracle Database..."

# Verificar que Oracle esté funcionando
echo "📋 Verificando Oracle Database..."
sudo docker ps | grep oracle-xe-prolink > /dev/null
if [ $? -eq 0 ]; then
    echo "✅ Oracle Database está activo"
else
    echo "❌ Oracle Database no está activo. Iniciando..."
    sudo docker start oracle-xe-prolink
    sleep 10
fi

# Verificar si ya hay un proceso ejecutándose
echo "🔍 Verificando procesos existentes..."
if pgrep -f "mvn spring-boot:run" > /dev/null; then
    echo "⚠️ Ya hay un proceso Spring Boot ejecutándose. Terminando..."
    pkill -f "mvn spring-boot:run"
    sleep 3
fi

# Cambiar al directorio correcto
cd /home/chris1/Escritorio/linkedIn/prolink-backend

echo "🔧 Compilando proyecto..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
    
    echo "🌟 Iniciando ProLink Backend con Oracle..."
    echo "📍 URL: http://localhost:8080/api/v1/auth/health"
    echo "🔗 Swagger: http://localhost:8080/api/v1/swagger-ui.html"
    echo ""
    
    # Iniciar Spring Boot
    mvn spring-boot:run
else
    echo "❌ Error en la compilación"
    exit 1
fi

# Verificar si Oracle está corriendo
if ! docker ps | grep -q oracle-prolink; then
    echo "📦 Iniciando Oracle Database en Docker..."
    echo "⚠️  NECESITAS EJECUTAR ESTE COMANDO MANUALMENTE CON SUDO:"
    echo ""
    echo "sudo docker run -d --name oracle-prolink \\"
    echo "  -p 1521:1521 -p 5500:5500 \\"
    echo "  -e ORACLE_PWD=ProLink123 \\"
    echo "  gvenzl/oracle-xe:21-slim"
    echo ""
    echo "Después ejecuta: ./start-prolink-oracle.sh"
    exit 1
fi

echo "✅ Oracle Database está corriendo"

# Esperar a que Oracle esté listo
echo "⏳ Esperando a que Oracle esté listo..."
sleep 10

# Configurar usuario ProLink si no existe
echo "👤 Configurando usuario ProLink..."
# Aquí iría la configuración del usuario

# Iniciar aplicación con perfil Oracle
echo "🚀 Iniciando ProLink Backend con Oracle..."
cd /home/chris1/Escritorio/linkedIn/prolink-backend
mvn spring-boot:run -Dspring-boot.run.profiles=oracle-prod

echo "✅ ProLink iniciado con Oracle!"
echo "🌐 Backend: http://localhost:8080/api/v1"
echo "📊 Swagger: http://localhost:8080/api/v1/swagger-ui/index.html"