package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.Distretto;
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
	
	public List<Integer> allYears(){
		String sql="SELECT DISTINCT YEAR(e.reported_date) AS anno "
				+ "FROM events e "
				+ "ORDER BY YEAR(e.reported_date)";
		List<Integer> anni= new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				anni.add(res.getInt("anno"));
			}
			conn.close();
			return anni ;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}

	public void loadIdMap(Map<Integer, Distretto> idMap, int anno) {
		String sql="SELECT e.district_id AS id, AVG(e.geo_lon) AS lon, AVG (e.geo_lat) AS lat "
				+ "FROM events e "
				+ "WHERE YEAR(e.reported_date)=? "
				+ "GROUP BY e.district_id "
				+ "ORDER BY e.district_id ";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
					Distretto d= new Distretto(res.getInt("id"),res.getDouble("lon"),res.getDouble("lat"));
					idMap.put(d.getId(), d);
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
