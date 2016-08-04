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

    @FXML
    private void handleBinaryToText(ActionEvent event) {
        ExecutorService thread = Executors.newCachedThreadPool();
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

        ExecutorService thread = Executors.newCachedThreadPool();
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

        if (input.length() % 8 != 0) {
            throw new IllegalArgumentException("input must be a multiple of 8");

        }
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i += 8) {
            int charCode = Integer.parseInt(input.substring(i, i + 8), 2);
            result.append((char) charCode);
        }
        return result.toString();
    }

    // Create the Binary To Text Task
    private String asciiToBinary(String text) {
        StringBuilder result = new StringBuilder();

        for (byte ch : text.getBytes()) {
            String binary = Integer.toBinaryString(ch);
            switch (binary.length()) {
                case 4:
                    result.append("0000").append(binary);
                    break;
                case 6:
                    result.append("00").append(binary);
                    break;
                case 7:
                    result.append("0").append(binary);
                    break;
                default:
                    result.append(binary);
            }
        }

        return result.toString();
    }

}
