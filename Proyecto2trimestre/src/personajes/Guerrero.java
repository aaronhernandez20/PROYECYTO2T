package personajes;

public class Guerrero extends Personajes {

    private int defensaAdrenalina;

    public Guerrero(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico,
            int defensaAdrenalina) {
        super(nombre, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
        // TODO Auto-generated constructor stub
        this.defensaAdrenalina = defensaAdrenalina;
    }

	private int defensaAdrenalina;

	public Guerrero(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico, int defensaAdrenalina) {
		super(nombre, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
		// TODO Auto-generated constructor stub
		this.defensaAdrenalina= defensaAdrenalina;
	}
	@Override
	public void recibirDano (int cantidadDano) {
		//daño despues de aplicar la defensa por adrenalina
		int danoReducido = cantidadDano - this.defensaAdrenalina;

		if (danoReducido < 0) {
		    danoReducido = 0;
		}
		super.recibirDano(danoReducido);
	}
	//polimorfismo
	@Override
	public void realizarAccion(Personajes objetivo) {
	    System.out.println(this.nombre + " se prepara para actuar...");
	}
}
