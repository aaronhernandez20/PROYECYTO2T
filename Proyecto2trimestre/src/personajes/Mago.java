package personajes;

public class Mago extends Personajes {

    private double multiplicadorMagico;

    public Mago(String nombre, int vidaMax, int recursoMax, int ataqueBase,
            int defensaBase, int poderMagico, double multiplicadorMagico) {
        super(nombre, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
        this.multiplicadorMagico = multiplicadorMagico;
    }

    // Calcula el poder real del hechizo multiplicando el poderMagico base
    public int calcularPoderHechizo() {
        return (int) (this.poderMagico * this.multiplicadorMagico);
    }

    // polimorfismo, el mago tiene su propia forma de actuar
    @Override
    public void realizarAccion(Personajes objetivo) {
        System.out.println(this.nombre + " canaliza su magia y se prepara para actuar...");
    }
}