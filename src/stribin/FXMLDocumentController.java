/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stribin;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Aamir khan
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    TextArea asciiTextArea;
    @FXML
    TextArea binaryTextArea;
    @FXML
    StackPane animationPane;
    private static final int AMOUNT_OF_BITS = 8;

    @FXML
    private void handleBinaryToText(ActionEvent event) {
        ExecutorService thread = Executors.newFixedThreadPool(1);
        thread.submit(() -> {
            final String result = binaryToAscii(binaryTextArea.getText());
            Platform.runLater(() -> {
                asciiTextArea.setText(result);
            });
        });
        thread.shutdown();
    }

    @FXML
    private void handleTextToBinary(ActionEvent event) {

        ExecutorService thread = Executors.newFixedThreadPool(1);
        thread.submit(() -> {
            final String result = asciiToBinary(asciiTextArea.getText());
            Platform.runLater(() -> {
                binaryTextArea.setText(result);
            });
        });
        thread.shutdown();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Create the Binary To Text Task task
    protected String binaryToAscii(final String input) {

        if (input.length() % AMOUNT_OF_BITS != 0) {
            throw new IllegalArgumentException("input must be a multiple of 8");

        }
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i += AMOUNT_OF_BITS) {
            int charCode = Integer.parseInt(input.substring(i, i + AMOUNT_OF_BITS), 2);
            result.append((char) charCode);
        }
        return result.toString();
    }

    // Create the Binary To Text Task
    private String asciiToBinary(String text) {
        final byte[] bytes = text.getBytes();
        StringBuilder result = new StringBuilder(bytes.length * 8);
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < AMOUNT_OF_BITS; i++) {
                result.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }

        }

        return result.toString();
    }

}
