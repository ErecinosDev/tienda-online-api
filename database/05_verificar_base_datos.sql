-- ============================================================
-- PROYECTO: Tienda en linea / Carrito de compra
-- ARCHIVO: 05_verificar_base_datos.sql
-- DESCRIPCION: Verificacion estructural y de configuracion.
--
-- REQUISITOS:
--   - Ejecutar los scripts 01, 02 y 03 previamente.
--   - Utilizar una cuenta administrativa autorizada.
--
-- SEGURIDAD:
--   - Solo realiza consultas.
--   - No modifica registros ni estructuras.
--   - No modifica usuarios ni privilegios.
-- ============================================================

USE tienda_online;

-- ============================================================
-- 1. VERIFICAR BASE DE DATOS
-- ============================================================

SELECT
    SCHEMA_NAME,
    DEFAULT_CHARACTER_SET_NAME,
    DEFAULT_COLLATION_NAME
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'tienda_online';


-- ============================================================
-- 2. VERIFICAR TABLAS Y MOTOR
-- ============================================================

SELECT
    TABLE_NAME,
    ENGINE
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'tienda_online'
  AND TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;


-- ============================================================
-- 3. VERIFICAR COLUMNAS Y TIPOS
-- ============================================================

SELECT
    TABLE_NAME,
    COLUMN_NAME,
    COLUMN_TYPE,
    IS_NULLABLE,
    COLUMN_KEY,
    EXTRA
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'tienda_online'
ORDER BY TABLE_NAME, ORDINAL_POSITION;


-- ============================================================
-- 4. VERIFICAR CLAVES PRIMARIAS Y UNIQUE
-- ============================================================

SELECT
    tc.TABLE_NAME,
    tc.CONSTRAINT_NAME,
    tc.CONSTRAINT_TYPE,
    kcu.COLUMN_NAME,
    kcu.ORDINAL_POSITION
FROM information_schema.TABLE_CONSTRAINTS tc
JOIN information_schema.KEY_COLUMN_USAGE kcu
    ON tc.CONSTRAINT_SCHEMA = kcu.CONSTRAINT_SCHEMA
   AND tc.TABLE_NAME = kcu.TABLE_NAME
   AND tc.CONSTRAINT_NAME = kcu.CONSTRAINT_NAME
WHERE tc.CONSTRAINT_SCHEMA = 'tienda_online'
  AND tc.CONSTRAINT_TYPE IN ('PRIMARY KEY', 'UNIQUE')
ORDER BY
    tc.TABLE_NAME,
    tc.CONSTRAINT_NAME,
    kcu.ORDINAL_POSITION;


-- ============================================================
-- 5. VERIFICAR CLAVES FORANEAS
-- ============================================================

SELECT
    kcu.TABLE_NAME,
    kcu.CONSTRAINT_NAME,
    kcu.COLUMN_NAME,
    kcu.REFERENCED_TABLE_NAME,
    kcu.REFERENCED_COLUMN_NAME,
    rc.DELETE_RULE,
    rc.UPDATE_RULE
FROM information_schema.KEY_COLUMN_USAGE kcu
JOIN information_schema.REFERENTIAL_CONSTRAINTS rc
    ON kcu.CONSTRAINT_SCHEMA = rc.CONSTRAINT_SCHEMA
   AND kcu.CONSTRAINT_NAME = rc.CONSTRAINT_NAME
   AND kcu.TABLE_NAME = rc.TABLE_NAME
WHERE kcu.CONSTRAINT_SCHEMA = 'tienda_online'
  AND kcu.REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY kcu.TABLE_NAME, kcu.CONSTRAINT_NAME;


-- ============================================================
-- 6. VERIFICAR RESTRICCIONES CHECK
-- ============================================================

SELECT
    tc.TABLE_NAME,
    tc.CONSTRAINT_NAME,
    cc.CHECK_CLAUSE
FROM information_schema.TABLE_CONSTRAINTS tc
JOIN information_schema.CHECK_CONSTRAINTS cc
    ON tc.CONSTRAINT_SCHEMA = cc.CONSTRAINT_SCHEMA
   AND tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
WHERE tc.CONSTRAINT_SCHEMA = 'tienda_online'
  AND tc.CONSTRAINT_TYPE = 'CHECK'
ORDER BY tc.TABLE_NAME, tc.CONSTRAINT_NAME;


-- ============================================================
-- 7. VERIFICAR INDICES
-- ============================================================

SELECT
    TABLE_NAME,
    INDEX_NAME,
    NON_UNIQUE,
    SEQ_IN_INDEX,
    COLUMN_NAME
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'tienda_online'
ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;


-- ============================================================
-- 8. VERIFICAR PRIVILEGIOS
-- ============================================================

SHOW GRANTS FOR 'tienda_lectura'@'localhost';

SHOW GRANTS FOR 'tienda_escritura'@'localhost';

SHOW GRANTS FOR 'tienda_app'@'localhost';


-- ============================================================
-- 9. VERIFICAR DATOS TEMPORALES
-- ============================================================

SELECT
    id_prueba,
    descripcion,
    valor
FROM prueba_permisos
WHERE id_prueba IN (9001, 9002, 9003, 9004, 9005)
ORDER BY id_prueba;
