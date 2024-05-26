package service;

import java.io.File;

import dao.FaturaDAO;

public class processQRCodeImage {
    private void processQRCodeImage(File qrCodeImage) {
        try {
            // Extract data from the QR code
            String[] extractedData = QRCodeDataExtractor.extractDataFromQRCode(qrCodeImage);
            int idCategoria = Integer.parseInt(extractedData[0]);
            int valor = Integer.parseInt(extractedData[1]);
            int nif = Integer.parseInt(extractedData[2]);
            String ficheiro = qrCodeImage.getName();
            // Insert data into fatura table
            FaturaDAO faturaDAO = new FaturaDAO();
            faturaDAO.insertFatura(idCategoria, valor, nif, ficheiro);

            // Display aggregated data
            DataDisplay dataDisplay = new DataDisplay();
            String aggregatedData = dataDisplay.getAggregatedData();
            System.out.println(aggregatedData); // Display aggregated data in console or handle as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
