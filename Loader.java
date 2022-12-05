package main;

import javafx.fxml.FXMLLoader;
import java.io.IOException;

/**
 * @author Thu Duong
 * Loader used for loading fxml pages
 */
public class Loader {
    /**
     * FXML loader used for directing pages
     * @param fileName path to the page
     * @param <T> generic type
     * @return needed root of the page
     */
    public static <T> T loadFXMLFile (String fileName) {
        FXMLLoader loader = new FXMLLoader(Loader.class.getResource(fileName));

        T root = null;

        try {
            root = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
}
