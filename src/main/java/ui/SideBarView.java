package ui;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import player.PlayerController;

import java.util.List;

public class SideBarView {
    private SideBarView() {}

    public static VBox getPlayerList(List<PlayerController> players) {
        var vbox = new VBox();
        for (int i = 0; i < players.size(); i++) {
            var pIndex = i + 1;

            var hbox = new HBox();
            hbox.setPrefWidth(200);
            hbox.setPrefHeight(60);
            hbox.getStyleClass().add("list_player");
            hbox.getStyleClass().add("list_player" + pIndex);
            hbox.applyCss();

            var thumb = new AnchorPane();
            thumb.setPrefHeight(26);
            thumb.setPrefWidth(26);
            thumb.setTranslateX(-20);
            thumb.getStyleClass().add("player");
            thumb.getStyleClass().add("player" + pIndex);
            thumb.applyCss();

            var innerVbox = new VBox();
            innerVbox.setPrefWidth(37);
            innerVbox.setPrefWidth(100);
            innerVbox.setTranslateY(7);
            innerVbox.setTranslateX(10);

            var textName = new Text();
            textName.setText(players.get(i).getPlayername());
            textName.getStyleClass().add("list_player_text");
            textName.applyCss();

            var textBalance = new Text();
            textBalance.setText("€" + players.get(i).getBalance());
            textBalance.getStyleClass().add("list_player_text");
            textBalance.applyCss();
            textBalance.setFill(Color.web("#1b1b1b"));
            if (players.get(i).getBalance() < 0) textBalance.setFill(Color.web("red"));
            if (players.get(i).getBalance() < -500) textBalance.setText("Bankrupt!");

            innerVbox.getChildren().add(textName);
            innerVbox.getChildren().add(textBalance);
            hbox.getChildren().add(thumb);
            hbox.getChildren().add(innerVbox);
            vbox.getChildren().add(hbox);
        }

        return vbox;
    }
}
