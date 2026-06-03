package personajes;

/**
 * La clase Sacerdote es una subclase de {@link Personajes}.
 * Representa a un personaje enfocado en el apoyo y la curación, 
 * contando con un bonus adicional fijo que potencia cualquier sanación que realice.
 */
public class Sacerdote extends Personajes {
	
	private int bonusSanacion;

    /**
     * Constructor para instanciar un Sacerdote.
     * 
     * @param nombre El nombre del personaje.
     * @param vidaMax La vida máxima.
     * @param recursoMax El recurso máximo (Fe/Maná).
     * @param ataqueBase Poder de ataque físico base.
     * @param defensaBase Capacidad de resistir daño físico.
     * @param poderMagico Potencia para habilidades mágicas y de curación.
     * @param bonusSanacion Cantidad fija que se suma siempre a las curaciones que aplica.
     */
    public Sacerdote(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico, int bonusSanacion) {
        super(nombre, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
        // TODO Auto-generated constructor stub
        this.bonusSanacion = bonusSanacion;

    }

    /**
     * Gestiona la recuperación de vida del sacerdote.
     * Polimorfismo: el sacerdote añade su bonus de sanación particular antes de enviarlo
     * a la clase padre para aplicar la cura final.
     * 
     * @param cantidadVida La cantidad de puntos de vida a recuperar inicial.
     */
    @Override
    public void curar(int cantidadVida) {
    	//suma el bonus de sanacion
        int curacionFinal = cantidadVida + this.bonusSanacion; 
        //le pasa la vida final tras la curacion a la clase padre
        super.curar(curacionFinal); 
        System.out.println("(Bonus de sanación: +" + this.bonusSanacion + ")");
    }

    /**
     * Acción que realiza el sacerdote al llegar su turno.
     * Polimorfismo: el sacerdote tiene su propia forma de actuar.
     * 
     * @param objetivo El personaje enemigo o aliado sobre el que recae la acción principal.
     */
    @Override
    public void realizarAccion(Personajes objetivo) {
        System.out.println(this.nombre + " evalúa el estado del equipo y se prepara para actuar...");
    }

}