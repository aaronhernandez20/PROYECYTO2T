/**
 * Contiene las clases relacionadas con la creación y gestión de las entidades del juego,
 * tanto la clase base como los roles específicos (Mago, Guerrero, Sacerdote).
 */
package personajes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import armas.Armas;
import estados.Estados;
import hechizos.Hechizos;



// Clase padre de la que heredan Guerrero, Mago y Sacerdote.
// Es abstracta porque no se puede crear un personaje generico, siempre sera uno de los 3.
public abstract class Personajes {

	// ID que corresponde al registro de este personaje en la base de datos
	protected int id;

	// Nombre del personaje que se muestra en el combate
	protected String nombre;

	// Vida maxima y vida actual del personaje
	protected int vidaMax;
	protected int vidaActual;

	// Recurso que se gasta al usar hechizos (mana, vigor, etc)
	protected int recursoMax;
	protected int recursoActual;

	// Estadisticas base que afectan al daño y la defensa
	protected int ataqueBase;
	protected int defensaBase;

	// Poder magico que usan los magos para calcular el daño de sus hechizos
	protected int poderMagico;

	// Arma que lleva equipada el personaje, afecta al calculo del daño
	protected Armas armaEquipada;

	// ArrayList con las 2 armas disponibles del personaje para elegir al inicio.
	// Lo usa CatalogoPersonajes para registrar las armas y Main para equipar una al
	// azar.
	protected ArrayList<Armas> armasDisponibles;

	// ArrayList de estados activos del personaje (quemadura, veneno, renovar...)
	protected ArrayList<Estados> estadosActivos;

	// ArrayList de hechizos que puede usar el personaje.
	protected ArrayList<Hechizos> hechizos;

	// Mapa para guardar los tiempos de recarga de los hechizos.
	// Clave: nombre del hechizo, Valor: turnos que le quedan de cooldown.
	protected Map<String, Integer> cooldowns;

	/**
	 * Constructor base para crear un personaje.
	 * * @param nombre El nombre del personaje que se muestra en el combate.
	 * @param vidaMax La cantidad de vida máxima que puede tener el personaje.
	 * @param recursoMax La cantidad máxima del recurso (maná, vigor, etc.) para usar hechizos.
	 * @param ataqueBase Estadística de ataque base.
	 * @param defensaBase Estadística de defensa base que reduce el daño físico.
	 * @param poderMagico Poder mágico utilizado para calcular el daño o curación de los hechizos.
	 */
	// Constructor base, todos los personajes nacen con estos datos
	public Personajes(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico) {
		this.nombre = nombre;
		this.vidaMax = vidaMax;
		// La vida actual empieza al maximo
		this.vidaActual = vidaMax;
		this.recursoMax = recursoMax;
		// El recurso siempre empieza al maximo
		this.recursoActual = recursoMax;
		this.ataqueBase = ataqueBase;
		this.defensaBase = defensaBase;
		this.poderMagico = poderMagico;

		// Las listas y el mapa nacen vacios, se van llenando durante el juego
		this.armasDisponibles = new ArrayList<>();
		this.estadosActivos = new ArrayList<>();
		this.hechizos = new ArrayList<>();
		this.cooldowns = new HashMap<>();
	}

	// METODOS DE VIDA Y RECURSO
	/**
	 * Comprueba si el personaje sigue vivo.
	 * * @return true si la vida actual es mayor que 0, false en caso contrario (ha muerto).
	 */
	// Devuelve true si el personaje sigue vivo, false si ha muerto
	public boolean estaVivo() {
		return this.vidaActual > 0;
	}
	/**
	 * Resta el daño recibido teniendo en cuenta la defensa base del personaje.
	 * * @param cantidadDano La cantidad de daño físico entrante antes de aplicar la defensa.
	 */
	// Resta el daño recibido teniendo en cuenta la defensa del personaje.
	// La defensa absorbe parte del daño antes de que llegue a la vida.
	public void recibirDano(int cantidadDano) {
		if (this.nombre.equalsIgnoreCase("Geralt de Rivia")) {
	        main.Main.logros.registrarDanoAGeralt();
	    }

		int danoFinal = cantidadDano - this.defensaBase;
		// Si la defensa supera el daño, el daño queda en 0 (no cura)
		if (danoFinal < 0) {
			danoFinal = 0;
		}
		this.vidaActual -= danoFinal;
		// La vida no puede bajar nunca de 0
		if (this.vidaActual < 0) {
			this.vidaActual = 0;
		}
		System.out.println(this.nombre + " recibe " + danoFinal + " de daño. ("
				+ this.vidaActual + "/" + this.vidaMax + " HP)");
	}
	/**
	 * Resta el daño mágico recibido, ignorando completamente la defensa base.
	 * * @param cantidadDano La cantidad de daño mágico entrante directo a la vida.
	 */
	// Recibe daño magico: ignora completamente la defensa del personaje.
	public void recibirDanoMagico(int cantidadDano) {
		if (this.nombre.equalsIgnoreCase("Geralt de Rivia")) {
	        main.Main.logros.registrarDanoAGeralt();
	    }

		this.vidaActual -= cantidadDano;
		// La vida no puede bajar nunca de 0
		if (this.vidaActual < 0) {
			this.vidaActual = 0;
		}
		System.out.println(this.nombre + " recibe " + cantidadDano + " de daño MAGICO (ignora defensa). ("
				+ this.vidaActual + "/" + this.vidaMax + " HP)");
	}
	/**
	 * Cura al personaje, incrementando su vida sin superar el límite de vida máxima.
	 * * @param cantidadVida La cantidad de puntos de vida a recuperar.
	 */
	// Suma vida al personaje sin que pueda superar la vida maxima
	public void curar(int cantidadVida) {
		this.vidaActual += cantidadVida;
		// La vida no puede superar el maximo
		if (this.vidaActual > this.vidaMax) {
			this.vidaActual = this.vidaMax;
		}
		System.out.println(this.nombre + " se cura " + cantidadVida + " HP. ("
				+ this.vidaActual + "/" + this.vidaMax + " HP)");
	}
	/**
	 * Intenta gastar el recurso del personaje para realizar una acción.
	 * * @param coste La cantidad de recurso que requiere la acción.
	 * @return true si se pudo gastar el recurso, false si no había suficiente.
	 */
	// Comprueba si tiene suficiente recurso y si puede lo gasta.
	// Devuelve false si no puede pagar el coste.
	public boolean gastarRecurso(int coste) {
		if (this.recursoActual >= coste) {
			this.recursoActual -= coste;
			System.out.println(this.nombre + " gasta " + coste + " de recurso. ("
					+ this.recursoActual + "/" + this.recursoMax + ")");
			return true;
		} else {
			System.out
					.println("  [SIN RECURSO] " + this.nombre + " no tiene suficiente recurso para lanzar el hechizo.");
			return false;
		}
	}
	/**
	 * Asigna un arma al personaje, reemplazando la actual.
	 * * @param nuevaArma El objeto Armas que se va a equipar.
	 */
	// METODOS DE ARMA

	// Asigna un arma al personaje, reemplaza el arma que tenia antes
	public void equiparArma(Armas nuevaArma) {
		this.armaEquipada = nuevaArma;
	}
	/**
	 * Añade un arma a la lista de armas disponibles del personaje.
	 * * @param arma El objeto Armas que se añade al catálogo disponible.
	 */
	// Añade un arma al ArrayList de armas disponibles.
	// Se usa en CatalogoPersonajes al crear cada personaje.
	public void agregarArmaDisponible(Armas arma) {
		this.armasDisponibles.add(arma);
	}
	/**
	 * Obtiene la lista de armas disponibles del personaje.
	 * * @return Un ArrayList con los objetos Armas disponibles para elegir.
	 */
	// Devuelve el ArrayList de armas disponibles.
	// Se usa en Main para elegir aleatoriamente cual equipar al inicio.
	public ArrayList<Armas> getArmasDisponibles() {
		return armasDisponibles;
	}

	/**
	 * Aplica un nuevo estado (efecto de daño o curación en el tiempo) al personaje, o lo renueva si ya existe.
	 * * @param nuevoEstado El estado a aplicar.
	 */
	// Recibe el estado que se quiere aplicar al personaje.
	// Esto evita que se apilen varias quemaduras a la vez.
	public void aplicarEstados(Estados nuevoEstado) {
		// Recorremos el ArrayList de estados activos buscando uno con el mismo nombre
		for (Estados estado : estadosActivos) {
			if (estado.getNombre().equals(nuevoEstado.getNombre())) {
				// Si ya existe ese estado, renovamos su duracion en vez de añadir uno nuevo
				estado.renovarDuracion(nuevoEstado.getTurnosRestantes());
				return;
			}
		}
		// Si no existia, lo añadimos al ArrayList y ejecutamos su efecto de inicio
		estadosActivos.add(nuevoEstado);
		nuevoEstado.alAplicar(this);
	}

	// Recorre todos los estados activos del personaje y aplica su efecto.
	// Se llama al final de cada ronda despues de que todos hayan actuado.
	// Al final borra del ArrayList los estados que ya hayan expirado.
	public void procesarEstados() {
		// Lista auxiliar para guardar los estados que han expirado
		ArrayList<Estados> expirados = new ArrayList<>();

		for (Estados estado : estadosActivos) {
			// Aplicamos el efecto del estado en este turno (daño o curacion)
			estado.alProcesarTurno(this);
			// Reducimos un turno su duracion
			estado.reducirDuracion();
			// Si ya no le quedan turnos, lo marcamos como expirado
			if (!estado.estaActivo()) {
				estado.alExpirar(this);
				expirados.add(estado);
			}
		}

		// Borramos del ArrayList los estados que han expirado
		estadosActivos.removeAll(expirados);
	}
	/**
	 * Añade un hechizo al repertorio del personaje.
	 * * @param hechizo El objeto Hechizos a añadir.
	 */
	

	// Añade un hechizo al ArrayList de hechizos del personaje.
	// Se usa en Main al crear cada personaje para asignarle sus hechizos.
	public void agregarHechizo(Hechizos hechizo) {
		this.hechizos.add(hechizo);
	}
	/**
	 * Obtiene los turnos de recarga restantes para un hechizo concreto.
	 * * @param nombreHechizo El nombre del hechizo a consultar.
	 * @return Los turnos de cooldown restantes (0 si está disponible).
	 */
	// Devuelve el cooldown actual de un hechizo buscandolo por nombre en el Map.
	// Si el hechizo no esta en el mapa (nunca se ha usado) devuelve 0.
	public int getCooldown(String nombreHechizo) {
		return cooldowns.getOrDefault(nombreHechizo, 0);
	}
	/**
	 * Establece el tiempo de recarga (cooldown) para un hechizo.
	 * * @param nombreHechizo El nombre del hechizo que entra en enfriamiento.
	 * @param turnos La cantidad de turnos que el hechizo no se podrá usar.
	 */
	// Guarda o actualiza el cooldown de un hechizo en el Map.
	// Se llama justo despues de lanzar un hechizo para ponerlo en recarga.
	public void setCooldown(String nombreHechizo, int turnos) {
		cooldowns.put(nombreHechizo, turnos);
	}
	/**
	 * Comprueba si el personaje cumple las condiciones (cooldown y maná) para lanzar un hechizo.
	 * * @param hechizo El hechizo que se quiere comprobar.
	 * @return true si el cooldown es 0 y tiene recurso suficiente, false en caso contrario.
	 */
	// Devuelve true si el personaje puede lanzar el hechizo (cooldown 0 y recurso suficiente)
	public boolean puedeLanzarHechizo(Hechizos hechizo) {
		return getCooldown(hechizo.getNombre()) == 0 && getRecursoActual() >= hechizo.getCosteMana();
	}

	// Reduce en 1 el cooldown de todos los hechizos del Map al final de cada ronda.
	// Cuando llega a 0 el hechizo vuelve a estar disponible.
	public void reducirCooldowns() {
		for (String nombreHechizo : cooldowns.keySet()) {
			if (cooldowns.get(nombreHechizo) > 0) {
				cooldowns.put(nombreHechizo, cooldowns.get(nombreHechizo) - 1);
			}
		}
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------

	// Muestra en consola el estado completo del personaje durante el combate.
	public void resumenCombate() {
		System.out.println("  " + this.nombre);
		System.out.println("  Vida:    " + this.vidaActual + "/" + this.vidaMax + " HP");
		System.out.println("  Recurso: " + this.recursoActual + "/" + this.recursoMax);
		System.out.println("  Arma:    " + (this.armaEquipada != null ? this.armaEquipada.getNombre() : "Sin arma"));
		if (estadosActivos.isEmpty()) {
			System.out.println("  Estados: Ninguno");
		} else {
			System.out.print("  Estados: ");
			for (Estados e : estadosActivos) {
				System.out.print(e.toString() + "  ");
			}
			System.out.println();
		}
	}

	/**
	 * Acción que realiza el personaje en su turno.
	 * * @param objetivo El personaje enemigo o aliado sobre el que recae la acción principal.
	 */
	// -------------------------------------------------------------------------
	// POLIMORFISMO: cada subclase decide como actua en su turno
	// -------------------------------------------------------------------------

	// El guerrero atacara con su espada, el mago lanzara hechizos, el sacerdote
	// curara
	public abstract void realizarAccion(Personajes objetivo);

	// -------------------------------------------------------------------------
	// GETTERS
	// -------------------------------------------------------------------------
	/** @return El ID del personaje en la BD. */
	public int getId() {
		return id;
	}
	/** @param id El ID a asignar al personaje en la BD. */
	public void setId(int id) {
		this.id = id;
	}
	/** @param vidaActual Los puntos de vida actuales a establecer. */
	// Setters para restaurar el estado desde la base de datos al cargar partida
	public void setVidaActual(int vidaActual) {
		this.vidaActual = vidaActual;
	}
	/** @param recursoActual Los puntos de recurso actuales a establecer. */
	public void setRecursoActual(int recursoActual) {
		this.recursoActual = recursoActual;
	}
	/** @return El nombre del personaje. */
	public String getNombre() {
		return nombre;
	}
	/** @return La vida actual del personaje. */
	public int getVidaActual() {
		return vidaActual;
	}
	/** @return La vida máxima del personaje. */
	public int getVidaMax() {
		return vidaMax;
	}
	/** @return El recurso (maná/vigor) actual del personaje. */
	public int getRecursoActual() {
		return recursoActual;
	}
	/** @return El recurso máximo del personaje. */
	public int getRecursoMax() {
		return recursoMax;
	}
	/** @return El nivel de ataque base. */
	public int getAtaqueBase() {
		return ataqueBase;
	}
	/** @return El nivel de defensa base. */
	public int getDefensaBase() {
		return defensaBase;
	}
	/** @return El poder mágico base. */
	public int getPoderMagico() {
		return poderMagico;
	}
	/** @return El arma actualmente equipada. */
	public Armas getArmaEquipada() {
		return armaEquipada;
	}
	/** @return La lista de hechizos del personaje. */
	public ArrayList<Hechizos> getHechizos() {
		return hechizos;
	}
	/** @return La lista de estados alterados actualmente activos. */
	public ArrayList<Estados> getEstadosActivos() {
		return estadosActivos;
	}
}