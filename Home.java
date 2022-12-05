package ManyPages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import main.Loader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Thu Duong
 * Home class, direct back and forth to pages
 */
public class Home {
    private static final String directory = "/FXMLpages/";

    private final String reactionTimePage;
    private final String aimTrainerPage;
    private final String chimpTestPage;
    private final String visualMemoryPage;
    private final String typingPage;
    private final String numberMemoryPage;
    private final String verbalMemoryPage;
    private final String colorMatchingPage;
    private final String loginPage;
    private Scene scene;

    /**
     * create path for pages
     */
    public Home() {
        reactionTimePage = directory + "ReactionTime.fxml";
        aimTrainerPage = directory + "AimTrainer.fxml";
        chimpTestPage = directory + "ChimpTest.fxml";
        visualMemoryPage = directory + "VisualMemory.fxml";
        typingPage = directory + "Typing.fxml";
        numberMemoryPage = directory + "NumberMemory.fxml";
        verbalMemoryPage = directory + "VerbalMemory.fxml";
        colorMatchingPage = directory + "ColorMatch.fxml";
        loginPage = directory + "Login.fxml";
    }

    /**
     * Setter for application scene (main scene)
     * @param scene application scene
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * going back to the homepage, used by other pages
     */
    public static void backToHome(Pane root) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(Home.class
                        .getResource("/FXMLpages/Home.fxml"));
        Pane homeRoot = loader.load();
        Home home = loader.getController();
        home.setScene(root.getScene());
        root.getScene().setRoot(homeRoot);
    }

    /**
     * Direct to reaction time page
     */
    public void reactionTimeClicked() {
        Pane newRoot = Loader.loadFXMLFile(reactionTimePage);
        scene.setRoot(newRoot);
    }

    /**
     * Direct to aim trainer page
     */
    public void aimTrainerClicked() {
        Pane newRoot = Loader.loadFXMLFile(aimTrainerPage);
        scene.setRoot(newRoot);
    }

    /**
     * Direct to chimp test page
     */
    public void chimpTestClicked() {
        Pane newRoot = Loader.loadFXMLFile(chimpTestPage);
        scene.setRoot(newRoot);
    }

    /**
     * Direct to visual memory page
     */
    public void visualMemoryClicked() {
        Pane newRoot = Loader.loadFXMLFile(visualMemoryPage);
        scene.setRoot(newRoot);
    }

    /**
     * Direct to typing page
     */
    public void typingClicked() {
        Pane newRoot = Loader.loadFXMLFile(typingPage);
        scene.setRoot(newRoot);
    }

    /**
     * Direct to number memory page
     */
    public void numberMemoryClicked() {
        Pane newRoot = Loader.loadFXMLFile(numberMemoryPage);
        scene.setRoot(newRoot);
    }

    /**
     * Direct to verbal memory page
     */
    public void verbalMemoryClicked() {
        Pane newRoot = Loader.loadFXMLFile(verbalMemoryPage);
        scene.setRoot(newRoot);
    }

    /**
     * Direct to color matching page
     */
    public void colorMatchingClicked() {
        Pane newRoot = Loader.loadFXMLFile(colorMatchingPage);
        scene.setRoot(newRoot);
    }
    /**
     * Direct to login page
     */
    public void loginClicked() {
        Pane newRoot = Loader.loadFXMLFile(loginPage);
        scene.setRoot(newRoot);
    }


//    /**
//     * username
//     */
//    public void setUsername (String username){
//        this.username = username;
//    }
//    /**
//     * Print score to CSV file
//     */
//    private void printScores() {
//        List<String> titles = Arrays.asList(
//                "Username", "Reaction Time", "Aim Trainer", "Chimp Test",
//                "Visual Memory", "Typing", "Number Memory", "Verbal Memory"
//        );
//        List<String> scores = Arrays.asList(
//                username,
//                reactionScore + "",
//                aimTrainerScore + "",
//                chimpTestScore + "",
//                visualMemScore + "",
//                typingScore + "",
//                numberMemScore + "",
//                verbalMemScore + ""
//        );
//        try {
//            FileWriter csvOutput =  new FileWriter("output.csv");
//            for (String column_Titles : titles){
//                csvOutput.append(column_Titles);
//                csvOutput.append(",");
//            }
//            csvOutput.append("\n");
//            for (String score : scores){
//                csvOutput.append(score);
//                csvOutput.append(",");
//            }
//            csvOutput.append("\n");
//            csvOutput.flush();
//            csvOutput.close();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//    }

}
