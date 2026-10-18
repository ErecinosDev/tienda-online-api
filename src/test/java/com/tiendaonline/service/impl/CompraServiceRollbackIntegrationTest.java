
package com.tiendaonline.service.impl;

import com.tiendaonline.dto.request.RealizarCompraRequestDTO;

import com.tiendaonline.entity.CarritoCompra;
import com.tiendaonline.entity.DetalleCarrito;
import com.tiendaonline.entity.MetodoPago;
import com.tiendaonline.entity.Producto;

import com.tiendaonline.exception.PagoTecnicoException;

import com.tiendaonline.repository.CarritoRepository;
import com.tiendaonline.repository.CompraRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "pagos.tarjeta.escenario=ERROR_TECNICO"
        }
)
@Import(
        CompraServiceRollbackIntegrationTest.CarritoBloqueadoDePruebaConfig.class
)
class CompraServiceRollbackIntegrationTest {

    @Autowired
    private CompraServiceImpl service;

    @Autowired
    private CompraRepository compras;

    @Test
    void revierteLaCompraInicialSiLaPasarelaFallaTecnicamente() {

        CarritoBloqueadoDePruebaConfig.carrito = carritoConDetalle();

        assertThrows(
                PagoTecnicoException.class,
                () -> service.realizarCompra(
                        "rollback-cart",
                        new RealizarCompraRequestDTO(
                                MetodoPago.TARJETA_CREDITO
                        )
                )
        );

        assertEquals(
                0L,
                compras.count()
        );

        assertEquals(
                1,
                CarritoBloqueadoDePruebaConfig.carrito
                        .getDetalles()
                        .size()
        );
    }

    private CarritoCompra carritoConDetalle() {

        Producto producto = new Producto(
                "ROLLBACK-1",
                "Producto rollback",
                new BigDecimal("25.00"),
                3
        );

        CarritoCompra carrito = new CarritoCompra("rollback-cart");

        carrito.agregarDetalle(
                new DetalleCarrito(producto, 2)
        );

        return carrito;
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class CarritoBloqueadoDePruebaConfig {

        private static CarritoCompra carrito;

        @Bean
        @Primary
        CarritoRepository carritoRepositoryParaPruebaRollback() {

            return (CarritoRepository) Proxy.newProxyInstance(
                    getClass().getClassLoader(),
                    new Class[]{CarritoRepository.class},
                    (proxy, method, args) -> {

                        if (method.getName().equals("hashCode")) {

                            return System.identityHashCode(proxy);
                        }

                        if (method.getName().equals("equals")) {

                            return proxy == args[0];
                        }

                        if (method.getName().equals("toString")) {

                            return "CarritoRepositoryParaPruebaRollback";
                        }

                        if (method.getName().equals(
                                "findByCodigoAnonimoForUpdate"
                        )) {

                            return Optional.ofNullable(carrito);
                        }

                        throw new UnsupportedOperationException(
                                method.getName()
                        );
                    }
            );
        }
    }
}
