-- ============================================================
-- PROYECTO: Tienda en linea / Carrito de compra
-- ARCHIVO: 06_limpiar_datos_prueba.sql
-- DESCRIPCION: Limpieza controlada de registros temporales.
--
-- REQUISITOS:
--   - Base de datos tienda_online existente.
--   - Tabla prueba_permisos creada.
--   - Ejecutar con cuenta autorizada.
--
-- SEGURIDAD:
--   - No elimina tablas.
--   - No utiliza TRUNCATE.
--   - No utiliza DROP.
--   - Solo elimina registros temporales identificados.
-- ============================================================

USE tienda_online;

-- ============================================================
-- 1. CONSULTAR REGISTROS ANTES DE ELIMINAR
-- ============================================================

SELECT
    id_prueba,
    descripcion,
    valor
FROM prueba_permisos
WHERE id_prueba IN (9001, 9002, 9003, 9004, 9005)
ORDER BY id_prueba;


-- ============================================================
-- 2. ELIMINAR REGISTROS TEMPORALES
-- ============================================================

DELETE FROM prueba_permisos
WHERE
    (id_prueba = 9001 AND descripcion = 'Prueba de lectura')
    OR (id_prueba = 9002 AND descripcion = 'Prueba de escritura')
    OR (id_prueba = 9003 AND descripcion = 'Prueba de restricciones')
    OR (id_prueba = 9004 AND descripcion = 'Prueba de integracion JDBC')
    OR (id_prueba = 9005 AND descripcion = 'Prueba de usuario aplicacion');


-- ============================================================
-- 3. VERIFICAR LIMPIEZA
-- ============================================================

SELECT
    id_prueba,
    descripcion,
    valor
FROM prueba_permisos
WHERE id_prueba IN (9001, 9002, 9003, 9004, 9005)
ORDER BY id_prueba;
