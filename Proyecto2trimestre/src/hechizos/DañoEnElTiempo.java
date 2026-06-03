package hechizos;

import personajes.Personajes;
import personajes.Mago;
import estados.Quemadura;

/**
 * La clase DañoEnElTiempo es una subclase de {@link Hechizos}.
 * Representa un hechizo ofensivo que no inflige daño directo al impactar,
 * sino que aplica un estado alterado (Quemadura) al objetivo para que reciba daño continuo.
 * Si el lanzador es de la clase Mago, el estado aplicado es más poderoso y duradero.
 */
// DAÑO EN EL TIEMPO (DoT) - Aplica el estado Quemadura al enemigo.
// Yennefer usa:  Fuego de Vengerberg
// Caranthir usa: Escarcha Corrosiva  (aplica Quemadura con nombre temático)
//
// No hace daño al impactar; solo inyecta el estado al objetivo.
// Si el lanzador es Mago, el estado hará más daño (potencia aumentada x1.5).

public class DañoEnElTiempo extends Hechizos {

    /**
     * Constructor para inicializar un hechizo de Daño en el Tiempo (DoT).
     * La potencia base se establece en 0, ya que el daño lo gestionará exclusivamente el estado alterado aplicado.
     * * @param nombre El nombre del hechizo (ej. "Fuego de Vengerberg").
     * @param costeMana La cantidad de recurso necesaria para lanzarlo.
     * @param cooldownMaximo Los turnos de recarga necesarios antes de poder volver a usarlo.
     */
    public DañoEnElTiempo(String nombre, int costeMana, int cooldownMaximo) {
        // potenciaBase = 0 porque el daño lo gestionará el estado Quemadura
        super(nombre, costeMana, 0, cooldownMaximo, TipoObjetivo.ENEMIGO_UNICO);
    }

    /**
     * Ejecuta el lanzamiento del hechizo sobre el objetivo.
     * Aplica el estado de Quemadura al enemigo. Si el lanzador es un {@link Mago}, 
     * la quemadura ve aumentada su potencia base y se prolonga su duración.
     * * @param lanzador El personaje que invoca y lanza el hechizo.
     * @param blanco El personaje enemigo que recibe el estado alterado.
     */
    @Override
    public void lanzar(Personajes lanzador, Personajes blanco) {
        if (!puedeUsarse(lanzador))
            return;

        System.out.println("" + lanzador.getNombre() + " lanza " + nombre
                + " sobre " + blanco.getNombre() + ". ¡Estado Quemadura aplicado!");

        // Si el mago lanza el DoT, el estado hace más daño (potencia escalada)
        if (lanzador instanceof Mago) {
            Mago mago = (Mago) lanzador;
            int potenciaMejorada = (int) (50 * mago.getMultiplicadorMagico()); // 50 = base de Quemadura
            int turnosMejorados = 4; // el mago extiende la duración un turno más
            Quemadura estadoMejorado = new Quemadura(potenciaMejorada, turnosMejorados);
            blanco.aplicarEstados(estadoMejorado);
            System.out.println("  (Quemadura potenciada por el poder mágico del Mago)");
        } else {
            Quemadura quemadura = new Quemadura(); // quemadura normal: 50 dmg, 3 turnos
            blanco.aplicarEstados(quemadura);
        }

        ponerEnCooldown(lanzador);
    }
}