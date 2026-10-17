-- ============================================================
-- PROYECTO: Tienda en línea / Carrito de compra
-- ARCHIVO: 04_preparar_datos_prueba.sql
-- DESCRIPCION: Preparacion de registros temporales.
--
-- REQUISITOS:
--   - Base de datos tienda_online creada.
--   - Tabla prueba_permisos existente.
--   - Ejecutar con cuenta administrativa autorizada.
--
-- SEGURIDAD:
--   - No modifica las tablas funcionales.
--   - No elimina registros existentes.
--   - No utiliza REPLACE ni ON DUPLICATE KEY UPDATE.
-- ============================================================

USE tienda_online;

-- ============================================================
-- 1. INSERTAR DATOS TEMPORALES DE PRUEBA
-- ============================================================

INSERT INTO prueba_permisos
    (id_prueba, descripcion, valor)
VALUES
    (9001, 'Prueba de lectura', 100),
    (9002, 'Prueba de escritura', 200),
    (9003, 'Prueba de restricciones', 300),
    (9004, 'Prueba de integracion JDBC', 400),
    (9005, 'Prueba de usuario aplicacion', 500);

-- ============================================================
-- 2. VERIFICAR LOS REGISTROS PREPARADOS
-- ============================================================

SELECT id_prueba, descripcion, valor
FROM prueba_permisos
WHERE id_prueba IN (9001, 9002, 9003, 9004, 9005)
ORDER BY id_prueba;
