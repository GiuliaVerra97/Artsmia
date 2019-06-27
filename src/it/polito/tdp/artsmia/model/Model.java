package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	
	private Graph<ArtObject, DefaultWeightedEdge> grafo;		//weight perchè è pesato
	
	private Map<Integer, ArtObject> idMap;			//uso la mappa per memorizzare gli oggetti del DB perchè è più veloce di una lista
	private List<ArtObject> best;
	public Model() {
		idMap=new HashMap<Integer, ArtObject>();
		grafo=new SimpleWeightedGraph<ArtObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}
	
	
	
	
	
	public void creaGrafo() {
		ArtsmiaDAO dao=new ArtsmiaDAO();
		dao.listObjects(idMap);		//anche se è un metodo che restituisce una lista, quando lo chiamo senza creare una variabile lista riempe la lista
		
		//aggiungo vertici
		Graphs.addAllVertices(grafo, idMap.values());
		
		
		//aggiungo archi- metodo 3 (un solo ciclo for, trovo gli archi direttamente dal DB facendo interagire tra loro le tabelle)
		List<Adiacenza> adiacenze=dao.listAdiacenza();
		
		for(Adiacenza a: adiacenze) {
			ArtObject source=idMap.get(a.getO1());		
			ArtObject dest=idMap.get(a.getO2());
			
			try {
				Graphs.addEdge(grafo, source, dest, a.getPeso());
			}catch(Throwable t) {		//per risolvere il problema nel DB, perchè ci sono degli id nella tabella exhibition che non sono più presenti nella tabella Object
				
			}
		}
		
		
		System.out.println("Grafo creato: "+grafo.vertexSet().size()+" vertici "+grafo.edgeSet().size()+" archi");
	}

	
	
	
	
	public Integer NumArchi() {
		return grafo.edgeSet().size();
	}

	public Integer getNumVertici() {
		return grafo.vertexSet().size();
	}





	public Graph<ArtObject, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}





	public void setGrafo(Graph<ArtObject, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}





	public Map<Integer, ArtObject> getIdMap() {
		return idMap;
	}





	public void setIdMap(Map<Integer, ArtObject> idMap) {
		this.idMap = idMap;
	}





	public int calcolatComponentiConnesse(int oggetto) {
		
		ArtObject o=idMap.get(oggetto);

		ConnectivityInspector<ArtObject, DefaultWeightedEdge> conn=new ConnectivityInspector<ArtObject, DefaultWeightedEdge>(grafo);
	
		return conn.connectedSetOf(o).size();		//mi da la dimensione deii vertici appartenenti alla stessa componente connessa del vertice passato come parametro
		
	}
	
	
	
	
	
	public List<ArtObject> cerca(int oggetto, int numero){
		ArtObject o=idMap.get(oggetto);
		best=new ArrayList<ArtObject>();
		List<ArtObject> parziale=new ArrayList<ArtObject>();
		parziale.add(o);
		this.ricorsione(parziale, numero);
		Collections.sort(best);
		return best;
		
	}





	private void ricorsione(List<ArtObject> parziale, int numero) {
		double pesoMax=0;
		if(parziale.size()==numero) {
			double peso=this.pesoCamminoMassimo(parziale);
			if(peso>pesoMax) {
				pesoMax=peso;
				best=new ArrayList<ArtObject>(parziale);
			}
		}
		
		List<ArtObject> vicini=Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1));
		for(ArtObject a: vicini) {
			if(!parziale.contains(a) && a.getClassification().equals(parziale.get(parziale.size()-1).getClassification())) {
				parziale.add(a);
				ricorsione(parziale, numero);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
		
	}
	
	
	
	public double pesoCamminoMassimo(List<ArtObject> lista) {
		double peso=0;
		for(int i=0; i<(lista.size()-1); i++) {
			 peso=peso+grafo.getEdgeWeight(grafo.getEdge(lista.get(i), lista.get(i+1)));
		}
		
		return peso;
	}
	
	
	
	
	
	
	
	
	
	
}
