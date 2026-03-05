package armas;

import personajes.Personajes;

public class ArmaADistancia extends Armas {

    public ArmaADistancia(String nombre, int danoBase, double modificador, double probCritico,
            double multiplicadorCritico) {
        super(nombre, "A_DISTANCIA", danoBase, modificador, probCritico);
    }

    @Override
    public int calcularDanio(Personajes atacante, Personajes defensor) {
        int danoTotal = this.danoBase;

        danoTotal = (int) (danoTotal * this.modificador);

        if (Math.random() < this.probCritico) {
        }

        return danoTotal;
    }
}