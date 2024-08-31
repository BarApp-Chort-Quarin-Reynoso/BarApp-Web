package com.barapp.web.data;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.FileInputStream;
import java.util.UUID;

@Configuration
public class FirestoreConfig {
    @Bean
    @Scope(scopeName = "singleton")
    public FirebaseApp firebaseApp() throws Exception {
        FileInputStream serviceAccount = new FileInputStream("./firebase-private-key.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("barapp-b1bc0.appspot.com")
                .build();

        return FirebaseApp.initializeApp(options, UUID.randomUUID().toString());
    }

    @Bean
    @Scope(scopeName = "singleton")
    public Firestore firestore(FirebaseApp firebaseApp) throws Exception {
        return FirestoreClient.getFirestore(firebaseApp);
    }

    @Bean
    @Scope(scopeName = "singleton")
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) throws Exception {
        return FirebaseAuth.getInstance(firebaseApp);
    }

    @Bean
    @Scope(scopeName = "singleton")
    public StorageClient firebaseStorage(FirebaseApp firebaseApp) throws Exception {
        return StorageClient.getInstance(firebaseApp);
    }

}
