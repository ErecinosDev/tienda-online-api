
package com.tiendaonline.strategy;

import com.tiendaonline.entity.EscenarioSimulacion;
import com.tiendaonline.entity.EstadoCompra;
import com.tiendaonline.entity.MetodoPago;

import com.tiendaonline.exception.PagoTecnicoException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SimuladorPagoTarjetaImpl implements SimuladorPagoTarjeta {

    private final EscenarioSimulacion escenario;

    public SimuladorPagoTarjetaImpl(
            @Value("${pagos.tarjeta.escenario:APROBADO}")
            EscenarioSimulacion escenario) {

        this.escenario = escenario;
    }

    @Override
    public ResultadoPago simular(MetodoPago metodo, BigDecimal total) {

        return switch (escenario) {

            case APROBADO -> new ResultadoPago(
                    EstadoCompra.CONFIRMADA,
                    "SIM-" + metodo,
                    "Pago simulado aprobado"
            );

            case RECHAZADO -> new ResultadoPago(
                    EstadoCompra.RECHAZADA,
                    "SIM-" + metodo,
                    "Pago simulado rechazado"
            );

            case ERROR_TECNICO -> throw new PagoTecnicoException(
                    "Error técnico del simulador de tarjeta"
            );
        };
    }

    @Override
    public String toString() {
        return "SimuladorPagoTarjetaImpl{" +
                "escenario=" + escenario +
                '}';
    }
}
