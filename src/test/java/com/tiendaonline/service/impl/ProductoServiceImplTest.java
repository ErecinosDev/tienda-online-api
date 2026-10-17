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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMapper productoMapper;

    private ProductoServiceImpl productoService;

    @BeforeEach
    void setUp() {
        productoService = new ProductoServiceImpl(productoRepository, productoMapper);
    }

    @Test
    void debeListarProductos() {
        Producto producto = producto("TEC-001");
        ProductoResponseDTO response = response("TEC-001");
        when(productoRepository.findAll()).thenReturn(List.of(producto));
        when(productoMapper.toResponse(producto)).thenReturn(response);

        List<ProductoResponseDTO> resultado = productoService.listar();

        assertThat(resultado).containsExactly(response);
    }

    @Test
    void debeObtenerProductoExistente() {
        Producto producto = producto("TEC-001");
        ProductoResponseDTO response = response("TEC-001");
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(productoMapper.toResponse(producto)).thenReturn(response);

        assertThat(productoService.obtener(1)).isSameAs(response);
    }

    @Test
    void debeRechazarProductoInexistente() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.obtener(99))
                .isInstanceOf(ProductoNoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void debeCrearProducto() {
        CrearProductoRequestDTO request = crearRequest("TEC-001");
        Producto producto = producto("TEC-001");
        ProductoResponseDTO response = response("TEC-001");
        when(productoRepository.existsByCodigo("TEC-001")).thenReturn(false);
        when(productoMapper.toEntity(request)).thenReturn(producto);
        when(productoRepository.saveAndFlush(producto)).thenReturn(producto);
        when(productoMapper.toResponse(producto)).thenReturn(response);

        assertThat(productoService.crear(request)).isSameAs(response);
    }

    @Test
    void debeRechazarCodigoDuplicadoAlCrear() {
        CrearProductoRequestDTO request = crearRequest("TEC-001");
        when(productoRepository.existsByCodigo("TEC-001")).thenReturn(true);

        assertThatThrownBy(() -> productoService.crear(request))
                .isInstanceOf(CodigoProductoDuplicadoException.class);
        verify(productoRepository, never()).saveAndFlush(any());
    }

    @Test
    void debeTraducirConflictoConcurrenteAlCrear() {
        CrearProductoRequestDTO request = crearRequest("TEC-001");
        Producto producto = producto("TEC-001");
        when(productoRepository.existsByCodigo("TEC-001")).thenReturn(false);
        when(productoMapper.toEntity(request)).thenReturn(producto);
        when(productoRepository.saveAndFlush(producto))
                .thenThrow(new DataIntegrityViolationException("unique"));

        assertThatThrownBy(() -> productoService.crear(request))
                .isInstanceOf(CodigoProductoDuplicadoException.class);
    }

    @Test
    void debeActualizarProducto() {
        Producto producto = producto("TEC-001");
        ActualizarProductoRequestDTO request = new ActualizarProductoRequestDTO(
                "TEC-002", "Teclado mecánico", new BigDecimal("250.00"), 5);
        ProductoResponseDTO response = new ProductoResponseDTO(
                1, "TEC-002", "Teclado mecánico", new BigDecimal("250.00"), 5);
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepository.existsByCodigo("TEC-002")).thenReturn(false);
        when(productoRepository.saveAndFlush(producto)).thenReturn(producto);
        when(productoMapper.toResponse(producto)).thenReturn(response);

        ProductoResponseDTO resultado = productoService.actualizar(1, request);

        assertThat(resultado).isSameAs(response);
        assertThat(producto.getCodigo()).isEqualTo("TEC-002");
        assertThat(producto.getNombre()).isEqualTo("Teclado mecánico");
        assertThat(producto.getPrecio()).isEqualByComparingTo("250.00");
        assertThat(producto.getStock()).isEqualTo(5);
    }

    @Test
    void noDebeComprobarDuplicadoSiElCodigoNoCambia() {
        Producto producto = producto("TEC-001");
        ActualizarProductoRequestDTO request = new ActualizarProductoRequestDTO(
                "TEC-001", "Teclado actualizado", new BigDecimal("130.00"), 8);
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepository.saveAndFlush(producto)).thenReturn(producto);
        when(productoMapper.toResponse(producto)).thenReturn(response("TEC-001"));

        productoService.actualizar(1, request);

        verify(productoRepository, never()).existsByCodigo(any());
    }

    @Test
    void debeRechazarCodigoDuplicadoAlActualizar() {
        Producto producto = producto("TEC-001");
        ActualizarProductoRequestDTO request = new ActualizarProductoRequestDTO(
                "TEC-002", "Teclado", new BigDecimal("125.50"), 10);
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepository.existsByCodigo("TEC-002")).thenReturn(true);

        assertThatThrownBy(() -> productoService.actualizar(1, request))
                .isInstanceOf(CodigoProductoDuplicadoException.class);
        verify(productoRepository, never()).saveAndFlush(any());
    }

    @Test
    void debeRechazarActualizacionDeProductoInexistente() {
        ActualizarProductoRequestDTO request = new ActualizarProductoRequestDTO(
                "TEC-001", "Teclado", new BigDecimal("125.50"), 10);
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.actualizar(99, request))
                .isInstanceOf(ProductoNoEncontradoException.class)
                .hasMessageContaining("99");
        verify(productoRepository, never()).saveAndFlush(any());
    }

    @Test
    void debeEliminarProducto() {
        Producto producto = producto("TEC-001");
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        productoService.eliminar(1);

        verify(productoRepository).delete(producto);
        verify(productoRepository).flush();
    }

    @Test
    void debeInformarCuandoElProductoEstaEnUso() {
        Producto producto = producto("TEC-001");
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        org.mockito.Mockito.doThrow(new DataIntegrityViolationException("foreign key"))
                .when(productoRepository).flush();

        assertThatThrownBy(() -> productoService.eliminar(1))
                .isInstanceOf(ProductoEnUsoException.class);
    }

    @Test
    void debeRechazarEliminacionDeProductoInexistente() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.eliminar(99))
                .isInstanceOf(ProductoNoEncontradoException.class)
                .hasMessageContaining("99");
        verify(productoRepository, never()).delete(any());
    }

    private Producto producto(String codigo) {
        return new Producto(codigo, "Teclado", new BigDecimal("125.50"), 10);
    }

    private CrearProductoRequestDTO crearRequest(String codigo) {
        return new CrearProductoRequestDTO(codigo, "Teclado", new BigDecimal("125.50"), 10);
    }

    private ProductoResponseDTO response(String codigo) {
        return new ProductoResponseDTO(1, codigo, "Teclado", new BigDecimal("125.50"), 10);
    }
}
