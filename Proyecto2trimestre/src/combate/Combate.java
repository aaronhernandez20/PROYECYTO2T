package combate;

import java.util.ArrayList;

import personajes.Personajes;

public class Combate {
	private ArrayList <Personajes> equipoBueno;
	private ArrayList <Personajes> equipoMalo;
	
	public Combate(ArrayList<Personajes> equipoBueno, ArrayList<Personajes> equipoMalo) {
		super();
		this.equipoBueno = equipoBueno;
		this.equipoMalo = equipoMalo;
	}
	
	
	private boolean hayVivos(ArrayList<Personajes>equipo) {
		for(Personajes p:equipo) {
			//Si hay vivos
			if(p.estaVivo()) {
				return true;
			}
		}
		//no hay vivos
		return false;
		
		
	}
	
	public void iniciar() {
	    int ronda = 1;
	    
	    while (hayVivos(equipoBueno) && hayVivos(equipoMalo)) {
	        System.out.println("=== RONDA " + ronda + " ===");
	        
	        // Fase 1: Acciones
	        
	        // Fase 2: Procesar estados
	        
	        // Fase 3: Comprobar victoria
	        
	        ronda++;
	    }
	    
	    // Resumen final
	}
}
