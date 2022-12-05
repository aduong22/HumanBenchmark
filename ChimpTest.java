package ManyPages;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Thu Duong
 * Chimp Test Game
 */
public class ChimpTest {
    private final int total;
    private final int maxStrike;
    private int currentStrike = 0;
    private ArrayList<Button> numData;
    private int nextInOrder;
    private final IntegerProperty scoreValue;

    @FXML
    private Pane root;
    @FXML
    private Button start;
    @FXML
    private GridPane numList;
    @FXML
    private Label numTextLabel;
    @FXML
    private Label result;
    @FXML
    private Label strike;

    /**
     * Default constructor
     */
    public ChimpTest() {
        scoreValue = new SimpleIntegerProperty(4);
        total = 18;
        maxStrike = 3;
    }

    /**
     * Initialize values and start welcome page
     */
    public void initialize() {
        result.textProperty().bind(scoreValue.asString());
        strike.setText("STRIKES\n0 of 3");
        result.setVisible(false);
        result.setLayoutY(180);
        strike.setVisible(false);
        numTextLabel.setVisible(false);

        numData = new ArrayList<>();
        for (int i = 1; i <= maxStrike + 1; i++) {
            numData.add(makeNumBtn(i));
        }
    }

    /**
     * Make a button with a given number
     * @param i given number
     * @return numbered button
     */
    private Button makeNumBtn(int i) {
        Button numBtn = new Button();
        numBtn.setPrefWidth(75);
        numBtn.setPrefHeight(75);
        numBtn.setId(i + "");
        numBtn.setText(i + "");
        numBtn.setFocusTraversable(false);
        numBtn.setOnAction(event -> {
            int id = Integer.parseInt(numBtn.getId());

            // Hide the number after clicking the number 1
            if (id == 1 && numData.size() > 4) {
                for (Button b : numData) {
                    b.setText("");
                    b.setStyle("-fx-background-color: white");
                }
            }

            // Check if the clicked number is in order
            if (id != nextInOrder) {
                currentStrike++;
                System.out.println(currentStrike);
                showResult();
            }
            else {
                int size = numData.size();
                nextInOrder++;
                if (nextInOrder > size) {
                    numData.add(makeNumBtn(nextInOrder));
                    showResult();
                }
            }
            numBtn.setVisible(false);
        });
        return numBtn;
    }

    /**
     * Show result after each level, and check if it is an end
     */
    private void showResult() {
        int size = numData.size();
        numList.setVisible(false);
        start.setVisible(true);
        result.setVisible(true);
        if (currentStrike < maxStrike && size <= total) {
            scoreValue.setValue(numData.size());
            strike.setText("STRIKES\n" + currentStrike + " of 3");
            strike.setVisible(true);
            numTextLabel.setVisible(true);
            start.setText("Continue");
        }
        else {
            root.setStyle("-fx-background-size: 1000 600;" +
                    "-fx-background-image: url('/Images/ChimpTest/result.jpg')");
            scoreValue.setValue(size - 1);
            start.setText("Try again");
            result.setLayoutY(250);
        }
    }

    /**
     * Find next empty location for number to be inserted in the board
     * @return location indexes (row, column)
     */
    private int[] nextEmptyLocation() {
        int[] location = new int[2];
        int randRow, randColumn;
        Node node;
        // Find a random empty location for the number
        do {
            node = null;
            randRow = ThreadLocalRandom.current().nextInt(0,4);
            randColumn = ThreadLocalRandom.current().nextInt(0,8);
            for (Node b : numList.getChildren()) {
                if (GridPane.getRowIndex(b) == randRow &&
                        GridPane.getColumnIndex(b) == randColumn) {
                    node = b;
                    break;
                }
            }
        }
        while (node != null);

        // Save location indexes
        location[0] = randRow;
        location[1] = randColumn;
        return location;
    }

    /**
     * Start button, Continue, and Try again
     */
    public void chimpTestClicked() {
        // Reset the game
        if (start.getText().equals("Try again")) {
            numData.clear();
            currentStrike = 0;
            initialize();
        }

        root.setStyle("-fx-background-color: #2b87d1");
        nextInOrder = 1;
        numList.getChildren().clear();

        // Create a game with numbers at current level in random locations
        int[] location;
        for (Button num : numData) {
            InnerShadow inner = new InnerShadow();
            inner.setColor(Color.LIGHTBLUE);
            num.setEffect(inner);
            num.setStyle("-fx-background-color: #2b87d1;" +
                         "-fx-text-fill: white; -fx-font-size: 30");
            num.setText(num.getId());
            num.setVisible(true);
            location = nextEmptyLocation();
            numList.add(num, location[1], location[0]);
        }

        start.setVisible(false);
        numList.setVisible(true);
        result.setVisible(false);
        strike.setVisible(false);
        numTextLabel.setVisible(false);
    }

    /**
     * Back to home page button
     * @throws IOException Stop program if cannot go back to homepage
     */
    public void backToHomePage() throws IOException {
        Home.backToHome(root);
    }
}
