package db;

import java.util.ArrayList;
import java.util.List;

import armas.Armas;
import catalogo.CatalogoPersonajes;
import estados.Estados;
import estados.Quemadura;
import estados.Renovar;
import estados.Veneno;
import hechizos.Hechizos;
import personajes.Personajes;

/**
 * La clase PersistenciaPartida es el núcleo de guardado y carga del juego.
 * <p>
 * Gestiona la inicialización segura de la base de datos (migraciones), 
 * el registro de jugadores, y la serialización y deserialización completa 
 * del estado de la partida (vida de personajes, armas equipadas, cooldowns 
 * de hechizos y estados alterados) contra las tablas de MySQL.
 * </p>
 */
public class PersistenciaPartida {

    /**
     * Crea las tablas y añade las columnas necesarias si no existen en la base de datos.
     * Actúa como un mecanismo de migración segura al iniciar la aplicación.
     */
    public static void inicializar() {
        ConexionBD.ejecutar(
            "CREATE TABLE IF NOT EXISTS jugadores (" +
            "ID_jugador INT AUTO_INCREMENT PRIMARY KEY, " +
            "nombre VARCHAR(100) NOT NULL, " +
            "victorias INT DEFAULT 0, " +
            "derrotas INT DEFAULT 0" +
            ")", new ArrayList<>());

        ConexionBD.ejecutar(
            "ALTER TABLE jugadores ADD COLUMN IF NOT EXISTS hechizosTotal INT DEFAULT 0",
            new ArrayList<>());
        ConexionBD.ejecutar(
            "ALTER TABLE jugadores ADD COLUMN IF NOT EXISTS curacionTotal INT DEFAULT 0",
            new ArrayList<>());
        ConexionBD.ejecutar(
            "ALTER TABLE jugadores ADD COLUMN IF NOT EXISTS quemadurasAplicadas INT DEFAULT 0",
            new ArrayList<>());
        ConexionBD.ejecutar(
            "ALTER TABLE jugadores ADD COLUMN IF NOT EXISTS renovaresPorSacerdote INT DEFAULT 0",
            new ArrayList<>());

        ConexionBD.ejecutar(
            "ALTER TABLE combate ADD COLUMN IF NOT EXISTS ID_jugador INT DEFAULT NULL",
            new ArrayList<>());
    }

    /**
     * Busca a un jugador por su nombre en la base de datos. Si no existe, lo registra.
     * * @param nombre El nombre del jugador (cazador).
     * @return El identificador único (ID) del jugador en la base de datos.
     */
    public static int obtenerOCrearJugador(String nombre) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT ID_jugador FROM jugadores WHERE nombre = ?", ConexionBD.params(nombre));
        if (!filas.isEmpty()) {
            int id = ((Number) filas.get(0)[0]).intValue();
            System.out.println("[BD] Bienvenido de nuevo, " + nombre + " (ID: " + id + ")");
            return id;
        }
        int id = ConexionBD.ejecutarYObtenerId(
            "INSERT INTO jugadores (nombre) VALUES (?)", ConexionBD.params(nombre));
        System.out.println("[BD] Nuevo jugador registrado: " + nombre + " (ID: " + id + ")");
        return id;
    }

    /**
     * Incrementa el contador global de victorias o derrotas del jugador tras finalizar un combate.
     * * @param idJugador El identificador del jugador.
     * @param victoria true si el jugador ganó el combate, false si perdió.
     */
    public static void actualizarEstadisticasJugador(int idJugador, boolean victoria) {
        if (idJugador <= 0) return;
        if (victoria) {
            ConexionBD.ejecutar(
                "UPDATE jugadores SET victorias = victorias + 1 WHERE ID_jugador = ?",
                ConexionBD.params(idJugador));
        } else {
            ConexionBD.ejecutar(
                "UPDATE jugadores SET derrotas = derrotas + 1 WHERE ID_jugador = ?",
                ConexionBD.params(idJugador));
        }
    }

    /**
     * Crea un nuevo registro de combate en la base de datos y asocia a los 6 personajes participantes.
     * * @param idJugador El identificador del jugador que inicia la partida.
     * @return El ID generado para el nuevo combate.
     */
    public static int nuevaPartida(int idJugador) {
        int idCombate;
        if (idJugador > 0) {
            idCombate = ConexionBD.ejecutarYObtenerId(
                "INSERT INTO COMBATE (turno, nRondas, ID_jugador) VALUES (1, 0, ?)",
                ConexionBD.params(idJugador));
        } else {
            idCombate = ConexionBD.ejecutarYObtenerId(
                "INSERT INTO COMBATE (turno, nRondas) VALUES (1, 0)", new ArrayList<>());
        }

        // Registra la participación de los 6 personajes fijos
        for (int idPersonaje = 1; idPersonaje <= 6; idPersonaje++) {
            ConexionBD.ejecutar(
                "INSERT INTO COMBATE_PERSONAJE (ID_COMBATE, ID_personaje) VALUES (?, ?)",
                ConexionBD.params(idCombate, idPersonaje));
        }

        System.out.println("[BD] Nueva partida creada con ID: " + idCombate);
        return idCombate;
    }

    /**
     * Guarda en la base de datos el estado exacto del combate actual para poder continuarlo más tarde.
     * * @param idCombate El identificador de la partida en curso.
     * @param equipoBueno Lista con el estado actual de los personajes aliados.
     * @param equipoMalo Lista con el estado actual de los personajes enemigos.
     * @param ronda El número de ronda en el que se solicita el guardado.
     */
    public static void guardarEstado(int idCombate, ArrayList<Personajes> equipoBueno,
                                     ArrayList<Personajes> equipoMalo, int ronda) {
        ArrayList<Personajes> todos = combinarEquipos(equipoBueno, equipoMalo);

        for (Personajes p : todos) {
            ConexionBD.ejecutar(
                "UPDATE PERSONAJES SET vidaActual=?, recursoActual=? WHERE ID_personaje=?",
                ConexionBD.params(p.getVidaActual(), p.getRecursoActual(), p.getId()));

            guardarArma(p);
            guardarCooldowns(p);
            guardarEstados(p);
        }

        ConexionBD.ejecutar(
            "UPDATE COMBATE SET nRondas=? WHERE ID_COMBATE=?",
            ConexionBD.params(ronda, idCombate));

        System.out.println("[BD] Partida guardada. Ronda: " + ronda);
    }

    /**
     * Actualiza en la tabla PERSONAJE_ARMA cuál de las armas disponibles tiene actualmente equipada el personaje.
     */
    private static void guardarArma(Personajes personaje) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT ID_ARMA FROM PERSONAJE_ARMA WHERE ID_personaje=? ORDER BY ID_ARMA",
            ConexionBD.params(personaje.getId()));

        // Buscar qué índice (0 o 1) tiene el arma equipada
        int indexEquipada = 0;
        ArrayList<Armas> disponibles = personaje.getArmasDisponibles();
        for (int i = 0; i < disponibles.size(); i++) {
            if (disponibles.get(i) == personaje.getArmaEquipada()) {
                indexEquipada = i;
                break;
            }
        }

        for (int i = 0; i < filas.size(); i++) {
            int idArma = ((Number) filas.get(i)[0]).intValue();
            int equipada = (i == indexEquipada) ? 1 : 0;
            ConexionBD.ejecutar(
                "UPDATE PERSONAJE_ARMA SET equipada=? WHERE ID_personaje=? AND ID_ARMA=?",
                ConexionBD.params(equipada, personaje.getId(), idArma));
        }
    }

    /**
     * Persiste los turnos de recarga (cooldowns) actuales de todos los hechizos del personaje.
     */
    private static void guardarCooldowns(Personajes personaje) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT ID_HECHIZO FROM PERSONAJE_HECHIZO WHERE ID_personaje=? ORDER BY ID_HECHIZO",
            ConexionBD.params(personaje.getId()));

        ArrayList<Hechizos> hechizos = personaje.getHechizos();
        for (int i = 0; i < filas.size() && i < hechizos.size(); i++) {
            int idHechizo = ((Number) filas.get(i)[0]).intValue();
            int cd = personaje.getCooldown(hechizos.get(i).getNombre());
            ConexionBD.ejecutar(
                "UPDATE PERSONAJE_HECHIZO SET cooldownActual=? WHERE ID_personaje=? AND ID_HECHIZO=?",
                ConexionBD.params(cd, personaje.getId(), idHechizo));
        }
    }

    /**
     * Borra los estados antiguos del personaje en la BD y registra los que tiene activos en este turno.
     */
    private static void guardarEstados(Personajes personaje) {
        ConexionBD.ejecutar(
            "DELETE FROM ESTADOS WHERE ID_personaje=?",
            ConexionBD.params(personaje.getId()));

        for (Estados estado : personaje.getEstadosActivos()) {
            String tipo = estado.getTipo() == Estados.TipoEstado.DOT ? "DOT" : "HOT";
            int porSacerdote = 0;
            if (estado instanceof Renovar) {
                porSacerdote = ((Renovar) estado).isAplicadoPorSacerdote() ? 1 : 0;
            }
            ConexionBD.ejecutar(
                "INSERT INTO ESTADOS (ID_personaje, nombre, turnosRestantes, potenciaPorTurno, tipoEstado, aplicadoPorSacerdote) VALUES (?,?,?,?,?,?)",
                ConexionBD.params(personaje.getId(), estado.getNombre(), estado.getTurnosRestantes(),
                       estado.getPotenciaPorTurno(), tipo, porSacerdote));
        }
    }

    /**
     * Restaura por completo una partida previamente guardada extrayendo sus datos de la base de datos.
     * * @param idCombate El identificador de la partida que se desea cargar.
     * @return Un objeto EstadoPartida con todos los datos listos para inyectar en el motor Combate, 
     * o null si no se encuentra la partida.
     */
    public static EstadoPartida cargarEstado(int idCombate) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT nRondas, ID_jugador FROM COMBATE WHERE ID_COMBATE=?", ConexionBD.params(idCombate));

        if (filas.isEmpty()) {
            System.out.println("[BD] No existe el combate con ID: " + idCombate);
            return null;
        }

        int ronda = ((Number) filas.get(0)[0]).intValue();
        int idJugador = filas.get(0)[1] != null ? ((Number) filas.get(0)[1]).intValue() : 0;

        // Crear los personajes desde el catalogo (ya llevan armas y hechizos)
        ArrayList<Personajes> equipoBueno = new ArrayList<>();
        equipoBueno.add(CatalogoPersonajes.crearGeralt());
        equipoBueno.add(CatalogoPersonajes.crearYennefer());
        equipoBueno.add(CatalogoPersonajes.crearCiri());

        ArrayList<Personajes> equipoMalo = new ArrayList<>();
        equipoMalo.add(CatalogoPersonajes.crearImlerith());
        equipoMalo.add(CatalogoPersonajes.crearCaranthir());
        equipoMalo.add(CatalogoPersonajes.crearEredin());

        ArrayList<Personajes> todos = combinarEquipos(equipoBueno, equipoMalo);

        for (Personajes p : todos) {
            // Restaurar vida y recurso
            List<Object[]> stats = ConexionBD.consultar(
                "SELECT vidaActual, recursoActual FROM PERSONAJES WHERE ID_personaje=?",
                ConexionBD.params(p.getId()));
            if (!stats.isEmpty()) {
                p.setVidaActual(((Number) stats.get(0)[0]).intValue());
                p.setRecursoActual(((Number) stats.get(0)[1]).intValue());
            }

            cargarArma(p);
            cargarCooldowns(p);
            cargarEstados(p);
        }

        System.out.println("[BD] Partida cargada. ID: " + idCombate + ", Ronda: " + ronda);
        return new EstadoPartida(equipoBueno, equipoMalo, ronda, idCombate, idJugador);
    }

    /**
     * Lee la base de datos y equipa al personaje con el arma que tenía en el momento de guardar.
     */
    private static void cargarArma(Personajes personaje) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT equipada FROM PERSONAJE_ARMA WHERE ID_personaje=? ORDER BY ID_ARMA",
            ConexionBD.params(personaje.getId()));

        ArrayList<Armas> disponibles = personaje.getArmasDisponibles();
        for (int i = 0; i < filas.size() && i < disponibles.size(); i++) {
            if (((Number) filas.get(i)[0]).intValue() == 1) {
                personaje.equiparArma(disponibles.get(i));
                break;
            }
        }
    }

    /**
     * Lee la base de datos y restaura los tiempos de recarga de los hechizos del personaje.
     */
    private static void cargarCooldowns(Personajes personaje) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT cooldownActual FROM PERSONAJE_HECHIZO WHERE ID_personaje=? ORDER BY ID_HECHIZO",
            ConexionBD.params(personaje.getId()));

        ArrayList<Hechizos> hechizos = personaje.getHechizos();
        for (int i = 0; i < filas.size() && i < hechizos.size(); i++) {
            int cd = ((Number) filas.get(i)[0]).intValue();
            if (cd > 0) {
                personaje.setCooldown(hechizos.get(i).getNombre(), cd);
            }
        }
    }

    /**
     * Reconstruye los objetos de los estados alterados (Quemadura, Renovar, Veneno) desde la BD
     * y los inyecta en el personaje sin ejecutar sus mecánicas iniciales (alAplicar).
     */
    private static void cargarEstados(Personajes personaje) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT nombre, turnosRestantes, potenciaPorTurno, aplicadoPorSacerdote FROM ESTADOS WHERE ID_personaje=?",
            ConexionBD.params(personaje.getId()));

        for (Object[] fila : filas) {
            String nombre = (String) fila[0];
            int turnos   = ((Number) fila[1]).intValue();
            int potencia = ((Number) fila[2]).intValue();
            boolean porSacerdote = ((Number) fila[3]).intValue() == 1;

            Estados estado;
            if (nombre.equals("Quemadura")) {
                estado = new Quemadura(potencia, turnos);
            } else if (nombre.equals("Renovar")) {
                estado = new Renovar(porSacerdote);
                estado.setTurnosRestantes(turnos);
            } else {
                estado = new Veneno(nombre, potencia, turnos);
            }
            personaje.getEstadosActivos().add(estado);
        }
    }

    /**
     * Actualiza el registro del combate en la base de datos para marcarlo como finalizado.
     * * @param idCombate El identificador de la partida.
     * @param rondas El número total de rondas que duró el enfrentamiento.
     * @param resumen Texto indicando el desenlace (ej. "Victoria de los Brujos").
     */
    public static void finalizarPartida(int idCombate, int rondas, String resumen) {
        ConexionBD.ejecutar(
            "UPDATE COMBATE SET nRondas=?, resumenFinal=? WHERE ID_COMBATE=?",
            ConexionBD.params(rondas, resumen, idCombate));
        System.out.println("[BD] Partida " + idCombate + " finalizada. " + resumen);
    }

    /**
     * Muestra por consola un listado con todas las partidas registradas en el sistema.
     */
    public static void listarPartidas() {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT ID_COMBATE, nRondas, resumenFinal, fechaGuardado FROM COMBATE ORDER BY fechaGuardado DESC");

        if (filas.isEmpty()) {
            System.out.println("  No hay partidas guardadas.");
            return;
        }
        System.out.println("  Partidas guardadas:");
        for (Object[] fila : filas) {
            int id      = ((Number) fila[0]).intValue();
            int rondas  = ((Number) fila[1]).intValue();
            String res  = fila[2] != null ? (String) fila[2] : "En progreso";
            String fecha = fila[3].toString();
            System.out.println("  [" + id + "] Ronda: " + rondas + " | " + res + " | " + fecha);
        }
    }

    /**
     * Elimina permanentemente una partida y toda su información vinculada de la base de datos.
     * * @param idCombate El identificador de la partida a borrar.
     */
    public static void borrarPartida(int idCombate) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT ID_COMBATE FROM COMBATE WHERE ID_COMBATE=?", ConexionBD.params(idCombate));
        if (filas.isEmpty()) {
            System.out.println("  No existe ninguna partida con ID " + idCombate + ".");
            return;
        }
        ConexionBD.ejecutar("DELETE FROM COMBATE_PERSONAJE WHERE ID_COMBATE=?", ConexionBD.params(idCombate));
        ConexionBD.ejecutar("DELETE FROM HISTORIAL WHERE ID_COMBATE=?", ConexionBD.params(idCombate));
        ConexionBD.ejecutar("DELETE FROM COMBATE WHERE ID_COMBATE=?", ConexionBD.params(idCombate));
        System.out.println("  Partida " + idCombate + " borrada correctamente.");
    }

    /**
     * Método auxiliar privado que fusiona las listas de ambos equipos en una sola.
     * Utilizado para iterar y procesar a todos los personajes simultáneamente.
     */
    private static ArrayList<Personajes> combinarEquipos(ArrayList<Personajes> equipoBueno, ArrayList<Personajes> equipoMalo) {
        ArrayList<Personajes> todos = new ArrayList<>(equipoBueno);
        todos.addAll(equipoMalo);
        return todos;
    }
}