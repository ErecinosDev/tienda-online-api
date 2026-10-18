
package com.tiendaonline.controller;

import com.tiendaonline.dto.request.RealizarCompraRequestDTO;
import com.tiendaonline.dto.response.CompraResponseDTO;
import com.tiendaonline.service.ICompraService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {

    private final ICompraService compras;

    @PostMapping
    public ResponseEntity<CompraResponseDTO> realizar(
            HttpServletRequest request,
            @Valid @RequestBody RealizarCompraRequestDTO dto) {

        HttpSession s = request.getSession(false);

        String codigo = s == null
                ? null
                : (String) s.getAttribute("codigoAnonimo");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(compras.realizarCompra(codigo, dto));
    }
}
