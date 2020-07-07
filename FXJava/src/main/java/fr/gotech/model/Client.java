package fr.gotech.model;

import java.io.IOException;

public class Client {
	
	//public Firestore db;
	public String adresse;
	public String cp;
	public String ville;
	public String tel;
	public String email;
	
	// Constructeur vide
	public Client() throws IOException {
		
	}
	
	//Définition de l'objet Client
	public Client(String adresse, String cp, String ville, String tel, String email) throws IOException {
			this.adresse = adresse;
			this.cp = cp;
			this.ville = ville;
			this.tel = tel;
			this.email = email;		
		}
	
	//Getter & Setter
	
	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/*
	 * 
	Méthodes de la classe client
	*
	*/
	
	
	//Lister tous les clients
	/*
	public static List<String> getAllClient() throws InterruptedException, ExecutionException  {
		List<String> Liste = new ArrayList<String>();
		int indice = 0;
		//asynchronously retrieve multiple documents
		ApiFuture<QuerySnapshot> future = db.collection("client").get();
		System.out.println(future.toString());
		List<QueryDocumentSnapshot> documents = future.get().getDocuments();
		for (QueryDocumentSnapshot document : documents) {
			//if (!document.getId().equals(null)) {
			//System.out.println(document.getId());
			//}
			Liste.add(document.getId());
		}
		return Liste;
	}


	public String addClient(Client add, String nomClient) throws InterruptedException, ExecutionException {
		DocumentReference docRef = db.collection("client").document(nomClient);
		ApiFuture<DocumentSnapshot> future = docRef.get();
		DocumentSnapshot document = future.get();
		if (document.exists()) {
			System.out.println("Ce client existe déjà");
		}
		else {
			ApiFuture<com.google.cloud.firestore.WriteResult> insert = docRef.set(add);
			return("Update time : " + insert.get().getUpdateTime());
		}		
		/*
		CollectionReference var = db.collection("client");
		Query query = var.where("id", nomClient);
		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		if (((QuerySnapshot) querySnapshot).size() > 0)
		{
			System.out.println("Ce client existe déjà");
		}
		else
		{
			ApiFuture<com.google.cloud.firestore.WriteResult> future = db.collection("client").document(nomClient).set(add);
			return("Update time : " + future.get().getUpdateTime());
		}

		return null;
	}
	
	public Client getOneClient(String nomClient, Client see) throws InterruptedException, ExecutionException {
		DocumentReference docRef = db.collection("client").document(nomClient);
		ApiFuture<DocumentSnapshot> future = docRef.get();
		DocumentSnapshot document = future.get();
		see = document.toObject(Client.class);
		return(see);
	}
	*/
	/* ------ Static
	public static void getAllClient() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> future = db.collection("client").get();
		List<QueryDocumentSnapshot> documents = future.get().getDocuments();
		int count =0;
		for (QueryDocumentSnapshot document : documents) {
			count++;
			System.out.println(document.getId());
		}
		System.out.println("Il y a "+count+" clients");
	}


	public static ObservableList comboboxClient(ObservableList options) throws ExecutionException, InterruptedException {
		ApiFuture<QuerySnapshot> combo = db.collection("client").get();
		List<QueryDocumentSnapshot> documents = combo.get().getDocuments();
		for (QueryDocumentSnapshot document : documents) {
			options.add(document.getId());
		}
		return options;
	}
	
	//Editer un client
	public String editClient(String valeur) {
		
		return valeur;
	}
	*/

}
