package ui;

import com.google.firebase.database.annotations.NotNull;
import database.DBManager;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import user.UserController;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static utils.Utils.*;

public class StartMenuView {

    @FXML
    TextField joinCode, userName;

    @FXML
    Node overlay;

    @FXML
    private void onJoinClick(@NotNull final ActionEvent event) {
        var usermodel = UserController.getInstance();

        var id = joinCode.getText();
        usermodel.setCurrentGameID(id);

        var name = userName.getText();
        if (name.equals("")) {
            String[] Namen = {"King Poly", "Holly Poly", "Python", "Angry Bird", "Flappy Bird", "Among Us", "Java", "The Legend", "Sinterklaas", "Corona", "FortNite", "Tesla", "Zwarte Piet", "Subway Surf", "Mario", "Stickman", "Spiderman", "javaFX", "Jumbo", "Albert Heijn", "Aldi", "Dirk", "Student"};
            Random random = new Random();
            int i = random.nextInt(Namen.length - 1);
            name = Namen[i];
        }
        usermodel.setName(name);

        DBManager.getInstance().joinGame();

        overlay.setVisible(true);
        var stage = (Stage) joinCode.getScene().getWindow();
        goToBoard(stage);
    }

    @FXML
    private void onCreateClick(final ActionEvent event) {
        overlay.setVisible(true);
        var usermodel = UserController.getInstance();

        var id = genID();
        usermodel.setCurrentGameID(id);

        var name = userName.getText();
        if (name.equals("")) {
            String[] Namen = {"King Poly", "Holly Poly", "Python", "Angry Bird", "Flappy Bird", "Among Us", "Java", "The Legend", "Sinterklaas", "Corona", "FortNite", "Tesla", "Zwarte Piet", "Subway Surf", "Mario", "Stickman", "Spiderman", "javaFX", "Jumbo", "Albert Heijn", "Aldi", "Dirk", "Student"};
            Random random = new Random();
            int i = random.nextInt(Namen.length - 1);
            name = Namen[i];
        }
        usermodel.setName(name);

        // تشغيل عمليات DBManager في Thread مستقل
        CompletableFuture.runAsync(() -> {
            DBManager.getInstance().createGame();
            DBManager.getInstance().joinGame();
        }).thenRun(() -> {
            // تحديث واجهة المستخدم بعد انتهاء العمليات
            Platform.runLater(() -> {
                overlay.setVisible(false);
                var stage = (Stage) joinCode.getScene().getWindow();
                goToBoard(stage);
            });
        });
    }
}
