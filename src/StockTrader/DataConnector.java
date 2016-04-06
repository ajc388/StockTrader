package StockTrader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnector {

	private String url;
	private String user;
	private String pword;
	public static Connection cnx;
	
	public DataConnector() 
	{
		url = "jdbc:postgresql://stocktrader.ccnmwmnsotyg.us-west-2.rds.amazonaws.com/StockTrader";
		user = "SudoNinja";
		pword = "xUbuntu1337!";
		pgConnect();
	}
	
	public void pgConnect() {
		System.out.println("Initializing PostgreSQL Connection.");
		
		try {
			cnx = DriverManager.getConnection(url, user, pword);
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		if (cnx != null) {
			System.out.println("Database connection successful.");
		} else {
			System.out.println("Failed to connect.");
		}
	}
	

}
