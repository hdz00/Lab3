/* package service;

import java.io.File;
import dao.FaturaDAO;
 */
package service;
import dao.FaturaDAO;
import java.io.File;
/* import java.util.Optional;
 */
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

 
public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Criar um seletor de arquivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha o arquivo de imagem");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.gif")
        );

        // Exibir o seletor de arquivo e obter o arquivo selecionado
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            // Se um arquivo foi selecionado, chame o método para extrair dados do QR code
            processQRCodeImage(selectedFile);
        }
    }

    private void processQRCodeImage(File qrCodeImage) {
        try {
            // Extrair dados do QR code
            String[] extractedData = QRCodeDataExtractor.extractDataFromQRCode(qrCodeImage);

            int idCategoria = Integer.parseInt(extractedData[0]);
            int valor = Integer.parseInt(extractedData[1]);
            int nif = Integer.parseInt(extractedData[2]);
/*             String nif = extractedData[2];
 */
            // Insert data into fatura table
            FaturaDAO faturaDAO = new FaturaDAO();
            faturaDAO.insertFatura(idCategoria, valor, nif);

            // Display aggregated data
            DataDisplay dataDisplay = new DataDisplay();
            dataDisplay.displayAggregatedData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}