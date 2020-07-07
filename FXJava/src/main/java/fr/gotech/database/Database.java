package fr.gotech.database;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Database {

    public Firestore db = null;

    public Database() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/main/java/fr/gotech/gotec-3f791-firebase-adminsdk-rp74a-3569336a9d.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://gotec-3f791.firebaseio.com/")
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
        Firestore db = FirestoreClient.getFirestore();
        this.db = db;
    }


    /*
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
         /*
        Database dbConnect = new Database();

        System.out.println(trouver);


        Database dbConnect = new Database();
        ApiFuture<QuerySnapshot> future = dbConnect.db.collection("client")
                .document("CERI")
                .collection("equipement").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents){

            System.out.println(document.getString("type"));
            String type = document.getString("type");
            System.out.println(type);

            System.out.println(document.getString("marque"));
            String marque = document.getString("marque");
            System.out.println(marque);

            System.out.println(document.getString("modele"));
            String modele = document.getString("modele");
            System.out.println(modele);

            System.out.println(document.getString("sn"));
            String sn = document.getString("sn");
            System.out.println(sn);

            System.out.println(document.getString("etat"));
            String etat = document.getString("etat");
            System.out.println(etat);
            System.out.println("------------");
        }


        ApiFuture<QuerySnapshot> collections = db.collection("typeEquipement").document("Routeur").collection("marqueEquipement").get();
        List<QueryDocumentSnapshot> documents = collections.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("Found subcollection with id: " + document.getId());
        }
        */
        /*
        String client = "CERI";
        String hostname = "SRV-AD-01";
        String path = "";
        ApiFuture<QuerySnapshot> future = db.collection("client")
                .document(client)
                .collection("equipement")
                .whereEqualTo("hostname", hostname).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (DocumentSnapshot document : documents) {
            path = document.getId();
            System.out.println(path);
        }
        Interface help = new Interface("10.10.10.10", "", "","","","","","");
        ApiFuture<com.google.cloud.firestore.WriteResult> ok = db.collection("client").document(client).collection("equipement").document(path).collection("interface").document().set(help);
    */
    }

