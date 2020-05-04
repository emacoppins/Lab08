package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private Graph<Airport, DefaultWeightedEdge> grafo; //Vertici artobject e archi scelti in base all'esigenza
	private Map<Integer, Airport> idMap;	
	
	public Model() {
		idMap = new HashMap<Integer,Airport>();
	}
	
	public Graph<Airport, DefaultWeightedEdge> creaGrafo(int distanzaMin) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO ();

		dao.loadAllAirports(idMap);
		
		//Aggiungere i vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		List<Rotta> rotte = dao.coppiaAeroporti(idMap) ;
		for(Rotta r : rotte) {
			if(r.getPeso() >= distanzaMin) {
				Graphs.addEdge(this.grafo, idMap.get(r.getSource().getId()), idMap.get(r.getTarget().getId()), r.getPeso());
			}
		}	
		
		return grafo;
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
}
