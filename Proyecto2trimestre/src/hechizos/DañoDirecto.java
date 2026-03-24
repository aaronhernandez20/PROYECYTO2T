package hechizos;

import personajes.Personajes;
import personajes.Mago;

// DAÑO DIRECTO - Hechizo de daño instantáneo a un enemigo único.
// Geralt/Yennefer usan: Señal de Igni
// Caranthir usa:        Lanza de Hielo
//
// Daño = potenciaBase + poderMagico del lanzador
// Si el lanzador es Mago, se aplica además su multiplicadorMagico (los
// hechizos del mago ignoran la defensa del rival: se llama a recibirDanoMagico).

public class DañoDirecto extends Hechizos {

    // Constructor: el nombre cambia según el personaje que lo use (Igni o Lanza de
    // Hielo)
    public DañoDirecto(String nombre, int costeMana, int potenciaBase, int cooldownMaximo) {
        super(nombre, costeMana, potenciaBase, cooldownMaximo, TipoObjetivo.ENEMIGO_UNICO);
    }

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
            System.out.println("  ✦ " + lanzador.getNombre() + " lanza «" + nombre
                    + "» sobre " + blanco.getNombre() + " ¡El hechizo ignora su defensa!");
            // recibirDanoMagico ignora defensaBase (daño 100%)
            blanco.recibirDanoMagico(dano);
        } else {
            System.out.println("  ✦ " + lanzador.getNombre() + " lanza «" + nombre
                    + "» sobre " + blanco.getNombre() + ".");
            blanco.recibirDano(dano);
        }

        // Ponemos el hechizo en cooldown
        ponerEnCooldown(lanzador);
    }
}
