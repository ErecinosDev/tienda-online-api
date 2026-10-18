
package com.tiendaonline.service;

import com.tiendaonline.dto.request.RealizarCompraRequestDTO;
import com.tiendaonline.dto.response.CompraResponseDTO;

public interface ICompraService {

    CompraResponseDTO realizarCompra(
            String codigoAnonimo,
            RealizarCompraRequestDTO request
    );
}
