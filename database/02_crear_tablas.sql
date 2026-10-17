-- ============================================================
-- PROYECTO: Tienda en línea / Carrito de compra
-- ARCHIVO: 02_crear_tablas.sql
-- DESCRIPCION: Creacion de las cinco tablas del sistema.
--
-- REQUISITO:
--   Ejecutar previamente 01_crear_base_datos.sql
--
-- SEGURIDAD:
--   No elimina tablas existentes.
--   No inserta ni modifica registros.
--   No modifica usuarios ni privilegios.
-- ============================================================

USE tienda_online;

-- ============================================================
-- 1. TABLA: producto
-- ============================================================

CREATE TABLE IF NOT EXISTS producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    precio DECIMAL(12,2) NOT NULL,
    stock INT NOT NULL,

    CONSTRAINT uk_producto_codigo
        UNIQUE (codigo),

    CONSTRAINT chk_producto_precio
        CHECK (precio >= 0),

    CONSTRAINT chk_producto_stock
        CHECK (stock >= 0)
) ENGINE = InnoDB;


-- ============================================================
-- 2. TABLA: carrito_compra
-- ============================================================

CREATE TABLE IF NOT EXISTS carrito_compra (
    id_carrito INT AUTO_INCREMENT PRIMARY KEY,
    codigo_anonimo VARCHAR(36) NOT NULL,

    CONSTRAINT uk_carrito_codigo_anonimo
        UNIQUE (codigo_anonimo)
) ENGINE = InnoDB;


-- ============================================================
-- 3. TABLA: detalle_carrito
-- ============================================================

CREATE TABLE IF NOT EXISTS detalle_carrito (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_carrito INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,

    CONSTRAINT uk_detalle_carrito_producto
        UNIQUE (id_carrito, id_producto),

    CONSTRAINT chk_detalle_cantidad
        CHECK (cantidad > 0),

    CONSTRAINT chk_detalle_subtotal
        CHECK (subtotal >= 0),

    CONSTRAINT fk_detalle_carrito
        FOREIGN KEY (id_carrito)
        REFERENCES carrito_compra(id_carrito)
        ON DELETE CASCADE,

    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto(id_producto)
        ON DELETE RESTRICT,

    INDEX idx_detalle_producto (id_producto)
) ENGINE = InnoDB;


-- ============================================================
-- 4. TABLA: compra
-- ============================================================

CREATE TABLE IF NOT EXISTS compra (
    id_compra BIGINT AUTO_INCREMENT PRIMARY KEY,
    total DECIMAL(12,2) NOT NULL,
    metodo_pago VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    fecha_creacion DATETIME(6) NOT NULL,

    CONSTRAINT chk_compra_total
        CHECK (total >= 0),

    CONSTRAINT chk_compra_metodo_pago
        CHECK (
            metodo_pago IN (
                'EFECTIVO',
                'TARJETA_CREDITO',
                'TARJETA_DEBITO',
                'PAYPAL'
            )
        ),

    CONSTRAINT chk_compra_estado
        CHECK (
            estado IN (
                'INICIADA',
                'PENDIENTE',
                'CONFIRMADA',
                'RECHAZADA'
            )
        )
) ENGINE = InnoDB;


-- ============================================================
-- 5. TABLA: prueba_permisos
-- ============================================================

CREATE TABLE IF NOT EXISTS prueba_permisos (
    id_prueba INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(150) NOT NULL,
    valor INT NOT NULL
) ENGINE = InnoDB;
