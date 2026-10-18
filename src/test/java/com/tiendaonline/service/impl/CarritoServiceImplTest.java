
package com.tiendaonline.service.impl;

import com.tiendaonline.dto.request.ActualizarCantidadRequestDTO;
import com.tiendaonline.dto.request.AgregarProductoRequestDTO;
import com.tiendaonline.dto.response.CarritoResponseDTO;

import com.tiendaonline.exception.CarritoNoEncontradoException;
import com.tiendaonline.exception.ProductoNoEncontradoException;

import com.tiendaonline.mapper.CarritoMapper;

import com.tiendaonline.repository.CarritoRepository;
import com.tiendaonline.repository.ProductoRepository;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CarritoServiceImplTest {

 private final CarritoRepository carritos =
         mock(CarritoRepository.class);

 private final ProductoRepository productos =
         mock(ProductoRepository.class);

 private final CarritoMapper mapper =
         mock(CarritoMapper.class);

 private final CarritoServiceImpl service =
         new CarritoServiceImpl(carritos, productos, mapper);

 @Test
 void agregarProductoInexistenteFalla() {

  when(productos.findById(9))
          .thenReturn(Optional.empty());

  assertThrows(
          ProductoNoEncontradoException.class,
          () -> service.agregarProducto(
                  "x",
                  new AgregarProductoRequestDTO(9, 1)
          )
  );
 }

 @Test
 void obtenerSinSesionDevuelveCarritoVacio() {

  CarritoResponseDTO r = new CarritoResponseDTO(
          null,
          List.of(),
          BigDecimal.ZERO
  );

  when(mapper.toEmptyResponse())
          .thenReturn(r);

  assertSame(
          r,
          service.obtenerCarrito(null)
  );

  verifyNoInteractions(carritos);
 }

 @Test
 void actualizarSinCarritoFalla() {

  when(carritos.findByCodigoAnonimoForUpdate("x"))
          .thenReturn(Optional.empty());

  assertThrows(
          CarritoNoEncontradoException.class,
          () -> service.actualizarCantidad(
                  "x",
                  1,
                  new ActualizarCantidadRequestDTO(1)
          )
  );
 }
}
