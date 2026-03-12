package com.tuempresa.skeleton.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.co.periferia.application.usecase.UsuarioUseCaseImpl;
import com.co.periferia.domain.exception.UsuarioNoEncontradaException;
import com.co.periferia.domain.model.EstadoUsuario;
import com.co.periferia.domain.model.Usuario;
import com.co.periferia.domain.port.in.UsuarioUseCase;
import com.co.periferia.domain.port.out.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  TEST: Aplicación (Caso de Uso)                             ║
 * ║  Prueba la orquestación con MOCKS de los puertos de salida. ║
 * ║  Sin Spring, sin BD real, sin Redis, sin Rabbit.            ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EntidadUseCase — Orquestación")
class UsuarioUseCaseTest {

    @Mock private UsuarioRepositoryPort repositorio;

    private UsuarioUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new UsuarioUseCaseImpl(repositorio);
    }

    @Test
    @DisplayName("Crear debe guardar, cachear y publicar evento")
    void crearDebeGuardarCachearYPublicar() {
        // ARRANGE
        var command = new UsuarioUseCase.CrearEntidadCommand("Nombre Test", "Descripción");
        when(repositorio.guardar(any())).thenAnswer(inv -> inv.getArgument(0));

        // ACT
        Usuario resultado = useCase.crear(command);

        // ASSERT
        assertEquals("Nombre Test", resultado.getNombre());
        assertEquals(EstadoUsuario.ACTIVO, resultado.getEstado());

        verify(repositorio).guardar(any());         // ← se persistió
       
    }

    @Test
    @DisplayName("BuscarPorId debe retornar desde caché si existe")
    void buscarDebeRetornarDesdeCacheSiExiste() {
        // ARRANGE
       

        // ACT
        Usuario resultado = useCase.buscarPorId("id-1");

        // ASSERT
        assertEquals("Del Caché", resultado.getNombre());
        verify(repositorio, never()).buscarPorId(any()); // ← NO fue a BD
    }

    @Test
    @DisplayName("BuscarPorId debe ir a BD si no está en caché (cache miss)")
    void buscarDebeIrABDSiNoEstaEnCache() {
        // ARRANGE
        var usuarioBD = new Usuario("id-1", "De BD", "desc");

        when(repositorio.buscarPorId("id-1")).thenReturn(Optional.of(usuarioBD));

        // ACT
        Usuario resultado = useCase.buscarPorId("id-1");

        // ASSERT
        assertEquals("De BD", resultado.getNombre());
        verify(repositorio).buscarPorId("id-1");       // ← fue a BD
       
    }

    @Test
    @DisplayName("BuscarPorId debe lanzar excepción si no existe en ningún lado")
    void buscarDebeLanzarExcepcionSiNoExiste() {
       
        when(repositorio.buscarPorId(anyString())).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradaException.class, () -> useCase.buscarPorId("no-existe"));
    }

    @Test
    @DisplayName("Desactivar debe invalidar caché y publicar evento")
    void desactivarDebeInvalidarCacheYPublicarEvento() {
        // ARRANGE
        var usuario = new Usuario("id-1", "Test", "desc");
        when(repositorio.buscarPorId("id-1")).thenReturn(Optional.of(usuario));
        when(repositorio.guardar(any())).thenAnswer(inv -> inv.getArgument(0));

        // ACT
        useCase.desactivar("id-1");

        // ASSERT
       
    }
}
