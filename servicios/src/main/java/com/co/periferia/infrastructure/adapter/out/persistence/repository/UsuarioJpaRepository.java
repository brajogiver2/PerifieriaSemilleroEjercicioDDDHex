package com.co.periferia.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.co.periferia.domain.model.EstadoUsuario;
import com.co.periferia.infrastructure.adapter.out.persistence.entity.UsuarioJpaEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Infraestructura — Persistencia                       ║
 * ║  CLASE: EntidadJpaRepository.java                           ║
 * ║  TIPO: Spring Data JPA (interfaz)                           ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Spring genera la implementación automáticamente.           ║
 * ║  Solo define los métodos de consulta, sin lógica.           ║
 * ║  Trabaja ÚNICAMENTE con EntidadJpaEntity, nunca con dominio.║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, String> {

    // Spring Data genera el SQL desde el nombre del método:
    List<UsuarioJpaEntity> findByEstado(EstadoUsuario estado);

    List<UsuarioJpaEntity> findByNombreContainingIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);

    // Para queries más complejas usa @Query con JPQL:
    @Query("SELECT e FROM UsuarioJpaEntity e WHERE e.estado = :estado AND e.creadoEn >= :desde")
    List<UsuarioJpaEntity> findByEstadoDesde(
        @Param("estado") EstadoUsuario estado,
        @Param("desde")  LocalDateTime desde
    );

    // Para SQL nativo cuando JPQL no alcanza:
    @Query(value = "SELECT * FROM entidades WHERE estado = 'ACTIVO' LIMIT :limite",
           nativeQuery = true)
    List<UsuarioJpaEntity> findActivosLimitado(@Param("limite") int limite);
}
