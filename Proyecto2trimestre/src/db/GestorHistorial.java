package db;

import java.util.ArrayList;
import java.util.List;

// Registra en la BD las acciones importantes que ocurren durante un combate.
// Permite consultar despues como fue evolucionando la partida.
public class GestorHistorial {

    // Guarda una accion en la tabla HISTORIAL
    public static void registrar(int idCombate, int ronda, String accion) {
        List<Object> params = new ArrayList<>();
        params.add(idCombate);
        params.add(ronda);
        params.add(accion);
        ConexionBD.ejecutar(
            "INSERT INTO HISTORIAL (ID_COMBATE, ronda, accion) VALUES (?, ?, ?)", params);
    }

    // Muestra el historial completo de un combate concreto
    public static void mostrarHistorialCombate(int idCombate) {
        List<Object> params = new ArrayList<>();
        params.add(idCombate);
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT ronda, accion, fecha FROM HISTORIAL WHERE ID_COMBATE = ? ORDER BY ID_HISTORIAL ASC",
            params);

        System.out.println("\n=== HISTORIAL DEL COMBATE " + idCombate + " ===");

        if (filas.size() == 0) {
            System.out.println("No hay registros para este combate.");
            return;
        }

        for (int i = 0; i < filas.size(); i++) {
            int ronda = ((Number) filas.get(i)[0]).intValue();
            String accion = (String) filas.get(i)[1];
            System.out.println("  [Ronda " + ronda + "] " + accion);
        }
        System.out.println();
    }

    // Muestra todos los combates con sus acciones registradas
    public static void mostrarTodo() {
        List<Object[]> combates = ConexionBD.consultar(
            "SELECT DISTINCT ID_COMBATE FROM HISTORIAL ORDER BY ID_COMBATE DESC");

        System.out.println("\n=== HISTORIAL DE PARTIDAS ===");

        if (combates.size() == 0) {
            System.out.println("No hay historial registrado todavia.");
            return;
        }

        for (int i = 0; i < combates.size(); i++) {
            int idCombate = ((Number) combates.get(i)[0]).intValue();
            mostrarHistorialCombate(idCombate);
        }
    }
}
