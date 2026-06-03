package armas;

import personajes.Personajes;

/**
 * ArmaADistancia es una subclase de {@link Armas}.
 * Representa un arma diseñada para atacar desde lejos (arcos, ballestas, hechizos básicos).
 * Su cálculo de daño incorpora el daño base, un modificador y una mecánica 
 * de probabilidad para asestar golpes críticos.
 */
public class ArmaADistancia extends Armas {

    /**
     * Constructor para instanciar un arma de combate a distancia.
     * Asigna automáticamente la categoría "A_DISTANCIA" al llamar al constructor padre.
     * * @param nombre El nombre del arma (ej. "Ballesta Ursina").
     * @param danoBase El daño estático que inflige el arma por defecto.
     * @param modificador Un multiplicador escalar (ej. 1.0 mantiene el daño base, 1.5 lo aumenta un 50%).
     * @param probCritico Probabilidad (en formato decimal, ej. 0.15 para 15%) de asestar un golpe crítico.
     */
    public ArmaADistancia(String nombre, int danoBase, double modificador, double probCritico) {
        super(nombre, "A_DISTANCIA", danoBase, modificador, probCritico);
    }

    /**
     * Calcula el daño final infligido por esta arma en un ataque.
     * Evalúa el modificador base y calcula si el ataque resulta ser un golpe crítico.
     * * @param atacante El personaje que porta el arma y ataca.
     * @param defensor El personaje que recibe el impacto del ataque.
     * @return El daño total calculado que se aplicará al defensor.
     */
    @Override
    public int calcularDano(Personajes atacante, Personajes defensor) {

        // Empezamos con el daño base del arma
        int danoTotal = this.danoBase;

        // Se aplica el multiplicador
        danoTotal = (int) (danoTotal * this.modificador);

        // Se comprueba si es un golpe critico usando la probabilidad del arma
        if (Math.random() < this.probCritico) {
            danoTotal = danoTotal * 2;
            main.Main.logros.registrarCritico();  
        }

        main.Main.logros.registrarDanoInfligido(danoTotal);  
        
        return danoTotal;
    }
}