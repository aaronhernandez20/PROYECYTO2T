package visualizacion;

import java.util.ArrayList;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler.LegendPosition;

import catalogo.CatalogoPersonajes;
import personajes.Personajes;

/**
 * Visualizador de la vida maxima inicial de cada personaje.
 * 
 * Muestra un grafico de barras comparando la vida maxima de los 6 personajes
 * del juego, separados en dos series: equipo bueno y equipo malo.
 * 
 * Para usarlo desde Main:
 *     EstadisticasVisualizer.mostrarVidaInicial();
 * 
 * Requiere la dependencia XChart en pom.xml:
 *     <dependency>
 *         <groupId>org.knowm.xchart</groupId>
 *         <artifactId>xchart</artifactId>
 *         <version>3.8.7</version>
 *     </dependency>
 */
public class EstadisticasVisualizar {

    public static void mostrarVidaInicial() {

        // 1. Crear los 6 personajes desde el catalogo
        Personajes geralt    = CatalogoPersonajes.crearGeralt();
        Personajes yennefer  = CatalogoPersonajes.crearYennefer();
        Personajes ciri      = CatalogoPersonajes.crearCiri();
        Personajes imlerith  = CatalogoPersonajes.crearImlerith();
        Personajes caranthir = CatalogoPersonajes.crearCaranthir();
        Personajes eredin    = CatalogoPersonajes.crearEredin();

        // 2. Preparar los datos
        // Los 6 personajes van en el eje X.
        // Hay dos series: equipo bueno y equipo malo.
        // Para que cada barra se pinte con su color de equipo, los personajes
        // del otro equipo se ponen a 0 en la serie correspondiente.
        ArrayList<String> nombres = new ArrayList<>();
        nombres.add(geralt.getNombre());
        nombres.add(yennefer.getNombre());
        nombres.add(ciri.getNombre());
        nombres.add(imlerith.getNombre());
        nombres.add(caranthir.getNombre());
        nombres.add(eredin.getNombre());

        // Vida del equipo bueno (los del equipo malo van a 0)
        ArrayList<Integer> vidaBuenos = new ArrayList<>();
        vidaBuenos.add(geralt.getVidaMax());
        vidaBuenos.add(yennefer.getVidaMax());
        vidaBuenos.add(ciri.getVidaMax());
        vidaBuenos.add(0);
        vidaBuenos.add(0);
        vidaBuenos.add(0);

        // Vida del equipo malo (los del equipo bueno van a 0)
        ArrayList<Integer> vidaMalos = new ArrayList<>();
        vidaMalos.add(0);
        vidaMalos.add(0);
        vidaMalos.add(0);
        vidaMalos.add(imlerith.getVidaMax());
        vidaMalos.add(caranthir.getVidaMax());
        vidaMalos.add(eredin.getVidaMax());

        // 3. Construir el grafico
        CategoryChart grafico = new CategoryChartBuilder()
                .width(900)
                .height(500)
                .title("Vida maxima inicial de los personajes")
                .xAxisTitle("Personaje")
                .yAxisTitle("Vida maxima (HP)")
                .build();

        // Configuracion visual basica
        grafico.getStyler().setLegendPosition(LegendPosition.InsideNW);
        grafico.getStyler().setStacked(false);

        // 4. Añadir las dos series
        grafico.addSeries("Equipo bueno", nombres, vidaBuenos);
        grafico.addSeries("Equipo malo",  nombres, vidaMalos);

        // 5. Mostrar el grafico en una ventana
        new SwingWrapper<>(grafico).displayChart();
    }

    /** Permite ejecutar este visualizador de forma independiente para pruebas. */
    public static void main(String[] args) {
        mostrarVidaInicial();
    }
}