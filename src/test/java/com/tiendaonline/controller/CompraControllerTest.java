package com.tiendaonline.controller;

import com.tiendaonline.dto.request.RealizarCompraRequestDTO;
import com.tiendaonline.dto.response.CompraResponseDTO;

import com.tiendaonline.entity.EstadoCompra;
import com.tiendaonline.entity.MetodoPago;

import com.tiendaonline.exception.CarritoNoEncontradoException;
import com.tiendaonline.exception.CarritoVacioException;
import com.tiendaonline.exception.EstrategiaPagoNoEncontradaException;
import com.tiendaonline.exception.GlobalExceptionHandler;
import com.tiendaonline.exception.PagoTecnicoException;

import com.tiendaonline.service.ICompraService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CompraControllerTest {

    private MockMvc mockMvc;

    private StubCompraService compraService;

    @BeforeEach
    void setUp() {

        compraService = new StubCompraService();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new CompraController(compraService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void debeRealizarCompraConElCodigoDeLaSesion() throws Exception {

        compraService.respuesta = response();

        MockHttpSession session = session();

        mockMvc.perform(post("/api/compras")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"metodoPago\":\"TARJETA_CREDITO\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCompra").value(1))
                .andExpect(jsonPath("$.estado").value("CONFIRMADA"))
                .andExpect(jsonPath("$.mensaje").value("Pago aprobado"));

        assertEquals(
                "codigo-sesion",
                compraService.codigoRecibido
        );

        assertEquals(
                MetodoPago.TARJETA_CREDITO,
                compraService.requestRecibido.getMetodoPago()
        );
    }

    @Test
    void debeResponderNotFoundSinSesionSinCrearCarrito() throws Exception {

        compraService.excepcion = new CarritoNoEncontradoException();

        mockMvc.perform(post("/api/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"metodoPago\":\"EFECTIVO\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(
                        MediaType.APPLICATION_PROBLEM_JSON
                ))
                .andExpect(jsonPath("$.title")
                        .value("Carrito no encontrado"))
                .andExpect(jsonPath("$.instance")
                        .value("/api/compras"));

        assertEquals(1, compraService.invocaciones);

        assertNull(compraService.codigoRecibido);
    }

    @Test
    void debeRechazarMetodoPagoAusente() throws Exception {

        mockMvc.perform(post("/api/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(
                        MediaType.APPLICATION_PROBLEM_JSON
                ))
                .andExpect(jsonPath("$.errors.metodoPago").isArray());

        assertEquals(0, compraService.invocaciones);
    }

    @Test
    void debeRechazarMetodoPagoDesconocido() throws Exception {

        mockMvc.perform(post("/api/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"metodoPago\":\"CRIPTO\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Solicitud inválida"));

        assertEquals(0, compraService.invocaciones);
    }

    @Test
    void debeResponderBadRequestParaCarritoVacio() throws Exception {

        compraService.excepcion = new CarritoVacioException();

        mockMvc.perform(post("/api/compras")
                        .session(session())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"metodoPago\":\"EFECTIVO\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Carrito vacío"));
    }

    @Test
    void debeDiferenciarEstrategiaAusenteDeFalloTecnico()
            throws Exception {

        compraService.excepcion =
                new EstrategiaPagoNoEncontradaException(
                        MetodoPago.PAYPAL
                );

        mockMvc.perform(post("/api/compras")
                        .session(session())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"metodoPago\":\"PAYPAL\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title")
                        .value("Configuración de pago inválida"));

        compraService.excepcion =
                new PagoTecnicoException("Pasarela no disponible");

        mockMvc.perform(post("/api/compras")
                        .session(session())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"metodoPago\":\"PAYPAL\"}"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.title")
                        .value("Pago no disponible"));
    }

    private MockHttpSession session() {

        MockHttpSession session = new MockHttpSession();

        session.setAttribute(
                "codigoAnonimo",
                "codigo-sesion"
        );

        return session;
    }

    private CompraResponseDTO response() {

        return new CompraResponseDTO(
                1L,
                new BigDecimal("100.00"),
                MetodoPago.TARJETA_CREDITO,
                EstadoCompra.CONFIRMADA,
                Instant.parse("2026-10-16T00:00:00Z"),
                "Pago aprobado"
        );
    }

    private static class StubCompraService implements ICompraService {

        private CompraResponseDTO respuesta;
        private RuntimeException excepcion;
        private String codigoRecibido;
        private RealizarCompraRequestDTO requestRecibido;
        private int invocaciones;

        @Override
        public CompraResponseDTO realizarCompra(
                String codigoAnonimo,
                RealizarCompraRequestDTO request) {

            invocaciones++;

            codigoRecibido = codigoAnonimo;
            requestRecibido = request;

            if (excepcion != null) {
                throw excepcion;
            }

            return respuesta;
        }
    }


}
