package db.visualizacion;

import java.util.ArrayList;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler.LegendPosition;

import catalogo.CatalogoPersonajes;
import personajes.Personajes;

/**
 * La clase EstadisticasVisualizar se encarga de mostrar visualmente
 * la vida máxima inicial de cada personaje mediante un gráfico de barras.
 * <p>
 * El gráfico compara los 6 personajes del juego, divididos en dos series
 * de datos: "Equipo bueno" y "Equipo malo".
 * </p>
 * <p>
 * Para usarlo desde Main basta con invocar: {@code EstadisticasVisualizar.mostrarVidaInicial();}
 * <br>
 * Requiere tener configurada la dependencia de XChart en el archivo pom.xml 
 * (org.knowm.xchart, versión 3.8.7).
 * </p>
 */
public class EstadisticasVisualizar {

    /**
     * Genera y muestra un gráfico de barras con la vida máxima de los personajes.
     * Instancia los personajes desde el catálogo, separa sus estadísticas según 
     * su equipo y renderiza la ventana del gráfico utilizando la librería XChart.
     */
    public static void mostrarVidaInicial() {

        // 1. Crear los 6 personajes desde el catalogo
        Personajes geralt = CatalogoPersonajes.crearGeralt();
        Personajes yennefer = CatalogoPersonajes.crearYennefer();
        Personajes ciri = CatalogoPersonajes.crearCiri();
        Personajes imlerith = CatalogoPersonajes.crearImlerith();
        Personajes caranthir = CatalogoPersonajes.crearCaranthir();
        Personajes eredin = CatalogoPersonajes.crearEredin();

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
        grafico.addSeries("Equipo malo", nombres, vidaMalos);

        // 5. Mostrar el grafico en una ventana
        new SwingWrapper<>(grafico).displayChart();
    }

    /** 
     * Método principal secundario que permite ejecutar este visualizador de forma 
     * independiente para realizar pruebas, sin necesidad de arrancar el juego completo.
     * 
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        mostrarVidaInicial();
    }
}