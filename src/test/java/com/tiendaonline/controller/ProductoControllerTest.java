package com.tiendaonline.controller;

import com.tiendaonline.dto.request.ActualizarProductoRequestDTO;
import com.tiendaonline.dto.request.CrearProductoRequestDTO;
import com.tiendaonline.dto.response.ProductoResponseDTO;

import com.tiendaonline.exception.CodigoProductoDuplicadoException;
import com.tiendaonline.exception.GlobalExceptionHandler;
import com.tiendaonline.exception.ProductoEnUsoException;
import com.tiendaonline.exception.ProductoNoEncontradoException;

import com.tiendaonline.service.IProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IProductoService productoService;

    @BeforeEach
    void setUp() {

        ProductoController controller =
                new ProductoController(productoService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void debeListarProductos() throws Exception {

        when(productoService.listar())
                .thenReturn(List.of(response()));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value(1))
                .andExpect(jsonPath("$[0].codigo").value("TEC-001"));
    }

    @Test
    void debeObtenerProducto() throws Exception {

        when(productoService.obtener(1))
                .thenReturn(response());

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Teclado"));
    }

    @Test
    void debeResponderNotFound() throws Exception {

        when(productoService.obtener(99))
                .thenThrow(new ProductoNoEncontradoException(99));

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(
                        MediaType.APPLICATION_PROBLEM_JSON
                ))
                .andExpect(jsonPath("$.title")
                        .value("Producto no encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.instance")
                        .value("/api/productos/99"));
    }

    @Test
    void debeRechazarIdentificadorConFormatoInvalido()
            throws Exception {

        mockMvc.perform(get("/api/productos/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Solicitud inválida"));
    }

    @Test
    void debeCrearProducto() throws Exception {

        when(productoService.crear(any(CrearProductoRequestDTO.class)))
                .thenReturn(response());

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo":"TEC-001","nombre":"Teclado","precio":125.50,"stock":10}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/api/productos/1"
                ))
                .andExpect(jsonPath("$.idProducto").value(1));
    }

    @Test
    void debeRechazarEntradaInvalida() throws Exception {

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo":" ","nombre":"","precio":-1.25,"stock":-1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(
                        MediaType.APPLICATION_PROBLEM_JSON
                ))
                .andExpect(jsonPath("$.title")
                        .value("Solicitud inválida"))
                .andExpect(jsonPath("$.errors.codigo").isArray())
                .andExpect(jsonPath("$.errors.nombre").isArray())
                .andExpect(jsonPath("$.errors.precio").isArray())
                .andExpect(jsonPath("$.errors.stock").isArray());
    }

    @Test
    void debeRechazarPrecioConMasDeDosDecimales()
            throws Exception {

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo":"TEC-001","nombre":"Teclado","precio":1.999,"stock":1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.precio").isArray());
    }

    @Test
    void debeRechazarCamposObligatoriosAusentes()
            throws Exception {

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.codigo").isArray())
                .andExpect(jsonPath("$.errors.nombre").isArray())
                .andExpect(jsonPath("$.errors.precio").isArray())
                .andExpect(jsonPath("$.errors.stock").isArray());
    }

    @Test
    void debeRechazarCamposQueExcedenElEsquema()
            throws Exception {

        String codigoLargo = "A".repeat(51);
        String nombreLargo = "B".repeat(151);

        String cuerpo = """
                {"codigo":"%s","nombre":"%s","precio":12345678901.00,"stock":1}
                """.formatted(codigoLargo, nombreLargo);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.codigo").isArray())
                .andExpect(jsonPath("$.errors.nombre").isArray())
                .andExpect(jsonPath("$.errors.precio").isArray());
    }

    @Test
    void debeResponderConflictoPorCodigoDuplicado()
            throws Exception {

        when(productoService.crear(any(CrearProductoRequestDTO.class)))
                .thenThrow(
                        new CodigoProductoDuplicadoException("TEC-001")
                );

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo":"TEC-001","nombre":"Teclado","precio":125.50,"stock":10}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title")
                        .value("Código de producto duplicado"));
    }

    @Test
    void debeActualizarProducto() throws Exception {

        when(productoService.actualizar(
                eq(1),
                any(ActualizarProductoRequestDTO.class)
        )).thenReturn(response());

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo":"TEC-001","nombre":"Teclado","precio":125.50,"stock":10}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(1));
    }

    @Test
    void debeEliminarProducto() throws Exception {

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService).eliminar(1);
    }

    @Test
    void debeResponderConflictoSiElProductoEstaEnUso()
            throws Exception {

        doThrow(new ProductoEnUsoException(1))
                .when(productoService)
                .eliminar(1);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title")
                        .value("Producto en uso"));
    }

    private ProductoResponseDTO response() {

        return new ProductoResponseDTO(
                1,
                "TEC-001",
                "Teclado",
                new BigDecimal("125.50"),
                10
        );
    }
}
