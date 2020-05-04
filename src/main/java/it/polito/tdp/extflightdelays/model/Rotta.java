package it.polito.tdp.extflightdelays.model;

public class Rotta {
	
	private Airport source;
	private Airport target;
	private float peso;

	public Rotta(Airport source, Airport target) {
		// TODO Auto-generated constructor stub
		this.source=source;
		this.target=target;
	}

	public Airport getSource() {
		return source;
	}

	public void setSource(Airport source) {
		this.source = source;
	}

	public Airport getTarget() {
		return target;
	}

	public void setTarget(Airport target) {
		this.target = target;
	}

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}

}
