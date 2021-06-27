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

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private SimpleWeightedGraph<Distretto, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private Map<Integer,Distretto> idMap;
	//private List<Adiacenza> adiacenze; //puo essere superfluo
	private boolean grafoCreato;
	//private ConnectivityInspector<Vertice, DefaultWeightedEdge> ci; //se chiede roba connessa ad un vertice
	
	//private List<Vertice> percorsoBest; //nel caso di ricorsione per percorso max
	//private Integer pesoMax; //peso del percorso
	
	public Model() {
		this.dao = new EventsDao();
		this.idMap= new HashMap<>();
		//this.adiacenze= new ArrayList<>();
		this.grafoCreato=false;
		//this.percorsoBest= new ArrayList<>(); //per ricorsione
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
		//this.ci= new ConnectivityInspector<>(grafo);
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
}
