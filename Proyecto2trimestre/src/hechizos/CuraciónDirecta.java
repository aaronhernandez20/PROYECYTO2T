package hechizos;

import personajes.Personajes;
import personajes.Sacerdote;

// CURACIÓN DIRECTA - Hechizo que restaura vida inmediata a un aliado.
// Ciri usa:  Poción de Golondrina
// Eredin usa: Sacrificio Oscuro
//
// Curación = potenciaBase (+200 HP fijo en los parámetros del hechizo).
// Si el lanzador es Sacerdote, el método curar() de Sacerdote ya añade
// automáticamente el bonusSanacion, así que no hay que hacer nada extra aquí.

public class CuraciónDirecta extends Hechizos {

    public CuraciónDirecta(String nombre, int costeMana, int potenciaBase, int cooldownMaximo) {
        super(nombre, costeMana, potenciaBase, cooldownMaximo, TipoObjetivo.ALIADO_UNICO);
    }

    @Override
    public void lanzar(Personajes lanzador, Personajes blanco) {
        if (!puedeUsarse(lanzador))
            return;

        System.out.println(" " + lanzador.getNombre() + " lanza «" + nombre
                + "» sobre " + blanco.getNombre() + ".");

        // Si el lanzador es Sacerdote, el override de curar() en Sacerdote
        // ya suma el bonusSanacion automáticamente. Sin trabajo extra.
        if (lanzador instanceof Sacerdote) {
            // El sacerdote también se beneficia de su propio bonus al curar aliados
            blanco.curar(this.potenciaBase);
            System.out.println("  (El Sacerdote aplica su bonus de sanación automáticamente)");
        } else {
            blanco.curar(this.potenciaBase);
        }

        ponerEnCooldown(lanzador);
    }
}
