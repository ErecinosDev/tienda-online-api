
package com.tiendaonline.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductoTest {

    @Test
    void debeActualizarTodosSusDatos() {

        Producto producto = new Producto(
                "TEC-001",
                "Teclado",
                new BigDecimal("125.50"),
                10
        );

        producto.actualizarDatos(
                "TEC-002",
                "Teclado mecánico",
                new BigDecimal("250.00"),
                5
        );

        assertThat(producto.getCodigo())
                .isEqualTo("TEC-002");

        assertThat(producto.getNombre())
                .isEqualTo("Teclado mecánico");

        assertThat(producto.getPrecio())
                .isEqualByComparingTo("250.00");

        assertThat(producto.getStock())
                .isEqualTo(5);
    }
}
