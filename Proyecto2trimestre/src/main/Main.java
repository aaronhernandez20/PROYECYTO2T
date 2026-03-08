package main;

import java.util.ArrayList;
import personajes.*;
import armas.*;

public class Main {

    public static void main(String[] args) {
        // se crean los dos equipos
        ArrayList<Personajes> equipoBueno = crearEquipoBueno();
        ArrayList<Personajes> equipoMalo = crearEquipoMalo();
    }

	public static ArrayList<Personajes> crearEquipoBueno() {
		// TODO Auto-generated method stub
		// equipo bueno
		ArrayList<Personajes> equipo = new ArrayList<>();
        equipo.add(crearGeralt());
        equipo.add(crearYennefer());
        equipo.add(crearCiri());
        return equipo;

	}

	private static Personajes crearCiri() {
		// TODO Auto-generated method stub
		Sacerdote ciri = new Sacerdote("Ciri", 600, 200, 70, 35, 50, 20);
        
		// Sus 2 armas

        ArmaCuerpoACuerpo zireael = new ArmaCuerpoACuerpo("Zireael", 70, 1.1, 0.30);
        ArmaADistancia amuletoMagico = new ArmaADistancia("Amuleto Mágico", 45, 1.0, 0.15);

        // El juego decide aleatoriamente que arma escoge

        if (Math.random() < 0.5) {
            ciri.equiparArma(zireael);
        } else {
            ciri.equiparArma(amuletoMagico);
        }
        return ciri;
    }
	

	private static Personajes crearYennefer() {
		// TODO Auto-generated method stub
		Mago yennefer = new Mago("Yennefer de Vengerberg", 400, 300, 30, 20, 100, 1.5);
		
		// Sus 2 armas
        ArmaCuerpoACuerpo dagaPlata = new ArmaCuerpoACuerpo("Daga de Plata", 30, 1.0, 0.10);
        ArmaADistancia cuervoCristal = new ArmaADistancia("Cuervo de Cristal", 55, 1.1, 0.20);
       
        // El juego decide aleatoriamente que arma escoge
        if (Math.random() < 0.5) {
            yennefer.equiparArma(dagaPlata);
        } else {
            yennefer.equiparArma(cuervoCristal);
        }
        return yennefer;
    }

	

	private static Personajes crearGeralt() {
		// TODO Auto-generated method stub
		Guerrero geralt = new Guerrero("Geralt de Rivia", 800, 100, 85, 50, 0, 20, 0.20);

        // Sus 2 armas
        ArmaCuerpoACuerpo aerondight = new ArmaCuerpoACuerpo("Aerondight", 80, 1.2, 0.20);
        ArmaADistancia ballestaUrsina = new ArmaADistancia("Ballesta Ursina", 50, 1.0, 0.15);

        // El juego decide aleatoriamente que arma escoge
        if (Math.random() < 0.5) {
            geralt.equiparArma(aerondight);
        } else {
            geralt.equiparArma(ballestaUrsina);
        }
        return geralt;
    }

	
	public static ArrayList<Personajes> crearEquipoMalo() {
		// TODO Auto-generated method stub
		// equipo malo
		ArrayList<Personajes> equipo = new ArrayList<>();
        equipo.add(crearImlerith());
        equipo.add(crearCaranthir());
        equipo.add(crearEredin());
        return equipo;
	}

	private static Personajes crearEredin() {
		// TODO Auto-generated method stub
		Sacerdote eredin = new Sacerdote("Eredin", 750, 200, 60, 40, 60, 15);

		// Sus 2 armas

        ArmaCuerpoACuerpo espadaCaceria = new ArmaCuerpoACuerpo("Espada de la Cacería", 75, 1.1, 0.15);
        ArmaADistancia proyeccionEspectral = new ArmaADistancia("Proyección Espectral", 55, 1.0, 0.20);

        // El juego decide aleatoriamente que arma escoge

        if (Math.random() < 0.5) {
            eredin.equiparArma(espadaCaceria);
        } else {
            eredin.equiparArma(proyeccionEspectral);
        }
        return eredin;
    }



	private static Personajes crearCaranthir() {
		// TODO Auto-generated method stub
		Mago caranthir = new Mago("Caranthir", 500, 280, 25, 15, 90, 1.3);

		// Sus 2 armas

        ArmaCuerpoACuerpo baculoNavegador = new ArmaCuerpoACuerpo("Báculo de Navegador", 35, 1.0, 0.10);
        ArmaADistancia orbeEscarcha = new ArmaADistancia("Orbe de Escarcha Blanca", 65, 1.2, 0.25);

        // El juego decide aleatoriamente que arma escoge

        if (Math.random() < 0.5) {
            caranthir.equiparArma(baculoNavegador);
        } else {
            caranthir.equiparArma(orbeEscarcha);
        }
        return caranthir;	}

	private static Personajes crearImlerith() {
		// TODO Auto-generated method stub
		Guerrero imlerith = new Guerrero("Imlerith", 1000, 50, 90, 70, 0, 30, 0.15);
		
		// Sus 2 armas

        ArmaCuerpoACuerpo mazaGigante = new ArmaCuerpoACuerpo("Maza Gigante", 100, 1.3, 0.15);
        ArmaADistancia hachaArrojadiza = new ArmaADistancia("Hacha Arrojadiza", 60, 1.0, 0.10);

        // El juego decide aleatoriamente que arma escoge
        
        if (Math.random() < 0.5) {
            imlerith.equiparArma(mazaGigante);
        } else {
            imlerith.equiparArma(hachaArrojadiza);
        }
        return imlerith;
	}
}