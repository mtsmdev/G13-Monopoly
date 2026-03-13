package database;

import com.google.cloud.firestore.*;
import user.UserController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class DBManager {
    private static DBManager instance;
    private Firestore db;
    private final GameData gameData;

    private static final String GAMES_COLLECTION = "games";

    public DBManager() {
        super();
        gameData = new GameData();

        try {
            db = new FirebaseManager().getDB();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }

        return instance;
    }

    private void registerSnapshotListener(DocumentReference docRef) {
        docRef.addSnapshotListener((snapshot, exception) -> {
            if (exception != null) exception.printStackTrace();
            if (snapshot != null) deserialize(snapshot);
        });
    }

    public CollectionReference getCollection() {
        return db.collection(GAMES_COLLECTION);
    }

    public String[] getGames() {
        var documentNames = new ArrayList<String>();
        getCollection().listDocuments().forEach(doc -> documentNames.add(doc.getId()));

        return documentNames.toArray(new String[0]);
    }

    public Map<String, Object> getDocRefData() {
        var gameID = UserController.getInstance().getCurrentGameID();
        var docrefFuture = getCollection().document(gameID).get();
        DocumentSnapshot snapshot = null;

        try {
            snapshot = docrefFuture.get();
        } catch(Exception ignored) {}

        if (snapshot == null) return Collections.emptyMap();
        return snapshot.getData();
    }

    public void forceUpdate () {
        gameData.deserialize(getDocRefData());
    }

    public void createGame() {
        serialize();
    }

    public void joinGame() {
        forceUpdate();
        gameData.getUsers().add(UserController.getInstance());
        serialize();

        var gameID = UserController.getInstance().getCurrentGameID();
        registerSnapshotListener(getCollection().document(gameID));
    }

    public void serialize() {
        var serialized = gameData.serialize();

        var col = DBManager.getInstance().getCollection();
        var gameID = UserController.getInstance().getCurrentGameID();

        try {
            col.document(gameID).set(serialized).get();
        } catch (Exception ignored) {}
    }

    private void deserialize(DocumentSnapshot snapshot) {
        var data = snapshot.getData();
        if (data != null) gameData.deserialize(data);
    }

    public GameData getGameData() {
        return gameData;
    }
}
