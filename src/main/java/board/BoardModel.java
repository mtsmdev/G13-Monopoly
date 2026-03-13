package board;

import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import observable.Observable;
import player.PlayerController;
import property.PropertyController;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardModel extends Observable<BoardModel> {
    private final List<PropertyController> properties;
    private final List<PlayerController> players;
    private final AnchorPane board;

    private int activePlayerIdx = 0;

    public BoardModel(AnchorPane board) {
        super();
        this.properties = new ArrayList<>();
        this.players = new ArrayList<>();
        this.board = board;
    }

    public void addProperty(PropertyController property) {
        properties.add(property);
        this.updateObservers(this);
    }

    public void addPlayer(PlayerController player){
        players.add(player);
        this.updateObservers(this);
    }

    public PlayerController getActivePlayer() {
        return players.get(activePlayerIdx);
    }

    public void setActivePlayerIdx(int index) {
        this.activePlayerIdx = index;
    }

    public void setActivePlayer(PlayerController player) {
        setActivePlayerIdx(players.indexOf(player));
    }
}
