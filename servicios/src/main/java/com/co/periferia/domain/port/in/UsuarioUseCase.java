package com.co.periferia.domain.port.in;

import java.util.List;

import com.co.periferia.domain.model.Usuario;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Dominio — Puerto de Entrada                          ║
 * ║  CLASE: EntidadUseCase.java                                 ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Define el CONTRATO de lo que puede hacer el sistema.       ║
 * ║  Es la "puerta de entrada" del hexágono.                    ║
 * ║                                                              ║
 * ║  Implementado por: EntidadUseCaseImpl (capa Aplicación)     ║
 * ║  Usado por:                                                  ║
 * ║    → EntidadController  (adaptador HTTP de entrada)         ║
 * ║    → EntidadConsumer    (adaptador RabbitMQ de entrada)     ║
 * ║    → EntidadScheduler   (adaptador Scheduler de entrada)    ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public interface UsuarioUseCase {

    /** Crea una nueva entidad */
    Usuario crear(CrearEntidadCommand command);

    /** Busca una entidad por su ID */
    Usuario buscarPorId(String id);

    /** Lista todas las entidades activas */
    List<Usuario> listarActivas();

    /** Actualiza el nombre de una entidad */
    Usuario actualizar(String id, ActualizarEntidadCommand command);

    /** Desactiva una entidad */
    void desactivar(String id);

    // ─── Commands ────────────────────────────────────────────────
    // Los Commands encapsulan los datos de entrada de cada operación.
    // Usar records (Java 16+): inmutables y concisos.

    /**
     * Datos necesarios para crear una entidad.
     * Sin ID (lo genera el caso de uso), sin estado (lo define el dominio).
     */
    record CrearEntidadCommand(
        String nombre,
        String descripcion
        // ← agrega los campos que necesites
    ) {}

    /**
     * Datos para actualizar una entidad.
     * Solo los campos que pueden cambiar.
     */
    record ActualizarEntidadCommand(
        String nombre,
        String descripcion
        // ← agrega los campos actualizables
    ) {}
}
