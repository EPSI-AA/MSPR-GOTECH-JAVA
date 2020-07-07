package fr.gotech.model;

import java.io.IOException;

public class Equipement {

	public String achat;
	public String admin;
	public String etat;
	public String garantie;
	public String hostname;
	public String install;
	public String marque;
	public String modele;
	public String sn;
	public String type;


	
	public Equipement()throws IOException{

	}
	
	public Equipement(String achat, String admin, String etat, String garantie, String hostname, String install, String marque, String modele, String sn, String type) throws IOException {
		this.achat=achat;
		this.admin=admin;
		this.etat=etat;
		this.garantie=garantie;
		this.hostname=hostname;
		this.install=install;
		this.marque=marque;
		this.modele=modele;
		this.sn=sn;
		this.type=type;

	}
	
	//Getter et Setter
	
	public String getAchat() {
		return achat;
	}

	public void setAchat(String achat) {
		this.achat = achat;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getGarantie() {
		return garantie;
	}

	public void setGarantie(String garantie) {
		this.garantie = garantie;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getInstall() {
		return install;
	}

	public void setInstall(String install) {
		this.install = install;
	}

	public String getMarque() {
		return marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}

	public String getModele() {
		return modele;
	}

	public void setModele(String modele) {
		this.modele = modele;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	private void setAdmin() {
	}

	/*
	 * 
	Méthodes de la classe Equipement 
	*
	*/
	
}
