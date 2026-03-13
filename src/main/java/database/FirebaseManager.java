package database;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

class FirebaseManager {
    public final Firestore db;

    public FirebaseManager() throws IOException {
        String serviceAccountLocation = getClass().getResource("/serviceAccountKey/key.json").getFile();
        FileInputStream serviceAccount = new FileInputStream(serviceAccountLocation);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        this.db = FirestoreClient.getFirestore();
    }

    public Firestore getDB() {
        return this.db;
    }
}
