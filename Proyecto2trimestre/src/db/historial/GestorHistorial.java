package db.historial;

import java.util.List;
import db.ConexionBD;

/**
 * La clase GestorHistorial se encarga de registrar en la base de datos 
 * las acciones más importantes que ocurren durante un combate.
 * <p>
 * Permite consultar a posteriori cómo fue evolucionando la partida, 
 * turno a turno, tanto de un combate específico como del registro global.
 * </p>
 */
public class GestorHistorial {

    /**
     * Guarda una acción o evento relevante en la tabla HISTORIAL de la base de datos.
     * * @param idCombate El identificador único del combate actual.
     * @param ronda El número de ronda en el que ocurre la acción.
     * @param accion Texto descriptivo de la acción (ej. "Geralt ataca a Imlerith").
     */
    public static void registrar(int idCombate, int ronda, String accion) {
        ConexionBD.ejecutar(
                "INSERT INTO HISTORIAL (ID_COMBATE, ronda, accion) VALUES (?, ?, ?)",
                ConexionBD.params(idCombate, ronda, accion));
    }

    /**
     * Muestra por consola el historial completo y detallado de un combate en concreto,
     * ordenado cronológicamente desde la primera acción hasta la última.
     * * @param idCombate El identificador del combate que se desea consultar.
     */
    public static void mostrarHistorialCombate(int idCombate) {
        List<Object[]> filas = ConexionBD.consultar(
                "SELECT ronda, accion, fecha FROM HISTORIAL WHERE ID_COMBATE = ? ORDER BY ID_HISTORIAL ASC",
                ConexionBD.params(idCombate));

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

    /**
     * Consulta la base de datos y muestra todos los combates que tienen 
     * acciones registradas, imprimiendo el historial de cada uno de ellos 
     * ordenados del combate más reciente al más antiguo.
     */
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