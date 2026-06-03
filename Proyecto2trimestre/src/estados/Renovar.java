package estados;

import personajes.Personajes;

/**
 * La clase Renovar es una subclase de {@link Estados}.
 * Representa un estado beneficioso de Curación en el Tiempo (HoT).
 * <p>
 * Efecto por defecto: El personaje recupera 40 puntos de salud (HP) por turno durante 4 turnos, 
 * sin superar en ningún caso su vida máxima.
 * Si este estado es aplicado por la clase Sacerdote, la curación se potencia con 20 HP extra por turno.
 * </p>
 */
public class Renovar extends Estados {

    // Valores fijos del estado
    private static final String NOMBRE_ESTADO = "Renovar";
    private static final int TURNOS_DEFAULT = 4; // dura 4 turnos
    private static final int POTENCIA_DEFAULT = 40; // cura 40 HP por turno

    // Guarda si fue un Sacerdote quien aplicó este estado.
    // Si es true, la curación por turno será 60 HP en vez de 40.
    private final boolean aplicadoPorSacerdote;

    /**
     * Constructor normal por defecto.
     * Este constructor lo utilizan los personajes que aplican regeneración y no pertenecen a la clase Sacerdote.
     */
    public Renovar() {
        super(NOMBRE_ESTADO, TURNOS_DEFAULT, POTENCIA_DEFAULT, TipoEstado.HOT);
        this.aplicadoPorSacerdote = false;
    }

    /**
     * Constructor personalizado para gestionar el bonus de la clase Sacerdote.
     * Si aplicadoPorSacerdote es true, suma automáticamente 20 HP extra a la potencia por turno.
     * Si es false, la potencia se queda en 40 HP, comportándose igual que el constructor normal.
     * 
     * @param aplicadoPorSacerdote Indica si el lanzador del estado es un Sacerdote.
     */
    public Renovar(boolean aplicadoPorSacerdote) {
        super(NOMBRE_ESTADO,
                TURNOS_DEFAULT,
                aplicadoPorSacerdote ? POTENCIA_DEFAULT + 20 : POTENCIA_DEFAULT,
                TipoEstado.HOT);
        this.aplicadoPorSacerdote = aplicadoPorSacerdote;
    }

    /**
     * Se ejecuta una sola vez en el instante en que el estado Renovar se aplica al personaje.
     * Muestra un mensaje indicando cuánto curará y durante cuántos turnos.
     * Además, si lo aplicó un Sacerdote, añade un aviso visual por consola del bonus de +20 HP.
     * 
     * @param objetivo El personaje que comienza a regenerar salud.
     */
    @Override
    public void alAplicar(Personajes objetivo) {
        String bonus = aplicadoPorSacerdote ? " (+20 HP bonus de Sacerdote)" : "";
        System.out.println(objetivo.getNombre()
                + " comienza a REGENERARSE. Recuperara " + potenciaPorTurno
                + " HP durante " + turnosRestantes + " turnos." + bonus);
    }

    /**
     * Se ejecuta una vez por ronda mientras el estado Renovar esté activo.
     * Guarda la vida antes de curar para calcular exactamente cuánto se ha recuperado
     * (ya que la salud obtenida puede ser menor si el personaje alcanza su máximo).
     * Posteriormente, muestra la vida actual tras la curación.
     * 
     * @param objetivo El personaje aliado que recupera vida en este turno.
     */
    @Override
    public void alProcesarTurno(Personajes objetivo) {
        int vidaAntes = objetivo.getVidaActual();
        objetivo.curar(potenciaPorTurno);
        int curado = objetivo.getVidaActual() - vidaAntes;
        System.out.println(objetivo.getNombre()
                + " recupera " + curado + " HP por Renovar. ("
                + objetivo.getVidaActual() + "/" + objetivo.getVidaMax() + " HP)");
    }

    /**
     * Se ejecuta cuando el contador de turnos llega a 0 y el estado concluye.
     * Muestra un mensaje indicando que el efecto de regeneración ha terminado.
     * 
     * @param objetivo El personaje que deja de regenerar vida.
     */
    @Override
    public void alExpirar(Personajes objetivo) {
        System.out.println("El efecto Renovar de " + objetivo.getNombre() + " ha terminado.");
    }

    /**
     * Devuelve la duración máxima por defecto para esta alteración.
     * Se utiliza para reiniciar el contador de turnos a este valor inicial en lugar 
     * de apilar un nuevo estado si se vuelve a aplicar sobre un personaje que ya se está regenerando.
     * 
     * @return El número de turnos máximos (4).
     */
    public int getTurnosMaximos() {
        return TURNOS_DEFAULT;
    }

    /**
     * Indica si este estado fue originado por un personaje de la clase Sacerdote.
     * 
     * @return true si lo aplicó un Sacerdote, false en caso contrario.
     */
    public boolean isAplicadoPorSacerdote() {
        return aplicadoPorSacerdote;
    }
}