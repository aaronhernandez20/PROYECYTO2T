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

    // El constructor
    // Lo llaman las subclases con super() para inicializarse.
    public Armas(String nombre, String tipo, int danoBase, double modificador, double probCritico) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.danoBase = danoBase;
        this.modificador = modificador;
        this.probCritico = probCritico;
    }

    // He puesto que el metodo sea abstracto porque cada subclase lo calcula de
    // forma diferente.
    // este metodo solo devuelve un numero, nunca modifica
    // la vida del personaje directamente, eso lo hace recibirDano().
    public abstract int calcularDano(Personajes atacante, Personajes defensor);

    public String getNombre() {
        return nombre;
    }
}