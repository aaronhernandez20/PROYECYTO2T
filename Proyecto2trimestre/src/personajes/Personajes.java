package personajes;

import java.util.ArrayList;

import armas.Armas;
import estados.Estados;
import hechizos.Hechizos;

public abstract class Personajes {
	protected String nombre;
	protected int vidaMax;
	protected int vidaActual;
	protected int recursoMax;
	protected int recursoActual;
	protected int ataqueBase;
	protected int defensaBase;
	protected int poderMagico;
	protected Armas armaEquipada;
	protected ArrayList <Estados> estadosActivos;
	protected ArrayList <Hechizos> hechizos;
	
		
	
}
