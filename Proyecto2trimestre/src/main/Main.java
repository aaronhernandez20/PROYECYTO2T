package main;

import java.util.ArrayList;
import personajes.*;
import armas.*;
import catalogo.CatalogoPersonajes;

public class Main {

    public static void main(String[] args) {
        // se crean los dos equipos
        ArrayList<Personajes> equipoBueno = crearEquipoBueno();
        ArrayList<Personajes> equipoMalo = crearEquipoMalo();
    }

	public static ArrayList<Personajes> crearEquipoBueno() {
		// TODO Auto-generated method stub
		// equipo bueno
		ArrayList<Personajes> equipoBueno = new ArrayList<>();
		Personajes geralt = CatalogoPersonajes.crearGeralt();
        asignarArmaAleatoria(geralt);
        equipoBueno.add(geralt);

        Personajes yennefer = CatalogoPersonajes.crearYennefer();
        asignarArmaAleatoria(yennefer);
        equipoBueno.add(yennefer);

        Personajes ciri = CatalogoPersonajes.crearCiri();
        asignarArmaAleatoria(ciri);
        equipoBueno.add(ciri);

        return equipoBueno;
    }
	public static ArrayList<Personajes> crearEquipoMalo() {
		// TODO Auto-generated method stub
		// equipo malo
		ArrayList<Personajes> equipoMalo = new ArrayList<>();
		Personajes imlerith = CatalogoPersonajes.crearImlerith();
        asignarArmaAleatoria(imlerith);
        equipoMalo.add(imlerith);

        Personajes caranthir = CatalogoPersonajes.crearCaranthir();
        asignarArmaAleatoria(caranthir);
        equipoMalo.add(caranthir);

        Personajes eredin = CatalogoPersonajes.crearEredin();
        asignarArmaAleatoria(eredin);
        equipoMalo.add(eredin);

        return equipoMalo;
    }

		
	// funcion para elegir un arma de las 2 disponible aleatoriamente
	private static void asignarArmaAleatoria(Personajes personaje){
		if (Math.random() < 0.5) {
			// equipa el arma de las disponibles en esta caso la de la posicion 0
            personaje.equiparArma(personaje.getArmasDisponibles().get(0));
        } else {
			// equipa el arma de las disponibles,ahora la de la posicion 1
            personaje.equiparArma(personaje.getArmasDisponibles().get(1));
        }

	}
}