-- ============================================================
-- PROYECTO: Tienda en línea / Carrito de compra
-- ARCHIVO: 01_crear_base_datos.sql
-- DESCRIPCION: Creacion de la base de datos del proyecto.
--
-- EJECUCION:
--   - Ejecutar con una cuenta administrativa autorizada.
--   - Utilizar durante la instalacion inicial.
--   - No es necesario ejecutarlo en una BD ya configurada.
--
-- SEGURIDAD:
--   - No elimina bases de datos existentes.
--   - No modifica usuarios ni contraseñas.
-- ============================================================

CREATE DATABASE IF NOT EXISTS tienda_online
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE tienda_online;
