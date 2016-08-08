package stribin;

import java.net.URL;
import javafx.fxml.FXML;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import java.util.concurrent.Callable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;

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
        Callable<String> task = () -> binaryToAscii(binaryTextArea.getText());
        try {
            runTask(task, asciiTextArea);
        } catch (InterruptedException | ExecutionException ex) {
            asciiTextArea.setText(ex.toString());
        }

    }

    @FXML
    private void handleTextToBinary(ActionEvent event) {
        Callable<String> task = () -> asciiToBinary(asciiTextArea.getText());
        try {
            runTask(task, binaryTextArea);
        } catch (InterruptedException | ExecutionException ex) {
            binaryTextArea.setText(ex.toString());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Validate the Binary TextArea
        final Pattern binaryRegex = Pattern.compile("\\A[01\\n]*\\Z");
        Predicate<String> tester = binaryRegex.asPredicate();
        binaryTextArea.setTextFormatter(new TextFormatter<>(change -> {
            if (!tester.test(change.getControlNewText())) {
                return null;
            }
            return change;
        }));
//        binaryTextArea.setTextFormatter();

    }

    // Create the Binary To Text Task task
    private String binaryToAscii(final String input) {

        if (input.length() % AMOUNT_OF_BITS != 0) {
            String msg = "Input must be a multiple of " + AMOUNT_OF_BITS;
            binaryTextArea.setText(msg);
            throw new IllegalArgumentException(msg);
        }
        final int INPUT_LEN = input.length();
        final int BUILDER_SIZE = INPUT_LEN / 8;
        StringBuilder result = new StringBuilder(BUILDER_SIZE);

        for (int i = 0; i < INPUT_LEN; i += AMOUNT_OF_BITS) {
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

    private void runTask(Callable<String> task, TextArea textArea) throws InterruptedException, ExecutionException {
        ExecutorService thread = Executors.newFixedThreadPool(1);
        final String result = thread.submit(task).get();
        Platform.runLater(() -> {
            textArea.setText(result);
        });
        thread.shutdown();
    }

}
