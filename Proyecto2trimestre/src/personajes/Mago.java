package personajes;

public class Mago extends Personajes {

    // El multiplicador magico escala el poder de los hechizos del mago.
    // Por ejemplo, con 1.5 sus hechizos hacen un 50% mas de daño.
    private double multiplicadorMagico;

    public Mago(String nombre, int vidaMax, int recursoMax, int ataqueBase,
            int defensaBase, int poderMagico, double multiplicadorMagico) {
        super(nombre, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
        this.multiplicadorMagico = multiplicadorMagico;
    }

    // Calcula el poder real del hechizo multiplicando el poderMagico base
    // por el multiplicador del mago (polimorfismo del profe: calcularPoderHechizo)
    public int calcularPoderHechizo() {
        return (int) (this.poderMagico * this.multiplicadorMagico);
    }

    // Polimorfismo: el mago tiene su propia forma de actuar en su turno
    @Override
    public void realizarAccion(Personajes objetivo) {
        System.out.println(this.nombre + " canaliza su magia y se prepara para actuar...");
    }

    // Getter necesario para que DañoDirecto y DañoEnElTiempo puedan
    // leer el multiplicador cuando el lanzador es un Mago
    public double getMultiplicadorMagico() {
        return multiplicadorMagico;
    }
}