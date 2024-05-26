package service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import config.DatabaseConfig;

public class DataDisplay {
    public String getAggregatedData() {
        StringBuilder dataBuilder = new StringBuilder();

        String selectCategoriasSQL = "SELECT * FROM categorias";
        String selectFaturaSQL = "SELECT * FROM fatura";
        String consolidatedDataSQL = "SELECT COUNT(*) AS num_faturas, SUM(valor) AS total_gasto, SUM(valor_iva) AS total_valor_iva, SUM(iva_restituicao) AS total_iva_restituido "
                +
                "FROM fatura";
        String totalGastoPorCategoriaSQL = "SELECT id_categoria, SUM(valor) AS total_gasto_por_categoria " +
                "FROM fatura " +
                "GROUP BY id_categoria";
        String totalIvaPorTaxaSQL = "SELECT taxa_iva, SUM(valor_iva) AS total_valor_iva_por_taxa " +
                "FROM fatura " +
                "JOIN categorias ON fatura.id_categoria = categorias.id " +
                "GROUP BY taxa_iva";

        try (Connection connection = DatabaseConfig.getConnection();
                Statement statement = connection.createStatement()) {

            // Fetch and append categories data
            ResultSet rsCategorias = statement.executeQuery(selectCategoriasSQL);
            dataBuilder.append("Categorias:\n");
            while (rsCategorias.next()) {
                dataBuilder.append("ID: ").append(rsCategorias.getInt("id"))
                        .append(", Nome: ").append(rsCategorias.getString("nome"))
                        .append(", Total Despesas: ").append(rsCategorias.getInt("total_despesas"))
                        .append(", Teto Despesa: ").append(rsCategorias.getInt("teto_despesa"))
                        .append(", Taxa IVA: ").append(rsCategorias.getBigDecimal("taxa_iva"))
                        .append(", Dedutível IRS: ").append(rsCategorias.getBoolean("dedutivel_irs"))
                        .append(", Taxa Dedução IVA: ").append(rsCategorias.getBigDecimal("taxa_deducao_iva"))
                        .append("\n");
            }

            // Fetch and append invoices data
            ResultSet rsFatura = statement.executeQuery(selectFaturaSQL);
            dataBuilder.append("\nFaturas:\n");
            while (rsFatura.next()) {
                dataBuilder.append("ID: ").append(rsFatura.getInt("id"))
                        .append(", ID Categoria: ").append(rsFatura.getInt("id_categoria"))
                        .append(", Valor: ").append(rsFatura.getInt("valor"))
                        .append(", NIF: ").append(rsFatura.getString("nif"))
                        .append(", Valor IVA: ").append(rsFatura.getInt("valor_iva"))
                        .append(", IVA Restituição: ").append(rsFatura.getBigDecimal("iva_restituicao"))
                        .append("\n");
            }

            // Fetch and append consolidated data
            ResultSet rsConsolidated = statement.executeQuery(consolidatedDataSQL);
            if (rsConsolidated.next()) {
                dataBuilder.append("\nConsolidated Data:\n");
                dataBuilder.append("Número de faturas armazenadas: ").append(rsConsolidated.getInt("num_faturas"))
                        .append("\n");
                dataBuilder.append("Gasto total geral: ").append(rsConsolidated.getInt("total_gasto")).append("\n");
                dataBuilder.append("Valor total do IVA: ").append(rsConsolidated.getInt("total_valor_iva"))
                        .append("\n");
                dataBuilder.append("Valor total do IVA restituído: ")
                        .append(rsConsolidated.getBigDecimal("total_iva_restituido")).append("\n");
            }

            // Fetch and append total expenses per category
            ResultSet rsTotalGastoPorCategoria = statement.executeQuery(totalGastoPorCategoriaSQL);
            dataBuilder.append("\nGasto total por categoria:\n");
            while (rsTotalGastoPorCategoria.next()) {
                dataBuilder.append("ID Categoria: ").append(rsTotalGastoPorCategoria.getInt("id_categoria"))
                        .append(", Total Gasto: ").append(rsTotalGastoPorCategoria.getInt("total_gasto_por_categoria"))
                        .append("\n");
            }

            // Fetch and append total VAT per rate
            ResultSet rsTotalIvaPorTaxa = statement.executeQuery(totalIvaPorTaxaSQL);
            dataBuilder.append("\nValor do IVA total a cada uma das taxas:\n");
            while (rsTotalIvaPorTaxa.next()) {
                dataBuilder.append("Taxa IVA: ").append(rsTotalIvaPorTaxa.getBigDecimal("taxa_iva"))
                        .append(", Total Valor IVA: ").append(rsTotalIvaPorTaxa.getInt("total_valor_iva_por_taxa"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataBuilder.toString();
    }
}
