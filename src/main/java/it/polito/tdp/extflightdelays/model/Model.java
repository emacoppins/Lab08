package it.polito.tdp.extflightdelays.model;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private Graph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> idMap;	
	
	/**
	 * Costruttore della classe model
	 */
	public Model() {
		idMap = new HashMap<Integer,Airport>();
	}
	
	
	/**
	 * Metodo usato per costruire il grafo
	 * @param distanzaMin
	 * @return un grafo con n vertici (n aeroporti) e n archi che rispattanto il vincolo di distanzaMin
	 */
	public Graph<Airport, DefaultWeightedEdge> creaGrafo(int distanzaMin) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO ();

		dao.loadAllAirports(idMap);
		
		//Aggiungere i vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//Map<String, Rotta> rotte = dao.coppiaAeroporti(idMap);
		List<Rotta> rotte = dao.coppiaAeroporti(idMap, distanzaMin);
		
		for(Rotta r : rotte) {
			Graphs.addEdge(this.grafo, idMap.get(r.getSource().getId()), idMap.get(r.getTarget().getId()), r.getPeso());
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
