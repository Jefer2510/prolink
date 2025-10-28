#!/bin/bash

# ProLink - Script de inicio completo
# Este script inicia todos los servicios necesarios para ProLink

echo "🚀 === INICIANDO PROLINK COMPLETO ==="
echo ""

# Verificar Oracle Database
echo "🗄️  Verificando Oracle Database..."
if ! sudo docker ps | grep -q oracle-xe-prolink; then
    echo "❌ Oracle Database no está ejecutándose"
    echo "   Iniciando Oracle Database..."
    sudo docker start oracle-xe-prolink
    echo "   Esperando que Oracle inicie..."
    sleep 10
else
    echo "✅ Oracle Database está ejecutándose"
fi

# Verificar Backend
echo ""
echo "🔧 Verificando Backend ProLink..."
if ! ps aux | grep -q "prolink-backend-1.0.0.jar"; then
    echo "❌ Backend no está ejecutándose"
    echo "   Iniciando Backend..."
    cd /home/chris1/Escritorio/linkedIn/prolink-backend
    nohup java -Dspring.profiles.active=oracle-prod -jar target/prolink-backend-1.0.0.jar > ../prolink-oracle.log 2>&1 &
    echo "   Esperando que Backend inicie..."
    sleep 15
    
    # Verificar que inició correctamente
    if curl -s http://localhost:8080/api/v1/auth/health > /dev/null 2>&1; then
        echo "✅ Backend iniciado correctamente"
    else
        echo "⚠️  Backend puede estar iniciando aún..."
    fi
else
    echo "✅ Backend está ejecutándose"
fi

# Verificar Frontend
echo ""
echo "🌐 Verificando Frontend Server..."
if ! ps aux | grep -q "python3 -m http.server 3000"; then
    echo "❌ Frontend server no está ejecutándose"
    echo "   Iniciando Frontend server..."
    cd /home/chris1/Escritorio/linkedIn
    nohup python3 -m http.server 3000 > /dev/null 2>&1 &
    sleep 2
    echo "✅ Frontend server iniciado"
else
    echo "✅ Frontend server está ejecutándose"
fi

echo ""
echo "🎉 === PROLINK COMPLETAMENTE INICIADO ==="
echo ""
echo "📱 URLs de acceso:"
echo "   🌐 Frontend: http://localhost:3000/prolink-app.html"
echo "   🔌 API:      http://localhost:8080/api/v1"
echo "   🗄️  Oracle:   localhost:1521/XEPDB1"
echo ""
echo "🧪 Usuarios de prueba:"
echo "   👤 testuser / password123"
echo "   👤 web_user_demo / Demo123!"
echo ""
echo "💡 Tip: Usa los botones de demo en la página para probar rápidamente"