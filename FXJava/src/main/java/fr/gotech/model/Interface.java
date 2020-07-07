package fr.gotech.model;

import java.io.IOException;
import java.util.HashMap;


public class Interface {
	
	public String ip;
	public String cidr;
	public String gw;
	public String dns1;
	public String dns2;
	public String name;
	public String typeAdr;
	public String typeCon;

	public Interface() throws IOException {
	}
	
	public Interface(String ip, String cidr, String gw, String dns1, String dns2, String name, String typeAdr, String typeCon) throws IOException {
		this.ip = ip;
		this.cidr = cidr;
		this.gw = gw;
		this.dns1 = dns1;
		this.dns2 = dns2;
		this.name = name;
		this.typeAdr = typeAdr;
		this.typeCon = typeCon;
	}

	// Getter et Setter 
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	public String getGw() {
		return gw;
	}

	public void setGw(String gw) {
		this.gw = gw;
	}

	public String getDns1() {
		return dns1;
	}

	public void setDns1(String dns1) {
		this.dns1 = dns1;
	}

	public String getDns2() {
		return dns2;
	}

	public void setDns2(String dns2) {
		this.dns2 = dns2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeAdr() {
		return typeAdr;
	}

	public void setTypeAdr(String typeAdr) {
		this.typeAdr = typeAdr;
	}

	public String getTypeCon() {
		return typeCon;
	}

	public void setTypeCon(String typeCon) {
		this.typeCon = typeCon;
	}

	public HashMap<String,String> getAll(){
		HashMap<String,String> info = new HashMap<>();
		info.put("name",getName());
		info.put("typeCon",getTypeCon());
		info.put("typeAdr",getTypeAdr());
		info.put("IP",getIp());
		info.put("CIDR",getCidr());
		info.put("Gw",getGw());
		info.put("dns1",getDns1());
		info.put("dns2",getDns2());
		return info;
	}

}
