-- ==============================================
-- PROLINK DATABASE SETUP FOR ORACLE
-- ==============================================

-- Conectar como SYSTEM o SYS
-- sqlplus system/ProLink123@localhost:1521/XE

-- Crear usuario ProLink
CREATE USER prolink IDENTIFIED BY ProLink123
DEFAULT TABLESPACE USERS
TEMPORARY TABLESPACE TEMP
QUOTA UNLIMITED ON USERS;

-- Otorgar permisos necesarios
GRANT CONNECT, RESOURCE TO prolink;
GRANT CREATE SESSION TO prolink;
GRANT CREATE TABLE TO prolink;
GRANT CREATE SEQUENCE TO prolink;
GRANT CREATE VIEW TO prolink;
GRANT CREATE PROCEDURE TO prolink;
GRANT CREATE TRIGGER TO prolink;

-- Permisos adicionales para Spring Boot
GRANT SELECT ANY DICTIONARY TO prolink;
GRANT CREATE ANY INDEX TO prolink;
GRANT ALTER ANY TABLE TO prolink;

-- Conectar como usuario prolink
-- sqlplus prolink/ProLink123@localhost:1521/XE

-- Las tablas se crearán automáticamente por Hibernate con ddl-auto=update
-- Pero podemos pre-crear algunas secuencias si es necesario

-- Secuencias para IDs (Hibernate las creará automáticamente)
-- CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;
-- CREATE SEQUENCE post_seq START WITH 1 INCREMENT BY 1;
-- CREATE SEQUENCE connection_seq START WITH 1 INCREMENT BY 1;
-- CREATE SEQUENCE message_seq START WITH 1 INCREMENT BY 1;
-- CREATE SEQUENCE conversation_seq START WITH 1 INCREMENT BY 1;
-- CREATE SEQUENCE comment_seq START WITH 1 INCREMENT BY 1;
-- CREATE SEQUENCE like_seq START WITH 1 INCREMENT BY 1;

-- Datos de prueba (opcional)
-- Se pueden insertar después de que Hibernate cree las tablas

COMMIT;

-- Verificar que el usuario fue creado correctamente
SELECT username, default_tablespace, temporary_tablespace, created
FROM dba_users 
WHERE username = 'PROLINK';