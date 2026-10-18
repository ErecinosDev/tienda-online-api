
package com.tiendaonline.service.impl;

import com.tiendaonline.dto.request.RealizarCompraRequestDTO;
import com.tiendaonline.dto.response.CompraResponseDTO;

import com.tiendaonline.entity.CarritoCompra;
import com.tiendaonline.entity.Compra;
import com.tiendaonline.entity.EstadoCompra;

import com.tiendaonline.exception.CarritoNoEncontradoException;
import com.tiendaonline.exception.CarritoVacioException;
import com.tiendaonline.exception.EstrategiaPagoNoEncontradaException;

import com.tiendaonline.mapper.CompraMapper;

import com.tiendaonline.repository.CarritoRepository;
import com.tiendaonline.repository.CompraRepository;

import com.tiendaonline.service.ICompraService;

import com.tiendaonline.strategy.EstrategiaPago;
import com.tiendaonline.strategy.ResultadoPago;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements ICompraService {

 private final CarritoRepository carritos;
 private final CompraRepository compras;
 private final List<EstrategiaPago> estrategias;
 private final CompraMapper mapper;

 @Override
 @Transactional
 public CompraResponseDTO realizarCompra(
         String codigo,
         RealizarCompraRequestDTO request) {

  if (codigo == null) {
   throw new CarritoNoEncontradoException();
  }

  CarritoCompra carrito = carritos
          .findByCodigoAnonimoForUpdate(codigo)
          .orElseThrow(CarritoNoEncontradoException::new);

  if (carrito.getDetalles().isEmpty()) {
   throw new CarritoVacioException();
  }

  BigDecimal total = carrito.getDetalles()
          .stream()
          .map(d -> d.getProducto()
                  .getPrecio()
                  .multiply(BigDecimal.valueOf(d.getCantidad())))
          .reduce(BigDecimal.ZERO, BigDecimal::add);

  Compra compra = compras.save(
          new Compra(total, request.getMetodoPago())
  );

  EstrategiaPago e = estrategias.stream()
          .filter(x -> x.getMetodoPago() == request.getMetodoPago())
          .findFirst()
          .orElseThrow(() ->
                  new EstrategiaPagoNoEncontradaException(
                          request.getMetodoPago()
                  )
          );

  ResultadoPago r = e.procesar(compra);

  compra.aplicarResultado(r);

  if (r.estado() == EstadoCompra.CONFIRMADA) {
   carrito.vaciar();
  }

  return mapper.toResponse(compra, r);
 }
}
