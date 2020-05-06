package it.polito.tdp.extflightdelays.model;

public class Rotta {
	
	//private String idRotta;
	private Airport source;
	private Airport target;
	private float peso;
	//private int conta;

	public Rotta(Airport source, Airport target) {
		// TODO Auto-generated constructor stub
		//this.idRotta = idRotta;
		this.source=source;
		this.target=target;
		this.peso = 0;
		//this.conta = 0;
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
		return this.peso;
	}

	public void setPeso(float peso) {
		this.peso=peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		Rotta other = (Rotta) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
}
