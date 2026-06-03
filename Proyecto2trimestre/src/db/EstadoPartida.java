package db;

import java.util.ArrayList;
import personajes.Personajes;

/**
 * La clase EstadoPartida actúa como un contenedor de datos simple 
 * para almacenar el estado exacto de una partida guardada.
 * <p>
 * Es devuelto principalmente por la clase PersistenciaPartida al llamar a cargarEstado() 
 * para inyectar esta información en el motor de Combate y permitir 
 * que la batalla continúe exactamente en el mismo punto donde se dejó.
 * </p>
 */
public class EstadoPartida {

    /** Lista de personajes aliados (equipo de los brujos). */
    public ArrayList<Personajes> equipoBueno;
    
    /** Lista de personajes enemigos (La Cacería Salvaje). */
    public ArrayList<Personajes> equipoMalo;
    
    /** Número de la ronda en la que se guardó la partida. */
    public int rondaActual;
    
    /** Identificador único del combate en la base de datos. */
    public int idCombate;
    
    /** Identificador único del jugador propietario de la partida. */
    public int idJugador;

    /**
     * Constructor que inicializa todos los datos necesarios para restaurar una partida.
     * * @param equipoBueno Lista de personajes aliados instanciados y con sus estadísticas restauradas.
     * @param equipoMalo Lista de personajes enemigos instanciados y con sus estadísticas restauradas.
     * @param rondaActual El turno exacto por el que iba el combate en el momento de guardar.
     * @param idCombate El ID de este combate en la tabla de la base de datos.
     * @param idJugador El ID del cazador (jugador) que inició esta partida.
     */
    public EstadoPartida(ArrayList<Personajes> equipoBueno, ArrayList<Personajes> equipoMalo,
                         int rondaActual, int idCombate, int idJugador) {
        this.equipoBueno = equipoBueno;
        this.equipoMalo = equipoMalo;
        this.rondaActual = rondaActual;
        this.idCombate = idCombate;
        this.idJugador = idJugador;
    }
}