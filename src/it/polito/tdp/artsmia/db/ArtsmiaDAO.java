package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	
	/**
	 * Metodo che restituisce una lista di tutti gli oggetti(vertici del grafo) e in più aggiunge alla mappa passata come
	 * parametro gli oggetti non ancora presenti, così da poter utilizzare questa mappa anche nel model 
	 * @param idMap mappa di {@link oggetti}
	 * @return lista di {@link oggetti}
	 */
	
	public List<ArtObject> listObjects(Map<Integer, ArtObject> idMap) {	//modifico metodo e passo mappa 
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.get(res.getInt("object_id"))==null) {		//avendo la mappa posso verificare se gli oggetti sono già presenti nella mappa, se non ci sono ancora gli aggiungo
				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
					
				idMap.put(artObj.getId(), artObj);
				result.add(artObj);
				
				}else {		//se no aggiungo alla lista l'oggetto già presente nella mappa
					result.add(idMap.get(res.getInt("object_id")));	
				}
				
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	/**
	 * Metodo che mi permette di scoprire gli archi del grafo. Ogni arco indica che due oggetti diversi sono stati esposti
	 * contemporaneamente alla stessa esibizione. Trova dunque le connessioni diverse tra i vertici (oggetti della mostra)
	 * e il loro peso dato dal numero di volte che gli oggetti sono stati esibiti insieme
	 * @return lista di {@link Adiacenza}
	 */
	
	public List<Adiacenza> listAdiacenza(){
		
		String sql="SELECT eo1.object_id as o1, eo2.object_id as o2, count(*) as conta " + 
					"FROM exhibition_objects eo1, exhibition_objects eo2 " + 
					"WHERE eo1.exhibition_id=eo2.exhibition_id and eo2.object_id>eo1.object_id " + 	//metto il maggiore per togliere la connessione dell'oggetto con l'oggetto stesso
					"GROUP BY eo1.object_id, eo2.object_id ";
		
		
		Connection conn=DBConnect.getConnection();
		List<Adiacenza> result=new LinkedList<Adiacenza>();
		
		try {
			
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result.add(new Adiacenza(res.getInt("o1"), res.getInt("o2"), res.getInt("conta")));		//conta indica il peso dell'arco, ovvero quante volte due oggetti diversi sono stati esibiti insieme
			}
			
			conn.close();
			return result;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;

		}
		
	}
	
	
	
	
	
	
}
