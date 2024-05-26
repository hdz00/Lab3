package service;

import dao.FaturaDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainApplicationGUI {
    public static void main(String[] args) {
        // Create a new frame
        JFrame frame = new JFrame("Hello World Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Create a label to display the message
        JLabel messageLabel = new JLabel("Hello, World!", SwingConstants.CENTER);

        // Create a label to display the selected image
        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);

        // Create a button to open file chooser
        JButton openButton = new JButton("Open Image");

        // Create a label to display QR Code data
        JLabel qrCodeDataLabel = new JLabel("", SwingConstants.CENTER);

        // Create an instance of FaturaDAO
        FaturaDAO faturaDAO = new FaturaDAO();

        // Set up the file chooser button action
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a file chooser
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(frame);

                // If a file is selected
                if (option == JFileChooser.APPROVE_OPTION) {
                    // Get the selected file
                    File file = fileChooser.getSelectedFile();

                    // Check if the file is an image
                    if (file != null && file.exists()) {
                        // Set the image to the label
                        imageLabel.setIcon(new ImageIcon(file.getAbsolutePath()));

                        // Extract QR code data
                        try {
                            String[] qrCodeData = QRCodeDataExtractor.extractDataFromQRCode(file);
                            qrCodeDataLabel.setText("ID Categoria: " + qrCodeData[0] + ", Valor: " + qrCodeData[1]);

                            // Parse the data
                            int idCategoria = Integer.parseInt(qrCodeData[0]);
                            int valor = Integer.parseInt(qrCodeData[1]);
                            int nif = Integer.parseInt(qrCodeData[2]);

                            // Call FaturaDAO to insert the data
                            faturaDAO.insertFatura(idCategoria, valor, nif);

                            JOptionPane.showMessageDialog(frame, "Fatura inserted successfully!", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException ioException) {
                            JOptionPane.showMessageDialog(frame,
                                    "Failed to decode QR code: " + ioException.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } catch (NumberFormatException numberFormatException) {
                            JOptionPane.showMessageDialog(frame, "Invalid data format in QR code.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // If not an image, show an error message
                        JOptionPane.showMessageDialog(frame, "The selected file is not an image.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Create a panel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(messageLabel, BorderLayout.NORTH);
        panel.add(openButton, BorderLayout.CENTER);
        panel.add(imageLabel, BorderLayout.SOUTH);
        panel.add(qrCodeDataLabel, BorderLayout.AFTER_LAST_LINE);

        // Add the panel to the frame
        frame.add(panel);

        // Set the frame visibility to true
        frame.setVisible(true);
    }
}
