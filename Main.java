package main;

import ManyPages.Home;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @author Thu Duong
 * Main class to run the application
 */
public class Main extends Application {
    /**
     * Main function
     * @param args input application
     */
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Human Benchmark");
        primaryStage.getIcons().add(new Image("/Images/icon.jpg"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLpages/Home.fxml"));
        Pane root = loader.load();

        Scene scene = new Scene(root);

        Home home = loader.getController();
        home.setScene(scene);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
