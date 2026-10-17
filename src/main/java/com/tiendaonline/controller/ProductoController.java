package com.tiendaonline.controller;

import com.tiendaonline.dto.request.ActualizarProductoRequestDTO;
import com.tiendaonline.dto.request.CrearProductoRequestDTO;
import com.tiendaonline.dto.response.ProductoResponseDTO;
import com.tiendaonline.service.IProductoService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final IProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{idProducto}")
    public ResponseEntity<ProductoResponseDTO> obtener(@PathVariable Integer idProducto) {
        return ResponseEntity.ok(productoService.obtener(idProducto));
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(
            @Valid @RequestBody CrearProductoRequestDTO request) {
        ProductoResponseDTO productoCreado = productoService.crear(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{idProducto}")
                .buildAndExpand(productoCreado.getIdProducto())
                .toUri();

        return ResponseEntity.created(ubicacion).body(productoCreado);
    }

    @PutMapping("/{idProducto}")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Integer idProducto,
            @Valid @RequestBody ActualizarProductoRequestDTO request) {
        return ResponseEntity.ok(productoService.actualizar(idProducto, request));
    }

    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idProducto) {
        productoService.eliminar(idProducto);
        return ResponseEntity.noContent().build();
    }
}
