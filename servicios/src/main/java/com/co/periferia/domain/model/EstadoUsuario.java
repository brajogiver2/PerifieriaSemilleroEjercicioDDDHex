package com.co.periferia.domain.model;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Dominio                                              ║
 * ║  CLASE: EstadoEntidad.java                                  ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Enum del ciclo de vida de la entidad.                      ║
 * ║  Vive en el dominio porque los estados son conocimiento     ║
 * ║  puro de negocio.                                           ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public enum EstadoUsuario {
    ACTIVO,
    INACTIVO,
    PENDIENTE
    // ← agrega los estados que necesite tu negocio
}
