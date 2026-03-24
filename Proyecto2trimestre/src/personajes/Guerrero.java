package personajes;

public class Guerrero extends Personajes {



	private int defensaAdrenalina;
	private double probBloqueo;

	public Guerrero(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico, int defensaAdrenalina, double probBloqueo) {
		super(nombre, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
		// TODO Auto-generated constructor stub
		this.defensaAdrenalina= defensaAdrenalina;
	}
		//polimorfismo
	@Override
	public void realizarAccion(Personajes objetivo) {
	    System.out.println(this.nombre + " se prepara para actuar...");
	}
	
	

	
	@Override
	public void recibirDano (int cantidadDano) {
		//daño despues de aplicar la defensa por adrenalina
		int danoReducido = cantidadDano - this.defensaAdrenalina;

		// bloquea el ataque si el numero aleatorio es menor que el numero que se establezca de probabilidad al guerrero
		if (Math.random() < this.probBloqueo) {
		    System.out.println(this.nombre + " ¡ha bloqueado el ataque!");
		    return; 
	}
		if (danoReducido < 0) {
		    danoReducido = 0;
		}
		super.recibirDano(danoReducido);
	
	
	}

	}

