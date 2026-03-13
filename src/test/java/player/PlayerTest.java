package player;

import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import property.PropertyController;

import static org.junit.Assert.assertEquals;
public class PlayerTest {

    @Test(expected = NullPointerException.class)
    public void playerTest_emptyBoard() {
        AnchorPane board = new AnchorPane();

        new PlayerController("test", board, 1, null);
    }

}
