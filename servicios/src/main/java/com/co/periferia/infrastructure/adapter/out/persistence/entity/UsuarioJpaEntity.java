package com.co.periferia.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.co.periferia.domain.model.EstadoUsuario;
import com.co.periferia.domain.model.Usuario;

import java.time.LocalDateTime;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Infraestructura — Persistencia                       ║
 * ║  CLASE: EntidadJpaEntity.java                               ║
 * ║  TIPO: JPA Entity (@Entity)                                 ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Mapea la tabla de BD ↔ objeto Java.                        ║
 * ║  Contiene también el mapper: from(dominio) y toDomain().    ║
 * ║                                                              ║
 * ║  VIVE en infraestructura porque las anotaciones JPA         ║
 * ║  (@Entity, @Table, @Column) son tecnología, no negocio.     ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Entity
@Table(name = "entidades")   // ← cambia al nombre de tu tabla
@EntityListeners(AuditingEntityListener.class) // para @CreatedDate y @LastModifiedDate
public class UsuarioJpaEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // Enum guardado como String legible (no como número ordinal)
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoUsuario estado;

    // Auditoría automática con Spring Data JPA
    @CreatedDate
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @LastModifiedDate
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    // JPA requiere constructor sin argumentos
    protected UsuarioJpaEntity() {}

    // ── MAPPER: Dominio → Entity ─────────────────────────────────
    /**
     * Convierte entidad de dominio → entity JPA para persistir.
     */
    public static UsuarioJpaEntity from(Usuario usuario) {
        var e = new UsuarioJpaEntity();
        e.id            = usuario.getId();
        e.nombre        = usuario.getNombre();
        e.descripcion   = usuario.getDescripcion();
        e.estado        = usuario.getEstado();
        e.creadoEn      = usuario.getCreadoEn();
        e.actualizadoEn = usuario.getActualizadoEn();
        return e;
    }

    // ── MAPPER: Entity → Dominio ─────────────────────────────────
    /**
     * Convierte entity JPA → entidad de dominio después de leer de BD.
     * Usa reconstituir() para no re-ejecutar validaciones.
     */
    public Usuario toDomain() {
        return Usuario.reconstituir(
            this.id, this.nombre, this.descripcion,
            this.estado, this.creadoEn, this.actualizadoEn
        );
    }
}
