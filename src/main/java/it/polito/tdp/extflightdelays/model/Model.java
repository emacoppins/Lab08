package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport>idMap;
	private ExtFlightDelaysDAO dao;
	
	
	public Model() {
		idMap=new HashMap<>();
		dao=new ExtFlightDelaysDAO();
		dao.loadAllAirports(idMap);

		}
		
	
	public void creaGrafo(int n) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, this.idMap.values());
	
		for(Line l: dao.getCollegamentiMaggioriMediaNumeroDato(idMap, n)) {
			DefaultWeightedEdge edge = grafo.getEdge(l.getA1(), l.getA2());
			if(edge == null) {
			Graphs.addEdge(grafo, l.getA1(), l.getA2(), l.getPeso());
			}
			else {
				// a posto di fare query complicate, utilizzo un trucchetto per fare le rotte sia su andata che su ritorno
				double peso = grafo.getEdgeWeight(edge);
				double newPeso = (peso + l.getPeso())/2;
				grafo.setEdgeWeight(edge, newPeso);
			}
			}
	}

	public int nvertici() {
		
		return grafo.vertexSet().size();
	}
	
	
	public int narchi() {
		return grafo.edgeSet().size();
	}
	
	
	public List<Line> getRotte(){
		//uso la classe Rotta per salvare gli archi del grafo con il relativo peso
		List<Line> rotte = new ArrayList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			rotte.add(new Line(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e)));
		}
		return rotte;
	}
}
