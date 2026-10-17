package com.tiendaonline.mapper;

import com.tiendaonline.dto.request.CrearProductoRequestDTO;
import com.tiendaonline.dto.response.ProductoResponseDTO;
import com.tiendaonline.entity.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductoMapperTest {

    private final ProductoMapper productoMapper = new ProductoMapper();

    @Test
    void debeConvertirRequestAEntidad() {
        CrearProductoRequestDTO request = new CrearProductoRequestDTO(
                "TEC-001",
                "Teclado",
                new BigDecimal("125.50"),
                10
        );

        Producto producto = productoMapper.toEntity(request);

        assertThat(producto.getIdProducto()).isNull();
        assertThat(producto.getCodigo()).isEqualTo("TEC-001");
        assertThat(producto.getNombre()).isEqualTo("Teclado");
        assertThat(producto.getPrecio()).isEqualByComparingTo("125.50");
        assertThat(producto.getStock()).isEqualTo(10);
    }

    @Test
    void debeConvertirEntidadARespuesta() {
        Producto producto = new Producto("TEC-001", "Teclado", new BigDecimal("125.50"), 10);
        ReflectionTestUtils.setField(producto, "idProducto", 1);

        ProductoResponseDTO response = productoMapper.toResponse(producto);

        assertThat(response.getIdProducto()).isEqualTo(1);
        assertThat(response.getCodigo()).isEqualTo("TEC-001");
        assertThat(response.getNombre()).isEqualTo("Teclado");
        assertThat(response.getPrecio()).isEqualByComparingTo("125.50");
        assertThat(response.getStock()).isEqualTo(10);
    }
}
