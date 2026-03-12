package com.co.periferia.domain.exception;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Dominio                                              ║
 * ║  CLASE: EntidadNoEncontradaException.java                   ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Lanzada cuando se busca una entidad por ID y no existe.    ║
 * ║  El GlobalExceptionHandler la convierte en HTTP 404.        ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public class UsuarioNoEncontradaException extends RuntimeException {
    public UsuarioNoEncontradaException(String id) {
        super("Entidad no encontrada con ID: " + id);
    }
    public UsuarioNoEncontradaException(String usuario, String id) {
        super(usuario + " no encontrada con ID: " + id);
    }
}
