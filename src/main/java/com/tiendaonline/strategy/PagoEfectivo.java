package com.tiendaonline.strategy;

import com.tiendaonline.entity.Compra;
import com.tiendaonline.entity.EstadoCompra;
import com.tiendaonline.entity.MetodoPago;

import org.springframework.stereotype.Component;

@Component
public class PagoEfectivo implements EstrategiaPago {

    @Override
    public MetodoPago getMetodoPago() {

        return MetodoPago.EFECTIVO;
    }

    @Override
    public ResultadoPago procesar(Compra c) {

        return new ResultadoPago(
                EstadoCompra.PENDIENTE,
                null,
                "Pago en efectivo pendiente"
        );
    }

    @Override
    public String toString() {
        return "PagoEfectivo{}";
    }
}

