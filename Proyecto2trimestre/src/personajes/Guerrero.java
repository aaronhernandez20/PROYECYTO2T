package personajes;

public class Guerrero extends Personajes {

	// defensaAdrenalina: por la adrenalina del combate el guerrero reduce el daño
	// recibido
	private int defensaAdrenalina;

	// probBloqueo: probabilidad de bloquear completamente un ataque (0.0 a 1.0)
	// Geralt tiene 0.20 (20%) e Imlerith tiene 0.15 (15%)
	private double probBloqueo;

	public Guerrero(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase,
			int poderMagico, int defensaAdrenalina, double probBloqueo) {
		super(nombre, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
		this.defensaAdrenalina = defensaAdrenalina;
		// CORRECCION: antes faltaba esta linea y probBloqueo siempre valia 0.0
		this.probBloqueo = probBloqueo;
	}

	// Polimorfismo: el guerrero tiene su propia forma de actuar en su turno
	@Override
	public void realizarAccion(Personajes objetivo) {
		System.out.println(this.nombre + " se prepara para actuar...");
	}

	// Polimorfismo: el guerrero sobrescribe recibirDano para aplicar
	// su reduccion por adrenalina y su probabilidad de bloqueo.
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