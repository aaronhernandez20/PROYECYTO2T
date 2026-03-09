package armas;

import personajes.Personajes;

// ArmaCuerpoACuerpo es una subclase de Armas.
// Calcula el daño usando el danoBase del arma mas el modificador y el critico.

public class ArmaCuerpoACuerpo extends Armas {

    // Constructor
    // "CUERPO_A_CUERPO".
    // Recibe el nombre, daño base, modificador y probabilidad de critico.
    public ArmaCuerpoACuerpo(String nombre, int danoBase, double modificador, double probCritico) {
        super(nombre, "CUERPO_A_CUERPO", danoBase, modificador, probCritico);
    }

    // Calcula el daño que hace esta arma en un ataque.
    @Override
    public int calcularDano(Personajes atacante, Personajes defensor) {

        // El daño empieza siendo el daño base del arma
        int danoTotal = this.danoBase;

        // Aplicamos el modificador al daño base.
        // Si el modificador es 1.0 el daño no cambia.
        // Si es 1.2 el arma hace un 20% mas de daño, por ejemplo si esta afilada.
        danoTotal = (int) (danoTotal * this.modificador);

        // Comprobamos si el golpe es critico.
        // Math.random() devuelve un numero entre 0.0 y 1.0.
        // Si ese numero es menor que probCritico se considera critico
        // y el daño se multiplica por 2.
        if (Math.random() < this.probCritico) {
            danoTotal = danoTotal * 2;
        }

        return danoTotal;
    }
}
