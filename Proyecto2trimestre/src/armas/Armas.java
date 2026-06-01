/**
 * Contiene la lógica y los tipos de armamento que pueden equipar los personajes,
 * divididos en combate cuerpo a cuerpo y a distancia.
 */
package armas;

import personajes.Personajes;

// Esta es la clase abstracta de todas las armas del juego.
// Las subclases (ArmaCuerpoACuerpo, ArmaADistancia) heredan de aqui
// y cada una calcula el daño a su manera.

public abstract class Armas {

    // Los atributos que tienen todas las armas en comun
    protected String nombre;
    protected String tipo;
    protected int danoBase;
    protected double modificador;
    protected double probCritico;
    /**
     * Constructor para inicializar los atributos base de un arma.
     * * @param nombre El nombre del arma (ej. "Espada de Plata").
     * @param tipo Categoría del arma (Cuerpo a cuerpo o A distancia).
     * @param danoBase El daño estático que inflige el arma sin contar modificadores.
     * @param modificador Un multiplicador (habitualmente basado en estadísticas) que escala el daño base.
     * @param probCritico Probabilidad en porcentaje (o ratio decimal) de ejecutar un golpe crítico.
     */
    // El constructor
    // Lo llaman las subclases con super() para inicializarse.
    public Armas(String nombre, String tipo, int danoBase, double modificador, double probCritico) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.danoBase = danoBase;
        this.modificador = modificador;
        this.probCritico = probCritico;
    }
    /**
     * Calcula el daño final infligido por el arma.
     * * @param atacante El personaje que porta el arma y ataca.
     * @param defensor El personaje que recibe el ataque.
     * @return El daño entero calculado a aplicar.
     */
    // He puesto que el metodo sea abstracto porque cada subclase lo calcula de
    // forma diferente.
    // este metodo solo devuelve un numero, nunca modifica
    // la vida del personaje directamente, eso lo hace recibirDano().
    public abstract int calcularDano(Personajes atacante, Personajes defensor);
    /** @return El nombre del arma. */
    public String getNombre() {
        return nombre;
    }
}