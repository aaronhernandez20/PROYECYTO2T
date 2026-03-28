package combate;

import java.util.ArrayList;

import personajes.Personajes;

public class Combate {
	private ArrayList <Personajes> equipoBueno;
	private ArrayList <Personajes> equipoMalo;
	
	private void esperar(int milisegundos) {
	    try {
	        Thread.sleep(milisegundos);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
	
	private void mostrarPresentacion() {
	    System.out.println("╔══════════════════════════════════════╗");
	    System.out.println("║     THE WITCHER - RPG POR TURNOS     ║");
	    System.out.println("╚══════════════════════════════════════╝");
	    
	    System.out.println("\n⚔️  EQUIPO DE GERALT ⚔️");
	    System.out.println("--------------------------------------");
	    for (Personajes p : equipoBueno) {
	        p.resumenCombate();
	        System.out.println("--------------------------------------");
	    }
	    
	    System.out.println("\n💀  LA CACERÍA SALVAJE 💀");
	    System.out.println("--------------------------------------");
	    for (Personajes p : equipoMalo) {
	        p.resumenCombate();
	        System.out.println("--------------------------------------");
	    }
	    System.out.println("\n¡Que comience el combate!\n");
	    esperar(2000);
	}
	
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
	private Personajes obtenerEnemigoVivo(ArrayList<Personajes> equipo) {
	    // recoge todos los personajes vivos en una lista
	    ArrayList<Personajes> vivos = new ArrayList<>();
	    for (Personajes p : equipo) {
	        if (p.estaVivo()) {
	            vivos.add(p);
	        }
	    }
	    
	    // si no hay nadie vivo devuelve null
	    if (vivos.isEmpty()) {
	        return null;
	    }
	    
	    // elige uno al azar de la lista de vivos
	    int indiceAleatorio = (int)(Math.random() * vivos.size());
	    return vivos.get(indiceAleatorio);
	}
	
	public void iniciar() {
	    int ronda = 1;
	    
	    while (hayVivos(equipoBueno) && hayVivos(equipoMalo)) {
	        System.out.println("=== RONDA " + ronda + " ===");
	        
	        // Fase 1: Acciones
	        
	        // equipo bieno ataca al malo
	        for (Personajes atacante : equipoBueno) {
	            if (atacante.estaVivo()) {
	            	
	                Personajes objetivo = obtenerEnemigoVivo(equipoMalo);
	                if (objetivo != null) {
	                    int dano = atacante.armaEquipada.calcularDano(atacante, objetivo);
	                    objetivo.recibirDano(dano);
	                    System.out.println(atacante.getNombre() + " ataca a " 
	                        + objetivo.getNombre() + " con " 
	                        + atacante.armaEquipada.getNombre() 
	                        + ". ¡" + dano + " de daño!");
	                    esperar(2000);
	                }
	            }
	        }
	     // equipo malo ataca al equipo bueno
	        for (Personajes atacante : equipoMalo) {
	            if (atacante.estaVivo()) {
	                Personajes objetivo = obtenerEnemigoVivo(equipoBueno);
	                if (objetivo != null) {
	                    int dano = atacante.armaEquipada.calcularDano(atacante, objetivo);
	                    objetivo.recibirDano(dano);
	                    System.out.println(atacante.getNombre() + " ataca a "
	                        + objetivo.getNombre() + " con "
	                        + atacante.armaEquipada.getNombre()
	                        + ". ¡" + dano + " de daño!");
	                }
	            }
	        }

	        
	     // Fase 2: Procesar estados
	        for (Personajes p : equipoBueno) {
	            if (p.estaVivo()) {
	                p.procesarEstados();
	            }
	        }
	        for (Personajes p : equipoMalo) {
	            if (p.estaVivo()) {
	                p.procesarEstados();
	            }
	        }	        
	     // Fase 3: Comprobar victoria
	        if (!hayVivos(equipoBueno) || !hayVivos(equipoMalo)) {
	            break; // sale del while inmediatamente
	        }
	        
	        ronda++;
	    }
	    
	 // Resumen final
	    System.out.println("\n=== FIN DEL COMBATE ===");
	    if (hayVivos(equipoBueno)) {
	        System.out.println("¡El equipo de Geralt ha ganado!");
	    } else {
	        System.out.println("¡La Cacería Salvaje ha ganado!");
	    }

	    // Estado final de cada personaje
	    System.out.println("\n--- Estado final ---");
	    for (Personajes p : equipoBueno) {
	        p.resumenCombate();
	    }
	    for (Personajes p : equipoMalo) {
	        p.resumenCombate();
	    }
	}
}
