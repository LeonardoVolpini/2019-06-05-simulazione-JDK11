package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.model.Agente.Stato;
import it.polito.tdp.crimes.model.Evento.EventType;

public class Simulator {


	//modello del mondo 
	//private SimpleWeightedGraph<Distretto, DefaultWeightedEdge> grafo;
	private Distretto stazionePolizia;
	private List<Event> events;
	private Map<Integer,Distretto> idMap;
	private List<Agente> agenti;
	
	//coda degli eventi
	private PriorityQueue<Evento> queue;
	
	//parametri in input
	int N; //numero agenti
	LocalDateTime oraInizio;
	LocalDateTime oraFine;
	
	//parametri in output
	int numCasiMalGestiti;
	
	
	public void init(Distretto polizia, int N, List<Event> allEvents, Map<Integer,Distretto> idMap) {
		//this.grafo=g;
		this.N=N;
		this.stazionePolizia=polizia;
		this.events= new ArrayList<>();
		this.events= allEvents;
		this.queue= new PriorityQueue<>();
		this.idMap= new HashMap<>();
		this.idMap=idMap;
		this.agenti= new ArrayList<>();
		
		for (int i=1; i<=N; i++) {
			Agente a = new Agente (stazionePolizia,Stato.LIBERO,i);
			agenti.add(a);
		}
		
		this.numCasiMalGestiti=0;
		
		this.oraInizio= events.get(0).getReported_date();
		this.oraFine= events.get(events.size()-1).getReported_date();
		
		int inseriti=0;
		while(inseriti!=events.size()) {
			Event event= events.get(inseriti);
			Distretto d= idMap.get(event.getDistrict_id());
			Evento e= new Evento(event.getReported_date(),EventType.ASSEGNAZIONE_AGENTE,idMap.get(event.getDistrict_id()),null,event);
			this.queue.add(e);
			inseriti++;
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e = queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Evento e) {
		LocalDateTime data= e.getOra();
		Distretto distretto = e.getDistretto();
		Event crimine = e.getCrimine();
		
		switch(e.getType()) {
		case ASSEGNAZIONE_AGENTE:
			double min=Double.MAX_VALUE;
			Agente assegnato=null;
			for (Agente a : this.agenti) {
				if (a.getStato()==Stato.LIBERO) {
					double distanza= this.calcolaDistanza(distretto, a.getPosizione());
					if(distanza<min) {
						min=distanza;
						assegnato=a;
					}
				}
			}
			if (assegnato==null) 
				this.numCasiMalGestiti++;
			else
				this.queue.add(new Evento(data,EventType.TRANSITO,distretto,assegnato,crimine) );
			break;
		case TRANSITO:
			Agente agente = e.getAgente();
			agente.setStato(Stato.IN_TRANSITO);
			long tempoTransito= this.calcolaTempoTransito(this.calcolaDistanza(distretto, agente.getPosizione()));
			LocalDateTime time= data.plusSeconds(tempoTransito) ;
			if (time.isAfter(data.plusMinutes(15)))
				this.numCasiMalGestiti++;
			this.queue.add(new Evento(time,EventType.GESTIONE_CASO,distretto,agente,crimine) );
			break;
		case GESTIONE_CASO:
			Agente agent = e.getAgente();
			agent.setPosizione(distretto);
			agent.setStato(Stato.OCCUPATO);
			long durata= this.durataEvento(crimine.getOffense_category_id());
			this.queue.add(new Evento(data.plusSeconds(durata),EventType.CASO_RISOLTO,distretto,agent,crimine) );
			break;
		case CASO_RISOLTO:
			Agente ag = e.getAgente();
			ag.setStato(Stato.LIBERO);
			break;
		}
	}
	
	private Long durataEvento(String tipo) {
		long durata=0;
		if (tipo.equals("all_other_crimes")) {
			int prob= (int) (Math.random()*100);
			if (prob<50) 
				durata=2*3600;
			else
				durata=3600;
		}
		else
			durata=3600;
		return durata;
	}
	
	private double calcolaDistanza(Distretto d1, Distretto d2) {
		return LatLngTool.distance(d1.getCentro(), d2.getCentro(), LengthUnit.KILOMETER);
	}
	
	private long calcolaTempoTransito(double distanza) { //forse problemi da double a long
		return (long)(distanza/60*3600); //in secondi
	}

	public int getNumCasiMalGestiti() {
		return numCasiMalGestiti;
	}
	
}
