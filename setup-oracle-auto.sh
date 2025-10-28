#!/bin/bash

# ==============================================
# SCRIPT AUTOMATIZADO PARA CONFIGURAR ORACLE
# ==============================================

echo "🚀 Iniciando configuración de Oracle Database para ProLink..."

# Verificar que Oracle esté funcionando
echo "📋 Verificando estado de Oracle..."
sudo docker ps | grep oracle-xe-prolink > /dev/null
if [ $? -eq 0 ]; then
    echo "✅ Oracle Database está ejecutándose"
else
    echo "❌ Oracle Database no está activo"
    exit 1
fi

# Crear usuario ProLink en Oracle
echo "👤 Creando usuario ProLink en Oracle PDB..."
sudo docker exec oracle-xe-prolink bash -c "
sqlplus -S system/ProLink2024!@XEPDB1 << 'EOF'
SET PAGESIZE 0
SET FEEDBACK OFF
SET VERIFY OFF

-- Intentar eliminar usuario si existe
DROP USER prolink CASCADE;

-- Crear usuario ProLink (sin C## prefix en PDB)
CREATE USER prolink IDENTIFIED BY ProLink123
DEFAULT TABLESPACE USERS
TEMPORARY TABLESPACE TEMP
QUOTA UNLIMITED ON USERS;

-- Otorgar permisos
GRANT CONNECT, RESOURCE TO prolink;
GRANT CREATE SESSION TO prolink;
GRANT CREATE TABLE TO prolink;
GRANT CREATE SEQUENCE TO prolink;
GRANT CREATE VIEW TO prolink;
GRANT CREATE PROCEDURE TO prolink;
GRANT CREATE TRIGGER TO prolink;
GRANT SELECT ANY DICTIONARY TO prolink;
GRANT CREATE ANY INDEX TO prolink;
GRANT ALTER ANY TABLE TO prolink;

COMMIT;

-- Verificar
SELECT 'Usuario ProLink creado exitosamente' FROM dual;
EXIT;
EOF
"

if [ $? -eq 0 ]; then
    echo "✅ Usuario ProLink creado en Oracle"
else
    echo "⚠️ Hubo un problema creando el usuario, continuando..."
fi

echo "🎉 Configuración de Oracle completada!"
echo "📝 Ahora necesitas cambiar la configuración de Spring Boot para usar Oracle"