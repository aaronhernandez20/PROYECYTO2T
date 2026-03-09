package estados;

import personajes.Personajes;

// Esta es la clase abstracta de todos los estados del juego.
// Los estados son efectos que duran varios turnos como daño o curacion.
// Las subclases (Quemadura, Veneno, Renovar) heredan de aqui.

public abstract class Estados {
    // No sabia como hacer lo de Dot y Hot y encontre Enum
    // Enum: Lo que hace es restringir los valores de una variable a una lista
    // predefinida de opciones, en nuestro caso los tipos de estado que tenemos.

    public enum TipoEstado {
        DOT, // DOT (daño en el tiempo) (quemadura, veneno)
        HOT, // HOT (curación en el tiempo) (renovar)
    }

    // Los atributos que tienen todos los estados en comun
    protected String nombre; // nombre del estado
    protected int turnosRestantes; // cuantos turnos le quedan al estado
    protected int potenciaPorTurno; // cuanto daño o curacion hace cada turno
    protected TipoEstado tipo; // si es DOT o HOT

    // El constructor
    // Lo llaman las subclases con super para que se inicien.
    public Estados(String nombre, int turnosRestantes, int potenciaPorTurno, TipoEstado tipo) {
        this.nombre = nombre;
        this.turnosRestantes = turnosRestantes;
        this.potenciaPorTurno = potenciaPorTurno;
        this.tipo = tipo;
    }

    // Estos son los métodos que hemos añadido (abstractos + polimorfismo).

    // Este solo se ejecuta una vez cuando el estado se aplica al personaje.
    public abstract void alAplicar(Personajes objetivo);

    // Se ejecuta una vez por ronda mientras el estado este activo.
    // Aplica daño o curación segun el tipo de estado.
    public abstract void alProcesarTurno(Personajes objetivo);

    // Se ejecuta cuando turnosRestantes llega a 0, es decir cuando el estado
    // termina.
    public abstract void alExpirar(Personajes objetivo);

    // Esta es la Logica de duracion

    // Reduce en 1 la duración del estado y esta debe llamarse al final de cada
    // ronda
    // despues de procesarTurno y lo que hace el if evita que baje de 0.
    public void reducirDuracion() {
        if (turnosRestantes > 0) {
            turnosRestantes--;
        }
    }

    // Esto lo que hace es reiniciar la duración al valor indicado.
    // Si el estado ya existe en el personaje, se renueva en lugar de añadir uno
    // nuevo.
    // Por ejemplo si Geralt ya tiene Quemadura y le aplican otra,
    // en vez de tener dos quemaduras se reinician los turnos.

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

    // Muestra el estado por ejemplo "Quemadura (2 turnos)".
    // Lo utilizamos como un resumen del Combate para mostrar los estados activos de
    // cada personaje.
    @Override
    public String toString() {
        return nombre + " (" + turnosRestantes + " turno" + (turnosRestantes != 1 ? "s" : "") + ")";
    }
}