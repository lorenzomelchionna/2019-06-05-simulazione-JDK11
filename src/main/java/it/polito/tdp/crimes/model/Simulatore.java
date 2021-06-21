package it.polito.tdp.crimes.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.db.EventsDao;
import it.polito.tdp.crimes.model.Evento.TipoEvento;

public class Simulatore {
	
	//input utente
	private Integer N;
	private Integer anno;
	private Integer mese;
	private Integer giorno;
	
	//modello del mondo
	private Graph<Integer,DefaultWeightedEdge> grafo;
	private Map<Integer,Integer> agenti;
	
	//coda eventi
	private PriorityQueue<Evento> queue;
	
	//output 
	private Integer malGestiti;
	
	public Simulatore(Integer N, Integer anno, Integer mese, Integer giorno, Graph<Integer,DefaultWeightedEdge> grafo) {
		
		this.N = N;
		this.anno = anno;
		this.mese = mese;
		this.giorno = giorno;
		
		this.grafo = grafo;
		
		this.malGestiti = 0;
		
		this.agenti = new HashMap<>();
		for(Integer d : grafo.vertexSet()) {
			agenti.put(d,0);
		}
		
		EventsDao dao = new EventsDao();
		
		Integer minD = dao.getDistrettoMin(anno);
		
		agenti.put(minD, N);
		
		this.queue = new PriorityQueue<Evento>();
		
		for(Event e : dao.getEventByDate(anno, mese, giorno)) {
			queue.add(new Evento(TipoEvento.CRIMINE, e.getReported_date(), e));
		}
		
	}
	
	public int run() {
		
		Evento e = queue.poll();
		
		while(e != null) {
			switch(e.getTipo()) {
				case CRIMINE:
					Integer partenza = null;
					partenza = cercaAgente(e.getCrimine().getDistrict_id());
					if(partenza != null) {
						
						this.agenti.put(partenza, this.agenti.get(partenza)-1);
						Double distanza;
						if(partenza.equals(e.getCrimine().getDistrict_id())) {
							distanza = 0.0;
						} else {
							distanza = this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, e.getCrimine().getDistrict_id()));
						}
						
						Long seconds = (long) ((distanza * 1000)/(60/3.6));
						this.queue.add(new Evento(TipoEvento.ARRIVA_AGENTE, e.getData().plusSeconds(seconds), e.getCrimine()));
						
					} else {
						this.malGestiti++;
					}
					break;
				case ARRIVA_AGENTE:
					Long duration = getDurata(e.getCrimine().getOffense_category_id());
					this.queue.add(new Evento(TipoEvento.GESTITO, e.getData().plusSeconds(duration), e.getCrimine()));
					if(e.getData().isAfter(e.getCrimine().getReported_date().plusMinutes(15))) {
						this.malGestiti++;
					}
					break;
				case GESTITO:
					this.agenti.put(e.getCrimine().getDistrict_id(), this.agenti.get(e.getCrimine().getDistrict_id())+1);
					break;
			}
			
		}
		
		return this.malGestiti;
		
	}

	private Long getDurata(String offense_category_id) {

		if(offense_category_id.equals("all_other_crimes")) {
			
			Random r = new Random();
			
			if(r.nextDouble() > 0.5) {
				return Long.valueOf(2*60*60);
			} else {
				return Long.valueOf(60*60);
			}
			
		} else {
			return Long.valueOf(60*60);
		}
		
	}

	private Integer cercaAgente(Integer district_id) {
		
		Double distanza = Double.MAX_VALUE;
		Integer distretto = null;
		
		for(Integer d : this.agenti.keySet()) {
			
			if(this.agenti.get(d) > 0) {
				if(district_id.equals(d)) {
					distanza = 0.0;
					distretto = d;
				}else if(this.grafo.getEdgeWeight(this.grafo.getEdge(district_id, d)) < distanza) {
					distanza = this.grafo.getEdgeWeight(this.grafo.getEdge(district_id, d));
					distretto = d;
				}
			}
			
		}
		
		return distretto;
		
	}
	
}
