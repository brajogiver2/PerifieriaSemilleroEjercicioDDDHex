package com.co.periferia.infrastructure.adapter.in.http;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.co.periferia.domain.model.Usuario;
import com.co.periferia.domain.port.in.UsuarioUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Infraestructura — Adaptador de Entrada               ║
 * ║  CLASE: EntidadController.java                              ║
 * ║  TIPO: REST HTTP (via Spring MVC)                           ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Responsabilidades:                                         ║
 * ║  1. Recibir y validar el request HTTP                       ║
 * ║  2. Traducir DTO de entrada → Command                       ║
 * ║  3. Llamar al puerto de entrada (caso de uso)               ║
 * ║  4. Traducir entidad de dominio → DTO de respuesta          ║
 * ║  5. Retornar el código HTTP correcto                        ║
 * ║                                                              ║
 * ║  NO contiene lógica de negocio.                             ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@RestController
@RequestMapping("/api/v1/usuario")  // ← cambia la ruta a la de tu recurso
@Tag(name = "Usuario", description = "Gestión de usuarios del sistema")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioUseCase usuarioUseCase; // ← interfaz, no implementación

    public UsuarioController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    /** POST /api/v1/usuario — Crear */
    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntidadResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en el request")
    })
    public ResponseEntity<EntidadResponse> crear(@Valid @RequestBody CrearEntidadRequest request) {

        log.info("evento=crear_usuario_request nombre={}", request.nombre());

        var command   = new UsuarioUseCase.CrearEntidadCommand(request.nombre(), request.descripcion());
        Usuario creada = usuarioUseCase.crear(command);

        log.info("evento=usuario_creado id={}", creada.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(EntidadResponse.from(creada));
    }

    /** GET /api/v1/usuario/{id} — Buscar por ID */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Busca y retorna un usuario específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntidadResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntidadResponse> buscarPorId(
            @Parameter(description = "ID único del usuario") 
            @PathVariable String id) {

        log.debug("evento=buscar_usuario_request id={}", id);

        Usuario usuario = usuarioUseCase.buscarPorId(id);

        log.info("evento=usuario_encontrado id={}", usuario.getId());

        return ResponseEntity.ok(EntidadResponse.from(usuario));
    }

    /** GET /api/v1/usuario — Listar todas las activas */
    @GetMapping
    @Operation(summary = "Listar usuarios activos", description = "Retorna una lista de todos los usuarios en estado ACTIVO")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios activos",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntidadResponse.class)))
    public ResponseEntity<List<EntidadResponse>> listar() {

        log.debug("evento=listar_usuarios_activos_request");

        List<EntidadResponse> respuesta = usuarioUseCase.listarActivas()
            .stream()
            .map(EntidadResponse::from)
            .toList();

        log.info("evento=usuarios_activos_listados total={}", respuesta.size());

        return ResponseEntity.ok(respuesta);
    }

    /** PUT /api/v1/usuario/{id} — Actualizar */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntidadResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en el request")
    })
    public ResponseEntity<EntidadResponse> actualizar(
            @Parameter(description = "ID único del usuario") 
            @PathVariable String id,
            @Valid @RequestBody ActualizarEntidadRequest request) {

        log.info("evento=actualizar_usuario_request id={}", id);

        var command      = new UsuarioUseCase.ActualizarEntidadCommand(request.nombre(), request.descripcion());
        Usuario actualizada = usuarioUseCase.actualizar(id, command);

        log.info("evento=usuario_actualizado id={}", actualizada.getId());

        return ResponseEntity.ok(EntidadResponse.from(actualizada));
    }

    /** DELETE/api/v1/usuario/{id} — Desactivar (soft delete) */
    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar usuario", description = "Marca un usuario como INACTIVO (soft delete)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuario desactivado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID único del usuario") 
            @PathVariable String id) {

        log.warn("evento=desactivar_usuario_request id={}", id);

        usuarioUseCase.desactivar(id);

        log.info("evento=usuario_desactivado id={}", id);

        return ResponseEntity.noContent().build();
    }

    // ════════════════════════════════════════════════════════════
    //  DTOs  —  Solo para la capa HTTP
    // ════════════════════════════════════════════════════════════

    /** DTO de ENTRADA para crear */
    public record CrearEntidadRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        @Parameter(description = "Nombre del usuario (requerido, máximo 100 caracteres)")
        String nombre,

        @Parameter(description = "Descripción breve del usuario")
        String descripcion
        // ← agrega los campos de entrada que necesites
    ) {}

    /** DTO de ENTRADA para actualizar */
    public record ActualizarEntidadRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Parameter(description = "Nuevo nombre del usuario")
        String nombre,

        @Parameter(description = "Nueva descripción del usuario")
        String descripcion
    ) {}

    /** DTO de SALIDA — Lo que devuelve la API */
    public record EntidadResponse(
        @Schema(description = "ID único del usuario", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,
        
        @Schema(description = "Nombre del usuario", example = "Juan Pérez")
        String nombre,
        
        @Schema(description = "Descripción breve del usuario", example = "Usuario activo del sistema")
        String descripcion,
        
        @Schema(description = "Estado actual del usuario (ACTIVO o INACTIVO)", example = "ACTIVO")
        String estado,
        
        @Schema(description = "Fecha y hora de creación", example = "2026-03-12T10:30:00")
        LocalDateTime creadoEn,
        
        @Schema(description = "Fecha y hora de última actualización", example = "2026-03-12T14:45:30")
        LocalDateTime actualizadoEn
    ) {
        /** Convierte entidad de dominio → DTO de respuesta */
        public static EntidadResponse from(Usuario e) {
            return new EntidadResponse(
                e.getId(), e.getNombre(), e.getDescripcion(),
                e.getEstado().name(), e.getCreadoEn(), e.getActualizadoEn()
            );
        }
    }
}
