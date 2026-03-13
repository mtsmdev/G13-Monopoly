package dice;

import observable.Observable;

import java.util.Random;

public class DiceModel extends Observable<DiceModel> {
    private int diceOutput = 0;
    private final Random random = new Random();

    public DiceModel(){
        roll();
    }

    public void roll() {
        diceOutput = (random.nextInt(5)) + 1;
        updateObservers(this);
    }

    public int getDiceOutput() {
        return diceOutput;
    }
}
