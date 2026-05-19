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

public class PersistenciaPartida {

    // Crea un nuevo combate en la BD y registra los 6 personajes.
    // Devuelve el ID del combate generado.
    public static int nuevaPartida() {
        int idCombate = ConexionBD.ejecutarYObtenerId(
            "INSERT INTO COMBATE (turno, nRondas) VALUES (1, 0)", new ArrayList<>());

        for (int idPersonaje = 1; idPersonaje <= 6; idPersonaje++) {
            ConexionBD.ejecutar(
                "INSERT INTO COMBATE_PERSONAJE (ID_COMBATE, ID_personaje) VALUES (?, ?)",
                params(idCombate, idPersonaje));
        }

        System.out.println("[BD] Nueva partida creada con ID: " + idCombate);
        return idCombate;
    }

    // Guarda el estado actual de todos los personajes y la ronda en la BD.
    public static void guardarEstado(int idCombate, ArrayList<Personajes> equipoBueno,
                                     ArrayList<Personajes> equipoMalo, int ronda) {
        ArrayList<Personajes> todos = new ArrayList<>(equipoBueno);
        todos.addAll(equipoMalo);

        for (Personajes p : todos) {
            ConexionBD.ejecutar(
                "UPDATE PERSONAJES SET vidaActual=?, recursoActual=? WHERE ID_personaje=?",
                params(p.getVidaActual(), p.getRecursoActual(), p.getId()));

            guardarArma(p);
            guardarCooldowns(p);
            guardarEstados(p);
        }

        ConexionBD.ejecutar(
            "UPDATE COMBATE SET nRondas=? WHERE ID_COMBATE=?",
            params(ronda, idCombate));

        System.out.println("[BD] Partida guardada. Ronda: " + ronda);
    }

    // Actualiza qué arma está equipada del personaje en PERSONAJE_ARMA.
    private static void guardarArma(Personajes personaje) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT ID_ARMA FROM PERSONAJE_ARMA WHERE ID_personaje=? ORDER BY ID_ARMA",
            params(personaje.getId()));

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
                params(equipada, personaje.getId(), idArma));
        }
    }

    // Guarda los cooldowns actuales de los hechizos del personaje.
    private static void guardarCooldowns(Personajes personaje) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT ID_HECHIZO FROM PERSONAJE_HECHIZO WHERE ID_personaje=? ORDER BY ID_HECHIZO",
            params(personaje.getId()));

        ArrayList<Hechizos> hechizos = personaje.getHechizos();
        for (int i = 0; i < filas.size() && i < hechizos.size(); i++) {
            int idHechizo = ((Number) filas.get(i)[0]).intValue();
            int cd = personaje.getCooldown(hechizos.get(i).getNombre());
            ConexionBD.ejecutar(
                "UPDATE PERSONAJE_HECHIZO SET cooldownActual=? WHERE ID_personaje=? AND ID_HECHIZO=?",
                params(cd, personaje.getId(), idHechizo));
        }
    }

    // Borra los estados del personaje en BD y guarda los actuales.
    private static void guardarEstados(Personajes personaje) {
        ConexionBD.ejecutar(
            "DELETE FROM ESTADOS WHERE ID_personaje=?",
            params(personaje.getId()));

        for (Estados estado : personaje.getEstadosActivos()) {
            String tipo = estado.getTipo() == Estados.TipoEstado.DOT ? "DOT" : "HOT";
            int porSacerdote = 0;
            if (estado instanceof Renovar) {
                porSacerdote = ((Renovar) estado).isAplicadoPorSacerdote() ? 1 : 0;
            }
            ConexionBD.ejecutar(
                "INSERT INTO ESTADOS (ID_personaje, nombre, turnosRestantes, potenciaPorTurno, tipoEstado, aplicadoPorSacerdote) VALUES (?,?,?,?,?,?)",
                params(personaje.getId(), estado.getNombre(), estado.getTurnosRestantes(),
                       estado.getPotenciaPorTurno(), tipo, porSacerdote));
        }
    }

    // Carga el estado guardado de un combate desde la BD.
    // Devuelve null si no existe ese combate.
    public static EstadoPartida cargarEstado(int idCombate) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT nRondas FROM COMBATE WHERE ID_COMBATE=?", params(idCombate));

        if (filas.isEmpty()) {
            System.out.println("[BD] No existe el combate con ID: " + idCombate);
            return null;
        }

        int ronda = ((Number) filas.get(0)[0]).intValue();

        // Crear los personajes desde el catalogo (ya llevan armas y hechizos)
        ArrayList<Personajes> equipoBueno = new ArrayList<>();
        equipoBueno.add(CatalogoPersonajes.crearGeralt());
        equipoBueno.add(CatalogoPersonajes.crearYennefer());
        equipoBueno.add(CatalogoPersonajes.crearCiri());

        ArrayList<Personajes> equipoMalo = new ArrayList<>();
        equipoMalo.add(CatalogoPersonajes.crearImlerith());
        equipoMalo.add(CatalogoPersonajes.crearCaranthir());
        equipoMalo.add(CatalogoPersonajes.crearEredin());

        ArrayList<Personajes> todos = new ArrayList<>(equipoBueno);
        todos.addAll(equipoMalo);

        for (Personajes p : todos) {
            // Restaurar vida y recurso
            List<Object[]> stats = ConexionBD.consultar(
                "SELECT vidaActual, recursoActual FROM PERSONAJES WHERE ID_personaje=?",
                params(p.getId()));
            if (!stats.isEmpty()) {
                p.setVidaActual(((Number) stats.get(0)[0]).intValue());
                p.setRecursoActual(((Number) stats.get(0)[1]).intValue());
            }

            cargarArma(p);
            cargarCooldowns(p);
            cargarEstados(p);
        }

        System.out.println("[BD] Partida cargada. ID: " + idCombate + ", Ronda: " + ronda);
        return new EstadoPartida(equipoBueno, equipoMalo, ronda, idCombate);
    }

    // Equipa el arma que estaba guardada como equipada en PERSONAJE_ARMA.
    private static void cargarArma(Personajes personaje) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT equipada FROM PERSONAJE_ARMA WHERE ID_personaje=? ORDER BY ID_ARMA",
            params(personaje.getId()));

        ArrayList<Armas> disponibles = personaje.getArmasDisponibles();
        for (int i = 0; i < filas.size() && i < disponibles.size(); i++) {
            if (((Number) filas.get(i)[0]).intValue() == 1) {
                personaje.equiparArma(disponibles.get(i));
                break;
            }
        }
    }

    // Restaura los cooldowns de los hechizos desde la BD.
    private static void cargarCooldowns(Personajes personaje) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT cooldownActual FROM PERSONAJE_HECHIZO WHERE ID_personaje=? ORDER BY ID_HECHIZO",
            params(personaje.getId()));

        ArrayList<Hechizos> hechizos = personaje.getHechizos();
        for (int i = 0; i < filas.size() && i < hechizos.size(); i++) {
            int cd = ((Number) filas.get(i)[0]).intValue();
            if (cd > 0) {
                personaje.setCooldown(hechizos.get(i).getNombre(), cd);
            }
        }
    }

    // Restaura los estados activos desde la BD sin llamar a alAplicar.
    private static void cargarEstados(Personajes personaje) {
        List<Object[]> filas = ConexionBD.consultar(
            "SELECT nombre, turnosRestantes, potenciaPorTurno, aplicadoPorSacerdote FROM ESTADOS WHERE ID_personaje=?",
            params(personaje.getId()));

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

    // Marca el combate como finalizado con el resultado.
    public static void finalizarPartida(int idCombate, int rondas, String resumen) {
        ConexionBD.ejecutar(
            "UPDATE COMBATE SET nRondas=?, resumenFinal=? WHERE ID_COMBATE=?",
            params(rondas, resumen, idCombate));
        System.out.println("[BD] Partida " + idCombate + " finalizada. " + resumen);
    }

    // Muestra todas las partidas guardadas en consola.
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

    // Helper para crear la lista de parametros de forma limpia.
    private static List<Object> params(Object... valores) {
        List<Object> lista = new ArrayList<>();
        for (Object v : valores) lista.add(v);
        return lista;
    }
}
