package hechizos;

import personajes.Personajes;
import personajes.Mago;

/**
 * La clase DañoDirecto es una subclase de {@link Hechizos}.
 * Representa un hechizo de daño instantáneo dirigido a un enemigo único.
 * <p>
 * <b>Mecánica de Daño:</b> El daño se calcula sumando la potenciaBase del hechizo 
 * y el poderMagico del lanzador.<br>
 * Si el lanzador pertenece a la clase {@link Mago}, se aplica además su multiplicador 
 * mágico y el impacto ignora completamente la defensa del rival (daño directo a la vida).
 * </p>
 */
public class DañoDirecto extends Hechizos {

    /**
     * Constructor para inicializar un hechizo de Daño Directo.
     * El nombre y las estadísticas varían según el personaje (ej. Geralt usa "Señal de Igni", 
     * Caranthir usa "Lanza de Hielo").
     * * @param nombre El nombre del hechizo en combate.
     * @param costeMana La cantidad de recurso (maná/vigor) requerida para lanzarlo.
     * @param potenciaBase El daño base que inflige el hechizo.
     * @param cooldownMaximo Los turnos de recarga (espera) antes de poder volver a usarlo.
     */
    public DañoDirecto(String nombre, int costeMana, int potenciaBase, int cooldownMaximo) {
        super(nombre, costeMana, potenciaBase, cooldownMaximo, TipoObjetivo.ENEMIGO_UNICO);
    }

    /**
     * Ejecuta el lanzamiento del hechizo ofensivo sobre el objetivo.
     * Calcula el daño final y lo aplica al enemigo, alterando la ejecución si el
     * atacante es un Mago (ignorando la armadura del rival).
     * * @param lanzador El personaje que invoca y lanza el hechizo.
     * @param blanco El personaje enemigo que recibe el impacto del hechizo.
     */
    @Override
    public void lanzar(Personajes lanzador, Personajes blanco) {
        // Comprobamos cooldown y recurso antes de hacer nada
        if (!puedeUsarse(lanzador))
            return;

        // Calculo de daño base: potenciaBase + poderMagico del lanzador
        int dano = this.potenciaBase + lanzador.getPoderMagico();

        // Si el lanzador es Mago, multiplica el poder y el hechizo ignora defensa
        if (lanzador instanceof Mago) {
            Mago mago = (Mago) lanzador;
            dano = (int) (dano * mago.getMultiplicadorMagico());
            System.out.println("  " + lanzador.getNombre() + " lanza «" + nombre
                    + "» sobre " + blanco.getNombre() + " ¡El hechizo ignora su defensa!");
            // recibirDanoMagico ignora defensaBase (daño 100%)
            blanco.recibirDanoMagico(dano);
        } else {
            System.out.println("   " + lanzador.getNombre() + " lanza «" + nombre
                    + "» sobre " + blanco.getNombre() + ".");
            blanco.recibirDano(dano);
        }

        // Ponemos el hechizo en cooldown
        ponerEnCooldown(lanzador);
    }
}