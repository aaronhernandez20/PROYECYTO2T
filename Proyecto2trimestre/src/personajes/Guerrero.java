package personajes;

public class Guerrero extends Personajes {

    private int defensaAdrenalina;

    public Guerrero(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico,
            int defensaAdrenalina) {
        super(nombre, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
        // TODO Auto-generated constructor stub
        this.defensaAdrenalina = defensaAdrenalina;
    }

}
