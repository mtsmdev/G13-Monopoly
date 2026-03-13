package duopoly;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static utils.Music.playMusic;
import static utils.Utils.setScene;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        setScene("/views/loginScreen.fxml", stage);
        stage.setFullScreen(false);
        stage.setResizable(false);
        Image icon = new Image(getClass().getResource("/images/login/duopoly_logo.png").toString());

        // App Config
        stage.setTitle("Monopoly Spel");
        stage.getIcons().add(icon);

        playMusic("login.wav");
    }
}