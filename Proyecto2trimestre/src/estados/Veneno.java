package estados;

import personajes.Personajes;

/**
 * La clase Veneno es una subclase de {@link Estados}.
 * Representa un estado alterado perjudicial de Daño en el Tiempo (DoT).
 * <p>
 * Efecto por defecto: El personaje afectado pierde 30 puntos de salud (HP) por turno 
 * durante 5 turnos. A diferencia de la Quemadura, el Veneno dura más rondas pero inflige 
 * menos daño en cada una de ellas.
 * </p>
 */
public class Veneno extends Estados {

    // Valores fijos del veneno
    private static final String NOMBRE_ESTADO = "Veneno";
    private static final int TURNOS_DEFAULT = 5;
    private static final int POTENCIA_DEFAULT = 30;

    /**
     * Constructor por defecto.
     * Crea un veneno estándar aplicando los valores base (30 de daño durante 5 turnos).
     */
    public Veneno() {
        super(NOMBRE_ESTADO, TURNOS_DEFAULT, POTENCIA_DEFAULT, TipoEstado.DOT);
    }

    /**
     * Constructor alternativo y personalizado.
     * Permite crear variantes del estado con distinto nombre, daño y duración.
     * <p>
     * Ejemplo de uso: El jefe Caranthir utiliza una variante de este estado 
     * llamada "Congelación", aplicando valores distintos al veneno normal.
     * </p>
     * 
     * @param nombre El nombre personalizado del estado (ej. "Congelación").
     * @param potenciaPorTurno Cantidad de daño que infligirá en cada ronda.
     * @param turnos Cantidad de rondas que durará el efecto.
     */
    public Veneno(String nombre, int potenciaPorTurno, int turnos) {
        super(nombre, turnos, potenciaPorTurno, TipoEstado.DOT);
    }

    /**
     * Se ejecuta una sola vez en el instante en que el veneno se aplica al personaje.
     * Muestra un mensaje por consola advirtiendo de que ha sido envenenado, indicando 
     * la potencia y la duración del efecto.
     * 
     * @param objetivo El personaje que empieza a sufrir el envenenamiento.
     */
    @Override
    public void alAplicar(Personajes objetivo) {
        System.out.println(objetivo.getNombre()
                + " Ha sido ENVENENADO. Sufrira " + potenciaPorTurno
                + " de dano durante " + turnosRestantes + " turnos.");
    }

    /**
     * Se ejecuta una vez por ronda mientras el veneno permanezca activo.
     * Aplica el daño correspondiente al personaje y muestra cuánta vida le queda.
     * 
     * @param objetivo El personaje al cual se le restan los puntos de salud.
     */
    @Override
    public void alProcesarTurno(Personajes objetivo) {
        objetivo.recibirDano(potenciaPorTurno);
        System.out.println(objetivo.getNombre()
                + " sufre " + potenciaPorTurno + " de dano por " + nombre + ". ("
                + objetivo.getVidaActual() + "/" + objetivo.getVidaMax() + " HP)");
    }

    /**
     * Se ejecuta cuando el contador de turnos llega a cero y el veneno termina.
     * Muestra un mensaje indicando que el efecto ha desaparecido.
     * 
     * @param objetivo El personaje que deja de estar afectado por el veneno.
     */
    @Override
    public void alExpirar(Personajes objetivo) {
        System.out.println("El " + nombre + " de " + objetivo.getNombre() + " ha desaparecido.");
    }

    /**
     * Devuelve el número de turnos máximos por defecto para esta alteración.
     * Se utiliza para reiniciar la duración a este valor si el veneno se vuelve 
     * a aplicar sobre alguien que ya lo padece, evitando apilar múltiples instancias.
     * 
     * @return El número de turnos por defecto (5).
     */
    public int getTurnosMaximos() {
        return TURNOS_DEFAULT;
    }
}