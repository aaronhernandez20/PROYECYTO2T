package personajes;

/**
 * La clase Mago es una subclase de {@link Personajes}.
 * Se especializa en el uso de hechizos y habilidades mágicas, contando con un 
 * multiplicador que escala su poder mágico base para infligir más daño o aplicar mejores efectos.
 */
public class Mago extends Personajes {

    // El multiplicador magico escala el poder de los hechizos del mago.
    // Por ejemplo, con 1.5 sus hechizos hacen un 50% mas de daño.
    private double multiplicadorMagico;

    /**
     * Constructor para instanciar un Mago.
     * 
     * @param nombre El nombre del personaje.
     * @param vidaMax La vida máxima.
     * @param recursoMax El recurso máximo (Maná).
     * @param ataqueBase Poder de ataque físico base.
     * @param defensaBase Capacidad de resistir daño físico.
     * @param poderMagico Potencia base para habilidades mágicas.
     * @param multiplicadorMagico Ratio decimal que incrementa la eficacia de los hechizos (ej. 1.5).
     */
    public Mago(String nombre, int vidaMax, int recursoMax, int ataqueBase,
            int defensaBase, int poderMagico, double multiplicadorMagico) {
        super(nombre, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
        this.multiplicadorMagico = multiplicadorMagico;
    }

    /**
     * Calcula el poder real del hechizo multiplicando el poderMagico base 
     * por el multiplicador del mago.
     * 
     * @return El poder de hechizo final calculado y convertido a entero.
     */
    public int calcularPoderHechizo() {
        return (int) (this.poderMagico * this.multiplicadorMagico);
    }

    /**
     * Acción que realiza el mago al llegar su turno.
     * Polimorfismo: el mago tiene su propia forma narrativa de actuar.
     * 
     * @param objetivo El personaje enemigo o aliado sobre el que recae la acción principal.
     */
    @Override
    public void realizarAccion(Personajes objetivo) {
        System.out.println(this.nombre + " canaliza su magia y se prepara para actuar...");
    }

    /**
     * Devuelve el multiplicador mágico, utilizado por clases hijas de Hechizos 
     * para calcular el daño amplificado si el lanzador es un mago.
     * 
     * @return El multiplicador mágico del mago.
     */
    public double getMultiplicadorMagico() {
        return multiplicadorMagico;
    }
}