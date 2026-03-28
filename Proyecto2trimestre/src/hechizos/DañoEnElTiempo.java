package hechizos;

import personajes.Personajes;
import personajes.Mago;
import estados.Quemadura;

// DAÑO EN EL TIEMPO (DoT) - Aplica el estado Quemadura al enemigo.
// Yennefer usa:  Fuego de Vengerberg
// Caranthir usa: Escarcha Corrosiva  (aplica Quemadura con nombre temático)
//
// No hace daño al impactar; solo inyecta el estado al objetivo.
// Si el lanzador es Mago, el estado hará más daño (potencia aumentada x1.5).

public class DañoEnElTiempo extends Hechizos {

    public DañoEnElTiempo(String nombre, int costeMana, int cooldownMaximo) {
        // potenciaBase = 0 porque el daño lo gestionará el estado Quemadura
        super(nombre, costeMana, 0, cooldownMaximo, TipoObjetivo.ENEMIGO_UNICO);
    }

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
