package hechizos;

import personajes.Personajes;
import personajes.Sacerdote;
import estados.Renovar;

/**
 * La clase CuraciónEnElTiempo es una subclase de {@link Hechizos}.
 * Representa un hechizo de apoyo de Curación en el Tiempo (HoT) que aplica el estado Renovar a un aliado.
 * <p>
 * Ejemplos de uso en el juego:<br>
 * - Ciri usa: Aura de la Vieja Sangre<br>
 * - Eredin usa: Presencia del Rey
 * </p>
 * <p>
 * Este hechizo no cura instantáneamente al impactar, sino que inyecta el estado Renovar al objetivo.
 * Si el lanzador es de la clase {@link Sacerdote}, el estado Renovar se potenciará curando 20 HP extra por turno.
 * </p>
 */
public class CuraciónEnElTiempo extends Hechizos {

    /**
     * Constructor para inicializar un hechizo de Curación en el Tiempo.
     * La potencia base se establece en 0 automáticamente, ya que la curación es gestionada
     * de forma exclusiva por el estado Renovar.
     * 
     * @param nombre El nombre del hechizo (ej. "Aura de la Vieja Sangre").
     * @param costeMana La cantidad de recurso (maná/fe) necesaria para lanzarlo.
     * @param cooldownMaximo Los turnos de recarga necesarios antes de poder volver a usarlo.
     */
    public CuraciónEnElTiempo(String nombre, int costeMana, int cooldownMaximo) {
        // potenciaBase = 0 porque la curación la gestiona el estado Renovar
        super(nombre, costeMana, 0, cooldownMaximo, TipoObjetivo.ALIADO_UNICO);
    }

    /**
     * Ejecuta el lanzamiento del hechizo sobre el objetivo aliado.
     * Aplica el estado de regeneración (Renovar). Si el lanzador es un Sacerdote, 
     * se aplica una versión potenciada del estado y se registra en los logros de la partida.
     * 
     * @param lanzador El personaje que invoca y lanza el hechizo.
     * @param blanco El personaje aliado que recibe el estado de curación continua.
     */
    @Override
    public void lanzar(Personajes lanzador, Personajes blanco) {
        if (!puedeUsarse(lanzador))
            return;

        System.out.println(" " + lanzador.getNombre() + " lanza " + nombre
                + " sobre " + blanco.getNombre() + ". ¡Estado Renovar aplicado!");

        // Si lo lanza un Sacerdote, Renovar cura +20 HP extra por turno
        if (lanzador instanceof Sacerdote) {
        	main.Main.logros.registrarRenovarPorSacerdote();
            Renovar renovarMejorado = new Renovar(true); // true = aplicado por Sacerdote
            blanco.aplicarEstados(renovarMejorado);
            System.out.println("  (Renovar potenciado: +20 HP extra por turno gracias al Sacerdote)");
        } else {
            Renovar renovar = new Renovar(); // 40 HP por turno, 4 turnos
            blanco.aplicarEstados(renovar);
        }

        ponerEnCooldown(lanzador);
    }
}