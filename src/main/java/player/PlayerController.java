package player;

import javafx.scene.layout.AnchorPane;
import property.PropertyController;
import serializable.Serializable;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerController implements Serializable {

    private final PlayerModel playerModel;
    private final PlayerView playerView;
    private final List<PropertyController> properties;

    public PlayerController(String playername, AnchorPane board, int ID, List<PropertyController> properties) {
        this.properties = properties;
        this.playerModel = new PlayerModel(playername, 1000, ID);
        this.playerView = new PlayerView(board, properties);

        this.playerModel.registerObserver(playerView);
    }

    public void moveTo(PropertyController property) {
        playerModel.setCurrentPropLoc(properties.indexOf(property));
    }

    @Override
    public Map<String, Object> serialize() {
        var data = new HashMap<String, Object>();

        data.put("ID", playerModel.getID());
        data.put("name", playerModel.getName());
        data.put("balance", playerModel.getBalance());
        data.put("propLoc", playerModel.getCurrentPropLoc());

        return data;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        playerModel.setName((String) data.get("name"));
        playerModel.setBalance((int)(long) data.get("balance"));
        playerModel.setCurrentPropLoc((int)(long) data.get("propLoc"));
    }

    public int getID() {
        return playerModel.getID();
    }
    public String getPlayername(){
        return playerModel.getName();
    }

    public int getCurrentPropLoc(){
        return playerModel.getCurrentPropLoc();
    }

    public int getBalance() { return playerModel.getBalance();}
    public void setBalance(int balance) { playerModel.setBalance(balance);}
}