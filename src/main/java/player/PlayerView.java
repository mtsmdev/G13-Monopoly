package player;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Translate;
import observable.Observer;
import property.PropertyController;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerView implements Observer<PlayerModel> {
    private final List<PropertyController> properties;
    private final AnchorPane playerLayer;
    private VBox anchor;

    public PlayerView(AnchorPane board, List<PropertyController> properties) {
        this.playerLayer = (AnchorPane) ((StackPane) board.getParent()).getChildren().stream().filter(p -> p.getId().equals("playerLayer")).collect(Collectors.toList()).get(0);
        this.properties = properties;
    }

    @Override
    public void update(PlayerModel data) {
        this.draw(data.getCurrentPropLoc(), data.getID());
    }

    public void draw(int propLoc, int ID) {
        playerLayer.getChildren().remove(anchor);
        anchor = new VBox();
        anchor.setAlignment(javafx.geometry.Pos.CENTER);
        anchor.setMaxWidth(60);
        anchor.setPrefWidth(60);

        anchor.setTranslateX(-30);
        anchor.setTranslateY(-30);
        anchor.getTransforms().add(new Translate(30, 30));

        playerLayer.getChildren().add(anchor);

        var image = new Image(getClass().getResource("/images/character/player" + ID + ".png").toString(), 30, 30, true, true);
        var imageView = new ImageView(image);

        anchor.getChildren().add(imageView);

        var property = properties.get(propLoc);
        var rotation = property.getRotation();

        var propertyPos = new Point2D(property.getX(), property.getY());
        var offsets = calcOffset(ID, rotation);
        var pos = propertyPos.add(offsets);

        anchor.setRotate(rotation);
        anchor.setLayoutX(pos.getX());
        anchor.setLayoutY(pos.getY());
    }

    private Point2D calcOffset(int propID, int rotation) {
        var offsetX = -30 + (15 * propID);
        var offsetY = 70;

        switch (rotation) {
            case 90 -> {
                offsetX = -52;
                offsetY = -15 + (15 * propID);
            }
            case 180 -> {
                offsetX = -5 + (15 * propID);
                offsetY = -40;
            }
            case 270 -> {
                offsetX = 55;
                offsetY = 45 + (-15 * propID);
            }
        }

        return new Point2D(offsetX, offsetY);
    }
}
