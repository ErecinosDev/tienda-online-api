
package com.tiendaonline.dto.request;

import com.tiendaonline.entity.MetodoPago;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RealizarCompraRequestDTO {

    @NotNull
    private MetodoPago metodoPago;
}

