package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Evento implements Comparable<Evento> {

	public enum EventType{
		ASSEGNAZIONE_AGENTE,
		TRANSITO,
		GESTIONE_CASO,
		CASO_RISOLTO
	}
	private LocalDateTime ora;
	private Distretto distretto;
	private Agente agente;
	private EventType type;
	private Event crimine;
	public Evento(LocalDateTime ora, EventType type, Distretto distretto, Agente agente, Event crimine) {
		super();
		this.ora = ora;
		this.type= type;
		this.distretto = distretto;
		this.agente = agente;
		this.crimine=crimine;
	}
	public LocalDateTime getOra() {
		return ora;
	}
	public Distretto getDistretto() {
		return distretto;
	}
	public void setDistretto(Distretto distretto) {
		this.distretto = distretto;
	}
	public Agente getAgente() {
		return agente;
	}
	public void setAgente(Agente agente) {
		this.agente = agente;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public void setOra(LocalDateTime ora) {
		this.ora = ora;
	}
	
	public Event getCrimine() {
		return crimine;
	}
	public void setCrimine(Event crimine) {
		this.crimine = crimine;
	}
	@Override
	public String toString() {
		return "Evento [ora=" + ora + ", distretto=" + distretto + ", agente=" + agente + ", type=" + type
				+ ", crimine=" + crimine + "]";
	}
	@Override
	public int compareTo(Evento o) {
		return this.ora.compareTo(o.ora);
	}
	
	
}
