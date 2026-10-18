package com.tiendaonline.strategy;

import com.tiendaonline.entity.EstadoCompra;

public record ResultadoPago(
        EstadoCompra estado,
        String referenciaExterna,
        String mensaje
) {

}

