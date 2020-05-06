package it.polito.tdp.extflightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import it.polito.tdp.extflightdelays.model.Airline;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Flight;
import it.polito.tdp.extflightdelays.model.Rotta;

public class ExtFlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT * from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRLINE")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports(Map<Integer, Airport> idMap) {
		String sql = "SELECT * FROM airports";
		List<Airport> result = new ArrayList<Airport>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!idMap.containsKey(rs.getInt("ID"))){
					Airport airport = new Airport(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRPORT"),
							rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), rs.getDouble("LATITUDE"),
							rs.getDouble("LONGITUDE"), rs.getDouble("TIMEZONE_OFFSET"));
					result.add(airport);
					idMap.put(airport.getId(), airport);
				}
				
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT * FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("ID"), rs.getInt("AIRLINE_ID"), rs.getInt("FLIGHT_NUMBER"),
						rs.getString("TAIL_NUMBER"), rs.getInt("ORIGIN_AIRPORT_ID"),
						rs.getInt("DESTINATION_AIRPORT_ID"),
						rs.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), rs.getDouble("DEPARTURE_DELAY"),
						rs.getDouble("ELAPSED_TIME"), rs.getInt("DISTANCE"),
						rs.getTimestamp("ARRIVAL_DATE").toLocalDateTime(), rs.getDouble("ARRIVAL_DELAY"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	/**
	 * Metodo per la creazione di rotte tra aeroporti con il calcolo del peso
	 * @param idMap
	 * @return una lista di rotte
	 */
	public List<Rotta> coppiaAeroporti(Map<Integer, Airport> idMap, int distMinima) {
		
		String sourceSQL = "SELECT ORIGIN_AIRPORT_ID AS PARTENZA, DESTINATION_AIRPORT_ID AS ARRIVO, AVG(DISTANCE) AS PESO FROM flights GROUP BY ORIGIN_AIRPORT_ID, DESTINATION_AIRPORT_ID HAVING AVG(DISTANCE) > ?" ;
		//String targetSQL = "SELECT DESTINATION_AIRPORT_ID AS PARTENZA, ORIGIN_AIRPORT_ID AS ARRIVO, AVG(DISTANCE) AS PESO FROM flights GROUP BY DESTINATION_AIRPORT_ID, ORIGIN_AIRPORT_ID HAVING COUNT(*) >= 1" ;
		
		//Map<String, Rotta> result = new HashMap<>();
		List<Rotta> result = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection() ;
			PreparedStatement stS = conn.prepareStatement(sourceSQL);
			stS.setInt(1, distMinima);
			ResultSet res = stS.executeQuery();

			
			while(res.next()) {
				//String idRottaS = ""+res.getInt("PARTENZA")+idMap.get(res.getInt("PARTENZA")).getCity()+res.getInt("ARRIVO")+idMap.get(res.getInt("ARRIVO")).getCity();
				
				//Controllo se arco esiste gia, se presente prendo il peso e faccio media aggiornata
				
				Rotta source = new Rotta(idMap.get(res.getInt("PARTENZA")),idMap.get(res.getInt("ARRIVO")));
				if(result.contains(source)) {
					source.aggiornaPeso(res.getFloat("PESO"));
				}else {
					result.add(source);
					source.setPeso(res.getFloat("PESO"));
				}

			}
			
			stS.close();
			
			/*
			PreparedStatement stT = conn.prepareStatement(targetSQL);
			ResultSet resT = stT.executeQuery();
			
			while(resT.next()) {
				String idRottaT = ""+resT.getInt("ARRIVO")+idMap.get(resT.getInt("ARRIVO")).getCity()+resT.getInt("PARTENZA")+idMap.get(resT.getInt("PARTENZA")).getCity();
				if(!result.containsKey(idRottaT)) {
					String idRottaNEW = ""+resT.getInt("PARTENZA")+idMap.get(resT.getInt("PARTENZA")).getCity()+resT.getInt("ARRIVO")+idMap.get(resT.getInt("ARRIVO")).getCity();
					Rotta target = new Rotta(idRottaNEW, idMap.get(resT.getInt("PARTENZA")),idMap.get(resT.getInt("ARRIVO")));
					target.setPeso(resT.getFloat("PESO"));
					result.put(idRottaNEW, target);
				}else {
					result.get(idRottaT).setPeso(resT.getFloat("PESO"));
				}
			}
			
			stT.close();*/
			
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return result ;
	}

	//METODO PER APPROCCIO SEMPLICE
	public int getPeso(Airport a1, Airport a2) {

		String sql = "SELECT AVG(DISTANCE) as PESO FROM flights WHERE ORIGIN_AIRPORT_ID = ? AND DESTINATION_AIRPORT_ID = ?";
		Connection conn = ConnectDB.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a1.getId());
			st.setInt(2, a2.getId());
			ResultSet res = st.executeQuery();
			if(res.next()) {
				int peso = res.getInt("PESO");
				conn.close();
				return peso;
			}
			conn.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return -1;
	}
	
}
