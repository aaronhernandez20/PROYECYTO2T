package ranking;

import java.util.List;
import db.ConexionBD;

public class Ranking {

	// Muestra las partidas terminadas ordenadas por numero de rondas
	public static void mostrarRanking() {
		List<Object[]> filas = ConexionBD.consultar(
			"SELECT ID_COMBATE, nRondas, resumenFinal, fechaGuardado " +
			"FROM COMBATE " +
			"WHERE resumenFinal IS NOT NULL " +
			"ORDER BY nRondas ASC");

		System.out.println("\n=== RANKING GLOBAL ===");

		if (filas.size() == 0) {
			System.out.println("No hay partidas finalizadas todavia.");
			return;
		}

		for (int i = 0; i < filas.size(); i++) {
			Object[] fila = filas.get(i);
			int id = ((Number) fila[0]).intValue();
			int rondas = ((Number) fila[1]).intValue();
			String resultado = (String) fila[2];

			System.out.println((i + 1) + ". [ID " + id + "] " + resultado + " - " + rondas + " rondas (" + fila[3] + ")");
		}

		// Contar victorias de cada equipo
		int victoriasGeralt = 0;
		int victoriasCaceria = 0;
		for (Object[] fila : filas) {
			String resultado = (String) fila[2];
			if (resultado.contains("Geralt")) {
				victoriasGeralt++;
			} else {
				victoriasCaceria++;
			}
		}

		System.out.println("----------------------------------------------------------");
		System.out.println("Victorias de Geralt: " + victoriasGeralt);
		System.out.println("Victorias de la Caceria Salvaje: " + victoriasCaceria);
		System.out.println();
	}
}
