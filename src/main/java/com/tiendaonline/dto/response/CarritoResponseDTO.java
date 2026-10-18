
package com.tiendaonline.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CarritoResponseDTO(
        Integer idCarrito,
        List<DetalleCarritoResponseDTO> detalles,
        BigDecimal total
) {
}
