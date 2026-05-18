package personajes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests de la clase Sacerdote.
 * Cubre el override de curar() que suma el bonusSanacion.
 */
public class SacerdoteTest {

    private Sacerdote ciri;

    @Before
    public void setUp() {
        // Ciri: 600 HP, 200 recurso, 70 ataque, 20 defensa, 50 poderMagico, 20 bonus
        ciri = new Sacerdote("Ciri", 600, 200, 70, 20, 50, 20);
    }

    // CASO NORMAL: al curar se suma el bonus de sanación
    @Test
    public void curar_sumaBonusSanacion() {
        ciri.recibirDanoMagico(300); // queda en 300
        // Cura 100 + 20 bonus = 120 -> 300 + 120 = 420
        ciri.curar(100);
        assertEquals(420, ciri.getVidaActual());
    }

    // CASO LÍMITE: curar con bonus no supera la vida máxima
    @Test
    public void curar_noSuperaMaximoConBonus() {
        ciri.recibirDanoMagico(10); // queda en 590
        // 50 + 20 bonus = 70 -> 590 + 70 = 660 -> tope a 600
        ciri.curar(50);
        assertEquals(600, ciri.getVidaActual());
    }

    // CASO LÍMITE: curar estando a vida completa no la pasa del máximo
    @Test
    public void curar_aVidaCompleta_seQuedaEnMaximo() {
        ciri.curar(100);
        assertEquals(600, ciri.getVidaActual());
    }

    // CASO NORMAL: curación desde vida muy baja
    @Test
    public void curar_desdeVidaMuyBaja_curaCorrectamente() {
        ciri.recibirDanoMagico(599); // queda en 1
        // Cura 200 + 20 bonus = 220 -> 1 + 220 = 221
        ciri.curar(200);
        assertEquals(221, ciri.getVidaActual());
    }
}
