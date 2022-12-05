package ManyPages;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Thu Duong
 * Color Match game (custom game)
 */
public class ColorMatch {
    private final String style = "-fx-text-fill: ";
    private final String[] colorList;
    private String currentMeaning;
    private String currentColor;
    private AnimationTimer timer;
    private long maxTime;
    private long timeLeft;
    private int score;

    @FXML
    private Pane root;
    @FXML
    private Button start;
    @FXML
    private Label icon;
    @FXML
    private Label description;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label meaningLabel;
    @FXML
    private Label colorLabel;
    @FXML
    private Button yesBtn;
    @FXML
    private Button noBtn;

    /**
     * Default constructor, create color list
     */
    public ColorMatch() {
        colorList = new String[] {"RED","GREEN","ORANGE","BLACK"};
    }

    /**
     * Set up welcome scene
     */
    public void initialize() {
        maxTime = 45;
        timeLeft = maxTime;
        score = 0;
        root.setStyle("-fx-background-image: url('/Images/ColorMatch/welcome.jpg')");
        scoreLabel.setText("Score: 0");
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 30");
        scoreLabel.setLayoutY(50);
        icon.setVisible(false);
        description.setVisible(false);
        scoreLabel.setVisible(false);
        timeLabel.setVisible(false);
        meaningLabel.setVisible(false);
        colorLabel.setVisible(false);
        yesBtn.setVisible(false);
        noBtn.setVisible(false);

        nextColor();
    }

    /**
     * Start, try again button
     * Create game or Reset to welcome page
     */
    public void colorMatchingClicked() {
        if (start.getText().equals("Try again")) {
            start.setText("Start");
            start.setPrefWidth(100);
            start.setLayoutX(450);
            initialize();
            return;
        }

        root.setStyle("-fx-background-color: #2b87d1");
        setTimer();

        // game state
        description.setVisible(true);
        scoreLabel.setVisible(true);
        timeLabel.setVisible(true);
        meaningLabel.setVisible(true);
        colorLabel.setVisible(true);
        yesBtn.setVisible(true);
        noBtn.setVisible(true);
        start.setVisible(false);
    }

    /**
     * Check if current meaning differs current color
     */
    public void noCheck() {
        if (!currentMeaning.equals(currentColor)) {
            score++;
            scoreLabel.setText("Score: " + score);
        }
        else {
            maxTime -= 5;
            timeLeft -= 5;
            String str = String.format("00:%02d", timeLeft);
            timeLabel.setText(str);
            penaltyTimeNotif();
        }
        nextColor();
    }

    /**
     * Check if current meaning matches current color
     */
    public void yesCheck() {
        if (currentMeaning.equals(currentColor)) {
            score++;
            scoreLabel.setText("Score: " + score);
        }
        else {
            maxTime -= 5;
            timeLeft -= 5;
            String str = String.format("00:%02d", timeLeft);
            timeLabel.setText(str);
            penaltyTimeNotif();
        }
        nextColor();
    }

    /**
     * Highlight the timer by red color to notify player if
     * chosen option is wrong
     */
    private void penaltyTimeNotif() {
        timeLabel.setStyle("-fx-text-fill: red; -fx-font-size: 20");
        Timer t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20");
            }
        };
        t.schedule(task,500);
    }

    /**
     * Set counting down timer
     */
    private void setTimer() {
        timer = new AnimationTimer() {
            private long startTime = -1;
            @Override
            public void handle(long now) {
                if (startTime < 0) { startTime = now; }

                Duration elapsed = Duration.ofNanos(now - startTime);
                timeLeft = maxTime - elapsed.toSeconds();
                if (timeLeft > 0) {
                    String str = String.format("00:%02d", timeLeft);
                    timeLabel.setText(str);
                }
                else {
                    showResult();
                }
            }
        };
        timer.start();
    }

    /**
     * Show result if game finish
     */
    private void showResult() {
        timer.stop();
        start.setPrefWidth(150);
        start.setLayoutX(425);
        start.setText("Try again");
        start.setVisible(true);
        icon.setVisible(true);
        description.setVisible(false);
        timeLabel.setVisible(false);
        meaningLabel.setVisible(false);
        colorLabel.setVisible(false);
        yesBtn.setVisible(false);
        noBtn.setVisible(false);

        scoreLabel.setText("Score\n" + score);
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 60");
        scoreLabel.setLayoutY(180);
    }

    /**
     * Set up random color and text
     */
    private void nextColor() {
        int size = colorList.length;
        int rand1 = ThreadLocalRandom.current().nextInt(size);
        int rand2 = ThreadLocalRandom.current().nextInt(size);
        int rand3 = ThreadLocalRandom.current().nextInt(size);
        int rand4 = ThreadLocalRandom.current().nextInt(size);

        currentMeaning = colorList[rand1];
        meaningLabel.setText(currentMeaning);
        meaningLabel.setStyle(style + colorList[rand2]);

        colorLabel.setText(colorList[rand3]);
        currentColor = colorList[rand4];
        colorLabel.setStyle(style + currentColor);
    }

    /**
     * Back to home page button
     * @throws IOException Stop program if cannot go back to homepage
     */
    public void backToHomePage() throws IOException {
        Home.backToHome(root);

    }
}
