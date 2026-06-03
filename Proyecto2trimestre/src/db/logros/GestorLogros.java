package db.logros;

import java.util.List;
import db.ConexionBD;
import personajes.Personajes;

/**
 * La clase GestorLogros administra el sistema de progresión y recompensas del jugador.
 * <p>
 * Se encarga de monitorizar eventos durante el combate, llevar el conteo de estadísticas 
 * globales (guardadas en la base de datos) y verificar las condiciones para desbloquear 
 * diversos logros tanto acumulativos como específicos de una sola batalla.
 * </p>
 */
public class GestorLogros {

    private int idJugador = 0;

    // Contadores acumulativos (se guardan en BD por jugador)
    private int combatesGanados = 0;
    private int hechizosTotal = 0;
    private int curacionTotal = 0;
    private int quemadurasAplicadas = 0;
    private int renovaresPorSacerdote = 0;

    // Contadores que se resetean al final de cada combate
    private int hechizosEsteCombate = 0;
    private boolean geraltSinDano = true;

    // Logros desbloqueados
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

    /**
     * Carga el estado del jugador desde la base de datos al inicio de cada partida.
     * Recupera tanto los logros que ya ha desbloqueado previamente como sus 
     * contadores estadísticos acumulados.
     * * @param id El identificador único del jugador en la base de datos.
     */
    public void setJugador(int id) {
        this.idJugador = id;
        resetearEstadoCombate();
        if (id <= 0)
            return;

        // Cargar logros ya desbloqueados por ese jugador
        List<Object[]> filas = ConexionBD.consultar(
                "SELECT l.nombre FROM logros_jugador lj " +
                "JOIN logros l ON l.ID_logro = lj.ID_logro " +
                "WHERE lj.ID_jugador = ?", ConexionBD.params(id));
        // marca logro conseguido, reemplaza la barra baja
        for (Object[] fila : filas) {
            activarLogro(((String) fila[0]).replace("_", " "));
        }

        // Cargar contadores acumulados
        List<Object[]> stats = ConexionBD.consultar(
                "SELECT victorias, hechizosTotal, curacionTotal, quemadurasAplicadas, renovaresPorSacerdote " +
                "FROM jugadores WHERE ID_jugador = ?",
                ConexionBD.params(id));
        if (!stats.isEmpty()) {
            combatesGanados = ((Number) stats.get(0)[0]).intValue();
            hechizosTotal = ((Number) stats.get(0)[1]).intValue();
            curacionTotal = ((Number) stats.get(0)[2]).intValue();
            quemadurasAplicadas = ((Number) stats.get(0)[3]).intValue();
            renovaresPorSacerdote = ((Number) stats.get(0)[4]).intValue();
        }
    }

    // ================= METODOS PARA REGISTRAR EVENTOS DEL JUEGO =================

    /**
     * Registra la ejecución de un golpe crítico en combate.
     * Desbloquea el logro "GOLPE CRITICO" si es la primera vez.
     */
    public void registrarCritico() {
        if (!golpeCritico) {
            golpeCritico = true;
            mostrarMensaje("GOLPE CRITICO", "Has asestado tu primer golpe critico.");
        }
    }

    /**
     * Evalúa el daño infligido en un solo ataque.
     * Desbloquea el logro "CARNICERO" si el daño alcanza o supera los 500 puntos.
     * * @param dano La cantidad de daño físico o mágico infligido.
     */
    public void registrarDanoInfligido(int dano) {
        if (dano >= 500 && !carnicero) {
            carnicero = true;
            mostrarMensaje("CARNICERO", "Has infligido 500 o mas de dano en un solo ataque.");
        }
    }

    /**
     * Aumenta los contadores de hechizos lanzados (globales y del combate actual).
     * Desbloquea el logro "MAGO EXPERIMENTADO" al alcanzar los 50 hechizos en total.
     */
    public void registrarHechizoLanzado() {
        hechizosTotal++;
        hechizosEsteCombate++;
        if (hechizosTotal >= 50 && !magoExperimentado) {
            magoExperimentado = true;
            mostrarMensaje("MAGO EXPERIMENTADO", "Has lanzado 50 hechizos en total.");
        }
    }

    /**
     * Suma la cantidad de puntos de vida restaurados al contador global.
     * Desbloquea el logro "SANADOR" al sanar 1000 HP o más en total.
     * * @param cantidad Puntos de salud restaurados.
     */
    public void registrarCuracion(int cantidad) {
        curacionTotal += cantidad;
        if (curacionTotal >= 1000 && !sanador) {
            sanador = true;
            mostrarMensaje("SANADOR", "Has curado 1000 HP en total.");
        }
    }

    /**
     * Registra la aplicación del estado "Quemadura".
     * Desbloquea el logro "PIROMANO" al aplicarlo 20 veces a lo largo de las partidas.
     */
    public void registrarQuemaduraAplicada() {
        quemadurasAplicadas++;
        if (quemadurasAplicadas >= 20 && !piromano) {
            piromano = true;
            mostrarMensaje("PIROMANO", "Has aplicado Quemadura 20 veces.");
        }
    }

    /**
     * Registra la aplicación potenciada del estado "Renovar" por parte de la clase Sacerdote.
     * Desbloquea el logro "RENOVADOR" al realizar esta acción 10 veces.
     */
    public void registrarRenovarPorSacerdote() {
        renovaresPorSacerdote++;
        if (renovaresPorSacerdote >= 10 && !renovador) {
            renovador = true;
            mostrarMensaje("RENOVADOR", "Has aplicado Renovar con un Sacerdote 10 veces.");
        }
    }

    /**
     * Invalida el logro de "GERALT IMPARABLE" para el combate actual 
     * al registrar que el personaje Geralt ha recibido daño.
     */
    public void registrarDanoAGeralt() {
        geraltSinDano = false;
    }

    /**
     * Verifica si el enemigo derrotado desencadena un logro específico.
     * Desbloquea el logro "VENCER AL REY" al derrotar a Eredin.
     * * @param enemigo El personaje enemigo que acaba de morir.
     */
    public void registrarMuerteEnemigo(Personajes enemigo) {
        if (enemigo.getNombre().equalsIgnoreCase("Eredin") && !vencerAlRey) {
            vencerAlRey = true;
            mostrarMensaje("VENCER AL REY", "Has derrotado a Eredin.");
        }
    }

    /**
     * Evalúa el final de un combate para determinar si se cumplen las condiciones 
     * de los logros condicionales (supervivencia, uso de habilidades, etc.).
     * Posteriormente, guarda los contadores actualizados en la base de datos.
     * * @param ganado true si el equipo aliado ha ganado, false si ha sido derrotado.
     * @param equipoJugador Array con los personajes aliados para evaluar su estado.
     */
    public void comprobarFinCombate(boolean ganado, Personajes[] equipoJugador) {
        if (!ganado) {
            resetearEstadoCombate();
            guardarContadores();
            return;
        }

        combatesGanados++;

        if (combatesGanados == 1 && !primeraSangre) {
            primeraSangre = true;
            mostrarMensaje("PRIMERA SANGRE", "Has ganado tu primer combate.");
        }

        if (combatesGanados >= 10 && !veterano) {
            veterano = true;
            mostrarMensaje("VETERANO", "Has ganado 10 combates.");
        }

        boolean todosVivos = true;
        boolean alguienBajo = false;
        for (Personajes p : equipoJugador) {
            if (!p.estaVivo())
                todosVivos = false;
            if (p.estaVivo() && p.getVidaActual() < p.getVidaMax() * 0.1)
                alguienBajo = true;
        }

        if (todosVivos && !imbatible) {
            imbatible = true;
            mostrarMensaje("IMBATIBLE", "Has ganado sin que muera ningun aliado.");
        }
        if (alguienBajo && !porLosPelos) {
            porLosPelos = true;
            mostrarMensaje("POR LOS PELOS", "Has ganado con un aliado por debajo del 10% de vida.");
        }
        if (hechizosEsteCombate == 0 && !soloCuerpoACuerpo) {
            soloCuerpoACuerpo = true;
            mostrarMensaje("SOLO CUERPO A CUERPO", "Has ganado un combate sin lanzar hechizos.");
        }
        if (geraltSinDano && !geraltImparable) {
            geraltImparable = true;
            mostrarMensaje("GERALT IMPARABLE", "Has ganado un combate sin que Geralt reciba dano.");
        }

        resetearEstadoCombate();
        guardarContadores();
    }

    /**
     * Imprime por consola el estado actual (desbloqueado/bloqueado) de todos 
     * los logros del jugador activo en la sesión.
     */
    public void mostrarLogros() {
        System.out.println("\n--- LOGROS ---");
        mostrarLinea("PRIMERA SANGRE", primeraSangre);
        mostrarLinea("VETERANO", veterano);
        mostrarLinea("IMBATIBLE", imbatible);
        mostrarLinea("POR LOS PELOS", porLosPelos);
        mostrarLinea("GOLPE CRITICO", golpeCritico);
        mostrarLinea("CARNICERO", carnicero);
        mostrarLinea("SOLO CUERPO A CUERPO", soloCuerpoACuerpo);
        mostrarLinea("MAGO EXPERIMENTADO", magoExperimentado);
        mostrarLinea("SANADOR", sanador);
        mostrarLinea("PIROMANO", piromano);
        mostrarLinea("RENOVADOR", renovador);
        mostrarLinea("GERALT IMPARABLE", geraltImparable);
        mostrarLinea("VENCER AL REY", vencerAlRey);
        System.out.println();
    }

    /**
     * Consulta la base de datos y muestra una lista global con todos los jugadores 
     * registrados y los logros que ha conseguido cada uno de ellos.
     */
    public static void mostrarLogrosGlobal() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                    LOGROS                            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        List<Object[]> jugadores = ConexionBD.consultar(
                "SELECT ID_jugador, nombre FROM jugadores ORDER BY nombre");

        if (jugadores.isEmpty()) {
            System.out.println("  No hay jugadores registrados todavia.");
            return;
        }

        for (Object[] fila : jugadores) {
            int id = ((Number) fila[0]).intValue();
            String nom = (String) fila[1];

            List<Object[]> logros = ConexionBD.consultar(
                    "SELECT l.nombre FROM logros_jugador lj " +
                    "JOIN logros l ON l.ID_logro = lj.ID_logro " +
                    "WHERE lj.ID_jugador = ? ORDER BY l.ID_logro",
                    ConexionBD.params(id));

            System.out.println("\n  [" + nom + "]  " + logros.size() + "/13 logros");
            System.out.println("  " + "-".repeat(40));
            if (logros.isEmpty()) {
                System.out.println("  Aun no ha desbloqueado ningun logro.");
            } else {
                for (Object[] l : logros) {
                    String nombreMostrar = ((String) l[0]).replace("_", " ");
                    System.out.println("  [X] " + nombreMostrar);
                }
            }
        }
        System.out.println();
    }

    // ================= PRIVADOS =================

    /**
     * Activa internamente la variable booleana correspondiente a un logro al cargarlo desde la BD.
     * * @param nombre El nombre del logro a activar.
     */
    private void activarLogro(String nombre) {
        switch (nombre) {
            case "PRIMERA SANGRE":
                primeraSangre = true;
                break;
            case "VETERANO":
                veterano = true;
                break;
            case "IMBATIBLE":
                imbatible = true;
                break;
            case "POR LOS PELOS":
                porLosPelos = true;
                break;
            case "GOLPE CRITICO":
                golpeCritico = true;
                break;
            case "CARNICERO":
                carnicero = true;
                break;
            case "SOLO CUERPO A CUERPO":
                soloCuerpoACuerpo = true;
                break;
            case "MAGO EXPERIMENTADO":
                magoExperimentado = true;
                break;
            case "SANADOR":
                sanador = true;
                break;
            case "PIROMANO":
                piromano = true;
                break;
            case "RENOVADOR":
                renovador = true;
                break;
            case "GERALT IMPARABLE":
                geraltImparable = true;
                break;
            case "VENCER AL REY":
                vencerAlRey = true;
                break;
        }
    }

    /**
     * Persiste en la base de datos la obtención de un nuevo logro para el jugador actual.
     * * @param nombre El nombre del logro recién conseguido.
     */
    private void guardarLogro(String nombre) {
        if (idJugador <= 0)
            return;
        String nombreBD = nombre.replace(" ", "_");
        ConexionBD.ejecutar(
                "INSERT IGNORE INTO logros_jugador (ID_jugador, ID_logro) " +
                "SELECT ?, ID_logro FROM logros WHERE nombre = ?",
                ConexionBD.params(idJugador, nombreBD));
    }

    /**
     * Actualiza los valores de los contadores estadísticos del jugador en la base de datos.
     */
    private void guardarContadores() {
        if (idJugador <= 0)
            return;
        ConexionBD.ejecutar(
                "UPDATE jugadores SET hechizosTotal=?, curacionTotal=?, quemadurasAplicadas=?, renovaresPorSacerdote=? " +
                "WHERE ID_jugador=?",
                ConexionBD.params(hechizosTotal, curacionTotal, quemadurasAplicadas, renovaresPorSacerdote, idJugador));
    }

    /**
     * Reinicia a sus valores por defecto los contadores y flags que solo 
     * tienen validez dentro de la duración de un único combate.
     */
    private void resetearEstadoCombate() {
        hechizosEsteCombate = 0;
        geraltSinDano = true;
    }

    /**
     * Muestra un banner decorativo por consola avisando al usuario de que 
     * ha desbloqueado un nuevo logro e inicia el proceso para guardarlo.
     * * @param nombre El título del logro.
     * @param descripcion Los detalles o requisitos que acaba de cumplir.
     */
    private void mostrarMensaje(String nombre, String descripcion) {
        System.out.println();
        System.out.println("================================================");
        System.out.println("    LOGRO DESBLOQUEADO: " + nombre);
        System.out.println("    " + descripcion);
        System.out.println("================================================");
        System.out.println();
        guardarLogro(nombre);
    }

    /**
     * Formatea e imprime una línea indicando si un logro específico está o no desbloqueado.
     * * @param nombre El nombre del logro.
     * @param desbloqueado El estado actual del logro.
     */
    private void mostrarLinea(String nombre, boolean desbloqueado) {
        System.out.println((desbloqueado ? "[X]" : "[ ]") + " " + nombre);
    }

}