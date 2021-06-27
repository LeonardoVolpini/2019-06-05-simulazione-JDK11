package it.polito.tdp.crimes.model;

public class Agente {

	public enum Stato{
		LIBERO,
		IN_TRANSITO,
		OCCUPATO;
	}
	
	private Distretto posizione;
	private Stato stato;
	private Integer id;
	public Agente(Distretto posizione, Stato stato, Integer id) {
		super();
		this.posizione = posizione;
		this.stato = stato;
		this.id=id;
	}
	public Distretto getPosizione() {
		return posizione;
	}
	public void setPosizione(Distretto posizione) {
		this.posizione = posizione;
	}
	public Stato getStato() {
		return stato;
	}
	public void setStato(Stato stato) {
		this.stato = stato;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
