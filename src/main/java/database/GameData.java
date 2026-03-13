package database;

import board.BoardController;
import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;
import observable.Observable;
import observable.Observer;
import serializable.Serializable;
import user.UserController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter @Setter
public class GameData extends Observable<GameData> implements Serializable {
    static final String EXISTS_FIELD = "exists";
    static final String GAMESTATE_FIELD = "gameState";
    static final String BOARD_FIELD = "board";
    static final String USERS_FIELD = "users";


    private boolean exists;
    private List<UserController> users;
    private BoardController board;
    private GameState gameState;

    public GameData() {
        this.exists = true;
        this.gameState = GameState.WAITING;
        this.users = new ArrayList<>();
    }

    @Override
    public Map<String, Object> serialize() {
        var data = new HashMap<String, Object>();
        data.put(EXISTS_FIELD, true);
        data.put(GAMESTATE_FIELD, gameState.name());

        var serializedUsers = users.stream().map(UserController::serialize).collect(Collectors.toList());
        data.put(USERS_FIELD, serializedUsers);

        if (board != null) {
            var serializedBoard = board.serialize();
            data.put(BOARD_FIELD, serializedBoard);
        }

        return data;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        exists = (boolean) data.get(EXISTS_FIELD);
        gameState = GameState.valueOf((String) data.get(GAMESTATE_FIELD));

        @SuppressWarnings("unchecked")
        var serializedUsers = (ArrayList<HashMap<String, Object>>) data.get(USERS_FIELD);

        for (int i = 0; i < users.size(); i++) {
            users.get(i).deserialize(serializedUsers.get(i));
        }
        serializedUsers.subList(0, users.size()).clear();

        if (!serializedUsers.isEmpty()) {
            for (HashMap<String, Object> serializedUser : serializedUsers) {
                var user = new UserController();
                user.deserialize(serializedUser);
                users.add(user);
            }
        }

        Platform.runLater(() -> {
            if (data.containsKey(BOARD_FIELD) && board != null) {
                @SuppressWarnings("unchecked")
                var serializedBoard = (HashMap<String, Object>) data.get(BOARD_FIELD);
                board.deserialize(serializedBoard);
            } else if (board != null) {
                DBManager.getInstance().serialize();
            }
        });

        for (Observer<GameData> observer : observers) {
            observer.update(this);
        }
    }
}