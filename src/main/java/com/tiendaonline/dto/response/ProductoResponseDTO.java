
package com.tiendaonline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductoResponseDTO {

    private final Integer idProducto;

    private final String codigo;

    private final String nombre;

    private final BigDecimal precio;

    private final Integer stock;
}
