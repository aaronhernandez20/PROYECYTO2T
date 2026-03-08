package estados;

import personajes.Personajes;

public abstract class Estados {

    // Enum: Lo que hace es restringir los valores de una variable a una lista
    // predefinida

    public enum TipoEstado {
        DOT, // DOT (daño en el tiempo) (quemadura, veneno)
        HOT, // HOT (curación en el tiempo) (renovar)
    }

    // Los Atributos

    protected String nombre;
    protected int turnosRestantes;
    protected int potenciaPorTurno;
    protected TipoEstado tipo;

    // El Constructor
    public Estados(String nombre, int turnosRestantes, int potenciaPorTurno, TipoEstado tipo) {
        this.nombre = nombre;
        this.turnosRestantes = turnosRestantes;
        this.potenciaPorTurno = potenciaPorTurno;
        this.tipo = tipo;
    }

    // Estos son los métodos del ciclo de vida (abstractos + polimorfismo)

    // Se ejecuta cuando el estado se aplica al personaje.
    public abstract void alAplicar(Personajes objetivo);

    // Se ejecuta una vez por ronda. Aplica daño o curación.
    public abstract void alProcesarTurno(Personajes objetivo);

    // Se ejecuta cuando turnosRestantes llega a 0.
    public abstract void alExpirar(Personajes objetivo);

    // Lógica de duración

    // Reduce en 1 la duración del estado y debe llamarse después de procesarTurno.
    public void reducirDuracion() {
        if (turnosRestantes > 0) {
            turnosRestantes--;
        }
    }

    /*
     * Esto lo que hace es reiniciar la duración al valor indicado.
     * Si el estado ya existe, se renueva en lugar de apilar uno nuevo.
     */
    public void renovarDuracion(int nuevosTurnos) {
        this.turnosRestantes = nuevosTurnos;
        System.out.println("  [ESTADO] " + nombre + " ha sido renovado (" + nuevosTurnos + " turnos).");
    }

    // Devuelve true si el estado sigue activo.
    public boolean estaActivo() {
        return turnosRestantes > 0;
    }

    // Getters de estados

    public String getNombre() {
        return nombre;
    }

    public int getTurnosRestantes() {
        return turnosRestantes;
    }

    public int getPotenciaPorTurno() {
        return potenciaPorTurno;
    }

    public TipoEstado getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return nombre + " (" + turnosRestantes + " turno" + (turnosRestantes != 1 ? "s" : "") + ")";
    }
}