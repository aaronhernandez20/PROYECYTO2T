/**
 * Define las habilidades mágicas del juego, incluyendo curaciones directas o en el tiempo,
 * así como hechizos de daño directo y daño continuado.
 */
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
    /**
     * Constructor base de los hechizos.
     * * @param nombre El nombre visible en combate del hechizo.
     * @param costeMana La cantidad de recurso que se debe gastar para lanzarlo.
     * @param potenciaBase El daño o curación base antes de sumarle el poder mágico.
     * @param cooldownMaximo Los turnos necesarios para que vuelva a estar disponible tras usarlo.
     * @param objetivo Define si el hechizo es ofensivo (enemigo) o de apoyo (aliado).
     */
    // Constructor base, lo llaman las subclases con super()
    public Hechizos(String nombre, int costeMana, int potenciaBase, int cooldownMaximo, TipoObjetivo objetivo) {
        this.nombre = nombre;
        this.costeMana = costeMana;
        this.potenciaBase = potenciaBase;
        this.cooldownMaximo = cooldownMaximo;
        this.objetivo = objetivo;
    }
    /**
     * Lógica de lanzamiento y efecto final del hechizo.
     * * @param lanzador El personaje que invoca el hechizo.
     * @param blanco El personaje afectado por el hechizo (puede ser aliado o enemigo).
     */
    // Método principal: cada subclase define qué hace el hechizo al lanzarse.
    // lanzador: quien lo lanza (necesitamos su poderMagico)
    // objetivo: sobre quien recae el efecto
    public abstract void lanzar(Personajes lanzador, Personajes blanco);
    /**
     * Verifica los requisitos del hechizo (cooldown y recurso) e intenta consumir su coste si es posible.
     * * @param lanzador El personaje intentando usar el hechizo.
     * @return true si pudo usarse (sin CD y con maná), false en caso negativo.
     */
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
    /**
     * Activa el estado de enfriamiento del hechizo asignándole su cooldown máximo.
     * * @param lanzador El personaje al cual se le impone el cooldown en este hechizo.
     */
    // Pone el hechizo en cooldown tras ser usado.
    // Se llama desde lanzar() una vez que el hechizo se ha ejecutado con éxito.
    public void ponerEnCooldown(Personajes lanzador) {
        lanzador.setCooldown(this.nombre, this.cooldownMaximo);
        main.Main.logros.registrarHechizoLanzado();
    }
    /**
     * Reduce en un turno el tiempo de recarga actual (hasta un límite de 0).
     * * @param lanzador El personaje sobre el que se reduce el cooldown.
     */
    // Reduce el cooldown en 1 al final de cada turno del lanzador.
    // Si ya es 0 no hace nada (evitamos negativos).
    public void reducirCooldown(Personajes lanzador) {
        int actual = lanzador.getCooldown(this.nombre);
        if (actual > 0) {
            lanzador.setCooldown(this.nombre, actual - 1);
        }
    }
    /** @return El nombre del hechizo. */
    // Getters
    public String getNombre() {
        return nombre;
    }
    /** @return El coste en recurso mágico para lanzarlo. */
    public int getCosteMana() {
        return costeMana;
    }
    /** @return El multiplicador o fuerza base de curación/daño. */
    public int getPotenciaBase() {
        return potenciaBase;
    }
    /** @return El número de turnos necesarios para volver a usarlo. */
    public int getCooldownMaximo() {
        return cooldownMaximo;
    }
    /** @return El tipo de objetivo válido para este hechizo (Aliado o Enemigo). */
    public TipoObjetivo getTipoObjetivo() {
        return objetivo;
    }
    /** @return Una representación en texto de las características clave del hechizo. */
    @Override
    public String toString() {
        return nombre + " [coste:" + costeMana + " mana | CD:" + cooldownMaximo + "t]";
    }
}
