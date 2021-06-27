package it.polito.tdp.crimes.model;

public class DistrettoAdiacente implements Comparable<DistrettoAdiacente>{

	private Distretto distretto;
	private Double distanza;
	public DistrettoAdiacente(Distretto distretto, Double distanza) {
		super();
		this.distretto = distretto;
		this.distanza = distanza;
	}
	public Distretto getDistretto() {
		return distretto;
	}
	public void setDistretto(Distretto distretto) {
		this.distretto = distretto;
	}
	public Double getDistanza() {
		return distanza;
	}
	public void setDistanza(Double distanza) {
		this.distanza = distanza;
	}
	@Override
	public int compareTo(DistrettoAdiacente o) {
		return Double.compare(this.distanza, o.distanza);
	}
}
