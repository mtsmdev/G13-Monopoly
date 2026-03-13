package board;

import cards.CardController;
import database.GameState;
import dice.DiceController;
import database.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import player.PlayerController;
import property.PropertyController;
import serializable.Serializable;
import user.UserController;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static ui.DialogView.*;
import static utils.Music.*;
import static utils.Utils.loadProperties;

public class BoardController implements Serializable {

    private final BoardModel boardModel;
    private final BoardView boardView;
    private final AnchorPane board;
    private final CardController cards;

    public BoardController(AnchorPane board) {
        DBManager.getInstance().getGameData().setBoard(this);
        boardModel = new BoardModel(board);
        boardView = new BoardView(board);
        cards = new CardController();
        this.board = board;

        var properties = loadProperties(this);
        for (PropertyController property: properties) {
            boardModel.addProperty(property);
        }

        boardModel.registerObserver(boardView);
        boardView.drawBoard(boardModel);
    }

    public List<PlayerController> getPlayers() {
        return boardModel.getPlayers();
    }

    public PlayerController addPlayer(String name){
        var nextId = boardModel.getPlayers().size() + 1;
        var player = new PlayerController(name, board, nextId, boardModel.getProperties());
        boardModel.addPlayer(player);
        return player;
    }

    public void startGame(){
        for (var player : boardModel.getPlayers()) {
            player.moveTo(boardModel.getProperties().get(0));
        }
        move();

        stopMusic();
        playMusic("main.wav");
    }

    public void openRegels(){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/spelregels.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Spelregels");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e){
            System.out.println("Kan spelregels niet laden");
        }

    }

    public void move() {
        if (!boardModel.getActivePlayer().getPlayername().equals(UserController.getInstance().getName())) return;
        new DiceController(board, dc -> {
            playSoundEffect("lopen.wav");
            dc.roll();
            var out = dc.getDiceOutput();
            var activePlayer = boardModel.getActivePlayer();
            var newCurrentProp = boardModel.getProperties().get((activePlayer.getCurrentPropLoc() + out) % 36);
            activePlayer.moveTo(newCurrentProp);

            DBManager.getInstance().serialize();
        }, dc -> {
            dc.remove();
            var property = boardModel.getProperties().get(boardModel.getActivePlayer().getCurrentPropLoc());
            runPropertyAction(property);
        });
    }

    private void runPropertyAction(PropertyController property) {
        switch(property.getType()) {
            case START -> {
                boardModel.getActivePlayer().setBalance(boardModel.getActivePlayer().getBalance() + 200);
                nextMove();
            }
            case GASTUDEREN -> {
                boardModel.getActivePlayer().moveTo(boardModel.getProperties().get(9));
                nextMove();
            }
            case KANS -> cards.getKansCard(this, boardModel.getActivePlayer(), property, this::nextMove);
            case ALGEMEENFONDS -> cards.getAlgemeenFonds(this, boardModel.getActivePlayer(), property, this::nextMove);

            case BELASTING -> {
                var player = boardModel.getActivePlayer();
                player.setBalance(player.getBalance() - property.getPrice());
                createBelastingDialog(board, this::nextMove);
            }

            case PROPERTY -> {
                if (property.getOwner() == null) {
                    if (boardModel.getActivePlayer().getBalance() < property.getPrice()) {
                        nextMove();
                        return;
                    }
                    createPropertyDialog(board, property, () -> {
                        property.buy(boardModel.getActivePlayer());
                        nextMove();
                    }, this::nextMove);
                } else if (property.getOwner() == boardModel.getActivePlayer()) {
                    if(property.getHouses() == 5 || boardModel.getActivePlayer().getBalance() < property.getRent()) {
                        nextMove();
                    }
                    else createHuisDialog(board, property, () -> {
                        if (property.getHouses() == 5 || boardModel.getActivePlayer().getBalance() < property.getRent()) {
                            nextMove();
                        } else {
                            property.setHouses(property.getHouses()+1);
                            boardModel.getActivePlayer().setBalance(boardModel.getActivePlayer().getBalance() - property.getRent());
                            nextMove();
                        }
                    }, this::nextMove);
                } else {
                    property.payAndreceiveRent(boardModel.getActivePlayer());
                    nextMove();
                }
            }

            default -> nextMove();
        }
    }

    private void nextMove() {
        var activePlayer = boardModel.getActivePlayer();
        var viablePlayers = boardModel.getPlayers().stream().filter(p -> p.getBalance() > -500 || p.getPlayername().equals(activePlayer.getPlayername())).collect(Collectors.toList());
        var newActivePlayer = viablePlayers.get((viablePlayers.indexOf(activePlayer) + 1) % viablePlayers.size());

        if (viablePlayers.size() == 2 && activePlayer.getBalance() < -500) {
            DBManager.getInstance().getGameData().setGameState(GameState.FINISHED);
        } else {
            boardModel.setActivePlayerIdx(boardModel.getPlayers().indexOf(newActivePlayer));
        }

        DBManager.getInstance().serialize();

        if (boardModel.getPlayers().size() == 1) move();
    }

    public AnchorPane getBoard() {
        return board;
    }

    public List<PropertyController> getProperties() {
        return boardModel.getProperties();
    }

    @Override
    public Map<String, Object> serialize() {
        var data = new HashMap<String, Object>();

        var serializedPlayers = boardModel.getPlayers().stream().map(PlayerController::serialize).collect(Collectors.toList());
        data.put("players", serializedPlayers);
        data.put("activePlayer", boardModel.getActivePlayerIdx());

        var serializedProperties = boardModel.getProperties().stream().map(PropertyController::serialize).collect(Collectors.toList());
        data.put("properties", serializedProperties);

        return data;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        @SuppressWarnings("unchecked")
        var serializedPlayers = (ArrayList<HashMap<String, Object>>) data.get("players");

        for (int i = 0; i < boardModel.getPlayers().size(); i++) {
            if (serializedPlayers.isEmpty()) break;
            var serializedPlayer = serializedPlayers.get(i);
            boardModel.getPlayers().get(i).deserialize(serializedPlayer);
        }

        if (!serializedPlayers.isEmpty()) {
            serializedPlayers.subList(0, boardModel.getPlayers().size()).clear();
            for (HashMap<String, Object> serializedPlayer : serializedPlayers) {
                var name = (String) serializedPlayer.get("name");
                var player = addPlayer(name);
                player.deserialize(serializedPlayer);
            }
        }

        //pls laat dit staan het zorgt ervoor dat alles werkt
        var playerExists = (int) getPlayers().stream().filter(n -> n.getPlayername().equals(UserController.getInstance().getName())).count();

        if(playerExists == 0) {
            addPlayer(UserController.getInstance().getName());
            DBManager.getInstance().serialize();
        }

        if (data.get("activePlayer") != null) {
            var serializedActivePlayer = boardModel.getPlayers().get((int) (long) data.get("activePlayer"));

            if (serializedActivePlayer == boardModel.getActivePlayer()) return;
            boardModel.setActivePlayer(serializedActivePlayer);
            move();
        }

        @SuppressWarnings("unchecked")
        var serializedProperties = (ArrayList<HashMap<String, Object>>) data.get("properties");
        for (int i = 0; i < boardModel.getProperties().size(); i++) {
            boardModel.getProperties().get(i).deserialize(serializedProperties.get(i));
        }

        if (DBManager.getInstance().getGameData().getGameState() == GameState.FINISHED) {
            var PlayerWon = boardModel.getPlayers().stream().filter(p -> p.getBalance() > -500).collect(Collectors.toList()).get(0);
            ((Text) board.getScene().lookup("#playerWon")).setText(PlayerWon.getPlayername() + "Won!");
        }
    }
}
