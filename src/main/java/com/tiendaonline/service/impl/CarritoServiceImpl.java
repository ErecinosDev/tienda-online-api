
package com.tiendaonline.service.impl;

import com.tiendaonline.dto.request.ActualizarCantidadRequestDTO;
import com.tiendaonline.dto.request.AgregarProductoRequestDTO;
import com.tiendaonline.dto.response.CarritoResponseDTO;

import com.tiendaonline.entity.CarritoCompra;
import com.tiendaonline.entity.DetalleCarrito;
import com.tiendaonline.entity.Producto;

import com.tiendaonline.exception.CarritoNoEncontradoException;
import com.tiendaonline.exception.DetalleCarritoNoEncontradoException;
import com.tiendaonline.exception.ProductoNoEncontradoException;

import com.tiendaonline.mapper.CarritoMapper;

import com.tiendaonline.repository.CarritoRepository;
import com.tiendaonline.repository.ProductoRepository;

import com.tiendaonline.service.ICarritoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements ICarritoService {

 private final CarritoRepository carritoRepository;
 private final ProductoRepository productoRepository;
 private final CarritoMapper carritoMapper;

 @Override
 @Transactional(readOnly = true)
 public CarritoResponseDTO obtenerCarrito(String codigo) {

  return codigo == null
          ? carritoMapper.toEmptyResponse()
          : carritoRepository.findByCodigoAnonimo(codigo)
          .map(carritoMapper::toResponse)
          .orElseGet(carritoMapper::toEmptyResponse);
 }

 @Override
 @Transactional
 public CarritoResponseDTO agregarProducto(
         String codigo,
         AgregarProductoRequestDTO r) {

  Producto p = productoRepository.findById(r.getIdProducto())
          .orElseThrow(() ->
                  new ProductoNoEncontradoException(r.getIdProducto())
          );

  CarritoCompra c = carritoRepository
          .findByCodigoAnonimoForUpdate(codigo)
          .orElseGet(() -> new CarritoCompra(codigo));

  DetalleCarrito d = c.buscarDetalle(p.getIdProducto());

  if (d == null) {

   c.agregarDetalle(
           new DetalleCarrito(p, r.getCantidad())
   );

  } else {

   d.sumarCantidad(r.getCantidad());
  }

  return carritoMapper.toResponse(
          carritoRepository.save(c)
  );
 }

 @Override
 @Transactional
 public CarritoResponseDTO actualizarCantidad(
         String codigo,
         Integer id,
         ActualizarCantidadRequestDTO r) {

  CarritoCompra c = buscarBloqueado(codigo);

  DetalleCarrito d = detalle(c, id);

  d.actualizarCantidad(r.getCantidad());

  return carritoMapper.toResponse(c);
 }

 @Override
 @Transactional
 public CarritoResponseDTO eliminarProducto(
         String codigo,
         Integer id) {

  CarritoCompra c = buscarBloqueado(codigo);

  c.eliminarDetalle(detalle(c, id));

  return carritoMapper.toResponse(c);
 }

 private CarritoCompra buscarBloqueado(String codigo) {

  return carritoRepository
          .findByCodigoAnonimoForUpdate(codigo)
          .orElseThrow(CarritoNoEncontradoException::new);
 }

 private DetalleCarrito detalle(
         CarritoCompra c,
         Integer id) {

  DetalleCarrito d = c.buscarDetalle(id);

  if (d == null) {

   throw new DetalleCarritoNoEncontradoException(id);
  }

  return d;
 }
}
