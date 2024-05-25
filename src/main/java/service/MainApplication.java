package service;

 

import dao.FaturaDAO;

import javafx.application.Application;

import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;

import javafx.stage.Stage;

 

import java.io.File;

 

public class MainApplication extends Application {

 

    public static void main(String[] args) {

        launch(args);

    }

 

    @Override

    public void start(Stage primaryStage) {

        // Create a button for uploading QRCode image file

        Button uploadButton = new Button("Upload QRCode image file");

        uploadButton.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Escolha o arquivo de imagem");

            fileChooser.getExtensionFilters().addAll(

                    new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.gif")

            );

 

            // Show the file chooser dialog

            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {

                // If a file was selected, call the method to process the QR code

                processQRCodeImage(selectedFile);

            }

        });

 

        // Add the button to the layout

        VBox layout = new VBox(10);

        layout.getChildren().add(uploadButton);

 

        // Set up the scene and stage

        Scene scene = new Scene(layout, 300, 200);

        primaryStage.setScene(scene);

        primaryStage.setTitle("QRCode Uploader");

        primaryStage.show();

    }

 

    private void processQRCodeImage(File qrCodeImage) {

        try {

            // Extract data from the QR code

            String[] extractedData = QRCodeDataExtractor.extractDataFromQRCode(qrCodeImage);

 

            int idCategoria = Integer.parseInt(extractedData[0]);

            int valor = Integer.parseInt(extractedData[1]);

            int nif = Integer.parseInt(extractedData[2]);

 

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