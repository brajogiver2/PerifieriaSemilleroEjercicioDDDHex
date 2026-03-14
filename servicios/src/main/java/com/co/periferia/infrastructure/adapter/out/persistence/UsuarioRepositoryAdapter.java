package com.co.periferia.infrastructure.adapter.out.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.co.periferia.domain.model.EstadoUsuario;
import com.co.periferia.domain.model.Usuario;
import com.co.periferia.domain.port.out.UsuarioRepositoryPort;
import com.co.periferia.infrastructure.adapter.out.persistence.entity.UsuarioJpaEntity;
import com.co.periferia.infrastructure.adapter.out.persistence.repository.UsuarioJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(UsuarioRepositoryAdapter.class);

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {

        log.info("evento=guardar_usuario id={}", usuario.getId());

        UsuarioJpaEntity entity = UsuarioJpaEntity.from(usuario);
        UsuarioJpaEntity guardada = jpaRepository.save(entity);

        log.debug("evento=usuario_guardado id={}", usuario.getId());

        return guardada.toDomain();
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {

        log.debug("evento=buscar_usuario id={}", id);

        Optional<Usuario> usuario = jpaRepository.findById(id)
                .map(UsuarioJpaEntity::toDomain);

        if (usuario.isEmpty()) {
            log.warn("evento=usuario_no_encontrado id={}", id);
        }

        return usuario;
    }

    @Override
    public List<Usuario> buscarActivas() {

        log.debug("evento=buscar_usuarios_activos");

        List<Usuario> usuarios = jpaRepository.findByEstado(EstadoUsuario.ACTIVO)
                .stream()
                .map(UsuarioJpaEntity::toDomain)
                .toList();

        log.info("evento=usuarios_activos_encontrados total={}", usuarios.size());

        return usuarios;
    }

    @Override
    public boolean existePorId(String id) {

        boolean existe = jpaRepository.existsById(id);

        log.debug("evento=existe_usuario id={} resultado={}", id, existe);

        return existe;
    }

    @Override
    public void eliminar(String id) {

        log.warn("evento=eliminar_usuario id={}", id);

        jpaRepository.deleteById(id);
    }
}