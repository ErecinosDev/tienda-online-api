
package com.tiendaonline.dto.response;

import com.tiendaonline.entity.EstadoCompra;
import com.tiendaonline.entity.MetodoPago;

import java.math.BigDecimal;
import java.time.Instant;

public record CompraResponseDTO(
        Long idCompra,
        BigDecimal total,
        MetodoPago metodoPago,
        EstadoCompra estado,
        Instant fechaCreacion,
        String mensaje
) {
}

