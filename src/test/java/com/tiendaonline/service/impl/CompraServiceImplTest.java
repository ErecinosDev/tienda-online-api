
package com.tiendaonline.service.impl;

import com.tiendaonline.dto.request.RealizarCompraRequestDTO;
import com.tiendaonline.dto.response.CompraResponseDTO;

import com.tiendaonline.entity.CarritoCompra;
import com.tiendaonline.entity.Compra;
import com.tiendaonline.entity.DetalleCarrito;
import com.tiendaonline.entity.EstadoCompra;
import com.tiendaonline.entity.MetodoPago;
import com.tiendaonline.entity.Producto;

import com.tiendaonline.exception.CarritoNoEncontradoException;
import com.tiendaonline.exception.CarritoVacioException;
import com.tiendaonline.exception.EstrategiaPagoNoEncontradaException;
import com.tiendaonline.exception.PagoTecnicoException;

import com.tiendaonline.mapper.CompraMapper;

import com.tiendaonline.repository.CarritoRepository;
import com.tiendaonline.repository.CompraRepository;

import com.tiendaonline.strategy.EstrategiaPago;
import com.tiendaonline.strategy.PagoEfectivo;
import com.tiendaonline.strategy.ResultadoPago;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompraServiceImplTest {

    private CarritoCompra carritoConsultado;
    private Compra compraGuardada;
    private int consultasCarrito;
    private int guardadosCompra;
    private CompraServiceImpl service;

    @BeforeEach
    void setUp() {

        service = new CompraServiceImpl(
                carritoRepository(),
                compraRepository(),
                List.of(new PagoEfectivo()),
                new CompraMapper()
        );
    }

    @Test
    void compraSinSesionFallaSinConsultarNiPersistir() {

        assertThrows(
                CarritoNoEncontradoException.class,
                () -> service.realizarCompra(
                        null,
                        request(MetodoPago.EFECTIVO)
                )
        );

        assertEquals(0, consultasCarrito);
        assertEquals(0, guardadosCompra);
    }

    @Test
    void compraConCarritoInexistenteFalla() {

        assertThrows(
                CarritoNoEncontradoException.class,
                () -> service.realizarCompra(
                        "x",
                        request(MetodoPago.EFECTIVO)
                )
        );

        assertEquals(1, consultasCarrito);
        assertEquals(0, guardadosCompra);
    }

    @Test
    void compraVaciaFalla() {

        carritoConsultado = new CarritoCompra("x");

        assertThrows(
                CarritoVacioException.class,
                () -> service.realizarCompra(
                        "x",
                        request(MetodoPago.EFECTIVO)
                )
        );

        assertEquals(0, guardadosCompra);
    }

    @Test
    void pagoEfectivoCalculaTotalYConservaCarrito() {

        carritoConsultado = carritoConDetalle("125.50", 2);

        CompraResponseDTO respuesta = service.realizarCompra(
                "x",
                request(MetodoPago.EFECTIVO)
        );

        assertEquals(
                new BigDecimal("251.00"),
                compraGuardada.getTotal()
        );

        assertEquals(
                EstadoCompra.PENDIENTE,
                compraGuardada.getEstado()
        );

        assertEquals(
                EstadoCompra.PENDIENTE,
                respuesta.estado()
        );

        assertEquals(
                1,
                carritoConsultado.getDetalles().size()
        );
    }

    @Test
    void pagoConfirmadoVaciaCarrito() {

        carritoConsultado = carritoConDetalle("10.00", 3);

        service = new CompraServiceImpl(
                carritoRepository(),
                compraRepository(),
                List.of(
                        estrategia(
                                MetodoPago.TARJETA_CREDITO,
                                new ResultadoPago(
                                        EstadoCompra.CONFIRMADA,
                                        "REF-1",
                                        "Pago aprobado"
                                )
                        )
                ),
                new CompraMapper()
        );

        CompraResponseDTO respuesta = service.realizarCompra(
                "x",
                request(MetodoPago.TARJETA_CREDITO)
        );

        assertEquals(
                EstadoCompra.CONFIRMADA,
                compraGuardada.getEstado()
        );

        assertEquals(
                EstadoCompra.CONFIRMADA,
                respuesta.estado()
        );

        assertEquals(
                0,
                carritoConsultado.getDetalles().size()
        );
    }

    @Test
    void pagoRechazadoConservaCarrito() {

        carritoConsultado = carritoConDetalle("10.00", 1);

        service = new CompraServiceImpl(
                carritoRepository(),
                compraRepository(),
                List.of(
                        estrategia(
                                MetodoPago.PAYPAL,
                                new ResultadoPago(
                                        EstadoCompra.RECHAZADA,
                                        "PP-1",
                                        "Pago rechazado"
                                )
                        )
                ),
                new CompraMapper()
        );

        service.realizarCompra(
                "x",
                request(MetodoPago.PAYPAL)
        );

        assertEquals(
                EstadoCompra.RECHAZADA,
                compraGuardada.getEstado()
        );

        assertEquals(
                1,
                carritoConsultado.getDetalles().size()
        );
    }

    @Test
    void estrategiaAusenteSeDiferenciaDelFalloTecnico() {

        carritoConsultado = carritoConDetalle("10.00", 1);

        assertThrows(
                EstrategiaPagoNoEncontradaException.class,
                () -> service.realizarCompra(
                        "x",
                        request(MetodoPago.TARJETA_CREDITO)
                )
        );

        assertEquals(1, guardadosCompra);

        assertEquals(
                1,
                carritoConsultado.getDetalles().size()
        );
    }

    @Test
    void falloTecnicoSePropagaYSinVaciarElCarrito() {

        carritoConsultado = carritoConDetalle("10.00", 1);

        service = new CompraServiceImpl(
                carritoRepository(),
                compraRepository(),
                List.of(
                        estrategia(
                                MetodoPago.TARJETA_DEBITO,
                                null
                        )
                ),
                new CompraMapper()
        );

        assertThrows(
                PagoTecnicoException.class,
                () -> service.realizarCompra(
                        "x",
                        request(MetodoPago.TARJETA_DEBITO)
                )
        );

        assertEquals(1, guardadosCompra);

        assertEquals(
                1,
                carritoConsultado.getDetalles().size()
        );
    }

    private CarritoRepository carritoRepository() {

        return (CarritoRepository) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{CarritoRepository.class},
                (proxy, method, args) -> {

                    if (method.getName().equals(
                            "findByCodigoAnonimoForUpdate"
                    )) {

                        consultasCarrito++;

                        return Optional.ofNullable(carritoConsultado);
                    }

                    throw new UnsupportedOperationException(
                            method.getName()
                    );
                }
        );
    }

    private CompraRepository compraRepository() {

        return (CompraRepository) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{CompraRepository.class},
                (proxy, method, args) -> {

                    if (method.getName().equals("save")) {

                        guardadosCompra++;

                        compraGuardada = (Compra) args[0];

                        return compraGuardada;
                    }

                    throw new UnsupportedOperationException(
                            method.getName()
                    );
                }
        );
    }

    private EstrategiaPago estrategia(
            MetodoPago metodo,
            ResultadoPago resultado) {

        return new EstrategiaPago() {

            @Override
            public MetodoPago getMetodoPago() {

                return metodo;
            }

            @Override
            public ResultadoPago procesar(Compra compra) {

                if (resultado == null) {

                    throw new PagoTecnicoException(
                            "Pasarela no disponible"
                    );
                }

                return resultado;
            }
        };
    }

    private RealizarCompraRequestDTO request(MetodoPago metodo) {

        return new RealizarCompraRequestDTO(metodo);
    }

    private CarritoCompra carritoConDetalle(
            String precio,
            int cantidad) {

        CarritoCompra carrito = new CarritoCompra("x");

        Producto producto = new Producto(
                "P-1",
                "Producto",
                new BigDecimal(precio),
                10
        );

        carrito.agregarDetalle(
                new DetalleCarrito(producto, cantidad)
        );

        return carrito;
    }
}
