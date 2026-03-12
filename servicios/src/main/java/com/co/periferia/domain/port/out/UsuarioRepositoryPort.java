package com.co.periferia.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.co.periferia.domain.model.Usuario;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Dominio — Puerto de Salida                           ║
 * ║  CLASE: EntidadRepositoryPort.java                          ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  El dominio define LO QUE NECESITA para persistir datos.    ║
 * ║  No sabe si es PostgreSQL, MongoDB, DynamoDB, etc.          ║
 * ║                                                              ║
 * ║  Implementado por: EntidadRepositoryAdapter (infraestructura)║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public interface UsuarioRepositoryPort {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(String id);
    List<Usuario> buscarActivas();
    boolean existePorId(String id);
    void eliminar(String id);
}
