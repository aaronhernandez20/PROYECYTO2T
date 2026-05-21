package ranking;

import java.util.List;
import db.ConexionBD;

public class Ranking {

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
        if (jugadores.isEmpty()) {
            System.out.println("  No hay jugadores registrados todavia.");
        } else {
            System.out.printf("  %-5s %-20s %-12s %-10s%n", "Pos.", "Jugador", "Victorias", "Derrotas");
            System.out.println("  " + "-".repeat(50));
            for (int i = 0; i < jugadores.size(); i++) {
                String nombre    = (String) jugadores.get(i)[0];
                int victorias    = ((Number) jugadores.get(i)[1]).intValue();
                int derrotas     = ((Number) jugadores.get(i)[2]).intValue();
                System.out.printf("  %-5s %-20s %-12d %-10d%n",
                    "#" + (i + 1), nombre, victorias, derrotas);
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
        if (combates.isEmpty()) {
            System.out.println("  No hay combates finalizados todavia.");
        } else {
            for (int i = 0; i < combates.size(); i++) {
                String jugador  = combates.get(i)[0] != null ? (String) combates.get(i)[0] : "Desconocido";
                int rondas      = ((Number) combates.get(i)[1]).intValue();
                String resultado = (String) combates.get(i)[2];
                String fecha    = combates.get(i)[3].toString();
                System.out.println("  " + (i + 1) + ". [" + jugador + "] " + resultado
                    + " | " + rondas + " rondas | " + fecha);
            }
        }
        System.out.println();
    }
}
