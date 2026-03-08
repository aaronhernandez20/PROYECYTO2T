package personajes;

import java.util.ArrayList;
import java.util.HashMap;
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
	protected ArrayList<Estados> estadosActivos;
	protected ArrayList<Hechizos> hechizos;

	protected Map<String, Integer> cooldowns;

	public Personajes(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico) {
		this.nombre = nombre;
		this.vidaMax = vidaMax;
		// la vida actual empieza al max, por eso se pone vidaMax en vidaActual
		this.vidaActual = vidaMax;
		this.recursoMax = recursoMax;
		// el recurso siempre empieza al maximo, por eso se pone el recursoMax en el
		// recurso Actual
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

	public void recibirDano(int cantidadDano) {
		// restamos el daño que recibe a la defensa
		int danoFinal = cantidadDano - this.defensaBase;
		if (danoFinal < 0) {
			// si el daño ya es menor que 0, que se quede en 0 y no siga en negativo
			danoFinal = 0;
		}
		this.vidaActual -= danoFinal;
		// la vida no puede bajar nunca de 0
		if (this.vidaActual < 0) {
			this.vidaActual = 0;
		}
		// se imprime cuanto daño y cuanta vida tiene el personaje
		System.out.println(this.nombre + " recibe " + danoFinal + " de daño. Vida restante: " + this.vidaActual);
	}

	public void equiparArma(Armas nuevaArma) {
		this.armaEquipada = nuevaArma;
	}

	public void curar(int cantidadVida) {
		this.vidaActual += cantidadVida;
		if (vidaActual > vidaMax) {
			vidaActual = vidaMax;
		}
		System.out.println("El personaje " + this.nombre + " se ha curado " + cantidadVida + " y tiene " + vidaActual
				+ " de vida actual ");
	}

	public boolean gastarRecurso(int coste) {
		if (this.recursoActual >= coste) {
			this.recursoActual -= coste;
			System.out.println("Este personaje: " + this.nombre + " gasta de recurso " + coste +
					" y le queda de recurso actual " + this.recursoActual + "/" + this.recursoMax);
			return true;
		} else {
			System.out.println("No tiene recursos suficientes");
			return false;
		}
	}

	// recibe el estado que se quiere aplicar al personaje
	public void aplicarEstados(Estados nuevoEstado) {
	}

	public void procesarEstados() {
	}

	// esta funcion es para que se muestre informacion del resumen del combate ya
	// sea daño vida etc.
	public void resumenCombate() {
	}

	// Getters

	public String getNombre() {
		return nombre;
	}

	public int getVidaActual() {
		return vidaActual;
	}

	public int getVidaMax() {
		return vidaMax;
	}

	public int getRecursoActual() {
		return recursoActual;
	}

	public int getRecursoMax() {
		return recursoMax;
	}

	public int getAtaqueBase() {
		return ataqueBase;
	}

	public int getDefensaBase() {
		return defensaBase;
	}

	public int getPoderMagico() {
		return poderMagico;
	}
		this.armaEquipada= nuevaArma;
	}

	public void curar(int cantidadVida) {
		this.vidaActual+=cantidadVida;
		if (vidaActual>vidaMax) {
			vidaActual=vidaMax;
		}
		System.out.println("El personaje " + this.nombre + " se ha curado " + cantidadVida + " y tiene " + vidaActual + " de vida actual ");
	}

	public boolean gastarRecurso(int coste) {
		if(this.recursoActual>=coste){
			this.recursoActual-=coste;
			System.out.println("Este personaje: "+ this.nombre+ " gasta de recurso " + coste+ 
					" y le queda de recurso actual " + this.recursoActual + "/"+ this.recursoMax);
			return true;
		}else {
			System.out.println("No tiene recursos suficientes");
			return false;
		}

	}

	public void aplicarEstados() {

	}

	public void procesarEstados() {

	}
	//esta funcin es para que se muestre informacion del resumen del combate ya sea daño vida etc.
	public void resumenCombate() {
		
	}

}
