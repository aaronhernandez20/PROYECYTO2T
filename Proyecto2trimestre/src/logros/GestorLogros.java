package logros;

import personajes.Personajes;

/**
 * Gestor del sistema de logros.
 * 
 * Cada logro es un boolean: false = bloqueado, true = desbloqueado.
 * Ademas hay contadores enteros para llevar el progreso.
 * 
 * Como usarlo:
 *   1. Crear una instancia en Main:  GestorLogros logros = new GestorLogros();
 *   2. Llamar a los metodos registrar...() desde el combate cuando ocurran cosas.
 *   3. Llamar a comprobarFinCombate() al terminar un combate.
 *   4. Llamar a mostrarLogros() para ver el estado.
 */
public class GestorLogros {

    // CONTADORES (cuantas veces ha pasado cada cosa)
    private int combatesGanados = 0;
    private int hechizosTotal = 0;
    private int curacionTotal = 0;
    private int quemadurasAplicadas = 0;
    private int renovaresPorSacerdote = 0;

    // Estos se resetean al final de cada combate
    private int hechizosEsteCombate = 0;
    private boolean geraltSinDano = true;

    // LOGROS (true = desbloqueado, false = bloqueado)
    private boolean primeraSangre = false;
    private boolean veterano = false;
    private boolean imbatible = false;
    private boolean porLosPelos = false;
    private boolean golpeCritico = false;
    private boolean carnicero = false;
    private boolean soloCuerpoACuerpo = false;
    private boolean magoExperimentado = false;
    private boolean sanador = false;
    private boolean piromano = false;
    private boolean renovador = false;
    private boolean geraltImparable = false;
    private boolean vencerAlRey = false;

    // METODOS PARA REGISTRAR EVENTOS DEL JUEGO

    /** Llamar cuando un arma haga un critico. */
    public void registrarCritico() {
        if (!golpeCritico) {
            golpeCritico = true;
            mostrarMensaje("GOLPE CRITICO", "Has asestado tu primer golpe critico.");
        }
    }

    /** Llamar despues de calcular el dano de un ataque. */
    public void registrarDanoInfligido(int dano) {
        if (dano >= 500 && !carnicero) {
            carnicero = true;
            mostrarMensaje("CARNICERO", "Has infligido 500 o mas de dano en un solo ataque.");
        }
    }

    /** Llamar cada vez que se lanza un hechizo. */
    public void registrarHechizoLanzado() {
        hechizosTotal++;
        hechizosEsteCombate++;
        if (hechizosTotal >= 50 && !magoExperimentado) {
            magoExperimentado = true;
            mostrarMensaje("MAGO EXPERIMENTADO", "Has lanzado 50 hechizos en total.");
        }
    }

    /** Llamar cuando alguien cure (CuracionDirecta o Sacerdote.curar). */
    public void registrarCuracion(int cantidad) {
        curacionTotal += cantidad;
        if (curacionTotal >= 1000 && !sanador) {
            sanador = true;
            mostrarMensaje("SANADOR", "Has curado 1000 HP en total.");
        }
    }

    /** Llamar cuando se aplique Quemadura a un enemigo. */
    public void registrarQuemaduraAplicada() {
        quemadurasAplicadas++;
        if (quemadurasAplicadas >= 20 && !piromano) {
            piromano = true;
            mostrarMensaje("PIROMANO", "Has aplicado Quemadura 20 veces.");
        }
    }

    /** Llamar cuando un Sacerdote aplique Renovar. */
    public void registrarRenovarPorSacerdote() {
        renovaresPorSacerdote++;
        if (renovaresPorSacerdote >= 10 && !renovador) {
            renovador = true;
            mostrarMensaje("RENOVADOR", "Has aplicado Renovar con un Sacerdote 10 veces.");
        }
    }

    /** Llamar cuando Geralt reciba dano. */
    public void registrarDanoAGeralt() {
        geraltSinDano = false;
    }

    /** Llamar cuando muera un enemigo. Solo desbloquea logro si es Eredin. */
    public void registrarMuerteEnemigo(Personajes enemigo) {
        if (enemigo.getNombre().equalsIgnoreCase("Eredin") && !vencerAlRey) {
            vencerAlRey = true;
            mostrarMensaje("VENCER AL REY", "Has derrotado a Eredin.");
        }
    }

    /**
     * Llamar al final de cada combate.
     * 
     * Javadoc
     * @param ganado          true si el equipo del jugador gano
     * @param equipoJugador   array con los 3 aliados (vivos o muertos)
     */
    public void comprobarFinCombate(boolean ganado, Personajes[] equipoJugador) {
        if (!ganado) {
            // Si pierde, solo resetea los contadores del combate
            hechizosEsteCombate = 0;
            geraltSinDano = true;
            return;
        }

        combatesGanados++;

        // PRIMERA SANGRE: primer combate ganado
        if (combatesGanados == 1 && !primeraSangre) {
            primeraSangre = true;
            mostrarMensaje("PRIMERA SANGRE", "Has ganado tu primer combate.");
        }

        // VETERANO: 10 combates ganados
        if (combatesGanados >= 10 && !veterano) {
            veterano = true;
            mostrarMensaje("VETERANO", "Has ganado 10 combates.");
        }

        // Recorremos los aliados para comprobar IMBATIBLE y POR LOS PELOS
        boolean todosVivos = true;
        boolean alguienBajo = false;
        for (int i = 0; i < equipoJugador.length; i++) {
            Personajes p = equipoJugador[i];
            if (!p.estaVivo()) {
                todosVivos = false;
            }
            if (p.estaVivo() && p.getVidaActual() < p.getVidaMax() * 0.1) {
                alguienBajo = true;
            }
        }

        if (todosVivos && !imbatible) {
            imbatible = true;
            mostrarMensaje("IMBATIBLE", "Has ganado sin que muera ningun aliado.");
        }
        if (alguienBajo && !porLosPelos) {
            porLosPelos = true;
            mostrarMensaje("POR LOS PELOS", "Has ganado con un aliado por debajo del 10% de vida.");
        }

        // SOLO CUERPO A CUERPO: no se lanzaron hechizos
        if (hechizosEsteCombate == 0 && !soloCuerpoACuerpo) {
            soloCuerpoACuerpo = true;
            mostrarMensaje("SOLO CUERPO A CUERPO", "Has ganado un combate sin lanzar hechizos.");
        }

        // GERALT IMPARABLE: Geralt no recibio dano
        if (geraltSinDano && !geraltImparable) {
            geraltImparable = true;
            mostrarMensaje("GERALT IMPARABLE", "Has ganado un combate sin que Geralt reciba dano.");
        }

        // Reset de los contadores del combate
        hechizosEsteCombate = 0;
        geraltSinDano = true;
    }

    // MOSTRAR MENSAJES Y ESTADO

    private void mostrarMensaje(String nombre, String descripcion) {
        System.out.println();
        System.out.println("================================================");
        System.out.println("    LOGRO DESBLOQUEADO: " + nombre);
        System.out.println("    " + descripcion);
        System.out.println("================================================");
        System.out.println();
    }

    /** Muestra el estado de todos los logros (para un menu de visualizacion). */
    public void mostrarLogros() {
        System.out.println();
        System.out.println("--- LOGROS ---");
        mostrarLinea("PRIMERA SANGRE",primeraSangre);
        mostrarLinea("VETERANO",veterano);
        mostrarLinea("IMBATIBLE",imbatible);
        mostrarLinea("POR LOS PELOS",porLosPelos);
        mostrarLinea("GOLPE CRITICO",golpeCritico);
        mostrarLinea("CARNICERO",carnicero);
        mostrarLinea("SOLO CUERPO A CUERPO",soloCuerpoACuerpo);
        mostrarLinea("MAGO EXPERIMENTADO", magoExperimentado);
        mostrarLinea("SANADOR",sanador);
        mostrarLinea("PIROMANO",piromano);
        mostrarLinea("RENOVADOR", renovador);
        mostrarLinea("GERALT IMPARABLE",geraltImparable);
        mostrarLinea("VENCER AL REY",vencerAlRey);
        System.out.println();
    }

    private void mostrarLinea(String nombre, boolean desbloqueado) {
        String marca;
        if (desbloqueado) {
            marca = "[X]";
        } else {
            marca = "[ ]";
        }
        System.out.println(marca + " " + nombre);
    }

   
}