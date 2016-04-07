package StockTrader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnector {

	private String url;
	private String user;
	private String pword;
	public Connection cnx;
	
	public DataConnector() 
	{
		url = "jdbc:postgresql://stocktrader.ccnmwmnsotyg.us-west-2.rds.amazonaws.com/StockTrader";
		user = "SudoNinja";
		pword = "xUbuntu1337!";
		cnx = pgConnect();
	}
	
	public Connection pgConnect() {
		System.out.println("Initializing PostgreSQL Connection.");
		try {
			return DriverManager.getConnection(url, user, pword);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
