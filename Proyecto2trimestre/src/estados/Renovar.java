package estados;

import personajes.Personajes;

//RENOVAR - Estado de tipo HOT.
//Efecto: Recupera 40 HP durante 4 turnos sin superar la vidaMax.
//Si lo aplica un Sacerdote, cura 20 HP extra por turno. 

public class Renovar extends Estados {

    private static final String NOMBRE_ESTADO = "Renovar";
    private static final int TURNOS_DEFAULT = 4;
    private static final int POTENCIA_DEFAULT = 40;

    private final boolean aplicadoPorSacerdote;

    // Constructor estandar: 40 HP / 4 turnos. */
    public Renovar() {
        super(NOMBRE_ESTADO, TURNOS_DEFAULT, POTENCIA_DEFAULT, TipoEstado.HOT);
        this.aplicadoPorSacerdote = false;
    }

    // Este es el Constructor personalizadobonus de Sacerdote.
    // Si es true, la potencia sube 20 HP por turno automaticamente.

    public Renovar(boolean aplicadoPorSacerdote) {
        super(NOMBRE_ESTADO,
                TURNOS_DEFAULT,
                aplicadoPorSacerdote ? POTENCIA_DEFAULT + 20 : POTENCIA_DEFAULT,
                TipoEstado.HOT);
        this.aplicadoPorSacerdote = aplicadoPorSacerdote;
    }

    @Override
    public void alAplicar(Personajes objetivo) {
        String bonus = aplicadoPorSacerdote ? " (+20 HP bonus de Sacerdote)" : "";
        System.out.println(objetivo.getNombre()
                + " comienza a REGENERARSE. Recuperara " + potenciaPorTurno
                + " HP durante " + turnosRestantes + " turnos." + bonus);
    }

    @Override
    public void alProcesarTurno(Personajes objetivo) {
        int vidaAntes = objetivo.getVidaActual();
        objetivo.curar(potenciaPorTurno);
        int curado = objetivo.getVidaActual() - vidaAntes;
        System.out.println(objetivo.getNombre()
                + " recupera " + curado + " HP por Renovar. ("
                + objetivo.getVidaActual() + "/" + objetivo.getVidaMax() + " HP)");
    }

    @Override
    public void alExpirar(Personajes objetivo) {
        System.out.println("El efecto Renovar de " + objetivo.getNombre() + " ha terminado.");
    }

    public int getTurnosMaximos() {
        return TURNOS_DEFAULT;
    }

    public boolean isAplicadoPorSacerdote() {
        return aplicadoPorSacerdote;
    }
}