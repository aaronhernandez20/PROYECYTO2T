package personajes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import armas.Armas;
import estados.Estados;
import hechizos.Hechizos;

// clase padre de la que heredan Guerrero, Mago y Sacerdote
// es abstracta porque no se puede crear un personaje generico, siempre sera uno de los 3
public abstract class Personajes {

	// nombre del personaje que se muestra en el combate
	protected String nombre;

	// vida maxima y vida actual del personaje
	protected int vidaMax;
	protected int vidaActual;

	// recurso que se gasta al usar hechizos (mana, vigor, etc)
	protected int recursoMax;
	protected int recursoActual;

	// estadisticas base que afectan al daño y la defensa
	protected int ataqueBase;
	protected int defensaBase;

	// poder magico que usan los magos para calcular el daño de sus hechizos
	protected int poderMagico;

	// arma que lleva equipada el personaje, afecta al calculo del daño
	protected Armas armaEquipada;

	// lista de estados activos del personaje (quemadura, veneno, renovar...)
	protected ArrayList<Estados> estadosActivos;

	// lista de hechizos que puede usar el personaje
	protected ArrayList<Hechizos> hechizos;

	// mapa para guardar los tiempos de recarga de los hechizos
	// clave: nombre del hechizo, valor: turnos que le quedan de cooldown
	protected Map<String, Integer> cooldowns;

	// constructor base, todos los personajes nacen con estos datos
	public Personajes(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico) {
		this.nombre = nombre;
		this.vidaMax = vidaMax;
		// la vida actual empieza al max, por eso se pone vidaMax en vidaActual
		this.vidaActual = vidaMax;
		this.recursoMax = recursoMax;
		// el recurso siempre empieza al maximo, por eso se pone el recursoMax en el recursoActual
		this.recursoActual = recursoMax;
		this.ataqueBase = ataqueBase;
		this.defensaBase = defensaBase;
		this.poderMagico = poderMagico;

		// las listas y el mapa nacen vacios, se van llenando durante el juego
		this.estadosActivos = new ArrayList<>();
		this.hechizos = new ArrayList<>();
		this.cooldowns = new HashMap<>();
	}

	// devuelve true si el personaje sigue vivo, false si ha muerto
	public boolean estaVivo() {
		return this.vidaActual > 0;
	}

	// resta el daño recibido teniendo en cuenta la defensa del personaje
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

	// asigna un arma al personaje, cambia el arma que tenia antes
	public void equiparArma(Armas nuevaArma) {
		this.armaEquipada = nuevaArma;
	}

	// suma vida al personaje sin que pueda superar la vida maxima
	public void curar(int cantidadVida) {
		this.vidaActual += cantidadVida;
		// la vida no puede superar el maximo
		if (vidaActual > vidaMax) {
			vidaActual = vidaMax;
		}
		System.out.println("El personaje " + this.nombre + " se ha curado " + cantidadVida + " y tiene " + vidaActual
				+ " de vida actual ");
	}

	// comprueba si tiene suficiente recurso y si puede lo gasta, devuelve false si no puede
	public boolean gastarRecurso(int coste) {
		if (this.recursoActual >= coste) {
			this.recursoActual -= coste;
			System.out.println("Este personaje: " + this.nombre + " gasta de recurso " + coste
					+ " y le queda de recurso actual " + this.recursoActual + "/" + this.recursoMax);
			return true;
		} else {
			System.out.println("No tiene recursos suficientes");
			return false;
		}
	}

	// recibe el estado que se quiere aplicar al personaje
	// si ya tiene ese estado lo renueva, si no lo tiene lo añade a la lista
	public void aplicarEstados(Estados nuevoEstado) {
	    for (int i = 0; i < estadosActivos.size(); i++) {
	        if (estadosActivos.get(i).getNombre().equals(nuevoEstado.getNombre())) {
	            // si ya existe ese estado lo renueva
	            estadosActivos.get(i).renovarDuracion(nuevoEstado.getTurnosRestantes());
	            return;
	        }
	    }
	    // si no existe lo añade
	    estadosActivos.add(nuevoEstado);
	    nuevoEstado.alAplicar(this);
	}

	// recorre todos los estados activos del personaje en sentido inverso es decir al reves y aplica su efecto
	// al final borra los estados que ya hayan expirado
	public void procesarEstados() {
	    for (int i = estadosActivos.size() - 1; i >= 0; i--) {
	        Estados estado = estadosActivos.get(i);
	        // aplica el efecto del estado
	        estado.alProcesarTurno(this);
	        // reduce la duracion
	        estado.reducirDuracion();
	        // si ha expirado lo elimina
	        if (!estado.estaActivo()) {
	            estado.alExpirar(this);
	            estadosActivos.remove(i);
	        }
	    }
	}
	// esta funcion es para que se muestre informacion del resumen del combate ya sea daño vida etc.
	public void resumenCombate() {
		System.out.println("RESUMEN DEL COMBATE:");
		System.out.println(this.nombre);
		System.out.println("Vida: " + this.vidaActual + "/" + this.vidaMax);
		System.out.println("Recurso: " + this.recursoActual + "/" + this.recursoMax);
		System.out.println("Arma equipada: " + this.armaEquipada);
	}

	// polimorfismo: cada subclase decide como actua en su turno
	// el guerrero atacara con su espada, el mago lanzara hechizos, el sacerdote curara
	public abstract void realizarAccion(Personajes objetivo);

	// Getters: devuelven los atributos privados para que otras clases puedan leerlos
	
	// las dos armas disponibles del personaje para elegir
	protected ArrayList<Armas> armasDisponibles = new ArrayList<>();

	// permite añadir un arma al listado de opciones
	public void agregarArmaDisponible(Armas arma) {
	    this.armasDisponibles.add(arma);
	}

	// devuelve la lista de armas disponibles
	public ArrayList<Armas> getArmasDisponibles() {
	    return armasDisponibles;
	}

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
}	





	
