package dice;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class DiceTest {

    @Test
    public void diceTest_roll() {
        DiceModel model = new DiceModel();

        model.roll();

        int output = model.getDiceOutput();
        boolean result = output > 0 && output < 6;

        assertTrue(result);
    }
}
