
package com.tiendaonline.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarCantidadRequestDTO {

    @NotNull
    @Positive
    private Integer cantidad;
}
