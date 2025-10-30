#!/bin/bash

# 🚀 ProLink - Script de Configuración Multi-IP
# ===============================================

echo "🌐 ProLink - Configurador de IP"
echo "================================="

# Detectar IP automáticamente
AUTO_IP=$(ip addr show | grep "inet " | grep -v 127.0.0.1 | head -1 | awk '{print $2}' | cut -d/ -f1)

echo ""
echo "📍 Tu IP detectada: $AUTO_IP"
echo ""
echo "Opciones disponibles:"
echo "1) Usar localhost (solo esta máquina)"
echo "2) Usar IP local detectada: $AUTO_IP (red local)"
echo "3) Introducir IP personalizada"
echo ""

read -p "Selecciona una opción (1-3): " choice

case $choice in
    1)
        NEW_IP="localhost"
        echo "✅ Configurando para localhost..."
        ;;
    2)
        NEW_IP="$AUTO_IP"
        echo "✅ Configurando para IP local: $AUTO_IP"
        ;;
    3)
        read -p "Introduce la IP o dominio: " NEW_IP
        echo "✅ Configurando para: $NEW_IP"
        ;;
    *)
        echo "❌ Opción inválida"
        exit 1
        ;;
esac

# Hacer backup
cp prolink-app.html prolink-app.html.backup
echo "📁 Backup creado: prolink-app.html.backup"

# Actualizar IP en el archivo
sed -i "s/const SERVER_IP = '.*';/const SERVER_IP = '$NEW_IP';/" prolink-app.html

echo "🔧 Archivo actualizado con IP: $NEW_IP"

# Desplegar
echo "🚀 Desplegando frontend..."
sudo cp prolink-app.html /var/www/html/prolink/

echo ""
echo "✅ ¡ProLink configurado exitosamente!"
echo ""
echo "📋 URLs de acceso:"
echo "   Frontend: http://$NEW_IP/prolink/prolink-app.html"
echo "   Backend:  http://$NEW_IP:8080/api/v1/"
echo "   H2 DB:    http://$NEW_IP:8080/api/v1/h2-console"
echo ""
echo "🎯 Para iniciar el backend:"
echo "   cd prolink-backend"
echo "   java -jar target/prolink-backend-1.0.0.jar --spring.profiles.active=h2"
echo ""