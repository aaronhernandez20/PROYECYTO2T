
package armas;

import personajes.Personajes;

public abstract class Armas {

    protected String nombre;
    // CUERPO A CUERPO o A_DISTANCIA
    protected String tipo;
    protected int danoBase;
    protected double modificador;
    // Probabilidad
    protected double probCritico;

    public Armas(String nombre, String tipo, int danoBase, double modificador, double probCritico) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.danoBase = danoBase;
        this.modificador = modificador;
        this.probCritico = probCritico;
    }

    public abstract int calcularDanio(Personajes atacante, Personajes defensor);

    public String getNombre() {
        return nombre;
    }
}