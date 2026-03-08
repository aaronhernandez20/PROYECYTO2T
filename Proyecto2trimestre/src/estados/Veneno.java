package estados;

import personajes.Personajes;

// VENENO - Estado de tipo DOT (Daño en el tiempo).
// Efecto: El personaje afectado pierde 30 HP durante 5 turnos.
// Dura más que la Quemadura pero hace menos daño por turno.

public class Veneno extends Estados {

    // Valores fijos del veneno
    private static final String NOMBRE_ESTADO = "Veneno";
    private static final int TURNOS_DEFAULT = 5;
    private static final int POTENCIA_DEFAULT = 30;

    // Constructor normal: crea un veneno estándar con los valores de arriba
    public Veneno() {
        super(NOMBRE_ESTADO, TURNOS_DEFAULT, POTENCIA_DEFAULT, TipoEstado.DOT);
    }

    // Este es el Constructor alternativo: Que nos permite crear variantes con
    // distinto nombre,
    // daño y duración. Por ejemplo se usa para Caranthir cuyo veneno se llama
    // Congelacion
    // y puede tener valores distintos al veneno normal.
    public Veneno(String nombre, int potenciaPorTurno, int turnos) {
        super(nombre, turnos, potenciaPorTurno, TipoEstado.DOT);
    }

    // Este solo se ejecuta una sola vez cuando el veneno se aplica al personaje.
    // y nos muestra un mensaje diciendo que ha sido envenenado.
    @Override
    public void alAplicar(Personajes objetivo) {
        System.out.println(objetivo.getNombre()
                + " Ha sido ENVENENADO. Sufrira " + potenciaPorTurno
                + " de dano durante " + turnosRestantes + " turnos.");
    }

    // Lo que quiero hacer es que se ejecuta una vez por ronda mientras el veneno
    // esté activo.
    // Aplica el daño al personaje y muestra cuánta vida le queda.
    @Override
    public void alProcesarTurno(Personajes objetivo) {
        objetivo.recibirDano(potenciaPorTurno);
        System.out.println(objetivo.getNombre()
                + " sufre " + potenciaPorTurno + " de dano por " + nombre + ". ("
                + objetivo.getVidaActual() + "/" + objetivo.getVidaMax() + " HP)");
    }

    // Este se ejecuta cuando los turnos llegan a 0, es decir, cuando el veneno
    // termina.
    // y nos muestra un mensaje indicando que el efecto ha desaparecido.

    @Override
    public void alExpirar(Personajes objetivo) {
        System.out.println("El " + nombre + " de " + objetivo.getNombre() + " ha desaparecido.");
    }

    // Lo que hace es si se aplica veneno a alguien que ya lo tiene,
    // se reinicia la duración a este valor en vez de apilar uno nuevo.

    public int getTurnosMaximos() {
        return TURNOS_DEFAULT;
    }
}
