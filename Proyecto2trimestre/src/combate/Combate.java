package combate;

import java.util.ArrayList;
import java.util.Scanner;

import hechizos.Hechizos;
import personajes.Personajes;

public class Combate {

	private ArrayList<Personajes> equipoBueno;
	private ArrayList<Personajes> equipoMalo;
	private boolean modoManual;
	private Scanner scanner = new Scanner(System.in);
    ArrayList<String> opciones = new ArrayList<>();


	public Combate(ArrayList<Personajes> equipoBueno, ArrayList<Personajes> equipoMalo, boolean modoManual) {
	    this.equipoBueno = equipoBueno;
	    this.equipoMalo = equipoMalo;
	    this.modoManual = modoManual;
	}
	private void ejecutarTurnoManual(Personajes atacante, ArrayList<Personajes> enemigos, ArrayList<Personajes> aliados) {
	    System.out.println("  >> Turno de " + atacante.getNombre());
	    System.out.println("  ------------------------------------------");
	    
	    // Construir opciones disponibles
	    ArrayList<String> opciones = new ArrayList<>();

	    // Añadir hechizos disponibles
	    for (Hechizos hechizo : atacante.getHechizos()) {
	        if (atacante.getCooldown(hechizo.getNombre()) == 0
	                && atacante.getRecursoActual() >= hechizo.getCosteMana()) {
	            opciones.add("HECHIZO:" + hechizo.getNombre());
	            System.out.println("  " + opciones.size() + ". Usar hechizo: " + hechizo.getNombre()
	                    + " [coste: " + hechizo.getCosteMana() + " mana]"
	                    + " [CD: " + hechizo.getCooldownMaximo() + " turnos]");
	        } else if (atacante.getCooldown(hechizo.getNombre()) > 0) {
	            System.out.println("  X. " + hechizo.getNombre()
	                    + " [en cooldown: " + atacante.getCooldown(hechizo.getNombre()) + " turnos]");
	        } else {
	            System.out.println("  X. " + hechizo.getNombre() + " [sin mana]");
	        }
	    }

	 // Añadir opción de atacar con arma
	    if (atacante.getArmaEquipada() != null) {
	        opciones.add("ARMA");
	        System.out.println("  " + opciones.size() + ". Atacar con " + atacante.getArmaEquipada().getNombre());
	    }

	    System.out.println("  ------------------------------------------");
	    System.out.print("  Elige una opcion: ");

	    // Leer opción válida
	    int eleccion = -1;
	    while (eleccion < 1 || eleccion > opciones.size()) {
	        try {
	            eleccion = scanner.nextInt();
	            if (eleccion < 1 || eleccion > opciones.size()) {
	                System.out.print("  Opcion no valida. Elige entre 1 y " + opciones.size() + ": ");
	            }
	        } catch (Exception e) {
	            scanner.nextLine();
	            System.out.print("  Opcion no valida. Elige entre 1 y " + opciones.size() + ": ");
	        }
	    }

	    String opcionElegida = opciones.get(eleccion - 1);

	    if (opcionElegida.equals("ARMA")) {
	        // Atacar con arma a enemigo aleatorio
	        Personajes objetivo = obtenerEnemigoVivo(enemigos);
	        if (objetivo != null) {
	            int dano = atacante.getArmaEquipada().calcularDano(atacante, objetivo);
	            System.out.println("\n  " + atacante.getNombre() + " ataca a "
	                    + objetivo.getNombre() + " con "
	                    + atacante.getArmaEquipada().getNombre()
	                    + ". ¡" + dano + " de daño!");
	            objetivo.recibirDano(dano);
	            esperar(1500);
	            System.out.println();
	        }
	    } else {
	        // Usar hechizo
	        String nombreHechizo = opcionElegida.replace("HECHIZO:", "");
	        Hechizos hechizoCargado = null;
	        for (Hechizos h : atacante.getHechizos()) {
	            if (h.getNombre().equals(nombreHechizo)) {
	                hechizoCargado = h;
	                break;
	            }
	        }
	        if (hechizoCargado != null) {
	            // Elegir objetivo según tipo
	            Personajes objetivo;
	            if (hechizoCargado.getTipoObjetivo() == Hechizos.TipoObjetivo.ENEMIGO_UNICO) {
	                objetivo = obtenerEnemigoVivo(enemigos);
	            } else {
	                objetivo = obtenerAliadoMasDebil(aliados);
	            }
	            if (objetivo != null) {
	                hechizoCargado.lanzar(atacante, objetivo);
	                esperar(1500);
	                System.out.println();
	            }
	        }
	    }
	}

	    

	// Pausa la ejecucion unos milisegundos para que la consola sea mas legible
	private void esperar(int milisegundos) {
		try {
			Thread.sleep(milisegundos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

	// Muestra la presentacion inicial con los dos equipos antes de empezar
	private void mostrarPresentacion() {
	    System.out.println("==========================================================");
	    System.out.println("            THE WITCHER  -  COMBAT SIMULATOR             ");
	    System.out.println("==========================================================");
	    System.out.println("  \"Evil is evil. Lesser, greater, middling... makes no");
	    System.out.println("   difference. The degree is arbitrary.\"");
	    System.out.println("                                       -- Geralt of Rivia");
	    System.out.println("----------------------------------------------------------");
	    esperar(2000);

	    System.out.println("\n  [ WITCHERS - GERALT'S COMPANY ]");
	    System.out.println("----------------------------------------------------------");
	    for (Personajes p : equipoBueno) {
	        p.resumenCombate();
	        System.out.println("----------------------------------------------------------");
	    }

	    System.out.println("\n  [ THE WILD HUNT - EREDIN'S RIDERS ]");
	    System.out.println("----------------------------------------------------------");
	    for (Personajes p : equipoMalo) {
	        p.resumenCombate();
	        System.out.println("----------------------------------------------------------");
	    }

	    System.out.println("\n  Initializing combat sequence...");
	    esperar(2000);
	    System.out.println("  All units ready.");
	    esperar(500);
	    System.out.println("\n==========================================================");
	    System.out.println("                    COMBAT START                         ");
	    System.out.println("==========================================================\n");
	    esperar(2000);
	}
	// Devuelve true si algún personaje del equipo sigue vivo
	private boolean hayVivos(ArrayList<Personajes> equipo) {
		for (Personajes p : equipo) {
			if (p.estaVivo()) {
				return true;
			}
		}
		return false;
	}

	// Devuelve un enemigo vivo al azar del equipo indicado.
	// Devuelve null si no queda nadie vivo.
	private Personajes obtenerEnemigoVivo(ArrayList<Personajes> equipo) {
		ArrayList<Personajes> vivos = new ArrayList<>();
		for (Personajes p : equipo) {
			if (p.estaVivo()) {
				vivos.add(p);
			}
		}
		if (vivos.isEmpty()) {
			return null;
		}
		int indiceAleatorio = (int) (Math.random() * vivos.size());
		return vivos.get(indiceAleatorio);
	}

	// Devuelve el aliado vivo con menos vida del equipo indicado.
	// Lo usan los sacerdotes para curar al más debil.
	// Devuelve null si no queda nadie vivo.
	private Personajes obtenerAliadoMasDebil(ArrayList<Personajes> equipo) {
		Personajes masDebil = null;
		for (Personajes p : equipo) {
			if (p.estaVivo()) {
				if (masDebil == null || p.getVidaActual() < masDebil.getVidaActual()) {
					masDebil = p;
				}
			}
		}
		return masDebil;
	}

	// Ejecuta el turno de un personaje:
	// Si tiene hechizos disponibles los usa, si no ataca con arma.
	// Los hechizos de daño van a enemigos y los de curacion al aliado mas debil.
	
	private void ejecutarTurno(Personajes atacante, ArrayList<Personajes> enemigos, ArrayList<Personajes> aliados) {
	    // Recorremos el ArrayList de hechizos del personaje
	    for (Hechizos hechizo : atacante.getHechizos()) {
	        // Segun el tipo de objetivo del hechizo elegimos el blanco
	        if (hechizo.getTipoObjetivo() == Hechizos.TipoObjetivo.ENEMIGO_UNICO) {
	            Personajes objetivo = obtenerEnemigoVivo(enemigos);
	            if (objetivo != null) {
	                // puedeUsarse comprueba cooldown y recurso internamente
	                if (atacante.getCooldown(hechizo.getNombre()) == 0
	                        && atacante.getRecursoActual() >= hechizo.getCosteMana()) {
	                    hechizo.lanzar(atacante, objetivo);
	                    esperar(1500);
	                    System.out.println();
	                    return; // usamos un hechizo, terminamos el turno
	                }
	            }
	        } else if (hechizo.getTipoObjetivo() == Hechizos.TipoObjetivo.ALIADO_UNICO) {
	            Personajes objetivo = obtenerAliadoMasDebil(aliados);
	            if (objetivo != null) {
	                if (atacante.getCooldown(hechizo.getNombre()) == 0
	                        && atacante.getRecursoActual() >= hechizo.getCosteMana()) {
	                    hechizo.lanzar(atacante, objetivo);
	                    esperar(1500);
	                    System.out.println();
	                    return; // usamos un hechizo, terminamos el turno
	                }
	            }
	        }
	    }

	    // Si no ha podido usar ningun hechizo, ataca con el arma equipada
	    Personajes objetivo = obtenerEnemigoVivo(enemigos);
	    if (objetivo != null && atacante.getArmaEquipada() != null) {
	        int dano = atacante.getArmaEquipada().calcularDano(atacante, objetivo);
	        System.out.println(atacante.getNombre() + " ataca a "
	                + objetivo.getNombre() + " con "
	                + atacante.getArmaEquipada().getNombre()
	                + ". ¡" + dano + " de daño!");
	        objetivo.recibirDano(dano);
	        esperar(1500);
	        System.out.println();
	    }
	}

	// Muestra el estado de vida de todos los personajes al inicio de cada ronda.
	// El profe pide que cada ronda imprima vida, recurso y estados activos.
	private void mostrarEstadoRonda() {
		System.out.println("\n  Equipo Geralt:");
		for (Personajes p : equipoBueno) {
			if (p.estaVivo()) {
				p.resumenCombate();
			} else {
				System.out.println("  " + p.getNombre() + " [MUERTO]");
			}
		}
		System.out.println("   La Cacería Salvaje:");
		for (Personajes p : equipoMalo) {
			if (p.estaVivo()) {
				p.resumenCombate();
			} else {
				System.out.println("  " + p.getNombre() + " [MUERTO]");
			}
		}
		System.out.println();
	}

	public void iniciar() {
		// Mostramos la presentacion inicial antes de empezar
		mostrarPresentacion();

		int ronda = 1;

		while (hayVivos(equipoBueno) && hayVivos(equipoMalo)) {
			System.out.println("=== RONDA " + ronda + " ===");

			// El profe pide mostrar vida/recurso/estados al inicio de cada ronda
			mostrarEstadoRonda();

			// -----------------------------------------------------------------
			// FASE 1: Acciones
			// Primero actua el equipo bueno, luego el malo.
			// Si un personaje muere durante la ronda ya no actua.
			// -----------------------------------------------------------------
			System.out.println("[ACCIONES]");

			for (Personajes atacante : equipoBueno) {
			    if (atacante.estaVivo()) {
			        if (modoManual) {
			            ejecutarTurnoManual(atacante, equipoMalo, equipoBueno);
			        } else {
			            ejecutarTurno(atacante, equipoMalo, equipoBueno);
			        }
			    }
			}

			for (Personajes atacante : equipoMalo) {
				if (atacante.estaVivo()) {
					ejecutarTurno(atacante, equipoBueno, equipoMalo);
				}
			}

			// -----------------------------------------------------------------
			// FASE 2: Procesamiento de estados (daño/curacion por turno)
			// El profe pide que esto se haga separado de las acciones.
			// -----------------------------------------------------------------
			System.out.println("\n[EFECTOS DE ESTADO]");

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

			// Reducimos el cooldown de todos los hechizos de todos los personajes
			// al final de la ronda para que vayan recuperandose turno a turno.
			for (Personajes p : equipoBueno) {
				p.reducirCooldowns();
			}
			for (Personajes p : equipoMalo) {
				p.reducirCooldowns();
			}

			// -----------------------------------------------------------------
			// FASE 3: Comprobar victoria
			// -----------------------------------------------------------------
			if (!hayVivos(equipoBueno) || !hayVivos(equipoMalo)) {
				break;
			}

			ronda++;
			esperar(2000);
		}

		// Resumen final: el profe pide equipo ganador, rondas jugadas y estado final
		System.out.println("\n╔══════════════════════════════════════╗");
		System.out.println("║           FIN DEL COMBATE            ║");
		System.out.println("╚══════════════════════════════════════╝");
		System.out.println("Rondas jugadas: " + ronda);

		if (hayVivos(equipoBueno)) {
			System.out.println(" ¡El equipo de Geralt ha ganado!");
		} else {
			System.out.println(" ¡La Cacería Salvaje ha ganado!");
		}

		System.out.println("\n--- Estado final de los personajes ---");
		System.out.println("  Equipo de Geralt:");
		for (Personajes p : equipoBueno) {
			p.resumenCombate();
		}
		System.out.println("  La Cacería Salvaje:");
		for (Personajes p : equipoMalo) {
			p.resumenCombate();
		}
	}
}