package estados;

import personajes.Personajes;

// RENOVAR - Estado de tipo HOT (Curación en el tiempo).
// Efecto: Recupera 40 HP durante 4 turnos sin superar la vidaMax.
// Si lo aplica un Sacerdote, cura 20 HP extra por turno. 

public class Renovar extends Estados {

    // Valores fijos del estado
    private static final String NOMBRE_ESTADO = "Renovar";
    private static final int TURNOS_DEFAULT = 4; // dura 4 turnos
    private static final int POTENCIA_DEFAULT = 40; // cura 40 HP por turno

    // Esto Guarda si fue un Sacerdote quien aplicó este estado.
    // Si es true, la curación por turno será 60 HP en vez de 40.
    private final boolean aplicadoPorSacerdote;

    // Constructor normal
    // Este costrutor lo utilizan los personajes que no sean Sacerdote.
    public Renovar() {
        super(NOMBRE_ESTADO, TURNOS_DEFAULT, POTENCIA_DEFAULT, TipoEstado.HOT);
        this.aplicadoPorSacerdote = false;
    }

    // Constructor personalizado para los Sacerdote.
    // Si aplicadoPorSacerdote es true, suma automáticamente 20 HP extra
    // Si es false, la potencia se queda en 40 HP como el constructor normal.
    public Renovar(boolean aplicadoPorSacerdote) {
        super(NOMBRE_ESTADO,
                TURNOS_DEFAULT,
                aplicadoPorSacerdote ? POTENCIA_DEFAULT + 20 : POTENCIA_DEFAULT,
                TipoEstado.HOT);
        this.aplicadoPorSacerdote = aplicadoPorSacerdote;
    }

    // Lo que hacemos con aplicar es que se ejecuta una sola vez cuando Renovar se
    // aplica al personaje.
    // Tanbien muestra un mensaje indicando cuánto curará y durante cuántos turnos.
    // Y si lo aplicó un Sacerdote, añade el aviso del bonus de +20 HP.
    @Override
    public void alAplicar(Personajes objetivo) {
        String bonus = aplicadoPorSacerdote ? " (+20 HP bonus de Sacerdote)" : "";
        System.out.println(objetivo.getNombre()
                + " comienza a REGENERARSE. Recuperara " + potenciaPorTurno
                + " HP durante " + turnosRestantes + " turnos." + bonus);
    }

    // Esto se ejecuta una vez por ronda mientras Renovar esté activo.
    // Guarda la vida antes de curar para calcular exactamente cuánto se ha curado.
    // La vida que sume puede ser menos si el personaje está casi al máximo.
    // Luego muestra la vida actual tras la curación.
    @Override
    public void alProcesarTurno(Personajes objetivo) {
        int vidaAntes = objetivo.getVidaActual();
        objetivo.curar(potenciaPorTurno);
        int curado = objetivo.getVidaActual() - vidaAntes;
        System.out.println(objetivo.getNombre()
                + " recupera " + curado + " HP por Renovar. ("
                + objetivo.getVidaActual() + "/" + objetivo.getVidaMax() + " HP)");
    }

    // Se ejecuta cuando los turnos llegan a 0,cuando Renovar termina.
    // Y nos muestra un mensaje indicando que el efecto ha terminado.
    @Override
    public void alExpirar(Personajes objetivo) {
        System.out.println("El efecto Renovar de " + objetivo.getNombre() + " ha terminado.");
    }

    // Lo que hace es si se aplica veneno a alguien que ya lo tiene,
    // se reinicia la duración a este valor en vez de apilar uno nuevo.

    public int getTurnosMaximos() {
        return TURNOS_DEFAULT;
    }

    // Devuelve true si Renovar fue aplicado por un Sacerdote.
    // Esto Aaron es util para saber desde fuera si tiene el bonus activo.

    public boolean isAplicadoPorSacerdote() {
        return aplicadoPorSacerdote;
    }
}