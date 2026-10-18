
package com.tiendaonline.mapper;

import com.tiendaonline.dto.response.CompraResponseDTO;

import com.tiendaonline.entity.Compra;

import com.tiendaonline.strategy.ResultadoPago;

import org.springframework.stereotype.Component;

@Component
public class CompraMapper {

    public CompraResponseDTO toResponse(Compra c, ResultadoPago r) {

        return new CompraResponseDTO(
                c.getIdCompra(),
                c.getTotal(),
                c.getMetodoPago(),
                c.getEstado(),
                c.getFechaCreacion(),
                r.mensaje()
        );
    }
}

