package armas;

import personajes.Personajes;

/**
 * ArmaCuerpoACuerpo es una subclase de {@link Armas}.
 * Representa un arma diseñada para el combate a corta distancia.
 * Calcula el daño final utilizando el daño base del arma, aplicando su modificador 
 * de escalado y evaluando la probabilidad de asestar un golpe crítico.
 */
public class ArmaCuerpoACuerpo extends Armas {

    /**
     * Constructor para instanciar un arma de combate cuerpo a cuerpo.
     * Asigna automáticamente la categoría "CUERPO_A_CUERPO" al llamar al constructor padre.
     * * @param nombre El nombre del arma (ej. "Espada de Plata").
     * @param danoBase El daño estático que inflige el arma por defecto.
     * @param modificador Un multiplicador escalar (ej. 1.2 aumenta el daño en un 20%).
     * @param probCritico Probabilidad (en formato decimal, ej. 0.2 para 20%) de ejecutar un golpe crítico.
     */
    public ArmaCuerpoACuerpo(String nombre, int danoBase, double modificador, double probCritico) {
        super(nombre, "CUERPO_A_CUERPO", danoBase, modificador, probCritico);
    }

    /**
     * Calcula el daño final que hace esta arma en un ataque determinado.
     * * @param atacante El personaje que porta el arma y ataca.
     * @param defensor El personaje que recibe el impacto del ataque.
     * @return El daño total infligido tras aplicar todos los modificadores y críticos.
     */
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
            main.Main.logros.registrarCritico();  

        }
        main.Main.logros.registrarDanoInfligido(danoTotal);   

        return danoTotal;
    }
}