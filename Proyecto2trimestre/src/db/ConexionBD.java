/**
 * Clases encargadas de la persistencia de datos, conexión a la base de datos MySQL,
 * y guardado/carga del estado de las partidas.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase ConexionBD es una utilidad centralizada para gestionar la comunicación 
 * con la base de datos MySQL mediante JDBC.
 * <p>
 * Proporciona métodos simplificados para establecer la conexión, ejecutar sentencias 
 * de modificación de datos (INSERT, UPDATE, DELETE) y realizar consultas (SELECT), 
 * mapeando automáticamente los parámetros y los resultados.
 * </p>
 */
public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/proyecto_rpg";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    /**
     * Establece y devuelve una nueva conexión abierta a la base de datos.
     * * @return Objeto Connection activo.
     * @throws SQLException Si ocurre un error de acceso a la base de datos o la URL es incorrecta.
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    /**
     * Ejecuta una sentencia SQL de modificación de datos (INSERT, UPDATE o DELETE).
     * * @param sql La sentencia SQL parametrizada a ejecutar (con signos '?').
     * @param params Lista de objetos con los parámetros a inyectar en la consulta.
     * @return El número de filas afectadas por la ejecución, o 0 si ocurre un error.
     */
    public static int ejecutar(String sql, List<Object> params) {
        try (Connection con = getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            setParams(ps, params);
            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Ejecuta una sentencia SQL de inserción (INSERT) y recupera el identificador 
     * autogenerado (AUTO_INCREMENT) por la base de datos para ese nuevo registro.
     * * @param sql La sentencia SQL parametrizada a ejecutar.
     * @param params Lista de objetos con los parámetros a inyectar.
     * @return El ID generado para el nuevo registro, o -1 si ocurre un error o no se generó ID.
     */
    public static int ejecutarYObtenerId(String sql, List<Object> params) {
        try (Connection con = getConexion();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setParams(ps, params);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Ejecuta una consulta SQL de lectura (SELECT) y mapea los resultados devueltos.
     * Cada fila resultante se convierte en un array de Objetos.
     * * @param sql La consulta SQL parametrizada a ejecutar.
     * @param params Lista de objetos con los parámetros a inyectar en el WHERE u otras cláusulas.
     * @return Una lista de arrays de Objetos, donde cada array representa una fila 
     * y cada índice corresponde a una columna de la tabla (ej. fila[0], fila[1]).
     */
    public static List<Object[]> consultar(String sql, List<Object> params) {
        List<Object[]> resultado = new ArrayList<>();

        try (Connection con = getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            setParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnas = meta.getColumnCount();

                while (rs.next()) {
                    Object[] fila = new Object[columnas];
                    for (int i = 0; i < columnas; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    resultado.add(fila);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    /**
     * Sobrecarga del método consultar para ejecutar consultas SQL (SELECT) estáticas,
     * es decir, aquellas que no requieren inyección de parámetros dinámicos.
     * * @param sql La consulta SQL a ejecutar.
     * @return Una lista con las filas devueltas por la base de datos.
     */
    public static List<Object[]> consultar(String sql) {
        return consultar(sql, new ArrayList<>());
    }

    /**
     * Método auxiliar privado que asigna dinámicamente los valores de la lista de parámetros 
     * a las incógnitas ('?') del PreparedStatement en el orden correcto.
     * * @param ps El PreparedStatement a configurar.
     * @param params La lista de valores a inyectar.
     * @throws SQLException Si ocurre un error al asignar un valor o no coincide con los tipos esperados.
     */
    private static void setParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }

    /**
     * Método de conveniencia (varargs) que permite crear rápidamente una lista de 
     * parámetros en una sola línea para pasarla a los métodos ejecutar() o consultar().
     * <p>
     * Ejemplo de uso: {@code ConexionBD.params(idJugador, nombre, 10)}
     * </p>
     * * @param valores Argumentos variables con los valores a empaquetar.
     * @return Una lista (List&lt;Object&gt;) que contiene los valores proporcionados.
     */
    public static List<Object> params(Object... valores) {
        List<Object> lista = new ArrayList<>();
        for (Object v : valores)
            lista.add(v);
        return lista;
    }
}