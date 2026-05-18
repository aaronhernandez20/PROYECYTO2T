package personajes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests de la clase Personajes (usando Guerrero como instancia concreta).
 * 4 tests que cubren: caso normal, casos límite y caso de error.
 */
public class PersonajesTest {

    private Guerrero personaje;

    @Before
    public void setUp() {
        // Personaje: 500 vida, 100 recurso, 50 ataque, 20 defensa
        // Sin adrenalina ni bloqueo para que los tests sean predecibles
        personaje = new Guerrero("TestHero", 500, 100, 50, 20, 0, 0, 0.0);
    }

    // CASO NORMAL: recibir daño aplica la defensa correctamente
    @Test
    public void recibirDano_normal_restaConDefensa() {
        // 100 daño - 20 defensa = 80 daño real -> 500 - 80 = 420
        personaje.recibirDano(100);
        assertEquals(420, personaje.getVidaActual());
    }

    // CASO LÍMITE: daño masivo no deja la vida negativa
    @Test
    public void recibirDano_masivo_vidaNoBajaDeCero() {
        personaje.recibirDano(99999);
        assertEquals(0, personaje.getVidaActual());
    }

    // CASO LÍMITE: curar más allá del máximo no sube de vidaMax
    @Test
    public void curar_excediendo_seQuedaEnMaximo() {
        personaje.recibirDanoMagico(50); // queda en 450
        personaje.curar(200);            // 450 + 200 = 650 -> tope a 500
        assertEquals(500, personaje.getVidaActual());
    }

    // CASO DE ERROR: intentar gastar más recurso del que se tiene
    @Test
    public void gastarRecurso_insuficiente_devuelveFalse() {
        // Personaje tiene 100 de recurso, intenta gastar 101
        assertFalse(personaje.gastarRecurso(101));
        // El recurso no debe haber cambiado
        assertEquals(100, personaje.getRecursoActual());
    }
}
