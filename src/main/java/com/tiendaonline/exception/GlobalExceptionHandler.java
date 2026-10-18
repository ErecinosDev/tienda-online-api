
package com.tiendaonline.exception;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> manejarValidacion(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        Map<String, List<String>> errores = new LinkedHashMap<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {

            errores.computeIfAbsent(
                    error.getField(),
                    ignored -> new ArrayList<>()
            ).add(error.getDefaultMessage());
        }

        ProblemDetail problem = crearProblema(
                HttpStatus.BAD_REQUEST,
                "Solicitud inválida",
                "Uno o más campos no son válidos",
                request
        );

        problem.setProperty("errors", errores);

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> manejarCuerpoIlegible(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {

        ProblemDetail problem = crearProblema(
                HttpStatus.BAD_REQUEST,
                "Solicitud inválida",
                "El cuerpo de la solicitud no tiene un formato válido",
                request
        );

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> manejarTipoDeArgumentoInvalido(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {

        ProblemDetail problem = crearProblema(
                HttpStatus.BAD_REQUEST,
                "Solicitud inválida",
                "El parámetro " + exception.getName() + " no tiene un valor válido",
                request
        );

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ProblemDetail> manejarProductoNoEncontrado(
            ProductoNoEncontradoException exception,
            HttpServletRequest request) {

        ProblemDetail problem = crearProblema(
                HttpStatus.NOT_FOUND,
                "Producto no encontrado",
                exception.getMessage(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problem);
    }

    @ExceptionHandler({
            CarritoNoEncontradoException.class,
            DetalleCarritoNoEncontradoException.class
    })
    public ResponseEntity<ProblemDetail> manejarCarritoNoEncontrado(
            RuntimeException exception,
            HttpServletRequest request) {

        ProblemDetail problem = crearProblema(
                HttpStatus.NOT_FOUND,
                "Carrito no encontrado",
                exception.getMessage(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problem);
    }

    @ExceptionHandler(CarritoVacioException.class)
    public ResponseEntity<ProblemDetail> manejarCarritoVacio(
            CarritoVacioException exception,
            HttpServletRequest request) {

        ProblemDetail problem = crearProblema(
                HttpStatus.BAD_REQUEST,
                "Carrito vacío",
                exception.getMessage(),
                request
        );

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(PagoTecnicoException.class)
    public ResponseEntity<ProblemDetail> manejarPagoTecnico(
            PagoTecnicoException exception,
            HttpServletRequest request) {

        ProblemDetail problem = crearProblema(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Pago no disponible",
                exception.getMessage(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(problem);
    }

    @ExceptionHandler(EstrategiaPagoNoEncontradaException.class)
    public ResponseEntity<ProblemDetail> manejarEstrategiaAusente(
            EstrategiaPagoNoEncontradaException exception,
            HttpServletRequest request) {

        ProblemDetail problem = crearProblema(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Configuración de pago inválida",
                exception.getMessage(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> manejarReglaInvalida(
            IllegalArgumentException exception,
            HttpServletRequest request) {

        ProblemDetail problem = crearProblema(
                HttpStatus.BAD_REQUEST,
                "Solicitud inválida",
                exception.getMessage(),
                request
        );

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(CodigoProductoDuplicadoException.class)
    public ResponseEntity<ProblemDetail> manejarCodigoDuplicado(
            CodigoProductoDuplicadoException exception,
            HttpServletRequest request) {

        ProblemDetail problem = crearProblema(
                HttpStatus.CONFLICT,
                "Código de producto duplicado",
                exception.getMessage(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(problem);
    }

    @ExceptionHandler(ProductoEnUsoException.class)
    public ResponseEntity<ProblemDetail> manejarProductoEnUso(
            ProductoEnUsoException exception,
            HttpServletRequest request) {

        ProblemDetail problem = crearProblema(
                HttpStatus.CONFLICT,
                "Producto en uso",
                exception.getMessage(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> manejarErrorInterno(
            Exception exception,
            HttpServletRequest request) {

        log.error(
                "Error no controlado al procesar {}",
                request.getRequestURI(),
                exception
        );

        ProblemDetail problem = crearProblema(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno",
                "Ocurrió un error inesperado al procesar la solicitud",
                request
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problem);
    }

    private ProblemDetail crearProblema(
            HttpStatus status,
            String titulo,
            String detalle,
            HttpServletRequest request) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                status,
                detalle
        );

        problem.setTitle(titulo);
        problem.setInstance(URI.create(request.getRequestURI()));

        return problem;
    }
}
