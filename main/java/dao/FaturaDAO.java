package dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import config.DatabaseConfig;

public class FaturaDAO {
    private static final String INSERT_FATURA_SQL = "INSERT INTO fatura (id_categoria, valor, nif, nome_ficheiroqr) VALUES (?, ?, ?, ?)";
    private static final String SELECT_FATURA_SQL = "SELECT * FROM fatura";

    // Specify the path where you want the CSV file to be created
    private static final String CSV_FILE_PATH = "csv/fatura_export.csv"; // Change
                                                                         // this
                                                                         // to
                                                                         // your
                                                                         // desired
                                                                         // path

    public void insertFatura(int idCategoria, int valor, int nif, String ficheiro) {
        try (Connection connection = DatabaseConfig.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FATURA_SQL)) {
            preparedStatement.setInt(1, idCategoria);
            preparedStatement.setInt(2, valor);
            preparedStatement.setInt(3, nif);
            preparedStatement.setString(4, ficheiro);
            preparedStatement.executeUpdate();

            // Export the data to CSV after inserting the record
            exportFaturaToCSV();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exportFaturaToCSV() {
        try (Connection connection = DatabaseConfig.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FATURA_SQL);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            // Ensure the directory exists
            File csvFile = new File(CSV_FILE_PATH);
            File csvDir = csvFile.getParentFile();
            if (csvDir != null && !csvDir.exists()) {
                csvDir.mkdirs();
            }

            try (FileWriter csvWriter = new FileWriter(CSV_FILE_PATH)) {
                // Write CSV header
                csvWriter.append("id,id_categoria,valor,nif,nome_ficheiroqr,data_ficheiroqr\n");

                // Write CSV rows
                while (resultSet.next()) {
                    csvWriter.append(String.valueOf(resultSet.getInt("id"))).append(",");
                    csvWriter.append(String.valueOf(resultSet.getInt("id_categoria"))).append(",");
                    csvWriter.append(String.valueOf(resultSet.getInt("valor"))).append(",");
                    csvWriter.append(String.valueOf(resultSet.getInt("nif"))).append(",");
                    csvWriter.append(resultSet.getString("nome_ficheiroqr")).append(",");
                    csvWriter.append(resultSet.getString("data_ficheiroqr")).append("\n");
                }

                csvWriter.flush();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
