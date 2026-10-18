
package com.tiendaonline.mapper;

import com.tiendaonline.dto.request.CrearProductoRequestDTO;
import com.tiendaonline.dto.response.ProductoResponseDTO;

import com.tiendaonline.entity.Producto;

import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(CrearProductoRequestDTO request) {

        return new Producto(
                request.getCodigo(),
                request.getNombre(),
                request.getPrecio(),
                request.getStock()
        );
    }

    public ProductoResponseDTO toResponse(Producto producto) {

        return new ProductoResponseDTO(
                producto.getIdProducto(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock()
        );
    }
}
