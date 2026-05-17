package personajes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests de la clase Guerrero.
 * Cubre el override de recibirDano() con adrenalina y bloqueo.
 * 
 * Nota: probBloqueo usa Math.random(), por eso usamos 0.0 (nunca bloquea)
 *       o 1.0 (siempre bloquea) para que los tests sean predecibles.
 */
public class GuerreroTest {

    private Guerrero guerrero;
    private Guerrero guerreroBloqueador;

    @Before
    public void setUp() {
        // 800 vida, 30 defensa, 20 adrenalina, probBloqueo = 0.0 -> nunca bloquea
        guerrero = new Guerrero("Geralt", 800, 100, 85, 30, 0, 20, 0.0);
        // Mismo guerrero pero con probBloqueo = 1.0 -> siempre bloquea
        guerreroBloqueador = new Guerrero("Geralt Bloqueo", 800, 100, 85, 30, 0, 20, 1.0);
    }

    // CASO NORMAL: el daño se reduce por adrenalina y defensa
    @Test
    public void recibirDano_normal_aplicaAdrenalinaYDefensa() {
        // 100 - 20 (adrenalina) = 80 -> 80 - 30 (defensa) = 50 real
        // 800 - 50 = 750
        guerrero.recibirDano(100);
        assertEquals(750, guerrero.getVidaActual());
    }

    // CASO LÍMITE: si el daño es menor que la adrenalina, no hace nada
    @Test
    public void recibirDano_danoMenorQueAdrenalina_noHaceDano() {
        // 15 daño - 20 adrenalina = negativo -> 0 -> sin daño
        guerrero.recibirDano(15);
        assertEquals(800, guerrero.getVidaActual());
    }

    // CASO NORMAL: bloqueo total (probBloqueo = 1.0) anula todo el daño
    @Test
    public void recibirDano_bloqueoTotal_noPierdaVida() {
        guerreroBloqueador.recibirDano(500);
        assertEquals(800, guerreroBloqueador.getVidaActual());
    }

    // VERIFICACIÓN: el daño mágico ignora adrenalina, defensa y bloqueo
    @Test
    public void recibirDanoMagico_ignoraAdrenalinaYDefensa() {
        // El daño mágico va directo: 100 daño -> 800 - 100 = 700
        guerrero.recibirDanoMagico(100);
        assertEquals(700, guerrero.getVidaActual());
    }
}
