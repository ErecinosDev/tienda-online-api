
package com.tiendaonline.strategy;

import com.tiendaonline.entity.EscenarioSimulacion;
import com.tiendaonline.entity.EstadoCompra;

import com.tiendaonline.exception.PagoTecnicoException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PasarelaPayPalSimulada implements PasarelaPayPal {

    private final EscenarioSimulacion escenario;

    public PasarelaPayPalSimulada(
            @Value("${pagos.paypal.escenario:APROBADO}")
            EscenarioSimulacion escenario) {

        this.escenario = escenario;
    }

    @Override
    public ResultadoPago procesarPago(Long id, BigDecimal total) {

        return switch (escenario) {

            case APROBADO -> new ResultadoPago(
                    EstadoCompra.CONFIRMADA,
                    "PP-" + id,
                    "Pago simulado aprobado"
            );

            case RECHAZADO -> new ResultadoPago(
                    EstadoCompra.RECHAZADA,
                    "PP-" + id,
                    "Pago simulado rechazado"
            );

            case ERROR_TECNICO -> throw new PagoTecnicoException(
                    "Error técnico de PayPal"
            );
        };
    }

    @Override
    public String toString() {
        return "PasarelaPayPalSimulada{" +
                "escenario=" + escenario +
                '}';
    }
}
