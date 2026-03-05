package armas;

import personajes.Personajes;

public class ArmaCuerpoACuerpo extends Armas {

    public ArmaCuerpoACuerpo(String nombre, int danoBase, double modificador, double probCritico) {
        super(nombre, "CUERPO_A_CUERPO", danoBase, modificador, probCritico);
    }

    @Override
    public int calcularDano(Personajes atacante, Personajes defensor) {
        // Daño inicial = Daño del arma
        int danoTotal = this.danoBase;

        // (Esto es duda) añadir el modificador 1.10 si está afilada)
        danoTotal = (int) (danoTotal * this.modificador);

        // Esto es si hace critico va a ser el daño que haga por 2
        if (Math.random() < this.probCritico) {
            danoTotal = danoTotal * 2;
        }

        return danoTotal;
    }
}
