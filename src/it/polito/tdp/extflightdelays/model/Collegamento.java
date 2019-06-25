package it.polito.tdp.extflightdelays.model;

public class Collegamento {

	private Airport a1;
	private Airport a2;
	private Double  distMedia;
	public Collegamento(Airport a1, Airport a2, Double distMedia) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.distMedia = distMedia;
	}
	public Airport getA1() {
		return a1;
	}
	public void setA1(Airport a1) {
		this.a1 = a1;
	}
	public Airport getA2() {
		return a2;
	}
	public void setA2(Airport a2) {
		this.a2 = a2;
	}
	public Double getDistMedia() {
		return distMedia;
	}
	public void setDistMedia(Double distMedia) {
		this.distMedia = distMedia;
	}
	@Override
	public String toString() {
		return "Collegamento [a1=" + a1 + ", a2=" + a2 + ", distMedia=" + distMedia + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a1 == null) ? 0 : a1.hashCode());
		result = prime * result + ((a2 == null) ? 0 : a2.hashCode());
		result = prime * result + ((distMedia == null) ? 0 : distMedia.hashCode());
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
		Collegamento other = (Collegamento) obj;
		if (a1 == null) {
			if (other.a1 != null)
				return false;
		} else if (!a1.equals(other.a1))
			return false;
		if (a2 == null) {
			if (other.a2 != null)
				return false;
		} else if (!a2.equals(other.a2))
			return false;
		if (distMedia == null) {
			if (other.distMedia != null)
				return false;
		} else if (!distMedia.equals(other.distMedia))
			return false;
		return true;
	}
	
	
}
