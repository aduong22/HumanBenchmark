package ManyPages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Thu Duong
 * Verbal Memory game
 */
public class VerbalMemory {
    private final int maxLive;
    private final int size;
    private final ArrayList<String> dictionary = new ArrayList<>();
    private final ArrayList<Integer> id = new ArrayList<>();
    private int wordIdx;
    private int live;
    private int score;
    private int result;

    @FXML
    private Pane root;
    @FXML
    private Label wordLabel;
    @FXML
    private Label liveLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label resultLabel;
    @FXML
    private Button seenBtn;
    @FXML
    private Button newBtn;
    @FXML
    private Button start;

    /**
     * Default constructor
     * Read 100 words from text file then set initial state
     */
    public VerbalMemory() {
        File file = new File("src/ManyPages/textsource/dictionary.txt");
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                dictionary.add(0,sc.nextLine());
                id.add(0);
            }
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found");
            System.exit(1);
        }

        maxLive = 3;
        size = dictionary.size();
    }

    /**
     * Initialize welcome scene
     */
    public void initialize() {
        // Reset word id's to not seen (0)
        for (int i = 0; i < id.size(); i++) {
            if (id.get(i) != 0) {
                id.set(i, 0);
            }
        }

        // Set initial values and label
        wordIdx = 0;
        live = maxLive;
        score = 0;
        result = 0;
        liveLabel.setText("Lives | " + live);
        scoreLabel.setText("Score | " + score);

        // Set welcome scene
        liveLabel.setVisible(false);
        scoreLabel.setVisible(false);
        wordLabel.setVisible(false);
        seenBtn.setVisible(false);
        newBtn.setVisible(false);
        resultLabel.setVisible(false);
    }

    /**
     * Start, try again button set up
     */
    public void verbalMemoryClicked() {
        if (start.getText().equals("Try again")) {
            root.setStyle("-fx-background-size: 1000 600;" +
                    "-fx-background-image: url('/Images/VerbalMem/welcome.jpg')");
            initialize();
            start.setText("Start");
            start.setPrefWidth(100);
            start.setLayoutX(450);
            return;
        }
        else {
            root.setStyle("-fx-background-color: #2b87d1");
        }
        setNextWord();

        // Game scene
        start.setVisible(false);
        liveLabel.setVisible(true);
        scoreLabel.setVisible(true);
        wordLabel.setVisible(true);
        seenBtn.setVisible(true);
        newBtn.setVisible(true);
    }

    /**
     * Set next random word from the dictionary
     */
    private void setNextWord() {
        wordIdx = ThreadLocalRandom.current().nextInt(size);
        wordLabel.setText(dictionary.get(wordIdx));
    }

    /**
     * Check whether the word is a seen word
     */
    public void checkSeen() {
        if (id.get(wordIdx) == 0) {
            live--;
            liveLabel.setText("Lives | " + live);
            id.set(wordIdx, 1);
        }
        else {
            score++;
            scoreLabel.setText("Score | " + score);
        }
        showResult();
    }

    /**
     * Check weather the word is a new word
     */
    public void checkNew() {
        if (id.get(wordIdx) == 1) {
            live--;
            liveLabel.setText("Lives | " + live);
        }
        else {
            id.set(wordIdx, 1);
            score++;
            scoreLabel.setText("Score | " + score);
            id.set(wordIdx, 1);
        }
        showResult();
    }

    /**
     * Check and show result if no lives left or all words seen
     */
    private void showResult() {
        // Check whether all words are seen
        boolean flag = true;
        for (int i : id) {
            if (i == 0) {
                flag = false;
                break;
            }
        }

        // print result when finish, continue game if not
        if (live == 0 || flag) {
            root.setStyle("-fx-background-image: " +
                    "url('/Images/VerbalMem/result.jpg')");
            start.setText("Try again");
            start.setPrefWidth(150);
            start.setLayoutX(425);
            start.setVisible(true);
            resultLabel.setVisible(true);
            liveLabel.setVisible(false);
            scoreLabel.setVisible(false);
            wordLabel.setVisible(false);
            seenBtn.setVisible(false);
            newBtn.setVisible(false);
            result = score;
            resultLabel.setText(result + " words");
        }
        else {
            setNextWord();
        }
    }

    /**
     * Back to home page button
     *
     * @throws IOException Stop program if cannot go back to homepage
     */
    public void backToHomePage() throws IOException {
        Home.backToHome(root);
    }
}
