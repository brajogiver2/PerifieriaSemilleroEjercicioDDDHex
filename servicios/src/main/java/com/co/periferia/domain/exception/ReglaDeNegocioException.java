package com.co.periferia.domain.exception;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Dominio                                              ║
 * ║  CLASE: ReglaDeNegocioException.java                        ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Excepción BASE para todas las violaciones de reglas        ║
 * ║  de negocio del dominio.                                    ║
 * ║                                                              ║
 * ║  PATRÓN: Crea subclases específicas para cada tipo de       ║
 * ║  error de negocio. Ejemplo:                                 ║
 * ║    class SaldoInsuficienteException extends ReglaDeNegocio  ║
 * ║    class EstadoInvalidoException extends ReglaDeNegocio     ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public class ReglaDeNegocioException extends RuntimeException {
    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }

    public ReglaDeNegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
