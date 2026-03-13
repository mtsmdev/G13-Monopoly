package dice;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import observable.Observer;

import java.util.stream.Collectors;

public class DiceView implements Observer<DiceModel> {

    private final String[] dobbelSteen = {"/dice_animation/dice1_animation.gif", "/dice1.png", "/dice2.png", "/dice3.png", "/dice4.png", "/dice5.png", "/dice6.png"};
    private final AnchorPane diceLayer;
    private VBox vbox;
    private final Runnable rollAction, dismissAction;

    public DiceView(AnchorPane board, Runnable rollAction, Runnable dismissAction) {
        this.diceLayer = (AnchorPane) ((StackPane) board.getParent()).getChildren().stream().filter(p -> p.getId().equals("diceLayer")).collect(Collectors.toList()).get(0);
        this.rollAction = rollAction;
        this.dismissAction = dismissAction;
        drawDice(0);
    }

    @Override
    public void update(DiceModel data) {
        this.drawDice(data.getDiceOutput());
    }

    public void remove() {
        diceLayer.getChildren().remove(vbox);
    }

    public void drawDice(int diceOutput) {
        remove();
        vbox = new VBox();
        vbox.setPrefWidth(200);
        vbox.setPrefHeight(200);
        vbox.setLayoutX(300);
        vbox.setLayoutY(300);

        vbox.setAlignment(Pos.CENTER);

        var image = new Image(getClass().getResource("/images/dice" + dobbelSteen[diceOutput]).toString(), 100, 100, true, true);
        var imageView = new ImageView(image);

        var text = new Text(diceOutput == 0? "Click to throw!": "Threw a " + diceOutput + "!");
        text.setFill(Color.WHITE);
        text.setStroke(Color.BLACK);
        text.setFont(Font.font("Arial", FontWeight.BLACK, 26));
        vbox.getChildren().add(text);

        if (diceOutput != 0) {
            var footText = new Text("(Click to dismiss)");
            footText.setFill(Color.BLACK);
            footText.setFont(Font.font("Arial", FontWeight.BLACK, 20));
            vbox.getChildren().add(footText);
        }
        
        vbox.getChildren().add(imageView);
        diceLayer.getChildren().add(vbox);

        
        imageView.setOnMouseClicked(e -> {
            var action = diceOutput == 0 ? rollAction : dismissAction;
            action.run();
        });
    }
}
