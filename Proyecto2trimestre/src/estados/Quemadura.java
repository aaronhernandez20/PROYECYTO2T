package estados;

import personajes.Personajes;

/**
 * La clase Quemadura es una subclase de {@link Estados}.
 * Representa un estado alterado de Daño en el Tiempo (DoT).
 * <p>
 * Efecto por defecto: El personaje afectado pierde 50 puntos de salud (HP) por turno 
 * durante 3 turnos. Es el estado más potente por turno, pero el de menor duración.
 * </p>
 */
public class Quemadura extends Estados {

    // Valores fijos de la quemadura
    private static final String NOMBRE_ESTADO = "Quemadura";
    private static final int TURNOS_DEFAULT = 3;
    private static final int POTENCIA_DEFAULT = 50;

    /**
     * Constructor por defecto para una Quemadura estándar.
     * Habitualmente lo usa Yennefer con su hechizo "Fuego de Vengerberg".
     */
    public Quemadura() {
        super(NOMBRE_ESTADO, TURNOS_DEFAULT, POTENCIA_DEFAULT, TipoEstado.DOT);
    }

    /**
     * Constructor personalizado para aplicar una Quemadura con potencia y duración diferentes.
     * Ejemplo de uso: Triss Merigold aplica una quemadura con menos potencia por turno 
     * pero que dura más tiempo, demostrando que dos magos del mismo tipo pueden comportarse diferente.
     * * @param potenciaPorTurno Cantidad de daño que infligirá en cada ronda.
     * @param turnos Cantidad de rondas que durará el efecto.
     */
    public Quemadura(int potenciaPorTurno, int turnos) {
        super(NOMBRE_ESTADO, turnos, potenciaPorTurno, TipoEstado.DOT);
    }

    /**
     * Se invoca una única vez en el instante en que el estado se aplica al personaje.
     * Muestra un mensaje de advertencia informando de que el personaje está ardiendo.
     * * @param objetivo El personaje que empieza a sufrir la quemadura.
     */
    @Override
    public void alAplicar(Personajes objetivo) {
        System.out.println(objetivo.getNombre()
                + " esta en LLAMAS. Sufrira " + potenciaPorTurno
                + " de dano durante " + turnosRestantes + " turnos.");
    }

    /**
     * Se ejecuta una vez por ronda mientras la quemadura permanezca activa.
     * Aplica el daño directamente al personaje y muestra cuánta vida le queda.
     * * @param objetivo El personaje al cual se le restan los puntos de salud.
     */
    @Override
    public void alProcesarTurno(Personajes objetivo) {
        objetivo.recibirDano(potenciaPorTurno); // resta 50 HP al personaje
        System.out.println(objetivo.getNombre()
                + " sufre " + potenciaPorTurno + " de dano por Quemadura. ("
                + objetivo.getVidaActual() + "/" + objetivo.getVidaMax() + " HP)");
    }

    /**
     * Se ejecuta cuando el contador de turnos llega a cero y el estado desaparece.
     * Muestra un mensaje indicando que el fuego se ha apagado.
     * * @param objetivo El personaje que deja de estar afectado por la quemadura.
     */
    @Override
    public void alExpirar(Personajes objetivo) {
        System.out.println("La Quemadura de " + objetivo.getNombre() + " se ha extinguido.");
    }

    /**
     * Devuelve el número de turnos por defecto para esta alteración.
     * Se utiliza para reiniciar la duración si se vuelve a aplicar el estado 
     * a un personaje que ya está quemándose (en lugar de apilar dos quemaduras).
     * * @return El número de turnos por defecto (3).
     */
    public int getTurnosMaximos() {
        return TURNOS_DEFAULT;
    }
}