package estados;

import personajes.Personajes;

/*
 * QUEMADURA - Estado de tipo DOT.
 * Efecto: Pierde 50 HP durante 3 turnos.
 */

public class Quemadura extends Estados {

    private static final String NOMBRE_ESTADO = "Quemadura";
    private static final int TURNOS_DEFAULT = 3;
    private static final int POTENCIA_DEFAULT = 50;

    // Este es el Constructor
    public Quemadura() {
        super(NOMBRE_ESTADO, TURNOS_DEFAULT, POTENCIA_DEFAULT, TipoEstado.DOT);
    }

    // Este es el Constructor personalizado de Triss Merigold que tiene menos
    // potencia y mas duracion).

    public Quemadura(int potenciaPorTurno, int turnos) {
        super(NOMBRE_ESTADO, turnos, potenciaPorTurno, TipoEstado.DOT);
    }

    @Override
    public void alAplicar(Personajes objetivo) {
        System.out.println(objetivo.getNombre()
                + " esta en LLAMAS. Sufrira " + potenciaPorTurno
                + " de dano durante " + turnosRestantes + " turnos.");
    }

    @Override
    public void alProcesarTurno(Personajes objetivo) {
        objetivo.recibirDano(potenciaPorTurno);
        System.out.println(objetivo.getNombre()
                + " sufre " + potenciaPorTurno + " de dano por Quemadura. ("
                + objetivo.getVidaActual() + "/" + objetivo.getVidaMax() + " HP)");
    }

    @Override
    public void alExpirar(Personajes objetivo) {
        System.out.println("La Quemadura de " + objetivo.getNombre() + " se ha extinguido.");
    }

    public int getTurnosMaximos() {
        return TURNOS_DEFAULT;
    }
}