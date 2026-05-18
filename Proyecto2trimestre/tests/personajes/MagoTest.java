package personajes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests de la clase Mago.
 * Cubre el método calcularPoderHechizo() que multiplica el poder mágico.
 */
public class MagoTest {

    private Mago yennefer;

    @Before
    public void setUp() {
        // Yennefer: 400 HP, 300 recurso, 30 ataque, 10 defensa, 100 poderMagico, x1.5
        yennefer = new Mago("Yennefer", 400, 300, 30, 10, 100, 1.5);
    }

    // CASO NORMAL: el poder se multiplica por el multiplicador mágico
    @Test
    public void calcularPoderHechizo_yennefer_150() {
        // 100 * 1.5 = 150
        assertEquals(150, yennefer.calcularPoderHechizo());
    }

    // CASO LÍMITE: multiplicador = 1.0 -> el poder no se modifica
    @Test
    public void calcularPoderHechizo_multiplicadorUno_igualAlBase() {
        Mago magoNormal = new Mago("Mago Normal", 400, 200, 20, 10, 80, 1.0);
        assertEquals(80, magoNormal.calcularPoderHechizo());
    }

    // CASO LÍMITE: poder mágico = 0 -> resultado siempre 0
    @Test
    public void calcularPoderHechizo_poderMagicoCero_devuelveCero() {
        Mago magoSinPoder = new Mago("Sin Poder", 400, 200, 20, 10, 0, 2.0);
        assertEquals(0, magoSinPoder.calcularPoderHechizo());
    }

    // VERIFICACIÓN: getter del multiplicador devuelve el valor correcto
    @Test
    public void getMultiplicadorMagico_devuelveCorrectamente() {
        assertEquals(1.5, yennefer.getMultiplicadorMagico(), 0.001);
    }
}
