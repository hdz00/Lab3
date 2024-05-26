package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import config.DatabaseConfig;

public class FaturaDAO {
    private static final String INSERT_FATURA_SQL = "INSERT INTO fatura (id_categoria, valor, nif) VALUES (?, ?, ?)";

    public void insertFatura(int idCategoria, int valor, int nif) {
        try (Connection connection = DatabaseConfig.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FATURA_SQL)) {
            preparedStatement.setInt(1, idCategoria);
            preparedStatement.setInt(2, valor);
            preparedStatement.setInt(3, nif);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
