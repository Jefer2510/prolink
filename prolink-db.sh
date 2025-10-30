#!/bin/bash

# ProLink Database Manager Script
# Gestiona y administra la base de datos Oracle de ProLink

DB_CONTAINER="oracle-xe-prolink"
DB_PASSWORD="ProLink123"

function show_help() {
    echo "🗄️  ProLink Database Manager"
    echo ""
    echo "Uso: $0 [comando]"
    echo ""
    echo "Comandos disponibles:"
    echo "  status     - Ver estado de la base de datos"
    echo "  connect    - Conectar a sqlplus como SYSTEM"
    echo "  web        - Mostrar URL para SQL Developer Web"
    echo "  logs       - Ver logs de Oracle"
    echo "  tables     - Mostrar tablas del esquema ProLink"
    echo "  users      - Mostrar usuarios de la base de datos"
    echo "  backup     - Crear backup de datos"
    echo "  restore    - Restaurar desde backup"
    echo "  reset      - Reiniciar contenedor de BD"
    echo ""
}

function check_container() {
    if ! docker ps | grep -q "$DB_CONTAINER"; then
        echo "❌ El contenedor de Oracle no está corriendo"
        echo "💡 Usa: docker compose -f docker-compose-simple.yml up -d"
        exit 1
    fi
}

case "$1" in
    status)
        echo "📊 Estado de Oracle Database:"
        if docker ps | grep -q "$DB_CONTAINER"; then
            echo "   ✅ Contenedor: Corriendo"
            
            # Verificar si la BD está lista
            docker logs "$DB_CONTAINER" 2>/dev/null | grep -q "DATABASE IS READY" && \
                echo "   ✅ Base de datos: Lista" || \
                echo "   ⏳ Base de datos: Iniciando..."
            
            # Mostrar puertos
            echo "   📡 Puertos:"
            echo "      - SQL: localhost:1521"
            echo "      - Web: localhost:5500"
        else
            echo "   ❌ Contenedor: Detenido"
        fi
        ;;
        
    connect)
        check_container
        echo "🔗 Conectando a Oracle como SYSTEM..."
        echo "💡 Para salir: type 'exit'"
        docker exec -it "$DB_CONTAINER" sqlplus system/"$DB_PASSWORD"@localhost:1521/XE
        ;;
        
    web)
        check_container
        echo "🌐 Oracle SQL Developer Web:"
        echo ""
        echo "   URL: http://localhost:5500/ords/sql-developer"
        echo "   Usuario: SYSTEM"
        echo "   Contraseña: $DB_PASSWORD"
        echo "   Esquema: XE"
        echo ""
        echo "💡 Abre la URL en tu navegador web"
        ;;
        
    logs)
        check_container
        echo "📜 Logs de Oracle Database:"
        docker logs -f "$DB_CONTAINER"
        ;;
        
    tables)
        check_container
        echo "📋 Tablas en el esquema ProLink:"
        docker exec -i "$DB_CONTAINER" sqlplus -s system/"$DB_PASSWORD"@localhost:1521/XE << EOF
SET PAGESIZE 50
SET LINESIZE 100
SELECT table_name, num_rows 
FROM user_tables 
ORDER BY table_name;
exit;
EOF
        ;;
        
    users)
        check_container
        echo "👥 Usuarios de la base de datos:"
        docker exec -i "$DB_CONTAINER" sqlplus -s system/"$DB_PASSWORD"@localhost:1521/XE << EOF
SET PAGESIZE 50
SET LINESIZE 100
SELECT username, created, account_status 
FROM dba_users 
WHERE username NOT IN ('SYS','SYSTEM','ANONYMOUS','AUDIT','CTXSYS','DBSNMP','EXFSYS','LBACSYS','MDSYS','MGMT_VIEW','OLAPSYS','OWBSYS','ORDPLUGINS','ORDSYS','OUTLN','SI_INFORMTN_SCHEMA','SYS','SYSMAN','SYSTEM','TSMSYS','WK_TEST','WKSYS','WKPROXY','WMSYS','XDB','APEX_050000','APEX_PUBLIC_USER','FLOWS_30000','FLOWS_FILES','MDDATA','ORACLE_OCM','XS\$NULL','ORDDATA','SPATIAL_CSW_ADMIN_USR','SPATIAL_WFS_ADMIN_USR')
ORDER BY created DESC;
exit;
EOF
        ;;
        
    backup)
        check_container
        BACKUP_DIR="./backups"
        BACKUP_FILE="prolink_backup_$(date +%Y%m%d_%H%M%S).sql"
        
        mkdir -p "$BACKUP_DIR"
        
        echo "💾 Creando backup en $BACKUP_DIR/$BACKUP_FILE..."
        
        docker exec "$DB_CONTAINER" sh -c "
            expdp system/$DB_PASSWORD@localhost:1521/XE \
            directory=DATA_PUMP_DIR \
            dumpfile=$BACKUP_FILE \
            schemas=PROLINK_USER \
            logfile=${BACKUP_FILE%.sql}.log
        " 2>/dev/null
        
        echo "✅ Backup completado"
        ;;
        
    restore)
        check_container
        echo "🔄 Función de restore - En desarrollo"
        echo "💡 Usa SQL Developer Web para importar datos manualmente"
        ;;
        
    reset)
        echo "🔄 Reiniciando contenedor de Oracle..."
        docker restart "$DB_CONTAINER"
        echo "⏳ Esperando que la base de datos esté lista..."
        sleep 10
        $0 status
        ;;
        
    *)
        show_help
        ;;
esac