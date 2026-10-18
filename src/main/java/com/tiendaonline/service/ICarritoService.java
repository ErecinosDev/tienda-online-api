
package com.tiendaonline.service;

import com.tiendaonline.dto.request.ActualizarCantidadRequestDTO;
import com.tiendaonline.dto.request.AgregarProductoRequestDTO;
import com.tiendaonline.dto.response.CarritoResponseDTO;

public interface ICarritoService {

    CarritoResponseDTO obtenerCarrito(String codigoAnonimo);

    CarritoResponseDTO agregarProducto(
            String codigoAnonimo,
            AgregarProductoRequestDTO request
    );

    CarritoResponseDTO actualizarCantidad(
            String codigoAnonimo,
            Integer idProducto,
            ActualizarCantidadRequestDTO request
    );

    CarritoResponseDTO eliminarProducto(
            String codigoAnonimo,
            Integer idProducto
    );
}
