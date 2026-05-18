package armas;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import personajes.Guerrero;

/**
 * Tests de las armas (ArmaCuerpoACuerpo y ArmaADistancia).
 * Cubre el cálculo de daño con y sin crítico.
 * 
 * Nota: probCritico usa Math.random(), por eso usamos 0.0 (nunca crítico)
 *       o 1.0 (siempre crítico) para que los tests sean predecibles.
 */
public class ArmasTest {

    private Guerrero atacante;
    private Guerrero defensor;

    @Before 
    public void setUp() {
        atacante = new Guerrero("Atacante", 500, 100, 50, 20, 0, 0, 0.0);
        defensor = new Guerrero("Defensor", 500, 100, 50, 20, 0, 0, 0.0);
    }

    // CASO NORMAL: arma cuerpo a cuerpo sin crítico
    @Test
    public void cuerpoACuerpo_sinCritico_aplicaModificador() {
        // 80 * 1.2 = 96
        ArmaCuerpoACuerpo aerondight = new ArmaCuerpoACuerpo("Aerondight", 80, 1.2, 0.0);
        assertEquals(96, aerondight.calcularDano(atacante, defensor));
    }

    // CASO NORMAL: arma a distancia sin crítico
    @Test
    public void distancia_sinCritico_aplicaModificador() {
        // 65 * 1.2 = 78
        ArmaADistancia orbe = new ArmaADistancia("Orbe", 65, 1.2, 0.0);
        assertEquals(78, orbe.calcularDano(atacante, defensor));
    }

    // VERIFICACIÓN: crítico de cuerpo a cuerpo multiplica el daño por 2
    @Test
    public void cuerpoACuerpo_siempreCritico_danoDoble() {
        // 80 * 1.0 = 80 -> crítico x2 = 160
        ArmaCuerpoACuerpo arma = new ArmaCuerpoACuerpo("Espada Crítica", 80, 1.0, 1.0);
        assertEquals(160, arma.calcularDano(atacante, defensor));
    }

    // VERIFICACIÓN: crítico a distancia multiplica el daño por 3
    @Test
    public void distancia_siempreCritico_danoTriple() {
        // 50 * 1.0 = 50 -> crítico x3 = 150
        ArmaADistancia arma = new ArmaADistancia("Ballesta Crítica", 50, 1.0, 1.0);
        assertEquals(150, arma.calcularDano(atacante, defensor));
    }
}
