
package com.tiendaonline.strategy;

import com.tiendaonline.entity.EscenarioSimulacion;
import com.tiendaonline.entity.EstadoCompra;
import com.tiendaonline.entity.MetodoPago;

import com.tiendaonline.exception.PagoTecnicoException;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimuladoresPagoTest {

 @Test
 void tarjetaAprueba() {

  assertEquals(
          EstadoCompra.CONFIRMADA,
          new SimuladorPagoTarjetaImpl(
                  EscenarioSimulacion.APROBADO
          ).simular(
                  MetodoPago.TARJETA_CREDITO,
                  BigDecimal.ONE
          ).estado()
  );
 }

 @Test
 void tarjetaRechaza() {

  assertEquals(
          EstadoCompra.RECHAZADA,
          new SimuladorPagoTarjetaImpl(
                  EscenarioSimulacion.RECHAZADO
          ).simular(
                  MetodoPago.TARJETA_DEBITO,
                  BigDecimal.ONE
          ).estado()
  );
 }

 @Test
 void tarjetaFallaTecnicamente() {

  assertThrows(
          PagoTecnicoException.class,
          () -> new SimuladorPagoTarjetaImpl(
                  EscenarioSimulacion.ERROR_TECNICO
          ).simular(
                  MetodoPago.TARJETA_CREDITO,
                  BigDecimal.ONE
          )
  );
 }

 @Test
 void paypalAprueba() {

  assertEquals(
          EstadoCompra.CONFIRMADA,
          new PasarelaPayPalSimulada(
                  EscenarioSimulacion.APROBADO
          ).procesarPago(
                  1L,
                  BigDecimal.ONE
          ).estado()
  );
 }

 @Test
 void paypalRechaza() {

  assertEquals(
          EstadoCompra.RECHAZADA,
          new PasarelaPayPalSimulada(
                  EscenarioSimulacion.RECHAZADO
          ).procesarPago(
                  1L,
                  BigDecimal.ONE
          ).estado()
  );
 }

 @Test
 void paypalFallaTecnicamente() {

  assertThrows(
          PagoTecnicoException.class,
          () -> new PasarelaPayPalSimulada(
                  EscenarioSimulacion.ERROR_TECNICO
          ).procesarPago(
                  1L,
                  BigDecimal.ONE
          )
  );
 }
}
