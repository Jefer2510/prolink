#!/bin/bash

# Script para manejar ProLink en servidor LAMP
# Uso: ./prolink-lamp.sh [start|stop|status|restart]

BACKEND_DIR="/home/chris1/Escritorio/linkedIn/prolink-backend"
BACKEND_LOG="/home/chris1/Escritorio/linkedIn/prolink-backend.log"
BACKEND_PID_FILE="/tmp/prolink-backend.pid"

case "$1" in
    start)
        echo "🚀 Iniciando ProLink en servidor LAMP..."
        
        # Iniciar backend si no está corriendo
        if ! pgrep -f "spring-boot:run" > /dev/null; then
            echo "📡 Iniciando backend Spring Boot..."
            cd "$BACKEND_DIR"
            nohup mvn spring-boot:run > "$BACKEND_LOG" 2>&1 &
            echo $! > "$BACKEND_PID_FILE"
            echo "   Backend iniciando... (PID: $(cat $BACKEND_PID_FILE))"
        else
            echo "   ✅ Backend ya está corriendo"
        fi
        
        # Verificar Apache
        if systemctl is-active --quiet apache2; then
            echo "   ✅ Apache2 está corriendo"
        else
            echo "   ❌ Apache2 no está corriendo. Iniciando..."
            sudo systemctl start apache2
        fi
        
        echo ""
        echo "🌐 ProLink disponible en:"
        echo "   Frontend: http://localhost/"
        echo "   Backend API: http://localhost:8080/"
        echo "   Swagger: http://localhost:8080/swagger-ui.html"
        echo ""
        echo "📋 Credenciales de prueba:"
        echo "   Usuario: admin@prolink.com"
        echo "   Contraseña: admin123"
        ;;
        
    stop)
        echo "🛑 Deteniendo ProLink..."
        
        # Detener backend
        if [ -f "$BACKEND_PID_FILE" ]; then
            BACKEND_PID=$(cat "$BACKEND_PID_FILE")
            if kill -0 "$BACKEND_PID" 2>/dev/null; then
                echo "📡 Deteniendo backend (PID: $BACKEND_PID)..."
                kill "$BACKEND_PID"
                rm "$BACKEND_PID_FILE"
            fi
        fi
        
        # También buscar por nombre del proceso
        pkill -f "spring-boot:run" 2>/dev/null && echo "   Backend detenido"
        
        echo "   ✅ ProLink detenido (Apache sigue corriendo)"
        ;;
        
    status)
        echo "📊 Estado de ProLink:"
        
        # Estado del backend
        if pgrep -f "spring-boot:run" > /dev/null; then
            echo "   📡 Backend: ✅ Corriendo"
            # Verificar si responde
            if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
                echo "      └─ API: ✅ Respondiendo"
            else
                echo "      └─ API: ⏳ Iniciando..."
            fi
        else
            echo "   📡 Backend: ❌ Detenido"
        fi
        
        # Estado de Apache
        if systemctl is-active --quiet apache2; then
            echo "   🌐 Apache2: ✅ Corriendo"
            echo "      └─ Frontend: http://localhost/"
        else
            echo "   🌐 Apache2: ❌ Detenido"
        fi
        
        # Estado de Oracle DB
        if docker ps | grep -q oracle; then
            echo "   🗄️  Oracle DB: ✅ Corriendo"
        else
            echo "   🗄️  Oracle DB: ❌ Detenido"
            echo "      └─ Usa: docker-compose up -d"
        fi
        ;;
        
    restart)
        echo "🔄 Reiniciando ProLink..."
        $0 stop
        sleep 3
        $0 start
        ;;
        
    logs)
        echo "📜 Logs del backend:"
        if [ -f "$BACKEND_LOG" ]; then
            tail -f "$BACKEND_LOG"
        else
            echo "No hay logs disponibles"
        fi
        ;;
        
    *)
        echo "🚀 ProLink - Servidor LAMP Manager"
        echo ""
        echo "Uso: $0 [comando]"
        echo ""
        echo "Comandos disponibles:"
        echo "  start    - Iniciar ProLink (backend + verificar Apache)"
        echo "  stop     - Detener ProLink backend"
        echo "  status   - Ver estado de todos los servicios"
        echo "  restart  - Reiniciar ProLink"
        echo "  logs     - Ver logs del backend en tiempo real"
        echo ""
        echo "URLs importantes:"
        echo "  Frontend: http://localhost/"
        echo "  Backend:  http://localhost:8080/"
        echo "  Swagger:  http://localhost:8080/swagger-ui.html"
        ;;
esac