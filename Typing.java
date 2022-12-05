package ManyPages;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Thu Duong
 * Typing game
 */
public class Typing {
    private final String directory = "src/ManyPages/textsource/";
    private char[] str;
    private int correctWordCount = 0;
    private double accuracy = 0;
    private AnimationTimer timer;
    private boolean isFirstChar;

    @FXML
    private Pane root;
    @FXML
    private TextField inputText;
    @FXML
    private TextFlow arrayText;
    @FXML
    private Button start;
    @FXML
    private Label wpmLabel;
    @FXML
    private Label description;
    @FXML
    private Label elapsedTime;

    /**
     * Default constructor
     */
    public Typing() {
    }

    /**
     * Initialize values and welcome scene
     */
    public void initialize() {
        root.setStyle("-fx-background-color:#2b87d1");
        arrayText.setVisible(true);
        inputText.setVisible(true);
        wpmLabel.setVisible(true);
        wpmLabel.setText("Typing Test");
        wpmLabel.setLayoutY(40);
        description.setVisible(true);
        elapsedTime.setVisible(true);
        elapsedTime.setText("Start typing to begin.");
        start.setVisible(false);
        inputText.setText("");
        correctWordCount = 0;
        isFirstChar = true;

        // Set up paragraph
        String paragraph = "";
        try {
            paragraph = getRandomParagraph();
        }
        catch (IOException e) {
            System.err.println("NO FILE FOUND");
            System.exit(1);
        }
        str = paragraph.toCharArray();
        arrayText.getChildren().clear();
        for (char value : str) {
            Text letter = new Text(value + "");
            letter.setStyle("-fx-font-size: 20");
            arrayText.getChildren().addAll(letter);
        }

        // Listener for input text
        inputText.textProperty().addListener((obsv, oldValue, newValue) -> {
            int newLength = newValue.length();
            int oldLength = oldValue.length();
            char[] tempChar = newValue.toCharArray();

            if (newLength < oldLength || newLength == 0) {
                Text c = (Text) arrayText.getChildren().get(newLength);
                c.setStyle("-fx-font-size: 20; -fx-fill: black");
                if (newLength != 0) {
                    correctWordCount--;
                    if (str[newLength] == ' ') {
                        c.setText(" ");
                    }
                }
            }

            // Check every character typed
            if (newLength > 0 && newLength <= arrayText.getChildren().size()) {
                correctWordCount = 0;
                for (int i = 0; i < tempChar.length; i++) {
                    Text c = (Text) arrayText.getChildren().get(i);
                    if (tempChar[i] == str[i]) {
                        c.setStyle("-fx-font-size: 20; -fx-fill: green");
                        correctWordCount++;
                    }
                    else {
                        c.setStyle("-fx-font-size: 20; -fx-fill: red");
                    }
                    if (str[i] == ' ') {
                        c.setText(" ");
                    }
                }

                // CALCULATE ACCURACY
                int inputLength = inputText.getText().length();
                accuracy = (double) correctWordCount / inputLength + 1;
            }

            // Show result if finish
            if (newLength >= str.length) {
                timer.stop();
                showResult();
            }
        });

        // Start timer for first character typed and prevent text selection
        inputText.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (isFirstChar) {
                startTimer();
                isFirstChar = false;
            }
            if (!inputText.getSelectedText().isEmpty()) {
                inputText.deselect();
            }
        });

        Platform.runLater(() -> inputText.requestFocus());
    }

    /**
     * Get ramdom paragraph from src
     * @return a paragraph
     * @throws IOException exception for reading file
     */
    private String getRandomParagraph() throws IOException {
        int rand = ThreadLocalRandom.current().nextInt(4);
        String path = directory + "Paragraph" + rand + ".txt";

        File file = new File(path);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");

        return sc.next();
    }

    /**
     * Show result when finish or time is too long
     */
    private void showResult() {
        root.setStyle("-fx-background-size: 1000 600;" +
                "-fx-background-image: url('/Images/Typing/result.jpg')");
        arrayText.setVisible(false);
        inputText.setVisible(false);
        description.setVisible(false);
        elapsedTime.setVisible(false);
        start.setVisible(true);
        wpmLabel.setLayoutY(250);
    }

    /**
     * Start, try again button
     */
    public void typingClicked() {
        initialize();

        arrayText.setVisible(true);
        inputText.setVisible(true);
        wpmLabel.setVisible(true);
        elapsedTime.setVisible(true);
        start.setVisible(false);
    }

    /**
     * Turn on timer
     */
    private void startTimer() {
        timer = new AnimationTimer() {
            private long startTime = -1;
            @Override
            public void handle(long now) {
                if (startTime < 0) { startTime = now; }

                Duration elapsed = Duration.ofNanos(now - startTime);
                long minutes = elapsed.toMinutes();
                long totalMilliSec = elapsed.toMillis();
                long seconds = elapsed.toSeconds() - (minutes * 60);
                String str = String.format("%02d:%02d", minutes, seconds);

                // Update time per second
                elapsedTime.setText(str);

                // Update WPM result per second
                if (totalMilliSec > 0) {
                    int result = calculateWPM(totalMilliSec);
                    wpmLabel.setText(result + " wpm");
                }

                // Show result if time is too long (1hr)
                if (totalMilliSec == 3_600_000) {
                    timer.stop();
                    showResult();
                }
            }
        };
        timer.start();
    }

    /**
     * Calculate word per minute
     * @param milliSec elapsed time in second
     * @return wpm
     */
    private int calculateWPM(long milliSec) {

        return (int) (correctWordCount * 5000 / milliSec * accuracy);
    }

    /**
     * Back to home page button
     * @throws IOException Stop program if cannot go back to homepage
     */
    public void backToHomePage() throws IOException {
        Home.backToHome(root);
    }
}
