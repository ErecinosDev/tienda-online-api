package com.tiendaonline.strategy;

import java.math.BigDecimal;

public interface PasarelaPayPal {

    ResultadoPago procesarPago(
            Long idCompra,
            BigDecimal total
    );


}

