package ManyPages;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author Thu Duong
 * Aim Trainer game
 */
public class AimTrainer {
    private final int totalClick = 30;
    private int numClick = 0;
    private final ArrayList<Long> clickTime = new ArrayList<>();
    private long startTime;
    private final StringProperty remainValue;
    private boolean isFirstClicked;

    @FXML
    private Pane root;
    @FXML
    private Button start;
    @FXML
    private Button target;
    @FXML
    private Label remain;

    /**
     * Default constructor
     */
    public AimTrainer() {
        remainValue = new SimpleStringProperty("Remaining\n" + totalClick);
    }

    /**
     * Initialize welcome screen and values
     */
    public void initialize() {
        root.setStyle("-fx-background-size: 1000 600;" +
                "-fx-background-image: url('/Images/AimTrainer/welcome.jpg')");
        remain.setStyle("-fx-font-size: 30; -fx-text-fill: white");
        remain.setLayoutX(350);
        remain.setLayoutY(20);
        target.setLayoutX(450);
        target.setLayoutY(250);
        target.setVisible(true);
        remain.setVisible(false);
        start.setVisible(false);
        remainValue.setValue("Remaining\n" + totalClick);
        remain.textProperty().bind(remainValue);
        isFirstClicked = true;
    }

    /**
     * Begin the game button
     */
    public void aimTrainerClicked() {
        if (start.getText().equals("Try again")) {
            start.setText("");
            initialize();
            return;
        }

        root.setStyle("-fx-background-color: #2b87d1");
        start.setVisible(false);
        target.setVisible(true);
        remain.setVisible(true);
        setNextPos();

        // Save start time for next click
        startTime = System.nanoTime();
    }

    /**
     * Calculate time between each click of target and save them
     */
    public void targetClicked() {
        if (isFirstClicked) {
            isFirstClicked = false;
            start.setVisible(false);
            start.fire();
            return;
        }
        if (numClick < totalClick - 1) {
            long finishTime = System.nanoTime();
            long reactionTimeNano = finishTime - startTime;
            long milliValue = TimeUnit.NANOSECONDS.toMillis(reactionTimeNano);
            clickTime.add(milliValue);

            startTime = System.nanoTime();
            numClick++;
            remainValue.setValue("Remaining\n" + (totalClick - numClick));
            setNextPos();
        }
        else
        {
            start.setText("Try again");
            start.setVisible(true);
            target.setVisible(false);
            long average = 0;
            for (long t : clickTime) {
                average += t;
            }
            average /= totalClick;
            remainValue.setValue(average + " ms");
            isFirstClicked = true;
            numClick = 0;
            root.setStyle("-fx-background-size: 1000 600;" +
                    "-fx-background-image: url('/Images/AimTrainer/result.jpg')");
            remain.setStyle("-fx-font-size: 60; -fx-text-fill: white");
            remain.setLayoutY(260);
        }
    }

    /**
     * Set new random location of target on screen
     */
    private void setNextPos() {
        // Zoom out animation of target
        ScaleTransition scale =
                new ScaleTransition(Duration.millis(100), target);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1);
        scale.setToY(1);
        scale.play();

        // Set new location for target
        long randX = ThreadLocalRandom.current().nextInt(300, 700);
        long randY = ThreadLocalRandom.current().nextInt(200, 400);
        target.setLayoutX(randX);
        target.setLayoutY(randY);
    }

    /**
     * Back to home page button
     * @throws IOException Stop program if cannot go back to homepage
     */
    public void backToHomePage() throws IOException {
        Home.backToHome(root);
    }
}
