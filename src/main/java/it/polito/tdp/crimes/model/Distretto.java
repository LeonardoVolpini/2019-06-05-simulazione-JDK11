package it.polito.tdp.crimes.model;

import com.javadocmd.simplelatlng.LatLng;

public class Distretto {

	private Integer id;
	private double lon;
	private double lan;
	private LatLng centro;
	private int numCrimini;
	public Distretto(Integer id, double lon, double lan, int numCrimini) {
		super();
		this.id = id;
		this.lon = lon;
		this.lan=lan;
		centro= new LatLng(this.lan,this.lon);
		this.numCrimini=numCrimini;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLan() {
		return lan;
	}
	public void setLan(double lan) {
		this.lan = lan;
	}
	
	public LatLng getCentro() {
		return centro;
	}
	public void setCentro(LatLng centro) {
		this.centro = centro;
	}
	public int getNumCrimini() {
		return numCrimini;
	}
	public void setNumCrimini(int numCrimini) {
		this.numCrimini = numCrimini;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Distretto other = (Distretto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
