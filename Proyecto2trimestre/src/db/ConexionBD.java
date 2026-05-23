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

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/proyecto_rpg";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    // Devuelve una conexion abierta
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    // INSERT / UPDATE / DELETE → devuelve filas afectadas
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

    // INSERT → devuelve el ID generado (AUTO_INCREMENT)
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

    // SELECT → cada fila es un Object[], accedes por indice: fila[0], fila[1]...
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

    // Sobrecarga sin parametros (para SELECTs sin WHERE dinamico)
    public static List<Object[]> consultar(String sql) {
        return consultar(sql, new ArrayList<>());
    }

    // Asigna los ? del PreparedStatement en orden
    private static void setParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }

    // Crea la lista de parametros para pasar a ejecutar() o consultar()
    public static List<Object> params(Object... valores) {
        List<Object> lista = new ArrayList<>();
        for (Object v : valores) lista.add(v);
        return lista;
    }
}