package com.tiendaonline.strategy;

import com.tiendaonline.entity.MetodoPago;

import java.math.BigDecimal;

public interface SimuladorPagoTarjeta {

    ResultadoPago simular(
            MetodoPago metodoPago,
            BigDecimal total
    );
}
