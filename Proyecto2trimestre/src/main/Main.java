package main;

import java.util.ArrayList;
import personajes.*;
import armas.*;
import hechizos.*;
import catalogo.CatalogoPersonajes;
import combate.Combate;

public class Main {

	public static void main(String[] args) {
	    ArrayList<Personajes> equipoBueno = crearEquipoBueno();
	    ArrayList<Personajes> equipoMalo = crearEquipoMalo();
	    
	    Combate combate = new Combate(equipoBueno, equipoMalo);
	    combate.iniciar();
	}
	public static ArrayList<Personajes> crearEquipoBueno() {
		// TODO Auto-generated method stub
		// equipo bueno
		ArrayList<Personajes> equipoBueno = new ArrayList<>();
		Personajes geralt = CatalogoPersonajes.crearGeralt();
        asignarArmaAleatoria(geralt);
        // Hechizos de Geralt: habilidad fisica con cooldown (segun el profe el guerrero
        // puede tenerlas)
        geralt.agregarHechizo(new DañoDirecto("Golpe Poderoso", 30, 120, 3));
        equipoBueno.add(geralt);

        Personajes yennefer = CatalogoPersonajes.crearYennefer();
        asignarArmaAleatoria(yennefer);
        // Hechizos de Yennefer: daño directo + daño en el tiempo (obligatorio para
        // Mago)
        yennefer.agregarHechizo(new DañoDirecto("Señal de Igni", 50, 80, 2));
        yennefer.agregarHechizo(new DañoEnElTiempo("Fuego de Vengerberg", 40, 3));
        equipoBueno.add(yennefer);

        Personajes ciri = CatalogoPersonajes.crearCiri();
        asignarArmaAleatoria(ciri);
        // Hechizos de Ciri: curacion directa + curacion en el tiempo (obligatorio para
        // Sacerdote)
        ciri.agregarHechizo(new CuraciónDirecta("Poción de Golondrina", 60, 200, 3));
        ciri.agregarHechizo(new CuraciónEnElTiempo("Aura de la Vieja Sangre", 50, 4));
        equipoBueno.add(ciri);

        return equipoBueno;
    }

    public static ArrayList<Personajes> crearEquipoMalo() {
        ArrayList<Personajes> equipoMalo = new ArrayList<>();

        Personajes imlerith = CatalogoPersonajes.crearImlerith();
        asignarArmaAleatoria(imlerith);
        // Hechizos de Imlerith: habilidad fisica con cooldown alto
        imlerith.agregarHechizo(new DañoDirecto("Embestida Brutal", 20, 150, 4));
        equipoMalo.add(imlerith);

        Personajes caranthir = CatalogoPersonajes.crearCaranthir();
        asignarArmaAleatoria(caranthir);
        // Hechizos de Caranthir: daño directo + daño en el tiempo (obligatorio para
        // Mago)
        caranthir.agregarHechizo(new DañoDirecto("Lanza de Hielo", 45, 70, 2));
        caranthir.agregarHechizo(new DañoEnElTiempo("Escarcha Corrosiva", 35, 3));
        equipoMalo.add(caranthir);

        Personajes eredin = CatalogoPersonajes.crearEredin();
        asignarArmaAleatoria(eredin);
        // Hechizos de Eredin: curacion directa + curacion en el tiempo (obligatorio
        // para Sacerdote)
        eredin.agregarHechizo(new CuraciónDirecta("Sacrificio Oscuro", 60, 200, 3));
        eredin.agregarHechizo(new CuraciónEnElTiempo("Presencia del Rey", 50, 4));
        equipoMalo.add(eredin);

        return equipoMalo;
    }

    // Funcion para elegir un arma de las 2 disponibles aleatoriamente
    private static void asignarArmaAleatoria(Personajes personaje) {
        if (Math.random() < 0.5) {
            personaje.equiparArma(personaje.getArmasDisponibles().get(0));
        } else {
            personaje.equiparArma(personaje.getArmasDisponibles().get(1));
        }
    }
}