package com.tiendaonline.strategy;

import com.tiendaonline.entity.Compra;
import com.tiendaonline.entity.MetodoPago;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagoTarjetaDebito implements EstrategiaPago {

    private final SimuladorPagoTarjeta simulador;

    @Override
    public MetodoPago getMetodoPago() {

        return MetodoPago.TARJETA_DEBITO;
    }

    @Override
    public ResultadoPago procesar(Compra c) {

        return simulador.simular(
                getMetodoPago(),
                c.getTotal()
        );
    }

    @Override
    public String toString() {
        return "PagoTarjetaDebito{" +
                "simulador=" + simulador +
                '}';
    }
}
