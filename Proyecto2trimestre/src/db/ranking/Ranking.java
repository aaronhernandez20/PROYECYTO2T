package db.ranking;

import java.util.List;
import db.ConexionBD;

/**
 * La clase Ranking se encarga de consultar y visualizar las clasificaciones
 * globales del juego directamente desde la base de datos.
 * <p>
 * Muestra dos secciones principales por consola:
 * 1. La clasificación de jugadores (ordenada por número de victorias y derrotas).
 * 2. El historial de combates finalizados, detallando el jugador, rondas invertidas y la fecha.
 * </p>
 */
public class Ranking {

    /**
     * Consulta la base de datos y muestra por consola el ranking global y el historial.
     * Ejecuta dos consultas SQL a través de {@link ConexionBD}: una para obtener 
     * las estadísticas consolidadas de los jugadores y otra con un LEFT JOIN para 
     * listar los últimos combates registrados y finalizados.
     */
    public static void mostrarRanking() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                  RANKING GLOBAL                     ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        // --- Clasificacion de jugadores ---
        List<Object[]> jugadores = ConexionBD.consultar(
                "SELECT nombre, victorias, derrotas " +
                        "FROM jugadores " +
                        "ORDER BY victorias DESC, derrotas ASC");

        System.out.println("\n  --- Clasificacion de Jugadores ---");
        if (jugadores.size() == 0) {
            System.out.println("  No hay jugadores registrados todavia.");
        } else {
            System.out.println("  Pos. | Jugador | Victorias | Derrotas");
            System.out.println("  ------------------------------------------");
            for (int i = 0; i < jugadores.size(); i++) {
                String nombre = (String) jugadores.get(i)[0];
                int victorias = ((Number) jugadores.get(i)[1]).intValue();
                int derrotas = ((Number) jugadores.get(i)[2]).intValue();
                System.out.println("  #" + (i + 1) + " | " + nombre + " | " + victorias + " victorias | " + derrotas + " derrotas");
            }
        }

        // --- Historial de combates con nombre de jugador ---
        List<Object[]> combates = ConexionBD.consultar(
                "SELECT j.nombre, c.nRondas, c.resumenFinal, c.fechaGuardado " +
                        "FROM combate c " +
                        "LEFT JOIN jugadores j ON j.ID_jugador = c.ID_jugador " +
                        "WHERE c.resumenFinal IS NOT NULL " +
                        "ORDER BY c.fechaGuardado DESC");

        System.out.println("\n  --- Historial de Combates ---");
        if (combates.size() == 0) {
            System.out.println("  No hay combates finalizados todavia.");
        } else {
            for (int i = 0; i < combates.size(); i++) {
                String jugador = "Desconocido";
                if (combates.get(i)[0] != null) {
                    jugador = (String) combates.get(i)[0];
                }
                int rondas = ((Number) combates.get(i)[1]).intValue();
                String resultado = (String) combates.get(i)[2];
                String fecha = combates.get(i)[3].toString();
                System.out.println("  " + (i + 1) + ". [" + jugador + "] " + resultado
                        + " | " + rondas + " rondas | " + fecha);
            }
        }
        System.out.println();
    }
}