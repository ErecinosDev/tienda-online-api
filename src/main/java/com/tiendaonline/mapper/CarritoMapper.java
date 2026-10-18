
package com.tiendaonline.mapper;

import com.tiendaonline.dto.response.CarritoResponseDTO;
import com.tiendaonline.dto.response.DetalleCarritoResponseDTO;

import com.tiendaonline.entity.CarritoCompra;
import com.tiendaonline.entity.DetalleCarrito;
import com.tiendaonline.entity.Producto;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CarritoMapper {

 public CarritoResponseDTO toResponse(CarritoCompra c) {

  List<DetalleCarritoResponseDTO> d = c.getDetalles()
          .stream()
          .map(this::toDetalleResponse)
          .toList();

  BigDecimal total = d.stream()
          .map(DetalleCarritoResponseDTO::subtotal)
          .reduce(BigDecimal.ZERO, BigDecimal::add);

  return new CarritoResponseDTO(
          c.getIdCarrito(),
          d,
          total
  );
 }

 public CarritoResponseDTO toEmptyResponse() {

  return new CarritoResponseDTO(
          null,
          List.of(),
          BigDecimal.ZERO
  );
 }

 public DetalleCarritoResponseDTO toDetalleResponse(DetalleCarrito d) {

  Producto p = d.getProducto();

  return new DetalleCarritoResponseDTO(
          p.getIdProducto(),
          p.getCodigo(),
          p.getNombre(),
          p.getPrecio(),
          d.getCantidad(),
          d.getSubtotal()
  );
 }
}
