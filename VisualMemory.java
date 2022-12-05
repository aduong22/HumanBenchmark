package ManyPages;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Thu Duong
 * Visual Memory game
 */
public class VisualMemory {
    private final String curveBorder = "-fx-background-radius: 8";
    private final String whiteSquare;
    private final String greySquare;
    private final double boardSize = 420;
    private final int maxStrike = 3;
    private final IntegerProperty level;
    private final IntegerProperty numLive;
    private int numSquare;
    private int boardDimension = 3;
    private double btnSize;
    private int clickedCount;
    private int strike;

    @FXML
    private Pane root;
    @FXML
    private GridPane board;
    @FXML
    private Pane boardMask;
    @FXML
    private Button start;
    @FXML
    private Label levelLabel;
    @FXML
    private Label liveLabel;

    /**
     * Default constructor, set up final values and colors
     */
    public VisualMemory() {
        level = new SimpleIntegerProperty(1);
        numLive = new SimpleIntegerProperty(3);

        whiteSquare = curveBorder + "; -fx-background-color: white";
        greySquare = curveBorder + "; -fx-background-color: black";
    }

    /**
     * Initialize all values and begin welcome scene
     */
    public void initialize() {
        root.setStyle("-fx-background-size: 1000 600;" +
                "-fx-background-image: url('Images/VisualMemory/welcome.jpg')");

        levelLabel.textProperty().bind(level.asString());
        liveLabel.textProperty().bind(numLive.asString());
        levelLabel.setVisible(false);
        liveLabel.setVisible(false);
        levelLabel.setLayoutX(470);
        levelLabel.setLayoutY(35);
        levelLabel.setStyle("-fx-text-fill: white; -fx-font-size: 33");

        level.setValue(1);
        numLive.setValue(3);
        boardDimension = 3;
    }

    /**
     * Set up new board size with new selected squares
     */
    private void setUp() {
        clickedCount = 0;
        strike = 0;
        numSquare = level.getValue() + 2;
        btnSize = boardSize / boardDimension;
        board.getChildren().clear();
        for (int i = 0; i < boardDimension; i++) {
            for (int j = 0; j < boardDimension; j++) {
                board.add(makeBtn(), j, i);
            }
        }
        board.setVisible(true);
    }

    /**
     * Make a functional button for a square
     * @return button
     */
    private Button makeBtn() {
        Button btn = new Button();
        btn.setPrefWidth(btnSize);
        btn.setPrefHeight(btnSize);
        btn.setStyle(whiteSquare);
        btn.setOpacity(0.5);
        btn.setId("0");
        btn.setFocusTraversable(false);

        btn.setOnAction(e -> {
            String id = btn.getId();
            if (id.equals("1")) {
                btn.setId(id + "1");
                /*btn.setStyle(whiteSquare);
                btn.setOpacity(1);*/
                buttonClickEffect(btn,true);
                clickedCount++;
            }
            else if (!id.matches("1{2,}|0{2,}")){
                //btn.setStyle(greySquare);
                btn.setId(id + "0");
                buttonClickEffect(btn,false);
                strike++;
            }

            // Check next game
            if (clickedCount == numSquare) {
                level.setValue(level.getValue() + 1);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(()->nextGame());
                    }
                };
                queueTask(task);
            }
            else if (strike == maxStrike) {
                int live = numLive.getValue();
                live--;
                if (live == 0) {
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> showResult());
                        }
                    };
                    queueTask(task);
                }
                else {
                    numLive.setValue(live);
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(()->nextGame());
                        }
                    };
                    queueTask(task);
                }
            }
        });
        return btn;
    }

    /**
     * Make a timer for masking
     * @param task task
     *
     */
    private void queueTask(TimerTask task) {
        maskTheBoard(500);
        Timer timer = new Timer();
        timer.schedule(task, 500);
    }

    /**
     * Avoid clicking while transition
     */
    private void maskTheBoard(long duration) {
        boardMask.setVisible(true);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                boardMask.setVisible(false);
            }
        };
        timer.schedule(task, duration);
    }

    /**
     * Show result if the game is finish
     */
    private void showResult() {
        root.setStyle("-fx-background-size: 1000 600;" +
                "-fx-background-image: url('/Images/VisualMemory/result.jpg')");
        board.setVisible(false);
        liveLabel.setVisible(false);

        levelLabel.textProperty().unbind();
        levelLabel.setText("Level " + numLive.getValue());
        levelLabel.setLayoutX(380);
        levelLabel.setLayoutY(250);
        levelLabel.setStyle("-fx-text-fill: white; -fx-font-size: 80");

        start.setVisible(true);
        start.setText("Try again");
        start.setPrefWidth(200);
        start.setLayoutX(400);
    }

    /**
     * Create new board
     */
    private void nextGame() {
        maskTheBoard(4000);
        switch (level.getValue()) {
            case 3: {
                boardDimension = 4;
                break;
            }
            case 6: {
                boardDimension = 5;
                break;
            }
            case 9: {
                boardDimension = 6;
                break;
            }
            case 14: {
                boardDimension = 7;
                break;
            }
            case 19: {
                showResult();
                break;
            }
            default: {
                break;
            }
        }
        setUp();
        chooseSquares(numSquare);
    }

    /**
     * Choose random number of white squares in current game
     * @param numSquare number of white squares
     */
    private void chooseSquares(int numSquare) {
        for (int i = 0; i < numSquare; i++) {
            Node chosenBtn = null;
            do {
                int randRow = ThreadLocalRandom.
                        current().nextInt(0,boardDimension);
                int randColumn = ThreadLocalRandom.current().
                        nextInt(0, boardDimension);
                for (Node n : board.getChildren()) {
                    if (GridPane.getRowIndex(n) == randRow &&
                            GridPane.getColumnIndex(n) == randColumn) {
                        chosenBtn = n;
                    }
                }
            }
            while (Objects.requireNonNull(chosenBtn).getId().equals("1"));
            chosenBtn.setId("1");

            // Transition for hint before play each game
            Duration duration = new Duration(2000);
            FadeTransition fs = new FadeTransition(duration,chosenBtn);
            fs.setFromValue(0.5);
            fs.setToValue(1);
            fs.setCycleCount(2);
            fs.setAutoReverse(true);
            fs.play();
        }
    }

    /**
     * Button effect when click, valid button will turn white, grey otherwise
     * @param btn clicked button
     * @param valid valid square
     */
    private void buttonClickEffect(Button btn, boolean valid) {
        if (!valid) {
            btn.setStyle(greySquare);
        }
        else {
            btn.setOpacity(1);
        }

        ScaleTransition scale = new ScaleTransition(Duration.millis(100), btn);
        scale.setFromY(1);
        scale.setToY(0);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.play();
    }

    /**
     * Start, try again button
     */
    public void VisualMemoryClicked() {
        if (start.getText().equals("Try again")) {
            start.setText("Start");
            start.setPrefWidth(100);
            start.setLayoutX(450);
            initialize();
            return;
        }

        root.setStyle("-fx-background-size: 1000 600;" +
                "-fx-background-image: url('/Images/VisualMemory/gameScene.jpg')");
        start.setVisible(false);
        levelLabel.setVisible(true);
        liveLabel.setVisible(true);
        nextGame();
    }

    /**
     * Back to home page button
     * @throws IOException Stop program if cannot go back to homepage
     */
    public void backToHomePage() throws IOException {
        Home.backToHome(root);
    }
}
