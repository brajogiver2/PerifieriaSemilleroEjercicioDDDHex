package com.tuempresa.skeleton.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.co.periferia.domain.exception.ReglaDeNegocioException;
import com.co.periferia.domain.model.EstadoUsuario;
import com.co.periferia.domain.model.Usuario;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  TEST: Dominio                                              ║
 * ║  Prueba las reglas de negocio SIN Spring, SIN BD, SIN nada. ║
 * ║  Son los tests más rápidos y valiosos del proyecto.         ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@DisplayName("Entidad — Reglas de negocio")
class UsuarioTest {

    private Usuario entidadValida() {
        return new Usuario("id-1", "Nombre Válido", "Descripción");
    }

    @Nested
    @DisplayName("Creación")
    class Creacion {

        @Test
        @DisplayName("Debe iniciar en estado ACTIVO")
        void debeIniciarEnActivo() {
            assertEquals(EstadoUsuario.ACTIVO, entidadValida().getEstado());
        }

        @Test
        @DisplayName("No debe crearse con nombre vacío")
        void noDebeCrearseConNombreVacio() {
            assertThrows(ReglaDeNegocioException.class,
                () -> new Usuario("id", "", "desc"));
        }

        @Test
        @DisplayName("No debe crearse con nombre nulo")
        void noDebeCrearseConNombreNulo() {
            assertThrows(ReglaDeNegocioException.class,
                () -> new Usuario("id", null, "desc"));
        }

        @Test
        @DisplayName("No debe crearse con nombre mayor a 100 caracteres")
        void noDebeCrearseConNombreLargo() {
            String nombreLargo = "A".repeat(101);
            assertThrows(ReglaDeNegocioException.class,
                () -> new Usuario("id", nombreLargo, "desc"));
        }
    }

    @Nested
    @DisplayName("Desactivación")
    class Desactivacion {

        @Test
        @DisplayName("Debe desactivarse correctamente")
        void debeDesactivarse() {
            Usuario usuario = entidadValida();
            usuario.desactivar();
            assertEquals(EstadoUsuario.INACTIVO, usuario.getEstado());
        }

        @Test
        @DisplayName("No debe desactivarse si ya está inactiva")
        void noDebeDesactivarseDobleVez() {
            Usuario usuario = entidadValida();
            usuario.desactivar();
            assertThrows(ReglaDeNegocioException.class, usuario::desactivar);
        }
    }
}
