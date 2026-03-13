package property;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import observable.Observer;
import player.PlayerController;

class PropertyView implements Observer<PropertyModel> {
    AnchorPane board, anchor;

    public PropertyView(AnchorPane board) {
        this.board = board;
    }

    @Override
    public void update(PropertyModel data) {
        if (!data.isCorner()) {
            this.draw(data.getX(), data.getY(), data.getWidth(), data.getName(), data.getPrice(), data.getColor(), data.getRotation(), data.getRent(), data.getHouses(), data.getType(),  data.getOwner());
        } else {
            drawCornerTile(data.getX(), data.getY(), 100, new String[]{data.getName()}, data.getClassName());
        }
    }

    public void drawCornerTile(int x, int y, int w, String[] text, String className) {
        board.getChildren().remove(anchor);
        anchor = new AnchorPane();
        anchor.setLayoutX(x);
        anchor.setLayoutY(y);
        anchor.setMinWidth(w);
        anchor.setMinHeight(w);
        anchor.getStyleClass().add(className);
        anchor.getStyleClass().add("cornerTile");
        anchor.applyCss();

        var vbox = new VBox();
        anchor.getChildren().add(vbox);

        for (var s : text) {
            var textNode = new Text(s);
            textNode.setFill(Paint.valueOf("#ffc145"));
            textNode.setStrokeWidth(2);
            textNode.setStroke(Color.BLACK);
            textNode.setStrokeType(StrokeType.OUTSIDE);
            textNode.setWrappingWidth(100);
            textNode.setFont(new Font(20));
            vbox.getChildren().add(textNode);
        }

        board.getChildren().add(anchor);
    }

    public void draw(int x, int y, int width, String name, int price, Color color, int rotation, int rent, int houses, PropertyType type, PlayerController owner) {
        board.getChildren().remove(anchor);
        anchor = new AnchorPane();

        var height = 100;
        anchor.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        anchor.setLayoutX(x);
        anchor.setLayoutY(y);
        anchor.setPrefWidth(width);
        anchor.setPrefHeight(height);
        anchor.getStyleClass().add("property");
        anchor.applyCss();

        anchor.setTranslateX(-width/2.0);
        anchor.setTranslateY(-height/2.0);
        anchor.getTransforms().add(new Translate(width/2.0, height/2.0));

        anchor.setRotate(rotation);

        board.getChildren().add(this.anchor);

        var vbox = new VBox();
        anchor.getChildren().add(vbox);
        vbox.setLayoutX(1);
        vbox.setLayoutY(1);

        if (type == PropertyType.PROPERTY) {
            var housesBox = new HBox();
            vbox.getChildren().add(housesBox);
            housesBox.setPrefHeight(30);
            housesBox.setBackground(new Background(new BackgroundFill(color, null, null)));
            housesBox.setPrefWidth(width);
            housesBox.setAlignment(Pos.CENTER);
            housesBox.setPadding(new Insets(5));

            for (int i = 0; i < houses; i++) {
                var house = new AnchorPane();
                house.getStyleClass().add("huis");
                house.applyCss();
                housesBox.getChildren().add(house);
            }
        }

        var content = new VBox();
        content.setPrefWidth(width);
        content.setAlignment(Pos.CENTER);
        vbox.getChildren().add(content);

        var nameContainer = new VBox();
        content.getChildren().add(nameContainer);

        if (owner != null) {
            var colors = new Color[]{
                    Color.rgb(238,148,158),
                    Color.rgb(140,200, 255),
                    Color.rgb(140,255,140),
                    Color.rgb(255,220,140)
            };
            nameContainer.setBackground(new Background(new BackgroundFill(colors[owner.getID()-1], null, null)));
            nameContainer.setPadding(new Insets(-2,-2,-2,-2));
        }

        var nameNode = new Text(name);
        nameNode.setFill(Color.BLACK);
        if (owner != null) {
            nameNode.setFill(Color.WHITE);
        }
        nameNode.setFont(Font.font("Comic Sans MS", FontWeight.BLACK, 13));
        nameNode.setWrappingWidth(width);
        nameContainer.getChildren().add(nameNode);

        if (type == PropertyType.PROPERTY || type == PropertyType.BELASTING) {
            var priceText = "" + price;
            if (owner != null) priceText = "" + (rent + rent * houses);

            var priceNode = new Text("€" + priceText);
            priceNode.setFill(Color.BLACK);
            priceNode.setWrappingWidth(width);
            content.getChildren().add(priceNode);
        }
    }
}
