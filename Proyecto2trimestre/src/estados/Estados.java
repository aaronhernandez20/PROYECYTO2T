/**
 * Gestiona los estados alterados que pueden sufrir los personajes durante el combate,
 * como envenenamientos, quemaduras o efectos de regeneración (DoT y HoT).
 */
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
    /**
     * Constructor para aplicar un efecto a lo largo del tiempo.
     * * @param nombre El nombre de la alteración (ej. "Quemadura").
     * @param turnosRestantes La duración total inicial del efecto en rondas.
     * @param potenciaPorTurno Cantidad fija de daño o curación procesada cada ronda.
     * @param tipo El tipo de efecto (DOT=Daño en el tiempo o HOT=Curación en el tiempo).
     */
    // El constructor
    // Lo llaman las subclases con super para que se inicien.
    public Estados(String nombre, int turnosRestantes, int potenciaPorTurno, TipoEstado tipo) {
        this.nombre = nombre;
        this.turnosRestantes = turnosRestantes;
        this.potenciaPorTurno = potenciaPorTurno;
        this.tipo = tipo;
    }
    /**
     * Se invoca una única vez al aplicarse por primera vez el estado.
     * * @param objetivo El personaje que empieza a sufrir o beneficiarse de la alteración.
     */
    // Estos son los métodos que hemos añadido (abstractos + polimorfismo).

    // Este solo se ejecuta una vez cuando el estado se aplica al personaje.
    public abstract void alAplicar(Personajes objetivo);
    /**
     * Ejecuta la mecánica del estado por cada ronda activa.
     * * @param objetivo El personaje al cual se le resta vida (DOT) o se le cura (HOT).
     */
    // Se ejecuta una vez por ronda mientras el estado este activo.
    // Aplica daño o curación segun el tipo de estado.
    public abstract void alProcesarTurno(Personajes objetivo);
    /**
     * Se ejecuta cuando el contador del efecto (turnosRestantes) llega a cero.
     * * @param objetivo El personaje del que se borra o desaparece este efecto.
     */
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
    /**
     * Actualiza la duración del efecto sin tener que crear un objeto nuevo, evitando duplicidades.
     * * @param nuevosTurnos La nueva duración total con la que se renueva el estado.
     */
    // Esto lo que hace es reiniciar la duración al valor indicado.
    // Si el estado ya existe en el personaje, se renueva en lugar de añadir uno
    // nuevo.
    // Por ejemplo si Geralt ya tiene Quemadura y le aplican otra,
    // en vez de tener dos quemaduras se reinician los turnos.

    public void renovarDuracion(int nuevosTurnos) {
        this.turnosRestantes = nuevosTurnos;
        System.out.println("  [ESTADO] " + nombre + " ha sido renovado (" + nuevosTurnos + " turnos).");
    }
    /**
     * Asigna directamente una duración en turnos, se usa generalmente en la persistencia.
     * * @param turnos Los turnos que debe durar el efecto.
     */
    // Ajusta los turnos restantes al cargar desde la BD, sin efectos de sonido/texto.
    public void setTurnosRestantes(int turnos) {
        this.turnosRestantes = turnos;
    }
    /**
     * Valida si la alteración tiene turnos pendientes de ejecutarse.
     * * @return true si le quedan más de 0 turnos, de lo contrario false.
     */
    // Devuelve true si el estado sigue activo.
    public boolean estaActivo() {
        return turnosRestantes > 0;
    }
    /** @return El nombre del estado. */
    // Getters de estados

    public String getNombre() {
        return nombre;
    }
    /** @return Las rondas que le quedan al estado antes de desaparecer. */
    public int getTurnosRestantes() {
        return turnosRestantes;
    }
    /** @return El volumen de puntos procesados por turno. */
    public int getPotenciaPorTurno() {
        return potenciaPorTurno;
    }
    /** @return Si el estado es DoT o HoT. */
    public TipoEstado getTipo() {
        return tipo;
    }
    /** @return Texto esquemático con el nombre y turnos restantes (Ej: "Quemadura (2 turnos)"). */
    // Muestra el estado por ejemplo "Quemadura (2 turnos)".
    // Lo utilizamos como un resumen del Combate para mostrar los estados activos de
    // cada personaje.
    @Override
    public String toString() {
        return nombre + " (" + turnosRestantes + " turno" + (turnosRestantes != 1 ? "s" : "") + ")";
    }
}