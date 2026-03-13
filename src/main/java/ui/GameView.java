package ui;

import board.BoardController;
import database.DBManager;
import database.GameData;
import database.GameState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import observable.Observable;
import observable.Observer;
import user.UserController;

public class GameView extends Observable<GameData> implements Observer<GameData>  {
    @FXML
    Pane playerList, board;

    @FXML
    Text gameID;

    @FXML
    Button startGame, leaveGame, spelregels;

    @FXML
    VBox overlay, overlayFinished;

    private GameState state = GameState.WAITING;
    private BoardController boardController;

    @FXML
    public void initialize() {
        board.setClip(new Rectangle(800, 800));
        DBManager.getInstance().getGameData().registerObserver(this);
        boardController = new BoardController((AnchorPane) board);

        gameID.setText(UserController.getInstance().getCurrentGameID());

        startGame.setOnAction(event -> {
            DBManager.getInstance().getGameData().setGameState(GameState.PLAYING);
            DBManager.getInstance().serialize();
            overlay.setVisible(false);
        });

        spelregels.setOnAction(event -> {
            boardController.openRegels();
        });

        leaveGame.setOnAction(event -> {
            Platform.exit();
        });
    }
    @Override
    public void update(GameData data) {
        for (Observer<GameData> observer: observers) {
            observer.update(data);
        }

        Platform.runLater(() -> {
            var players = DBManager.getInstance().getGameData().getBoard().getPlayers();
            var entries = SideBarView.getPlayerList(players).getChildren();

            if (!players.isEmpty()) ((Text) overlay.getChildren().get(0)).setText("Players: " + players.size());

            playerList.getChildren().clear();
            playerList.getChildren().addAll(entries);

            var newState = DBManager.getInstance().getGameData().getGameState();
            if (newState != state) {
                state = newState;

                if (state == GameState.PLAYING) {
                    boardController.startGame();
                    overlay.setVisible(false);
                }

                if (state == GameState.FINISHED) {
                    overlayFinished.setVisible(true);
                }
            }
        });
    }

}
