package estados;

import personajes.Personajes;

/*
 * VENENO - Estado de tipo DOT.
 * Efecto: Pierde 30 HP durante 5 turnos.
 */
public class Veneno extends Estados {

    private static final String NOMBRE_ESTADO = "Veneno";
    private static final int TURNOS_DEFAULT = 5;
    private static final int POTENCIA_DEFAULT = 30;

    // Constructor
    public Veneno() {
        super(NOMBRE_ESTADO, TURNOS_DEFAULT, POTENCIA_DEFAULT, TipoEstado.DOT);
    }

    // Constructor con nombre alternativo ("Congelacion" para Caranthir).

    public Veneno(String nombre, int potenciaPorTurno, int turnos) {
        super(nombre, turnos, potenciaPorTurno, TipoEstado.DOT);
    }

    @Override
    public void alAplicar(Personajes objetivo) {
        System.out.println(objetivo.getNombre()
                + " Ha sido ENVENENADO. Sufrira " + potenciaPorTurno
                + " de dano durante " + turnosRestantes + " turnos.");
    }

    @Override
    public void alProcesarTurno(Personajes objetivo) {
        objetivo.recibirDano(potenciaPorTurno);
        System.out.println(objetivo.getNombre()
                + " sufre " + potenciaPorTurno + " de dano por " + nombre + ". ("
                + objetivo.getVidaActual() + "/" + objetivo.getVidaMax() + " HP)");
    }

    @Override
    public void alExpirar(Personajes objetivo) {
        System.out.println("El " + nombre + " de " + objetivo.getNombre() + " ha desaparecido.");
    }

    public int getTurnosMaximos() {
        return TURNOS_DEFAULT;
    }
}
