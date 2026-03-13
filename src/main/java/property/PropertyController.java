package property;

import board.BoardController;
import javafx.scene.paint.Color;
import player.PlayerController;
import serializable.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertyController implements Serializable {

    public final PropertyView propertyView;
    public final PropertyModel propertyModel;
    public final BoardController boardController;

    public PropertyController(BoardController boardController, String name, int price, int rent, Color color) {
        this.propertyModel = new PropertyModel(name, price, rent, PropertyType.PROPERTY);
        this.propertyView = new PropertyView(boardController.getBoard());
        this.boardController = boardController;
        propertyModel.setColor(color);

        this.propertyModel.registerObserver(this.propertyView);
    }

    public PropertyController(BoardController boardController, String name, PropertyType type) {
        this.propertyModel = new PropertyModel(name, type);
        this.propertyView = new PropertyView(boardController.getBoard());
        this.boardController = boardController;
        if (type == PropertyType.BELASTING) {
            propertyModel.setPrice(200);
        }

        this.propertyModel.registerObserver(this.propertyView);
    }

    public PropertyController(BoardController boardController, String name, String className, PropertyType type) {
        this.propertyModel = new PropertyModel(name, className, type);
        this.propertyView = new PropertyView(boardController.getBoard());
        this.boardController = boardController;

        this.propertyModel.registerObserver(this.propertyView);
    }

    public void buy(PlayerController player) {
        if (propertyModel.getType() != PropertyType.PROPERTY) return;
        if(player.getBalance() < propertyModel.getPrice() || propertyModel.getOwner() != null)  return;
        if(player.getBalance() >= propertyModel.getPrice()) {
            propertyModel.setOwner(player);
            player.setBalance(player.getBalance() - propertyModel.getPrice());
        }
    }

    public void payAndreceiveRent(PlayerController player){
        if(this.propertyModel.getOwner() != null){
            if(!this.propertyModel.getOwner().getPlayername().equals(player.getPlayername())){
                player.setBalance(player.getBalance() - propertyModel.getRent());
                var owner =  this.propertyModel.getOwner();
               owner.setBalance(owner.getBalance() + this.propertyModel.getRent());
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        var data = new HashMap<String, Object>();

        data.put("owner", null);
        if (propertyModel.getOwner() != null) {
            data.put("owner", propertyModel.getOwner().getID());
        }
        data.put("houses", propertyModel.getHouses());

        return data;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        propertyModel.setHouses((int) (long) data.get("houses"));

        if (data.get("owner") == null) return;
        var serializedOwnerID = (int) (long) data.get("owner");
        var owner = boardController.getPlayers().stream().filter(p -> p.getID() == serializedOwnerID).collect(Collectors.toList()).get(0);
        propertyModel.setOwner(owner);

    }

    public void setX(int x) {
        propertyModel.setX(x);
    }
    public int getX() {
        return propertyModel.getX();
    }

    public int getY() {
        return propertyModel.getY();
    }
    public void setY(int y) {
        propertyModel.setY(y);
    }

    public int getWidth() {
        return propertyModel.getWidth();
    }
    public void setWidth(int width) {
        propertyModel.setWidth(width);
    }

    public String getName(){
        return propertyModel.getName();
    }
    public int getPrice() {
        return propertyModel.getPrice();
    }

    public int getRotation() {
        return propertyModel.getRotation();
    }
    public void setRotation(int rotation) {
        propertyModel.setRotation(rotation);
    }

    public int getHouses() {
        return propertyModel.getHouses();
    }
    public void setHouses(int houses) {
        propertyModel.setHouses(houses);
    }

    public boolean isCorner() {
        return propertyModel.isCorner();
    }

    public PropertyType getType() {
        return propertyModel.getType();
    }
    public Color getColor(){ return propertyModel.getColor(); }
    public int getRent(){return propertyModel.getRent(); }
    public PlayerController getOwner() {
        return propertyModel.getOwner();
    }
}
