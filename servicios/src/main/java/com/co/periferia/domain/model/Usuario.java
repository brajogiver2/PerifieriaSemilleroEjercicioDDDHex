package com.co.periferia.domain.model;

import java.time.LocalDateTime;

import com.co.periferia.domain.exception.ReglaDeNegocioException;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Dominio                                              ║
 * ║  CLASE: Entidad.java                                        ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Entidad principal del dominio.                             ║
 * ║  → Sin anotaciones de Spring, JPA, Jackson ni frameworks   ║
 * ║  → Contiene los datos + las reglas de negocio              ║
 * ║  → Es Java puro: testeable sin levantar nada               ║
 * ║                                                              ║
 * ║  CÓMO USARLO:                                               ║
 * ║  Renombra esta clase con el nombre de tu entidad de negocio ║
 * ║  (ej: Pedido, Factura, Usuario, Producto, etc.)             ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public class Usuario {

    // ── Atributos ────────────────────────────────────────────────
    private final String        id;          // ID único (UUID recomendado)
    private       String        nombre;      // ← reemplaza con tus campos
    private       String        descripcion; // ← reemplaza con tus campos
    private       EstadoUsuario estado;      // Estado del ciclo de vida
    private final LocalDateTime creadoEn;
    private       LocalDateTime actualizadoEn;

    // ── Constructor para CREAR (aplica todas las validaciones) ───
    public Usuario(String id, String nombre, String descripcion) {
        validarNombre(nombre);

        this.id             = id;
        this.nombre         = nombre;
        this.descripcion    = descripcion;
        this.estado         = EstadoUsuario.ACTIVO; // Estado inicial
        this.creadoEn       = LocalDateTime.now();
        this.actualizadoEn  = LocalDateTime.now();
    }

    // ── Constructor para RECONSTRUIR desde base de datos ─────────
    // No re-valida: el dato ya fue validado al crearse
    public static Usuario reconstituir(String id, String nombre, String descripcion,
                                       EstadoUsuario estado, LocalDateTime creadoEn,
                                       LocalDateTime actualizadoEn) {
        Usuario e = new Usuario(id, nombre, descripcion);
        e.estado          = estado;
        e.actualizadoEn   = actualizadoEn;
        return e;
    }

    // ── Métodos de negocio (operaciones con reglas) ───────────────

    /** Actualiza el nombre con la regla de negocio correspondiente */
    public void actualizarNombre(String nuevoNombre) {
        validarNombre(nuevoNombre);
        this.nombre       = nuevoNombre;
        this.actualizadoEn = LocalDateTime.now();
    }

    /** Desactiva la entidad */
    public void desactivar() {
        if (this.estado == EstadoUsuario.INACTIVO) {
            throw new ReglaDeNegocioException("La entidad ya está inactiva");
        }
        this.estado        = EstadoUsuario.INACTIVO;
        this.actualizadoEn = LocalDateTime.now();
    }

    public boolean estaActivo() {
        return this.estado == EstadoUsuario.ACTIVO;
    }

    // ── Validaciones privadas de negocio ─────────────────────────
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ReglaDeNegocioException("El nombre no puede estar vacío");
        }
        if (nombre.length() > 100) {
            throw new ReglaDeNegocioException("El nombre no puede tener más de 100 caracteres");
        }
    }

    // ── Getters (sin setters — estado cambia solo por métodos) ───
    public String         getId()            { return id; }
    public String         getNombre()        { return nombre; }
    public String         getDescripcion()   { return descripcion; }
    public EstadoUsuario  getEstado()        { return estado; }
    public LocalDateTime  getCreadoEn()      { return creadoEn; }
    public LocalDateTime  getActualizadoEn() { return actualizadoEn; }
}
