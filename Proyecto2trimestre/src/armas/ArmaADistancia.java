package armas;

import personajes.Personajes;

public class ArmaADistancia extends Armas {

    public ArmaADistancia(String nombre, int danoBase, double modificador, double probCritico) {
        super(nombre, "A_DISTANCIA", danoBase, modificador, probCritico);
    }

    @Override
    public int calcularDano(Personajes atacante, Personajes defensor) {
        int danoTotal = this.danoBase;

        // Se aplica el modificador
        danoTotal = (int) (danoTotal * this.modificador);

        // Con esto si tienes suerte y sale crítico le haces por 3 de daño
        if (Math.random() < this.probCritico) {

            danoTotal = danoTotal * 3;
        }

        return danoTotal;
    }
}