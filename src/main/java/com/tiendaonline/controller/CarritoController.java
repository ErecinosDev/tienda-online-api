
package com.tiendaonline.controller;

import com.tiendaonline.dto.request.ActualizarCantidadRequestDTO;
import com.tiendaonline.dto.request.AgregarProductoRequestDTO;
import com.tiendaonline.dto.response.CarritoResponseDTO;
import com.tiendaonline.service.ICarritoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

 private static final String CODIGO_ANONIMO = "codigoAnonimo";

 private final ICarritoService carritoService;

 @GetMapping
 public ResponseEntity<CarritoResponseDTO> obtener(
         HttpServletRequest request) {

  return ResponseEntity.ok(
          carritoService.obtenerCarrito(
                  codigoExistente(request.getSession(false))
          )
  );
 }

 @PostMapping("/productos")
 public ResponseEntity<CarritoResponseDTO> agregar(
         HttpServletRequest request,
         @Valid @RequestBody AgregarProductoRequestDTO r) {

  return ResponseEntity.ok(
          carritoService.agregarProducto(
                  codigoCrear(request.getSession(true)),
                  r
          )
  );
 }

 @PutMapping("/productos/{idProducto}")
 public ResponseEntity<CarritoResponseDTO> actualizar(
         HttpServletRequest request,
         @PathVariable Integer idProducto,
         @Valid @RequestBody ActualizarCantidadRequestDTO r) {

  return ResponseEntity.ok(
          carritoService.actualizarCantidad(
                  codigoExistente(request.getSession(false)),
                  idProducto,
                  r
          )
  );
 }

 @DeleteMapping("/productos/{idProducto}")
 public ResponseEntity<CarritoResponseDTO> eliminar(
         HttpServletRequest request,
         @PathVariable Integer idProducto) {

  return ResponseEntity.ok(
          carritoService.eliminarProducto(
                  codigoExistente(request.getSession(false)),
                  idProducto
          )
  );
 }

 private String codigoExistente(HttpSession s) {

  return s == null
          ? null
          : (String) s.getAttribute(CODIGO_ANONIMO);
 }

 private String codigoCrear(HttpSession s) {

  String c = codigoExistente(s);

  if (c == null) {
   c = UUID.randomUUID().toString();
   s.setAttribute(CODIGO_ANONIMO, c);
  }

  return c;
 }
}
