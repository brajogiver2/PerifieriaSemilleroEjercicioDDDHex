package com.co.periferia.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.co.periferia.domain.model.EstadoUsuario;
import com.co.periferia.domain.model.Usuario;
import com.co.periferia.domain.port.out.UsuarioRepositoryPort;
import com.co.periferia.infrastructure.adapter.out.persistence.entity.UsuarioJpaEntity;
import com.co.periferia.infrastructure.adapter.out.persistence.repository.UsuarioJpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Infraestructura — Adaptador de Salida                ║
 * ║  CLASE: EntidadRepositoryAdapter.java                       ║
 * ║  TIPO: Persistencia JPA (implementa puerto de dominio)      ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Conecta el puerto del dominio con Spring Data JPA.         ║
 * ║  Es el TRADUCTOR entre los dos mundos:                      ║
 * ║    Dominio  ←→  Infraestructura                             ║
 * ║    Entidad  ←→  EntidadJpaEntity                            ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioJpaEntity entity  = UsuarioJpaEntity.from(usuario); // dominio → JPA
        UsuarioJpaEntity guardada = jpaRepository.save(entity);
        return guardada.toDomain();                                 // JPA → dominio
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return jpaRepository.findById(id)
                            .map(UsuarioJpaEntity::toDomain);      // JPA → dominio
    }

    @Override
    public List<Usuario> buscarActivas() {
        return jpaRepository.findByEstado(EstadoUsuario.ACTIVO)
                            .stream()
                            .map(UsuarioJpaEntity::toDomain)
                            .toList();
    }

    @Override
    public boolean existePorId(String id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void eliminar(String id) {
        jpaRepository.deleteById(id);
    }
}
