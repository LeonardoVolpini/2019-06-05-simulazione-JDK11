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
	
	public List<Event> getAllEventsByYearMonthDay(int anno, int mese, int giorno){
		String sql="SELECT * "
				+ "FROM events e "
				+ "WHERE YEAR(e.reported_date)=? AND MONTH(e.reported_date)=? AND DAY(e.reported_date)=? "
				+ "ORDER BY e.reported_date";
		List<Event> list = new ArrayList<>() ;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2,mese);
			st.setInt(3, giorno);
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
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
			}
			conn.close();
			return list ;
		}catch (SQLException e) {
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
	
	public List<Integer> allMonths(int anno){
		String sql="SELECT DISTINCT MONTH(e.reported_date) AS mese "
				+ "FROM events e "
				+ "WHERE YEAR(e.reported_date)=? "
				+ "ORDER BY MONTH(e.reported_date)";
		List<Integer> mesi= new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				mesi.add(res.getInt("mese"));
			}
			conn.close();
			return mesi ;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> allDays(int anno){
		String sql="SELECT DISTINCT DAY(e.reported_date) AS day "
				+ "FROM events e "
				+ "WHERE YEAR(e.reported_date)=? "
				+ "ORDER BY DAY(e.reported_date)";
		List<Integer> giorni= new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				giorni.add(res.getInt("day"));
			}
			conn.close();
			return giorni ;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}

	public void loadIdMap(Map<Integer, Distretto> idMap, int anno) {
		String sql="SELECT e.district_id AS id, AVG(e.geo_lon) AS lon, AVG (e.geo_lat) AS lat, COUNT(*) AS numCrimini "
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
					Distretto d= new Distretto(res.getInt("id"),res.getDouble("lon"),res.getDouble("lat"),res.getInt("numCrimini"));
					idMap.put(d.getId(), d);
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
