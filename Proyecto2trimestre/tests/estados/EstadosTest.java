package estados;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import personajes.Guerrero;

/**
 * Tests de los estados (Quemadura, Veneno, Renovar).
 * Cubre los efectos por turno y la lógica de duración.
 */
public class EstadosTest {

    private Guerrero personaje;

    @Before
    public void setUp() {
        // Personaje sin defensa para que el daño del estado sea directo
        personaje = new Guerrero("Test", 500, 100, 50, 0, 0, 0, 0.0);
    }

    // CASO NORMAL: Quemadura hace daño cada turno
    @Test
    public void quemadura_procesarTurno_haceDano() {
        Quemadura q = new Quemadura(); // 50 dmg por turno
        q.alProcesarTurno(personaje);
        // 500 - 50 = 450
        assertEquals(450, personaje.getVidaActual());
    }

    // VERIFICACIÓN: Renovar aplicado por sacerdote cura 60 (40 normal + 20 bonus)
    @Test
    public void renovar_aplicadoPorSacerdote_60porTurno() {
        Renovar r = new Renovar(true);
        assertEquals(60, r.getPotenciaPorTurno());
        assertTrue(r.isAplicadoPorSacerdote());
    }

    // CASO LÍMITE: cuando los turnos llegan a 0 el estado deja de estar activo
    @Test
    public void estaActivo_sinTurnos_devuelveFalse() {
        Quemadura q = new Quemadura(); // 3 turnos
        q.reducirDuracion();
        q.reducirDuracion();
        q.reducirDuracion();
        assertFalse(q.estaActivo());
    }

    // CASO LÍMITE: reducir duración no la deja negativa
    @Test
    public void reducirDuracion_noBajaDeZero() {
        Quemadura q = new Quemadura();
        // Reducimos más veces de las que dura
        for (int i = 0; i < 10; i++) {
            q.reducirDuracion();
        }
        assertEquals(0, q.getTurnosRestantes());
    }
}
