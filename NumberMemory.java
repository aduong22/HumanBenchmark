package ManyPages;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Thu Duong
 * Number memory game
 */
public class NumberMemory {
    private final int maxNumDigit = 18;
    private int numDigit = 1;
    private int correctDigit = 0;

    @FXML
    private Pane root;
    @FXML
    private Label digitList;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TextField inputText;
    @FXML
    private Button submit;
    @FXML
    private Button start;
    @FXML
    private TextFlow checkedText;
    @FXML
    private Label description;
    @FXML
    private Label numberLabel;
    @FXML
    private Label answLabel;
    @FXML
    private Label level;
    @FXML
    private Pane mask;

    /**
     * Default constructor
     */
    public NumberMemory() {
    }

    /**
     * Initialize scene state
     */
    public void initialize() {
        root.setStyle("-fx-background-size: 1000 600;" +
                "-fx-background-image: url('/Images/NumberMemory/welcome.jpg')");

        progressBar.setProgress(1);
        progressBar.setVisible(false);
        inputText.setVisible(false);
        submit.setVisible(false);
        level.setVisible(false);
        checkedText.setVisible(false);
        numberLabel.setVisible(false);
        answLabel.setVisible(false);
        description.setVisible(false);

        digitList.setText("");
        checkedText.getChildren().clear();
        digitList.setLayoutY(200);
        inputText.setText("");
        start.setPrefWidth(80);
        start.setLayoutX(460);
    }

    /**
     * Start, Next, and Continue
     */
    public void numberMemoryClicked() {
        correctDigit = 0;

        if (start.getText().equals("Try again")) {
            numDigit = 1;
            start.setText("Start");
            initialize();
            return;
        }
        else if (start.getText().equals("Next")) {
            numDigit++;
        }
        else {
            start.setText("Next");
        }


        initialize();
        if (start.getText().equals("Next")) {
            root.setStyle("-fx-background-color: #2b87d1");
        }
        setNextNumber();
        showNextNumber();

        progressBar.setVisible(true);
        start.setVisible(false);
    }

    /**
     * Set next number with given digits
     */
    private void setNextNumber() {
        String num = "";
        for (int i = 0; i < numDigit; i++) {
            int rand;
            if (i == 0) {
                rand = ThreadLocalRandom.current().nextInt(1,10);
            }
            else {
                rand = ThreadLocalRandom.current().nextInt(10);
            }

            num += rand + "";
            digitList.setText(num + "");
        }
    }

    /**
     * Show next number in a short time
     */
    private void showNextNumber() {
        int time = digitList.getText().length() * 1000;

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(progressBar.progressProperty(), 0);
        KeyFrame keyFrame = new KeyFrame(new Duration(time), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
        timeline.setOnFinished(event -> {
            digitList.setVisible(false);
            progressBar.setVisible(false);
            description.setVisible(true);
            inputText.setVisible(true);
            submit.setVisible(true);
            inputText.requestFocus();
        });
    }

    /**
     * Compare digits when click submit
     */
    public void compareDigits() {
        String str = inputText.getText();
        String digit = digitList.getText();
        if (str.length() == 0) { return; }
        correctDigit = 0;

        // High light error if there is any
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            Text inputChar = new Text(c + "");
            inputChar.setStyle("-fx-font-size: 50;");

            if (i >= numDigit) {
                inputChar.setStrikethrough(true);
            }
            else {
                char d = digit.charAt(i);
                if ((c != d)) {
                    inputChar.setStrikethrough(true);
                }
                else {
                    correctDigit++;
                    inputChar.setStyle("-fx-font-size: 50; -fx-fill: white");
                }
            }

            checkedText.getChildren().add(inputChar);
        }

        // highlight character typed
        if (correctDigit != numDigit) {
            mask.setStyle("-fx-background-color: red");
        }
        else {
            mask.setStyle("-fx-background-color: white");
        }
        mask.setVisible(true);
        submit.setVisible(false);
        inputText.setFocusTraversable(false);
        FadeTransition ft = new FadeTransition(Duration.millis(1500), mask);
        ft.setFromValue(0.6);
        ft.setToValue(0);
        ft.play();
        ft.setOnFinished(e -> {
            mask.setVisible(false);
            inputText.setFocusTraversable(true);
            showResult();
        });
    }

    /**
     * Show result after comparing
     */
    private void showResult() {
        start.setVisible(true);
        checkedText.setVisible(true);
        level.setVisible(true);
        inputText.setVisible(false);
        description.setVisible(false);
        digitList.setVisible(true);
        digitList.setLayoutY(80);
        numberLabel.setVisible(true);
        answLabel.setVisible(true);

        if (correctDigit != numDigit || numDigit == maxNumDigit) {
            start.setText("Try again");
            start.setPrefWidth(150);
            start.setLayoutX(425);
        }
        else {
            start.setText("Next");
        }

        level.setText("Level " + numDigit);
    }

    /**
     * Back to home page button
     * @throws IOException Stop program if cannot go back to homepage
     */
    public void backToHomePage() throws IOException {
        Home.backToHome(root);
    }
}
