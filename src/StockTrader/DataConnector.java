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
		try {
			System.out.println("Intialized connection.");
			return DriverManager.getConnection(url, user, pword);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void close()
	{
		try {
			cnx.close();
			System.out.println("Closed connection.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
