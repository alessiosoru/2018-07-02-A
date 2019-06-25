package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private List<Airport> airports;
	private Map<Integer, Airport> airportsIdMap;
	private ExtFlightDelaysDAO dao;
	List<Collegamento> collegamenti;
	
	private Graph<Airport, DefaultWeightedEdge> grafo;
	
	private List<Airport> percorsoBest;
	private Double migliaBest;
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
		
	}

	public void creaGrafo(Integer distanzaMinima) {
		this.grafo = new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.airports = new ArrayList<Airport>();
		this.airportsIdMap = new HashMap<Integer, Airport>();
		this.collegamenti = new ArrayList<Collegamento>();
		this.airports = dao.loadAllAirports(airportsIdMap);
		
//		Graphs.addAllVertices(this.grafo, airports);
		
		// ATTENZIONE ! LEGGERE BENE IL DB E NON FARE QUERY INUTILI PIU' COMPLESSE
		// IN QUESTO CASO PRENDO TUTTO DALLLA TABELLA FLIGHTS
		// NON FACCIO IL CONTROLLO A1<A2 PERCHE' devo considereare entrambe le direzioni,
		// avrò collegammenti doppi per lo stesso arco che non è orientato, quindi che sia 
		// source uno o l'altro, l'arco sarà lo stesso per entrambi i casi, perciò
		// se l'arco non esiste -> aggiungo
		// se esiste -> rotta inversa ma stesso arco, aggiorno il peso
		this.collegamenti = dao.getCollegamentiDistMin(this.airportsIdMap, distanzaMinima);
		
		for(Collegamento c : this.collegamenti) {
			Airport source = c.getA1();
			Airport dest = c.getA2();
			
			if(!this.grafo.containsVertex(source)) {
				this.grafo.addVertex(source);
			}
			
			if(!this.grafo.containsVertex(dest)) {
				this.grafo.addVertex(dest);
			}
			DefaultWeightedEdge e = this.grafo.getEdge(source, dest);
			if(e==null) {
				Graphs.addEdgeWithVertices(this.grafo, source, dest, c.getDistMedia());
			}else {
				Double peso = this.grafo.getEdgeWeight(e);
				Double newPeso =  (peso + c.getDistMedia())/2;				
				this.grafo.setEdgeWeight(e, newPeso);
				
			}
		}
		
		System.out.println("Grafo creato !\n");
		System.out.println("Vetici: "+grafo.vertexSet().size());
		System.out.println("\nArchi: "+grafo.edgeSet().size()+"\n");
	}
	
	public Integer getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Integer getNumArchi() {
		return this.grafo.edgeSet().size();
	}

	public Set<Airport> getAeroporti() {
		return this.grafo.vertexSet();
	}

	public List<Airport> getAdiacentiDi(Airport partenza) {
		return Graphs.neighborListOf(this.grafo, partenza);
	}

	/*
	 * RICORSIONE
	 * 
	 * Soluzione parziale: lista (percorso )di aeroporti 
	 * 	(mappa di aeroporti)
	 * Livello della ricorsione: numero di aeroporti nel percorso
	 * Casi terminali: 
	 * 	1.  Alla prima volta che supero il numero di miglia 
	 * 		 -> Verifico se il percorso
	 * 		ha max numero di aeroporti - 1 (ultimo da eliminare)
	 * 		fino ad ora, nel caso salvo best come nuovo percorso in cui elimino
	 * 			l'ultimo del parziale toccato nel percorso e miglia
	 * Ho il vertice partenza iniziale
	 * Generazione delle soluzioni: dato un vertice, aggiungo un vertice adiacente
	 * 	(ATTENZIONE: ADIACENTE AL PRECEDENTE MA NON A QUELLI A ANCORA PRIMA)
	 * 	non ancora parte del percorso e aggiungo le miglia percorse con il peso dell'arco
	 * 
	 * Calcolo il tasso di sconfitta come peso archi entranti per ogni pilota
	 * 
	 */
	
	public List<Airport> cercaItinerario(Double migliaMax, Airport partenza) {
		System.out.println(migliaMax);
		this.migliaBest=0.0;
		percorsoBest = new ArrayList<Airport>();
		List<Airport> parziale = new ArrayList<Airport>();
		Integer livello = 0;
		parziale.add(partenza);
		Double migliaPercorse=0.0;		
		cerca(migliaMax, migliaPercorse, partenza, livello+1, parziale);
		return this.percorsoBest;
	}

	private void cerca(Double migliaMax, Double migliaPercorse,
			Airport origine, int livello, List<Airport> parziale) {
//		System.out.println(parziale.size()-1);
		if(parziale.size()-1>this.percorsoBest.size()) {
				List<Airport> newBest= new ArrayList<Airport>(parziale);
				newBest.remove(origine); // nella lista origine è livello-1
				DefaultWeightedEdge e = this.grafo.getEdge(parziale.get(livello-2), origine);
				
				Double migliaDaEliminare = this.grafo.getEdgeWeight(e);
				Double newMigliaBest = migliaPercorse - migliaDaEliminare;

//				System.out.println("Nuovo best"+newBest.size()+" -- "+migliaPercorse);
				this.migliaBest = newMigliaBest;
				this.percorsoBest=newBest;

				for(Airport a : percorsoBest) {
//					System.out.println(a.toString());
				}
			
			return;
		}
		
		for(Airport a : Graphs.neighborListOf(this.grafo, origine)) {
			if(!parziale.contains(a)) {
				DefaultWeightedEdge e = this.grafo.getEdge(origine, a);
				Double migliaDaAgg= this.grafo.getEdgeWeight(e);
//				System.out.println(migliaPercorse + migliaDaAgg);
				if(migliaPercorse + migliaDaAgg < migliaMax) {
					migliaPercorse = migliaPercorse + migliaDaAgg;
					parziale.add(a);
//					System.out.println(a.getCity()+" - "+migliaPercorse);
					
					cerca(migliaMax, migliaPercorse, a, livello+1, parziale);
					
					parziale.remove(a);

					migliaPercorse = migliaPercorse - migliaDaAgg;
				}				
			}
		}
		
	}
	
	public Double getMigliaBest() {
		return this.migliaBest;
	}
	

}
