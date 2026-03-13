package board;

import javafx.scene.layout.*;
import javafx.scene.text.Text;
import observable.Observer;
import property.PropertyController;

import java.util.List;
import java.util.stream.Collectors;

public class BoardView implements Observer<BoardModel> {
    private final AnchorPane board;

    public BoardView(AnchorPane board) {
        this.board = board;
    }

    @Override
    public void update(BoardModel data) {
        drawBoard(data);
        updateActivePlayer(data.getActivePlayer().getPlayername());
    }

    public void updateActivePlayer(String playername) {
        var scene = board.getScene();
        var text = (Text) scene.lookup("#wieSpeelt");
        text.setText(playername);
    }

    public void drawBoard(BoardModel data) {
        var corners = data.getProperties().stream().filter(PropertyController::isCorner).collect(Collectors.toList());
        drawCorners(corners);

        var properties = data.getProperties().stream().filter(p -> !p.isCorner()).collect(Collectors.toList());
        drawProperties(properties);
    }

    public void drawCorners(List<PropertyController> corners) {
        for (PropertyController corner : corners) {
            corner.setWidth(100);
            switch (corner.getType()) {
                case START -> {
                    corner.setX(700);
                    corner.setY(700);
                }

                case STUDEREN -> {
                    corner.setX(0);
                    corner.setY(700);
                }

                case FIETSENSTALLING -> {
                    corner.setX(0);
                    corner.setY(0);
                }

                case GASTUDEREN -> {
                    corner.setX(700);
                    corner.setY(0);
                }
            }
        }
    }

    public void drawProperties(List<PropertyController> properties) {
        var propertiesPerRow = 8;
        var cornerTileWidth = 100;
        var propertyWidth = (int) (board.getPrefWidth() - 2 * cornerTileWidth) / propertiesPerRow;

        for (int i = 0; i < properties.size(); i++) {
            var rowIndex = i % 8;
            var rotation = (i / 8)*90;
            var x = 0;
            var y = 0;

            switch (rotation) {
                case 0 -> {
                    x = 726 - (cornerTileWidth + rowIndex * propertyWidth);
                    y = 700;
                }
                case 90 -> {
                    x = 100;
                    y = 726 - (cornerTileWidth + rowIndex * propertyWidth);
                }
                case 180 -> {
                    x = cornerTileWidth + rowIndex * propertyWidth + 74;
                    y = 100;
                }
                case 270 -> {
                    x = 700;
                    y = cornerTileWidth + rowIndex * propertyWidth + 74;
                }
            }

            properties.get(i).setX(x);
            properties.get(i).setY(y);
            properties.get(i).setRotation(rotation);
            properties.get(i).setWidth(propertyWidth);
        }
    }
}
