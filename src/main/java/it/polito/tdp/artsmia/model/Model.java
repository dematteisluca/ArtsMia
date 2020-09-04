package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;


public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> graph;
	private Map<Integer, ArtObject> idMap;
	private ArtsmiaDAO dao;
	
	public Model() {
		this.dao= new ArtsmiaDAO();
	}
	
	public void creaGrafo() {
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<Integer, ArtObject>();
		dao.listObjects(idMap);
		Graphs.addAllVertices(graph, idMap.values());
		for(Adiacenza a: dao.getAdiacenze(idMap)) {
			if(graph.getEdge(a.getA1(), a.getA2())==null) {
				Graphs.addEdgeWithVertices(graph, a.getA1(), a.getA2(), a.getPeso());
			}
		}
	}
	
	public int vertexNumber() {
		return graph.vertexSet().size();
	}
	
	public int edgeNumber() {
		return graph.edgeSet().size();
	}

	
	public List<ArtObject> dammiConnesse(Integer id){
		if(idMap.containsKey(id)) {
			ArtObject a= idMap.get(id);
	List<ArtObject> vicini=Graphs.neighborListOf(this.graph, a);
		
	return vicini;
		} else return null;
	}

}
