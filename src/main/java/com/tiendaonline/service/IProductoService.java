package com.tiendaonline.service;

import com.tiendaonline.dto.request.ActualizarProductoRequestDTO;
import com.tiendaonline.dto.request.CrearProductoRequestDTO;
import com.tiendaonline.dto.response.ProductoResponseDTO;

import java.util.List;

public interface IProductoService {

    List<ProductoResponseDTO> listar();

    ProductoResponseDTO obtener(Integer idProducto);

    ProductoResponseDTO crear(CrearProductoRequestDTO request);

    ProductoResponseDTO actualizar(
            Integer idProducto,
            ActualizarProductoRequestDTO request
    );

    void eliminar(Integer idProducto);
}