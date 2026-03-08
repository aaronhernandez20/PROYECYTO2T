package personajes;

public class Sacerdote extends Personajes {
	  private int bonusSanacion;

    public Sacerdote(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico,int bonusSanacion) {
        super(nombre, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
        // TODO Auto-generated constructor stub
        this.bonusSanacion = bonusSanacion;

    }
    @Override
    public void curar(int cantidadVida) {
    	//suma el bonus de sanacion
        int curacionFinal = cantidadVida + this.bonusSanacion; 
        //le pasa la vida final tras la curacion a la clase padre
        super.curar(curacionFinal); 
        System.out.println("(Bonus de sanación: +" + this.bonusSanacion + ")");
    }
    //polimorfsmo el sacerdote tiene su propia forma de actuar
    @Override
    public void realizarAccion(Personajes objetivo) {
        System.out.println(this.nombre + " evalúa el estado del equipo y se prepara para actuar...");
    }

}
