package personajes;

/**
 * La clase Guerrero es una subclase de {@link Personajes}.
 * Especializada en el combate físico, posee mecánicas únicas de supervivencia
 * como la probabilidad de bloquear ataques y la reducción de daño por adrenalina.
 */
public class Guerrero extends Personajes {

	// defensaAdrenalina: por la adrenalina del combate el guerrero reduce el daño
	// recibido
	private int defensaAdrenalina;

	// probBloqueo: probabilidad de bloquear completamente un ataque (0.0 a 1.0)
	// Geralt tiene 0.20 (20%) e Imlerith tiene 0.15 (15%)
	private double probBloqueo;

	/**
	 * Constructor para instanciar un Guerrero.
	 * 
	 * @param nombre El nombre del personaje.
	 * @param vidaMax La vida máxima.
	 * @param recursoMax El recurso máximo (Ira o Vigor).
	 * @param ataqueBase Poder de ataque físico base.
	 * @param defensaBase Capacidad de resistir daño físico.
	 * @param poderMagico Potencia para habilidades mágicas.
	 * @param defensaAdrenalina Puntos de daño fijos que se restan a cualquier ataque recibido antes de la defensa base.
	 * @param probBloqueo Probabilidad (0.0 a 1.0) de ignorar un ataque físico por completo.
	 */
	public Guerrero(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase,
			int poderMagico, int defensaAdrenalina, double probBloqueo) {
		super(nombre, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
		this.defensaAdrenalina = defensaAdrenalina;
		this.probBloqueo = probBloqueo;
	}

	/**
	 * Acción que realiza el guerrero al llegar su turno.
	 * Polimorfismo: el guerrero tiene su propia forma de actuar.
	 * 
	 * @param objetivo El personaje enemigo o aliado sobre el que recae la acción principal.
	 */
	@Override
	public void realizarAccion(Personajes objetivo) {
		System.out.println(this.nombre + " se prepara para actuar...");
	}

	/**
	 * Gestiona la recepción de daño físico del guerrero.
	 * Polimorfismo: sobrescribe recibirDano para aplicar su reducción por 
	 * adrenalina y su probabilidad de bloqueo antes de usar la defensa base del padre.
	 * 
	 * @param cantidadDano La cantidad de daño físico entrante antes de bloqueos y defensas.
	 */
	@Override
	public void recibirDano(int cantidadDano) {
		// Primero comprobamos si bloquea el ataque completamente
		if (Math.random() < this.probBloqueo) {
			System.out.println(this.nombre + " ¡ha bloqueado el ataque!");
			return;
		}

		// Si no bloquea, la adrenalina reduce parte del daño antes de pasarlo al padre
		int danoReducido = cantidadDano - this.defensaAdrenalina;
		if (danoReducido < 0) {
			danoReducido = 0;
		}

		// Llamamos al recibirDano del padre con el daño ya reducido por adrenalina.
		// El padre aplica ademas la defensaBase normal.
		super.recibirDano(danoReducido);
	}

}