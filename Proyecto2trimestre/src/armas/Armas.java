package armas;

import personajes.Personajes;

/**
 * Clase abstracta que representa todas las armas del juego.
 * Las subclases (ArmaCuerpoACuerpo, ArmaADistancia) heredan de aquí
 * y cada una calcula el daño a su manera.
 */
public abstract class Armas {

    // Los atributos que tienen todas las armas en comun
    protected String nombre;
    protected String tipo;
    protected int danoBase;
    protected double modificador;
    protected double probCritico;

    /**
     * Constructor para inicializar los atributos base de un arma.
     * Lo llaman las subclases con super() para inicializarse.
     * * @param nombre El nombre del arma (ej. "Espada de Plata").
     * @param tipo Categoría del arma (Cuerpo a cuerpo o A distancia).
     * @param danoBase El daño estático que inflige el arma sin contar modificadores.
     * @param modificador Un multiplicador (habitualmente basado en estadísticas) que escala el daño base.
     * @param probCritico Probabilidad (en decimal, ej. 0.20 para 20%) de ejecutar un golpe crítico.
     */
    public Armas(String nombre, String tipo, int danoBase, double modificador, double probCritico) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.danoBase = danoBase;
        this.modificador = modificador;
        this.probCritico = probCritico;
    }

    /**
     * Calcula el daño final infligido por el arma.
     * Es un método abstracto porque cada subclase lo calcula de forma diferente.
     * Este método solo devuelve un número, nunca modifica la vida del personaje directamente, 
     * eso lo hace el método recibirDano().
     * * @param atacante El personaje que porta el arma y ataca.
     * @param defensor El personaje que recibe el ataque.
     * @return El daño entero calculado a aplicar al defensor.
     */
    public abstract int calcularDano(Personajes atacante, Personajes defensor);

    /**
     * Obtiene el nombre del arma.
     * * @return El nombre de esta arma.
     */
    public String getNombre() {
        return nombre;
    }
}