
package com.tiendaonline.dto.response;

import java.math.BigDecimal;

public record DetalleCarritoResponseDTO(
        Integer productoId,
        String codigo,
        String nombre,
        BigDecimal precio,
        Integer cantidad,
        BigDecimal subtotal
) {
}
