package com.co.periferia.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.co.periferia.domain.exception.UsuarioNoEncontradaException;
import com.co.periferia.domain.model.Usuario;
import com.co.periferia.domain.port.in.UsuarioUseCase;
import com.co.periferia.domain.port.out.*;

import java.util.List;
import java.util.UUID;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CAPA: Aplicación                                           ║
 * ║  CLASE: EntidadUseCaseImpl.java                             ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  ORQUESTADOR: Implementa el caso de uso coordinando         ║
 * ║  todos los puertos de salida.                               ║
 * ║                                                              ║
 * ║  REGLA: Esta clase NO contiene lógica de negocio.           ║
 * ║  Solo coordina pasos. La lógica vive en Entidad.java.       ║
 * ║                                                              ║
 * ║  ÚNICA anotación permitida: @Service (ciclo de vida Spring) ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Service
public class UsuarioUseCaseImpl implements UsuarioUseCase {

    private static final Logger log = LoggerFactory.getLogger(UsuarioUseCaseImpl.class);

    // ── Solo depende de INTERFACES (puertos) ─────────────────────
    private final UsuarioRepositoryPort repositorio;


    // ── Inyección por constructor (mejor práctica) ───────────────
    public UsuarioUseCaseImpl(UsuarioRepositoryPort repositorio
                              ) {
        this.repositorio    = repositorio;
    }

    @Override
    public Usuario crear(CrearEntidadCommand command) {
        log.info("Creando entidad con nombre='{}'", command.nombre());

        // 1. Crear la entidad (el dominio aplica las validaciones)
        var usuario = new Usuario(
            UUID.randomUUID().toString(),
            command.nombre(),
            command.descripcion()
        );

        // 2. (Opcional) Consultar servicio externo antes de guardar
        // var respuesta = servicioExterno.consultar(command.nombre());
        // if (!respuesta.exitoso()) throw new ReglaDeNegocioException(respuesta.mensaje());

        // 3. Persistir en base de datos
        Usuario guardada = repositorio.guardar(usuario);

      

        // 6. Notificar (si aplica)
        // notificacion.enviarEmail("admin@empresa.com", "Nueva entidad", "Se creó: " + guardada.getNombre());

        log.info("Entidad creada con ID={}", guardada.getId());
        return guardada;
    }

    @Override
    public Usuario buscarPorId(String id) {
        log.debug("Buscando entidad ID={}", id);

        // Estrategia Cache-Aside: busca en caché primero, si no está, va a BD
       
        
                log.debug("Cache miss para ID={}. Consultando BD.", id);
                Usuario usuario = repositorio.buscarPorId(id)
                    .orElseThrow(() -> new UsuarioNoEncontradaException(id));
                return usuario;
            
    }

    @Override
    public List<Usuario> listarActivas() {
        return repositorio.buscarActivas();
    }

    @Override
    public Usuario actualizar(String id, ActualizarEntidadCommand command) {
        log.info("Actualizando entidad ID={}", id);

        // 1. Buscar (lanza excepción si no existe)
        Usuario usuario = repositorio.buscarPorId(id)
            .orElseThrow(() -> new UsuarioNoEncontradaException(id));

        // 2. Aplicar cambio (la regla de validación vive en Entidad.java)
        usuario.actualizarNombre(command.nombre());

        // 3. Persistir
        Usuario actualizada = repositorio.guardar(usuario);

        return actualizada;
    }

    @Override
    public void desactivar(String id) {
        log.info("Desactivando entidad ID={}", id);

        Usuario usuario = repositorio.buscarPorId(id)
            .orElseThrow(() -> new UsuarioNoEncontradaException(id));

        usuario.desactivar(); // La regla de negocio vive aquí

        repositorio.guardar(usuario);
    }
}
