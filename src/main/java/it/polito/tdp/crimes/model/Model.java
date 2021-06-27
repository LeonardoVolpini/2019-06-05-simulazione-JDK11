package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private SimpleWeightedGraph<Distretto, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private Map<Integer,Distretto> idMap;
	private boolean grafoCreato;
	
	private Distretto stazionePolizia;
	private Simulator sim;
	
	public Model() {
		this.dao = new EventsDao();
		this.idMap= new HashMap<>();
		this.grafoCreato=false;
		this.stazionePolizia=null;
		this.sim= new Simulator();
	}
	
	public void creaGrafo(int anno) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao.loadIdMap(idMap,anno); //con solo i vertici
		Graphs.addAllVertices(grafo, idMap.values());
		for (Distretto d1 : grafo.vertexSet()) {
			for (Distretto d2: grafo.vertexSet()) {
				if(!d1.equals(d2)) {
					double distanza = LatLngTool.distance(d1.getCentro(), d2.getCentro(), LengthUnit.KILOMETER);
					Graphs.addEdgeWithVertices(grafo, d1, d2, distanza);
				}
			}
		}
		this.grafoCreato=true;
	}
	
	public List<Integer> getAllYears(){
		return dao.allYears();
	}
	
	public boolean isGrafoCreato() {
		return grafoCreato;
	}

	public int getNumVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return grafo.edgeSet().size();
	}

	public Set<Distretto> getVertici(){
		return grafo.vertexSet();
	}
	
	public List<DistrettoAdiacente> adiacenti(Distretto distretto){ 
		List<DistrettoAdiacente> ris= new ArrayList<>();
		for (DefaultWeightedEdge e : grafo.edgesOf(distretto)) {
			DistrettoAdiacente g = new DistrettoAdiacente(Graphs.getOppositeVertex(grafo, e, distretto), grafo.getEdgeWeight(e));
			ris.add(g);
		}
		Collections.sort(ris);
		return ris;
	}

	public List<Integer> getAllMonths(int anno) {
		return dao.allMonths(anno);
	}

	public List<Integer> getAllDays(int anno) {
		return dao.allDays(anno);
	}
	
	private Distretto calcolaStazionePolizia() {
		Distretto dis=null;
		int min=Integer.MAX_VALUE;
		for (Distretto d : grafo.vertexSet()) {
			if(d.getNumCrimini()<min) {
				min=d.getNumCrimini();
				dis=d;
			}
		}
		return dis;
	}
	
	public void Simula(int n, int anno, int mese, int giorno) {
		this.stazionePolizia=this.calcolaStazionePolizia();
		this.sim.init(stazionePolizia, n, dao.getAllEventsByYearMonthDay(anno, mese, giorno), idMap);
		this.sim.run();
	}
	
	public int getNumCasiMalGestiti() {
		return sim.getNumCasiMalGestiti();
	}
	
	
}
