package db;

import java.util.ArrayList;
import personajes.Personajes;

// Contenedor simple con el estado de una partida guardada.
// Lo devuelve PersistenciaPartida.cargarEstado() para que Combate pueda continuar.
public class EstadoPartida {

    public ArrayList<Personajes> equipoBueno;
    public ArrayList<Personajes> equipoMalo;
    public int rondaActual;
    public int idCombate;

    public EstadoPartida(ArrayList<Personajes> equipoBueno, ArrayList<Personajes> equipoMalo,
                         int rondaActual, int idCombate) {
        this.equipoBueno = equipoBueno;
        this.equipoMalo = equipoMalo;
        this.rondaActual = rondaActual;
        this.idCombate = idCombate;
    }
}
