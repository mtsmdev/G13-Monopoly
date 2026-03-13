package player;

import lombok.Getter;
import observable.Observable;

@Getter
public class PlayerModel extends Observable<PlayerModel> {
    private final int ID;
    private int posX;
    private int posY;
    private String name;
    private int balance;
    private int currentPropLoc = 0;

    public PlayerModel(String name, int balance, int ID) {
        this.name = name;
        this.balance = balance;
        this.ID = ID;
        this.posX = 700 + 20 * ID;
        this.posY = 700 + 20 * ID;
    }

    public void setName(String name) {
        this.name = name;
        this.updateObservers(this);
    }

    public void setBalance(int balance) {
        this.balance = balance;
        this.updateObservers(this);
    }

    public void setCurrentPropLoc(int propLoc) {
        this.currentPropLoc = propLoc;
        this.updateObservers(this);
    }
}
