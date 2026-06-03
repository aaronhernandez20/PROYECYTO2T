package hechizos;

import personajes.Personajes;
import personajes.Sacerdote;

/**
 * La clase CuraciónDirecta es una subclase de {@link Hechizos}.
 * Representa un hechizo de apoyo que restaura vida de forma inmediata a un aliado.
 * <p>
 * <b>Mecánica de Curación:</b> La cantidad de vida curada viene determinada por 
 * la potencia base del hechizo (habitualmente +200 HP fijos).<br>
 * Si el personaje que lanza este hechizo es un {@link Sacerdote}, gracias al polimorfismo, 
 * su propio método de curación aplicará automáticamente un bonus de sanación extra, 
 * sin requerir cálculos adicionales desde esta clase.
 * </p>
 */
public class CuraciónDirecta extends Hechizos {

    /**
     * Constructor para inicializar un hechizo de Curación Directa.
     * El nombre varía temáticamente según el personaje (ej. Ciri usa "Poción de Golondrina", 
     * Eredin usa "Sacrificio Oscuro").
     * * @param nombre El nombre del hechizo en combate.
     * @param costeMana La cantidad de recurso (maná/fe) requerida para lanzarlo.
     * @param potenciaBase La cantidad de puntos de vida fijos que restaura al impacto.
     * @param cooldownMaximo Los turnos de recarga (espera) antes de poder volver a usarlo.
     */
    public CuraciónDirecta(String nombre, int costeMana, int potenciaBase, int cooldownMaximo) {
        super(nombre, costeMana, potenciaBase, cooldownMaximo, TipoObjetivo.ALIADO_UNICO);
    }

    /**
     * Ejecuta el lanzamiento del hechizo de sanación sobre el objetivo aliado.
     * Restaura la vida del objetivo, notifica por consola si el lanzador es un Sacerdote 
     * (quien aplica su propio bonus) y finalmente registra el uso para los logros.
     * * @param lanzador El personaje que invoca y lanza el hechizo.
     * @param blanco El personaje aliado (puede ser el propio lanzador) que recibe la curación.
     */
    @Override
    public void lanzar(Personajes lanzador, Personajes blanco) {
        if (!puedeUsarse(lanzador))
            return;

        System.out.println(" " + lanzador.getNombre() + " lanza " + nombre
                + " sobre " + blanco.getNombre() + ".");

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
        main.Main.logros.registrarCuracion(this.potenciaBase);
    }
}