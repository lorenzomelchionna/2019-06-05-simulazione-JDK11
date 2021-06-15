package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
	
	class ComparatoreVicini implements Comparator<Adiacenza>{

		@Override
		public int compare(Adiacenza a0, Adiacenza a1) {
			return a0.getDist().compareTo(a1.getDist());
		}
		
	}
	
	public String visualizzaAdiacenze() {
		
		String result = "";
		
		for(District d : grafo.vertexSet()) {
			 
			List<District> vicini = Graphs.neighborListOf(grafo, d);
			
			List<Adiacenza> distVicini = new ArrayList<>();
			
			result += "District "+d.getId()+" distances:\n";
			
			for(District dd : vicini) {
				distVicini.add(new Adiacenza(dd,grafo.getEdgeWeight(grafo.getEdge(d, dd))));
			}
		
			Collections.sort(distVicini, new ComparatoreVicini());
			
			for(Adiacenza a : distVicini) {
				result += a.getD().getId()+" - "+a.getDist()+"\n";
			}
			
		}
		
		return result;
		
	}
	
}
