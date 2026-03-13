package dice;

import javafx.scene.layout.AnchorPane;

import java.util.function.Consumer;

import static utils.Music.playSoundEffect;

public class DiceController {

    private final DiceModel model;
    public final DiceView view;

    public DiceController(AnchorPane board, Consumer<DiceController> rollAction, Consumer<DiceController> dismissAction) {
        model = new DiceModel();
        view = new DiceView(board, () -> rollAction.accept(this), () -> dismissAction.accept(this));
        model.registerObserver(view);
    }

    public void roll(){
        playSoundEffect("dice.wav");
        this.model.roll();
    }

    public void remove() {
        this.view.remove();
    }

    public int getDiceOutput(){
        return this.model.getDiceOutput();
    }

}
