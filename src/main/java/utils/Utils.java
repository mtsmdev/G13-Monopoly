package utils;

import board.BoardController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.yaml.snakeyaml.Yaml;
import property.PropertyController;
import property.PropertyType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Utils {
    private Utils(){}

    private static final Random rand = new Random();

    public static String genID() {
        int id = rand.nextInt(900000) + 100000;
        return Integer.toString(id);
    }

    public static void setScene(String path, Stage stage) {
        var loader = new FXMLLoader();
        loader.setLocation(Utils.class.getResource(path));

        try {
            Parent content = loader.load();
            final Scene scene = new Scene(content);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void goToBoard(Stage stage) {
        new Timer().schedule(new TimerTask() {
            @Override public void run() {
                Platform.runLater(() ->  setScene("/views/MonopolyBoard.fxml", stage));
            }
        }, 10L);
    }

    public static List<PropertyController> loadProperties(BoardController board) {
        ArrayList<PropertyController> propertyControllers = new ArrayList<>();
        List<String> properties;
        try {
            InputStream inputStream = new FileInputStream(Utils.class.getResource("/properties.yml").getFile());
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);
            properties = (ArrayList<String>) data.get("properties");

        } catch (Exception e) {
            e.printStackTrace();
            return propertyControllers;
        }

        for (String propertyString: properties) {
            propertyString = propertyString.replaceAll("\\s+", "");
            propertyString = propertyString.replaceAll("\\|", " ");
            var stringParts = propertyString.split(",");
            switch (stringParts[0]) {
                case "CORNER" -> propertyControllers.add(new PropertyController(board, stringParts[2], stringParts[3], PropertyType.valueOf(stringParts[1])));
                case "PROPERTY" -> propertyControllers.add(new PropertyController(board, stringParts[1], Integer.parseInt(stringParts[2]), Integer.parseInt(stringParts[3]), Color.valueOf(stringParts[4])));
                case "UTILITY" -> propertyControllers.add(new PropertyController(board, stringParts[1], PropertyType.valueOf(stringParts[2])));
            }
        }

        return propertyControllers;
    }
}
