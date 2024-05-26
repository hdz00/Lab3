package service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import config.DatabaseConfig;

public class DataDisplay {
    public void displayAggregatedData() {
        String selectCategoriasSQL = "SELECT * FROM categorias";
        String selectFaturaSQL = "SELECT * FROM fatura";

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rsCategorias = statement.executeQuery(selectCategoriasSQL);
            System.out.println("Categorias:");
            while (rsCategorias.next()) {
                System.out.println("ID: " + rsCategorias.getInt("id") +
                                   ", Nome: " + rsCategorias.getString("nome") +
                                   ", Total Despesas: " + rsCategorias.getInt("total_despesas") +
                                   ", Teto Despesa: " + rsCategorias.getInt("teto_despesa") +
                                   ", Taxa IVA: " + rsCategorias.getBigDecimal("taxa_iva") +
                                   ", Dedutível IRS: " + rsCategorias.getBoolean("dedutivel_irs") +
                                   ", Taxa Dedução IVA: " + rsCategorias.getBigDecimal("taxa_deducao_iva"));
            }

            ResultSet rsFatura = statement.executeQuery(selectFaturaSQL);
            System.out.println("\nFaturas:");
            while (rsFatura.next()) {
                System.out.println("ID: " + rsFatura.getInt("id") +
                                   ", ID Categoria: " + rsFatura.getInt("id_categoria") +
                                   ", Valor: " + rsFatura.getInt("valor") +
                                   ", NIF: " + rsFatura.getString("nif") +
                                   ", Valor IVA: " + rsFatura.getInt("valor_iva") +
                                   ", IVA Restituição: " + rsFatura.getBigDecimal("iva_restituicao"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
