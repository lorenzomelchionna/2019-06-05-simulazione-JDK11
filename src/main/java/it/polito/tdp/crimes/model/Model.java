package it.polito.tdp.crimes.model;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	
	private SimpleWeightedGraph<District,DefaultWeightedEdge> grafo;
	
	public Model() {
		
		dao = new EventsDao();
		
	}
	
	public String creaGrafo(int anno) {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, dao.getDistretti(anno));
		
		for(District d : grafo.vertexSet()) {
			
			for(District dd : grafo.vertexSet()) {
				
				if(d.getId() != dd.getId()) {
					
					double distance = LatLngTool.distance(d.getCoords(), dd.getCoords(), LengthUnit.KILOMETER);
					
					if(distance > 0) {
						Graphs.addEdgeWithVertices(grafo, d, dd, distance);
					}
					
				}
				
			}
			
		}
		
		return "#Vertici: "+grafo.vertexSet().size()+"\n#Archi: "+grafo.edgeSet().size()+"\n";
		
	}
	
}
