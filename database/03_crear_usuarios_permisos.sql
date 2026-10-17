-- ============================================================
-- PROYECTO: Tienda en línea / Carrito de compra
-- ARCHIVO: 03_crear_usuarios_permisos.sql
-- DESCRIPCION: Usuarios y privilegios MySQL.
--
-- REQUISITOS:
--   - Ejecutar previamente los scripts 01 y 02.
--   - Usar una cuenta administrativa autorizada.
--   - Ejecutar únicamente durante una instalación nueva.
--
-- SEGURIDAD:
--   - Sustituir marcadores en una copia privada local.
--   - Nunca versionar contraseñas reales.
--   - No modificar contraseñas de usuarios existentes.
--   - No utilizar root para conectar la API.
-- ============================================================

-- ============================================================
-- 1. CREACION DE USUARIOS
-- ============================================================

CREATE USER IF NOT EXISTS 'tienda_lectura'@'localhost'
IDENTIFIED BY '__CLAVE_LOCAL_LECTURA__';

CREATE USER IF NOT EXISTS 'tienda_escritura'@'localhost'
IDENTIFIED BY '__CLAVE_LOCAL_ESCRITURA__';

CREATE USER IF NOT EXISTS 'tienda_app'@'localhost'
IDENTIFIED BY '__CLAVE_LOCAL_APP__';


-- ============================================================
-- 2. USUARIO: tienda_lectura
-- ============================================================

GRANT SELECT
ON tienda_online.prueba_permisos
TO 'tienda_lectura'@'localhost';


-- ============================================================
-- 3. USUARIO: tienda_escritura
-- ============================================================

GRANT INSERT, UPDATE, DELETE
ON tienda_online.prueba_permisos
TO 'tienda_escritura'@'localhost';

-- Lectura limitada a la columna identificadora.
-- Necesaria para el UPDATE condicionado validado.

GRANT SELECT (id_prueba)
ON tienda_online.prueba_permisos
TO 'tienda_escritura'@'localhost';


-- ============================================================
-- 4. USUARIO: tienda_app
-- ============================================================

GRANT SELECT, INSERT, UPDATE, DELETE
ON tienda_online.*
TO 'tienda_app'@'localhost';
