
package com.tiendaonline.service.impl;

import com.tiendaonline.dto.request.ActualizarProductoRequestDTO;
import com.tiendaonline.dto.request.CrearProductoRequestDTO;
import com.tiendaonline.dto.response.ProductoResponseDTO;

import com.tiendaonline.entity.Producto;

import com.tiendaonline.exception.CodigoProductoDuplicadoException;
import com.tiendaonline.exception.ProductoEnUsoException;
import com.tiendaonline.exception.ProductoNoEncontradoException;

import com.tiendaonline.mapper.ProductoMapper;

import com.tiendaonline.repository.ProductoRepository;

import com.tiendaonline.service.IProductoService;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements IProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listar() {

        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtener(Integer idProducto) {

        return productoMapper.toResponse(
                buscarPorId(idProducto)
        );
    }

    @Override
    @Transactional
    public ProductoResponseDTO crear(CrearProductoRequestDTO request) {

        verificarCodigoDisponible(request.getCodigo());

        try {

            Producto producto = productoMapper.toEntity(request);

            return productoMapper.toResponse(
                    productoRepository.saveAndFlush(producto)
            );

        } catch (DataIntegrityViolationException exception) {

            throw new CodigoProductoDuplicadoException(
                    request.getCodigo()
            );
        }
    }

    @Override
    @Transactional
    public ProductoResponseDTO actualizar(
            Integer idProducto,
            ActualizarProductoRequestDTO request) {

        Producto producto = buscarPorId(idProducto);

        if (!Objects.equals(producto.getCodigo(), request.getCodigo())) {

            verificarCodigoDisponible(request.getCodigo());
        }

        producto.actualizarDatos(
                request.getCodigo(),
                request.getNombre(),
                request.getPrecio(),
                request.getStock()
        );

        try {

            return productoMapper.toResponse(
                    productoRepository.saveAndFlush(producto)
            );

        } catch (DataIntegrityViolationException exception) {

            throw new CodigoProductoDuplicadoException(
                    request.getCodigo()
            );
        }
    }

    @Override
    @Transactional
    public void eliminar(Integer idProducto) {

        Producto producto = buscarPorId(idProducto);

        try {

            productoRepository.delete(producto);
            productoRepository.flush();

        } catch (DataIntegrityViolationException exception) {

            throw new ProductoEnUsoException(idProducto);
        }
    }

    private Producto buscarPorId(Integer idProducto) {

        return productoRepository.findById(idProducto)
                .orElseThrow(() ->
                        new ProductoNoEncontradoException(idProducto)
                );
    }

    private void verificarCodigoDisponible(String codigo) {

        if (productoRepository.existsByCodigo(codigo)) {

            throw new CodigoProductoDuplicadoException(codigo);
        }
    }
}
