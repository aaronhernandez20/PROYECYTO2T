package main;

import java.util.ArrayList;
import java.util.List;
import personajes.*;
import hechizos.*;
import db.ConexionBD;
import logros.GestorLogros;
import catalogo.CatalogoPersonajes;
import combate.Combate;
import java.util.Scanner;

public class Main {

    public static GestorLogros logros = new GestorLogros();

    public static void main(String[] args) {
        ArrayList<Personajes> equipoBueno = crearEquipoBueno();
        ArrayList<Personajes> equipoMalo = crearEquipoMalo();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona modo de juego:");
        System.out.println("1. Automatico");
        System.out.println("2. Manual (controlas el equipo de Geralt)");
        int modo = scanner.nextInt();
        scanner.close();
        boolean modoManual = (modo == 2);

        Combate combate = new Combate(equipoBueno, equipoMalo, modoManual);
        combate.iniciar();
        logros.mostrarLogros();

        // PRUEBA DE CONEXION - borrar después
        List<Object[]> test = ConexionBD.consultar("SELECT nombre FROM PERSONAJES");
        System.out.println("Personajes en BD: " + test.size()); // debe imprimir 6
    }

    public static ArrayList<Personajes> crearEquipoBueno() {
        ArrayList<Personajes> equipoBueno = new ArrayList<>();

        Personajes geralt = CatalogoPersonajes.crearGeralt();
        asignarArmaAleatoria(geralt);
        geralt.agregarHechizo(new DañoDirecto("Golpe Poderoso", 30, 120, 3));
        equipoBueno.add(geralt);

        Personajes yennefer = CatalogoPersonajes.crearYennefer();
        asignarArmaAleatoria(yennefer);
        yennefer.agregarHechizo(new DañoDirecto("Señal de Igni", 50, 80, 2));
        yennefer.agregarHechizo(new DañoEnElTiempo("Fuego de Vengerberg", 40, 3));
        equipoBueno.add(yennefer);

        Personajes ciri = CatalogoPersonajes.crearCiri();
        asignarArmaAleatoria(ciri);
        ciri.agregarHechizo(new CuraciónDirecta("Poción de Golondrina", 60, 200, 3));
        ciri.agregarHechizo(new CuraciónEnElTiempo("Aura de la Vieja Sangre", 50, 4));
        equipoBueno.add(ciri);

        return equipoBueno;
    }

    public static ArrayList<Personajes> crearEquipoMalo() {
        ArrayList<Personajes> equipoMalo = new ArrayList<>();

        Personajes imlerith = CatalogoPersonajes.crearImlerith();
        asignarArmaAleatoria(imlerith);
        imlerith.agregarHechizo(new DañoDirecto("Embestida Brutal", 20, 150, 4));
        equipoMalo.add(imlerith);

        Personajes caranthir = CatalogoPersonajes.crearCaranthir();
        asignarArmaAleatoria(caranthir);
        caranthir.agregarHechizo(new DañoDirecto("Lanza de Hielo", 45, 70, 2));
        caranthir.agregarHechizo(new DañoEnElTiempo("Escarcha Corrosiva", 35, 3));
        equipoMalo.add(caranthir);

        Personajes eredin = CatalogoPersonajes.crearEredin();
        asignarArmaAleatoria(eredin);
        eredin.agregarHechizo(new CuraciónDirecta("Sacrificio Oscuro", 60, 200, 3));
        eredin.agregarHechizo(new CuraciónEnElTiempo("Presencia del Rey", 50, 4));
        equipoMalo.add(eredin);

        return equipoMalo;
    }

    private static void asignarArmaAleatoria(Personajes personaje) {
        if (Math.random() < 0.5) {
            personaje.equiparArma(personaje.getArmasDisponibles().get(0));
        } else {
            personaje.equiparArma(personaje.getArmasDisponibles().get(1));
        }
    }

}
