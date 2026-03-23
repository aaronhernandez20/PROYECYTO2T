package catalogo;

import armas.ArmaCuerpoACuerpo;
import armas.ArmaADistancia;
import personajes.*;

public class CatalogoPersonajes {

    // EQUIPO BUENO 

    public static Personajes crearGeralt() {
        Guerrero geralt = new Guerrero("Geralt de Rivia", 800, 100, 85, 50, 0, 20, 0.20);
        geralt.agregarArmaDisponible(new ArmaCuerpoACuerpo("Aerondight", 80, 1.2, 0.20));
        geralt.agregarArmaDisponible(new ArmaADistancia("Ballesta Ursina", 50, 1.0, 0.15));
        return geralt;
    }

    public static Personajes crearYennefer() {
        Mago yennefer = new Mago("Yennefer de Vengerberg", 400, 300, 30, 20, 100, 1.5);
        yennefer.agregarArmaDisponible(new ArmaCuerpoACuerpo("Daga de Plata", 30, 1.0, 0.10));
        yennefer.agregarArmaDisponible(new ArmaADistancia("Cuervo de Cristal", 55, 1.1, 0.20));
        return yennefer;
    }

    public static Personajes crearCiri() {
        Sacerdote ciri = new Sacerdote("Ciri", 600, 200, 70, 35, 50, 20);
        ciri.agregarArmaDisponible(new ArmaCuerpoACuerpo("Zireael", 70, 1.1, 0.30));
        ciri.agregarArmaDisponible(new ArmaADistancia("Amuleto Mágico", 45, 1.0, 0.15));
        return ciri;
    }

    // EQUIPO MALO

    public static Personajes crearImlerith() {
        Guerrero imlerith = new Guerrero("Imlerith", 1000, 50, 90, 70, 0, 30, 0.15);
        imlerith.agregarArmaDisponible(new ArmaCuerpoACuerpo("Maza Gigante", 100, 1.3, 0.15));
        imlerith.agregarArmaDisponible(new ArmaADistancia("Hacha Arrojadiza", 60, 1.0, 0.10));
        return imlerith;
    }

    public static Personajes crearCaranthir() {
        Mago caranthir = new Mago("Caranthir", 500, 280, 25, 15, 90, 1.3);
        caranthir.agregarArmaDisponible(new ArmaCuerpoACuerpo("Báculo de Navegador", 35, 1.0, 0.10));
        caranthir.agregarArmaDisponible(new ArmaADistancia("Orbe de Escarcha Blanca", 65, 1.2, 0.25));
        return caranthir;
    }

    public static Personajes crearEredin() {
        Sacerdote eredin = new Sacerdote("Eredin", 750, 200, 60, 40, 60, 15);
        eredin.agregarArmaDisponible(new ArmaCuerpoACuerpo("Espada de la Cacería", 75, 1.1, 0.15));
        eredin.agregarArmaDisponible(new ArmaADistancia("Proyección Espectral", 55, 1.0, 0.20));
        return eredin;
    }
}