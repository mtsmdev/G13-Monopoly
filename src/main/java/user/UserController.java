package user;

import serializable.Serializable;

import java.util.HashMap;
import java.util.Map;

public class UserController implements Serializable {
    private final UserModel model;
    private static UserController instance;

    public UserController() {
        this.model = new UserModel();
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public String getName() {
        return this.model.getName();
    }
    public void setName(String name) {
        this.model.setName(name);
    }

    public String getCurrentGameID() {
        return this.model.getCurrentGameID();
    }
    public void setCurrentGameID(String currentGameID) {
        this.model.setCurrentGameID(currentGameID);
    }

    @Override
    public Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put("name", this.model.getName());

        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        this.model.setName((String) data.get("name"));
    }
}
