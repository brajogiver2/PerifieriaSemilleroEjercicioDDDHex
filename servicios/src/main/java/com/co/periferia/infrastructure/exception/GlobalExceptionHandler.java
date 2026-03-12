package com.co.periferia.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.co.periferia.domain.exception.ReglaDeNegocioException;
import com.co.periferia.domain.exception.UsuarioNoEncontradaException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Infraestructura — Manejo de Errores                  ║
 * ║  CLASE: GlobalExceptionHandler.java                         ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Intercepta excepciones del dominio y las convierte en      ║
 * ║  respuestas HTTP con el código de estado correcto.          ║
 * ║                                                              ║
 * ║  El dominio NO sabe nada de HTTP 404/400/422.               ║
 * ║  Este handler es el traductor.                              ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** 404 — Recurso no encontrado */
    @ExceptionHandler(UsuarioNoEncontradaException.class)
    public ResponseEntity<ApiError> handleNotFound(UsuarioNoEncontradaException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiError("NOT_FOUND", ex.getMessage()));
    }

    /** 422 — Violación de regla de negocio */
    @ExceptionHandler(ReglaDeNegocioException.class)
    public ResponseEntity<ApiError> handleBusinessRule(ReglaDeNegocioException ex) {
        log.warn("Regla de negocio violada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(new ApiError("BUSINESS_RULE_VIOLATION", ex.getMessage()));
    }

    /** 400 — Validación de campos de entrada (@Valid) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String detalle = ex.getBindingResult().getFieldErrors().stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .collect(Collectors.joining(" | "));
        log.warn("Error de validación: {}", detalle);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiError("VALIDATION_ERROR", detalle));
    }

    /** 500 — Error inesperado */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiError("INTERNAL_ERROR",
                               "Ha ocurrido un error inesperado. Por favor intenta más tarde."));
    }

    // ── DTO de respuesta de error ─────────────────────────────────
    public record ApiError(
        String        codigo,
        String        mensaje,
        LocalDateTime timestamp
    ) {
        public ApiError(String codigo, String mensaje) {
            this(codigo, mensaje, LocalDateTime.now());
        }
    }
}
