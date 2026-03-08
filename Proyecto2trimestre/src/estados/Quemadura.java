package estados;

import personajes.Personajes;

// QUEMADURA - Estado de tipo DOT (Daño en el tiempo).
// Efecto: Pierde 50 HP durante 3 turnos.
// Es el estado más potente por turno pero el que menos dura.

public class Quemadura extends Estados {

    // Valores fijos de la quemadura
    private static final String NOMBRE_ESTADO = "Quemadura";
    private static final int TURNOS_DEFAULT = 3;
    private static final int POTENCIA_DEFAULT = 50;

    // Constructor normal
    // Lo usa Yennefer con su hechizo Fuego de Vengerberg.
    public Quemadura() {
        super(NOMBRE_ESTADO, TURNOS_DEFAULT, POTENCIA_DEFAULT, TipoEstado.DOT);
    }

    // Constructor personalizado para Triss Merigold.
    // Su quemadura tiene menos potencia por turno pero dura mas turnos,
    // asi demostramos que dos magos del mismo tipo se comportan diferente.
    public Quemadura(int potenciaPorTurno, int turnos) {
        super(NOMBRE_ESTADO, turnos, potenciaPorTurno, TipoEstado.DOT);
    }

    // Lo que hacemos con alAplicar es que se ejecuta una sola vez cuando
    // la quemadura se aplica al personaje.
    // Solo muestra un mensaje avisando de que esta en llamas.
    @Override
    public void alAplicar(Personajes objetivo) {
        System.out.println(objetivo.getNombre()
                + " esta en LLAMAS. Sufrira " + potenciaPorTurno
                + " de dano durante " + turnosRestantes + " turnos.");
    }

    // Esto se ejecuta una vez por ronda mientras la quemadura este activa.
    // Aplica el daño al personaje y muestra cuanta vida le queda.
    @Override
    public void alProcesarTurno(Personajes objetivo) {
        objetivo.recibirDano(potenciaPorTurno); // resta 50 HP al personaje
        System.out.println(objetivo.getNombre()
                + " sufre " + potenciaPorTurno + " de dano por Quemadura. ("
                + objetivo.getVidaActual() + "/" + objetivo.getVidaMax() + " HP)");
    }

    // Se ejecuta cuando los turnos llegan a 0, cuando la quemadura termina.
    // Y nos muestra un mensaje indicando que el fuego se ha apagado.
    @Override
    public void alExpirar(Personajes objetivo) {
        System.out.println("La Quemadura de " + objetivo.getNombre() + " se ha extinguido.");
    }

    // Lo que hace es si se aplica quemadura a alguien que ya la tiene,
    // se reinicia la duracion a este valor en vez de apilar una nueva.
    public int getTurnosMaximos() {
        return TURNOS_DEFAULT;
    }
}