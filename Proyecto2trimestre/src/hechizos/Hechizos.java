package hechizos;

import personajes.Personajes;

// Clase abstracta base de todos los hechizos del juego.
// Todos los hechizos (DañoDirecto, CuraciónDirecta, etc.) heredan de aqui.
// Usa un enum TipoObjetivo para saber si el hechizo va a un enemigo o a un aliado.

public abstract class Hechizos {

    // TipoObjetivo: restringe los posibles objetivos de un hechizo.
    // ENEMIGO_UNICO: hechizos de daño (Señal de Igni, Lanza de Hielo...)
    // ALIADO_UNICO: hechizos de curación (Poción de Golondrina, Sacrificio
    // Oscuro...)
    public enum TipoObjetivo {
        ENEMIGO_UNICO,
        ALIADO_UNICO
    }

    // Atributos comunes a todos los hechizos
    protected String nombre; // nombre que se muestra en combate
    protected int costeMana; // recurso que cuesta lanzarlo
    protected int potenciaBase; // valor base de daño o curación
    protected int cooldownMaximo; // turnos de espera entre usos
    protected TipoObjetivo objetivo; // a quién va dirigido

    // Constructor base, lo llaman las subclases con super()
    public Hechizos(String nombre, int costeMana, int potenciaBase, int cooldownMaximo, TipoObjetivo objetivo) {
        this.nombre = nombre;
        this.costeMana = costeMana;
        this.potenciaBase = potenciaBase;
        this.cooldownMaximo = cooldownMaximo;
        this.objetivo = objetivo;
    }

    // Método principal: cada subclase define qué hace el hechizo al lanzarse.
    // lanzador: quien lo lanza (necesitamos su poderMagico)
    // objetivo: sobre quien recae el efecto
    public abstract void lanzar(Personajes lanzador, Personajes blanco);

    // Comprueba si el lanzador tiene suficiente recurso y si el hechizo no
    // está en cooldown. Si puede lanzarlo, gasta el recurso y devuelve true.
    // El Map<String,Integer> de cooldowns vive en Personajes y lo gestionamos
    // desde aquí para que el personaje no tenga que saber nada de hechizos.
    public boolean puedeUsarse(Personajes lanzador) {
        // Miramos el cooldown actual del hechizo en el mapa del personaje
        int cdActual = lanzador.getCooldown(this.nombre);
        if (cdActual > 0) {
            System.out.println("  [COOLDOWN] " + nombre + " no está disponible (" + cdActual + " turno/s restante/s).");
            return false;
        }
        // Intentamos gastar el recurso (el método de Personajes ya imprime mensajes)
        return lanzador.gastarRecurso(this.costeMana);
    }

    // Pone el hechizo en cooldown tras ser usado.
    // Se llama desde lanzar() una vez que el hechizo se ha ejecutado con éxito.
    public void ponerEnCooldown(Personajes lanzador) {
        lanzador.setCooldown(this.nombre, this.cooldownMaximo);
    }

    // Reduce el cooldown en 1 al final de cada turno del lanzador.
    // Si ya es 0 no hace nada (evitamos negativos).
    public void reducirCooldown(Personajes lanzador) {
        int actual = lanzador.getCooldown(this.nombre);
        if (actual > 0) {
            lanzador.setCooldown(this.nombre, actual - 1);
        }
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getCosteMana() {
        return costeMana;
    }

    public int getPotenciaBase() {
        return potenciaBase;
    }

    public int getCooldownMaximo() {
        return cooldownMaximo;
    }

    public TipoObjetivo getTipoObjetivo() {
        return objetivo;
    }

    @Override
    public String toString() {
        return nombre + " [coste:" + costeMana + " mana | CD:" + cooldownMaximo + "t]";
    }
}
