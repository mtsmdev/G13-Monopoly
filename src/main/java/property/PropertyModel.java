package property;

import javafx.scene.paint.Color;
import lombok.Getter;
import observable.Observable;
import player.PlayerController;

@Getter
public class PropertyModel extends Observable<PropertyModel> {

    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int rotation = 0;

    private int price = 0;
    private int rent = 0;
    private Color color = Color.TRANSPARENT;
    private boolean isCorner = false;

    private final String name;
    private final PropertyType type;
    private String className;

    private PlayerController owner = null;
    private int houses = 0;

    public PropertyModel(String name, PropertyType type) {
        this.name = name;
        this.type = type;
    }
    public PropertyModel(String name, int price, int rent, PropertyType type) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.type = type;
    }

    public PropertyModel(String name, String className, PropertyType type) {
        this.name = name;
        this.className = className;
        this.type = type;
        this.isCorner = true;
    }

    public void setHouses(int houses){
        this.houses = houses;
        updateObservers(this);
    }
    public void setOwner(PlayerController player) {
        this.owner = player;
        updateObservers(this);
    }

    public PlayerController getOwner() {
       return this.owner;
    }

    public void setColor(Color color) {
        this.color = color;
        this.updateObservers(this);
    }

    public void setPrice(int price) {
        this.price = price;
        updateObservers(this);
    }
    public void setX(int x) {
        this.x = x;
        this.updateObservers(this);
    }
    public void setY(int y) {
        this.y = y;
        this.updateObservers(this);
    }
    public void setWidth(int width) {
        this.width = width;
        this.updateObservers(this);
    }
    public void setRotation(int rotation) {
        this.rotation = rotation;
        this.updateObservers(this);
    }
}