package it.polito.tdp.artsmia.model;

public class Adiacenza {
	
	//per recuperare i dati che ci fornisce la query ovvero le colonne della tabella in esame

	private int o1;		//id oggetto 1
	private int o2;		//id oggetto 2
	private int peso;	//conta del dataBase ovvero quante volte è capitato che due oggetti apparissero insieme nella stessa esibizione
	
	public Adiacenza(int o1, int o2, int peso) {
		super();
		this.o1 = o1;
		this.o2 = o2;
		this.peso = peso;
	}

	public int getO1() {
		return o1;
	}

	public void setO1(int o1) {
		this.o1 = o1;
	}

	public int getO2() {
		return o2;
	}

	public void setO2(int o2) {
		this.o2 = o2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	
	
	
}
