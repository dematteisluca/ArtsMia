package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	public void listObjects(Map<Integer, ArtObject> idMap) {
		
		String sql = "SELECT * from objects";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("object_id"))) {
				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				idMap.put(artObj.getId(), artObj);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
		
	public List<Adiacenza> getAdiacenze(Map<Integer, ArtObject>idMap){
		String sql = "SELECT  o1.object_id AS id1, o2.object_id AS id2, COUNT(e1.exhibition_id) AS peso " + 
				"FROM objects o1, objects o2, exhibition_objects e1, exhibition_objects e2 " + 
				"WHERE o1.object_id > o2.object_id AND " + 
				"      o1.object_id = e1.object_id AND o2.object_id = e2.object_id " + 
				"      AND e1.exhibition_id= e2.exhibition_id " + 
				"GROUP BY  o1.object_id, o2.object_id ";
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("id1")) && idMap.containsKey(res.getInt("id2"))) {
					result.add(new Adiacenza(idMap.get(res.getInt("id1")),idMap.get(res.getInt("id2")), res.getInt("peso")));
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	}

