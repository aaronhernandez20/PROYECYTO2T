package hechizos;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import personajes.Guerrero;

/**
 * Tests de los hechizos.
 * Cubre la lógica de puedeUsarse() (cooldown y mana) y la activación del cooldown.
 */
public class HechizosTest {

    private Guerrero lanzador;
    private Guerrero enemigo;

    @Before
    public void setUp() {
        // Lanzador: 100 de recurso, sin defensa
        lanzador = new Guerrero("Lanzador", 500, 100, 50, 0, 0, 0, 0.0);
        enemigo = new Guerrero("Enemigo", 500, 100, 50, 0, 0, 0, 0.0);
    }

    // CASO NORMAL: el hechizo se puede usar si hay mana y no está en cooldown
    @Test
    public void puedeUsarse_sinCooldownConMana_devuelveTrue() {
        DañoDirecto hechizo = new DañoDirecto("Test", 30, 100, 3);
        assertTrue(hechizo.puedeUsarse(lanzador));
    }

    // CASO DE ERROR: el hechizo no se puede usar si está en cooldown
    @Test
    public void puedeUsarse_enCooldown_devuelveFalse() {
        DañoDirecto hechizo = new DañoDirecto("Test", 30, 100, 3);
        lanzador.setCooldown("Test", 2);
        assertFalse(hechizo.puedeUsarse(lanzador));
    }

    // CASO DE ERROR: el hechizo no se puede usar si no hay mana suficiente
    @Test
    public void puedeUsarse_sinMana_devuelveFalse() {
        // Lanzador tiene 100 recurso, hechizo cuesta 150
        DañoDirecto hechizo = new DañoDirecto("Caro", 150, 100, 3);
        assertFalse(hechizo.puedeUsarse(lanzador));
    }

    // VERIFICACIÓN: al lanzar un hechizo se activa su cooldown
    @Test
    public void lanzar_activaCooldown() {
        DañoDirecto hechizo = new DañoDirecto("Test", 30, 100, 3);
        hechizo.lanzar(lanzador, enemigo);
        assertEquals(3, lanzador.getCooldown("Test"));
    }
}
