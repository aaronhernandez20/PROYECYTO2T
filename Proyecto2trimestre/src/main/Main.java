package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import catalogo.CatalogoPersonajes;
import combate.Combate;
import db.ConexionBD;
import db.EstadoPartida;
import db.PersistenciaPartida;
import db.historial.GestorHistorial;
import db.logros.GestorLogros;
import db.ranking.Ranking;
import db.visualizacion.EstadisticasVisualizar;
import personajes.Personajes;

public class Main {

    public static GestorLogros logros = new GestorLogros();

    public static void main(String[] args) {
        PersistenciaPartida.inicializar();
        Scanner scanner = new Scanner(System.in);
        boolean seguir = true;

        while (seguir) {
            System.out.println("\n=== WITCHER RPG ===");
            System.out.println("1. Nueva partida");
            System.out.println("2. Cargar partida");
            System.out.println("3. Ver ranking");
            System.out.println("4. Ver historial de partidas");
            System.out.println("5. Ver estadisticas");
            System.out.println("6. Ver logros");
            System.out.println("7. Borrar partida");
            System.out.println("8. Salir");
            System.out.print("Elige una opcion: ");
            int opcion = scanner.nextInt();

            if (opcion == 8) {
                seguir = false;
                break;
            }

            if (opcion == 6) {
                GestorLogros.mostrarLogrosGlobal();
                continue;
            }

            if (opcion == 7) {
                PersistenciaPartida.listarPartidas();
                System.out.print("  Introduce el ID de la partida a borrar: ");
                int idBorrar = scanner.nextInt();
                PersistenciaPartida.borrarPartida(idBorrar);
                continue;
            }

            if (opcion == 3) {
                Ranking.mostrarRanking();
                continue;
            }

            if (opcion == 4) {
                GestorHistorial.mostrarTodo();
                continue;
            }

            if (opcion == 5) {
                EstadisticasVisualizar.mostrarVidaInicial();
                continue;
            }

            ArrayList<Personajes> equipoBueno;
            ArrayList<Personajes> equipoMalo;
            int idCombate = 0;
            int rondaInicio = 1;
            int idJugador = 0;

            if (opcion == 2) {
                List<Object[]> partidas = ConexionBD.consultar(
                    "SELECT ID_COMBATE FROM COMBATE WHERE resumenFinal IS NULL");

                if (partidas.size() == 0) {
                    System.out.println("No hay partidas guardadas. Iniciando nueva partida...");
                    scanner.nextLine();
                    idJugador = PersistenciaPartida.obtenerOCrearJugador(pedirNombre(scanner));
                    logros.setJugador(idJugador);
                    equipoBueno = crearEquipoBueno();
                    equipoMalo = crearEquipoMalo();
                } else {
                    PersistenciaPartida.listarPartidas();
                    System.out.print("Introduce el ID de la partida: ");
                    int idSeleccionado = scanner.nextInt();
                    EstadoPartida estado = PersistenciaPartida.cargarEstado(idSeleccionado);

                    if (estado == null) {
                        System.out.println("Partida no encontrada. Iniciando nueva partida...");
                        scanner.nextLine();
                        idJugador = PersistenciaPartida.obtenerOCrearJugador(pedirNombre(scanner));
                        logros.setJugador(idJugador);
                        equipoBueno = crearEquipoBueno();
                        equipoMalo = crearEquipoMalo();
                    } else {
                        idJugador = estado.idJugador;
                        logros.setJugador(idJugador);
                        equipoBueno = estado.equipoBueno;
                        equipoMalo = estado.equipoMalo;
                        idCombate = estado.idCombate;
                        rondaInicio = estado.rondaActual + 1;
                    }
                }
            } else {
                scanner.nextLine();
                idJugador = PersistenciaPartida.obtenerOCrearJugador(pedirNombre(scanner));
                logros.setJugador(idJugador);
                equipoBueno = crearEquipoBueno();
                equipoMalo = crearEquipoMalo();
            }

            System.out.println("Selecciona modo de juego:");
            System.out.println("1. Automatico");
            System.out.println("2. Manual (controlas el equipo de Geralt)");
            System.out.print("Elige una opcion: ");
            int modo = scanner.nextInt();
            boolean modoManual = (modo == 2);

            Combate combate;
            if (idCombate > 0) {
                combate = new Combate(equipoBueno, equipoMalo, modoManual, idCombate, rondaInicio, idJugador);
            } else {
                combate = new Combate(equipoBueno, equipoMalo, modoManual, idJugador);
            }

            combate.iniciar();
            logros.mostrarLogros();
        }

        scanner.close();
    }

    public static ArrayList<Personajes> crearEquipoBueno() {
        ArrayList<Personajes> equipoBueno = new ArrayList<>();

        Personajes geralt = CatalogoPersonajes.crearGeralt();
        asignarArmaAleatoria(geralt);
        equipoBueno.add(geralt);

        Personajes yennefer = CatalogoPersonajes.crearYennefer();
        asignarArmaAleatoria(yennefer);
        equipoBueno.add(yennefer);

        Personajes ciri = CatalogoPersonajes.crearCiri();
        asignarArmaAleatoria(ciri);
        equipoBueno.add(ciri);

        return equipoBueno;
    }

    public static ArrayList<Personajes> crearEquipoMalo() {
        ArrayList<Personajes> equipoMalo = new ArrayList<>();

        Personajes imlerith = CatalogoPersonajes.crearImlerith();
        asignarArmaAleatoria(imlerith);
        equipoMalo.add(imlerith);

        Personajes caranthir = CatalogoPersonajes.crearCaranthir();
        asignarArmaAleatoria(caranthir);
        equipoMalo.add(caranthir);

        Personajes eredin = CatalogoPersonajes.crearEredin();
        asignarArmaAleatoria(eredin);
        equipoMalo.add(eredin);

        return equipoMalo;
    }

    private static String pedirNombre(Scanner scanner) {
        while (true) {
            System.out.print("¿Como te llamas, Cazador? ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("  Error: el nombre no puede estar vacio.");
                continue;
            }
            if (nombre.length() > 20) {
                System.out.println("  Error: el nombre no puede tener mas de 20 letras.");
                continue;
            }
            if (!nombre.matches("[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ ]+")) {
                System.out.println("  Error: solo se permiten letras, sin numeros ni simbolos.");
                continue;
            }
            return nombre;
        }
    }

    private static void asignarArmaAleatoria(Personajes personaje) {
        if (Math.random() < 0.5) {
            personaje.equiparArma(personaje.getArmasDisponibles().get(0));
        } else {
            personaje.equiparArma(personaje.getArmasDisponibles().get(1));
        }
    }
}
