package ManyPages;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author Thu Duong
 * Reaction Time game
 */
public class ReactionTime {
    private final String redColor = "-fx-background-color:#ce2636";
    private final String blueColor = "-fx-background-color:#2b87d1";
    private final String greenColor = "-fx-background-color:#4bdb6a";

    private final String path = "-fx-background-image: url('/Images/ReactionTime/";
    private final String imgSize = ";-fx-background-size: 1000 600";
    private final String welcome;
    private final String waitGreen;
    private final String tooSoon;
    private final String click;
    private final String result;

    @FXML
    private Pane root;
    @FXML
    private Button start;
    @FXML
    private Button finish;
    @FXML
    private Label reactionTime;

    private long startTime;
    private long duration;
    private final StringProperty reactionTimeValue;
    private Timer timer;

    /**
     * Default constructor
     */
    public ReactionTime() {
        reactionTimeValue  = new SimpleStringProperty("");
        welcome = path + "welcome.jpg')" + imgSize;
        waitGreen = path +"wait_for_green.jpg')" +imgSize;
        tooSoon = path + "too_soon.jpg')" + imgSize;
        click = path + "click.jpg')" + imgSize;
        result = path + "result.jpg')" + imgSize;
    }

    /**
     * Initialize values
     */
    public void initialize() {
        reactionTime.textProperty().bind(reactionTimeValue);
        reactionTime.setVisible(false);
        finish.setVisible(false);
        start.setStyle(blueColor + ";" + welcome);
    }

    /**
     * Timer for game
     */
    public void startTimer() {
        start.setVisible(false);
        finish.setVisible(true);
        finish.setStyle(redColor + ";" + waitGreen);
        reactionTime.setVisible(false);

        duration = ThreadLocalRandom.current().nextLong(1000,5000);
        startTime = System.nanoTime();

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                finish.setStyle(greenColor + ";" + click);
            }
        };
        timer.schedule(task, duration);
    }

    /**
     * Stop timer when finish game
     */
    public void finishTimer() {
        start.setVisible(true);
        finish.setVisible(false);
        long finishTime = System.nanoTime();
        long reactionTimeNano = finishTime - startTime;
        long milliValue =
                TimeUnit.NANOSECONDS.toMillis(reactionTimeNano) - duration;

        if (milliValue <= 0) {
            timer.cancel();
            start.setStyle(blueColor + ";" + tooSoon);
        }
        else {
            start.setStyle(blueColor + ";" + result);
            reactionTimeValue.setValue(milliValue + " ms");
            reactionTime.setVisible(true);
        }
    }

    /**
     * Back to home page button
     * @throws IOException Stop program if cannot go back to homepage
     */
    public void backToHomePage() throws IOException {
        Home.backToHome(root);
    }
}
