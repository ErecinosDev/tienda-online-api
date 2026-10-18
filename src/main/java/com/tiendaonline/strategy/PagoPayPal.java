package com.tiendaonline.strategy;

import com.tiendaonline.entity.Compra;
import com.tiendaonline.entity.MetodoPago;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagoPayPal implements EstrategiaPago {

    private final PasarelaPayPal pasarela;

    @Override
    public MetodoPago getMetodoPago() {

        return MetodoPago.PAYPAL;
    }

    @Override
    public ResultadoPago procesar(Compra c) {

        return pasarela.procesarPago(
                c.getIdCompra(),
                c.getTotal()
        );
    }

    @Override
    public String toString() {
        return "PagoPayPal{" +
                "pasarela=" + pasarela +
                '}';
    }
}
