package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.crimes.model.District;
import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<District> getDistretti(int anno){
		
		String sql = "SELECT district_id AS id, AVG(geo_lat) AS latc, AVG(geo_lon) AS lonc "
				+ "FROM `events` "
				+ "WHERE YEAR(reported_date) = ? "
				+ "GROUP BY district_id" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, anno);
			
			List<District> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				list.add(new District(res.getInt("id"), new LatLng(res.getDouble("latc"),res.getDouble("lonc"))));
				
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getAnni(){
		
		String sql = "SELECT DISTINCT YEAR(reported_date) as anno FROM events";
		List<Integer> result = new ArrayList<Integer>();
		
		try {
			
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result.add(res.getInt("anno"));
			}
			conn.close();
			Collections.sort(result);
			return result;
			
		} catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
		
	}
	
	public List<Integer> getMesi(){
		
		String sql = "SELECT DISTINCT MONTH(reported_date) as anno FROM events";
		List<Integer> result = new ArrayList<Integer>();
		
		try {
			
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result.add(res.getInt("anno"));
			}
			conn.close();
			Collections.sort(result);
			return result;
			
		} catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
		
	}
	
	public List<Integer> getGiorni(){
		
		String sql = "SELECT DISTINCT DAY(reported_date) as anno FROM events";
		List<Integer> result = new ArrayList<Integer>();
		
		try {
			
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result.add(res.getInt("anno"));
			}
			conn.close();
			Collections.sort(result);
			return result;
			
		} catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
		
	}
	
	public Integer getDistrettoMin(Integer year) {
		
		String sql = "SELECT e.district_id AS id "
				+ "FROM `events` e "
				+ "WHERE YEAR(e.reported_date) = ? "
				+ "GROUP BY e.district_id "
				+ "ORDER BY COUNT(*) ASC "
				+ "LIMIT 1";
		
		Integer result = null;
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) 
				result = res.getInt("id");
				
			conn.close();
			
			return result;
			
		} catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
		
	}
	
	public List<Event> getEventByDate(Integer anno, Integer mese, Integer giorno) {
		
		String sql = "SELECT * "
				+ "FROM `events` e "
				+ "WHERE YEAR(e.reported_date) = ? AND MONTH(e.reported_date) AND DAY(e.reported_date)";
		
		List<Event> result = new ArrayList<>();
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					result.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
				
			conn.close();
			
			return result;
			
		} catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
		
	}

}
