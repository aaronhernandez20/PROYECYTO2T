package personajes;

import java.util.ArrayList;
import java.util.Map;

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
	
	protected Map<String, Integer> cooldowns;
	
	
	public Personajes(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico) {
        this.nombre = nombre;
        this.vidaMax = vidaMax;
        // la vida actual empieza al max, por eso se pone vidaMax en vidaActual
        this.vidaActual = vidaMax; 
        this.recursoMax = recursoMax;
        //el recurso siempre empieza al maximo, por eso se pone el recursoMax en el recurso Actual
        this.recursoActual = recursoMax; 
        this.ataqueBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.poderMagico = poderMagico;		
		
        this.estadosActivos = new ArrayList<>();
        this.hechizos = new ArrayList<>();
        this.cooldowns = new HashMap<>();
	}
	public boolean estaVivo() {
        return this.vidaActual > 0;
    }
	public void recibirDanio(int cantidad) {
        // restamos el daño que recibe a la defensa
        int danioFinal = cantidad - this.defensaBase;
        if (danioFinal < 0) {
        	// si el daño ya es menor que 0, que se quede en 0 y no siga en negativo
            danioFinal = 0;
        }
        this.vidaActual -= danioFinal;
        // la vida no puede bajar nunca de 0
        if (this.vidaActual < 0) {
            this.vidaActual = 0;
        }
        System.out.println(this.nombre + " recibe " + danioFinal + " de daño. Vida restante: " + this.vidaActual);
    }
}
	
