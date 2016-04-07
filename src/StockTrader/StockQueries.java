package StockTrader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import DataModel.Transaction;
import DataModel.User;

public class StockQueries {

	private static PreparedStatement statement;
	private static String qry;
	/***
	 * 
	 * @param uid
	 * @return
	 */
	//This operation is O(n)
	public static List<Integer> getFriendsListForUser(int uid)
	{
		List<Integer> friendsList = new LinkedList<Integer>();
		DataConnector connector;
		try {
            connector = new DataConnector();
			
            //Have to escape quotation marks because I used caps in database
            statement = connector.cnx.prepareStatement(
					"SELECT \"FriendID\" FROM public.\"FriendsList\" WHERE \"UserID\" = ?");
			statement.setInt(1, uid);
            ResultSet rs = statement.executeQuery();
            
            //O(n)
            while (rs.next())
            	friendsList.add(rs.getInt(1)); 
            return friendsList;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        } finally {
        	connector = null;
		}
	}
	
	/**
	 * 
	 * @param uid
	 * @return
	 */
	//This is an O(*) runtime
	public static List<String> getTradeTransactionsForUser(int uid)
	{
		List<String> transactions = new LinkedList<String>();
		DataConnector connector;
		try {
            connector = new DataConnector();
			
            qry =  "SELECT 	st.\"StockTransactionID\", tt.\"Type\", sti.\"ShortName\", st.date "+
            		"FROM public.\"StockTransaction\" st "+ 
	            	"	INNER JOIN public.\"TransactionType\" tt "+
	            	"	ON (st.\"TransactionTypeID\" = tt.\"TransactionTypeID\") "+
	            	"	INNER JOIN public.\"Ticker\" sti "+
            		"ON (st.\"StockTransactionID\" = sti.\"TickerID\") "+  
            		"WHERE st.\"UserID\" = ? "+
            		"ORDER BY date ASC ";
            
            statement = connector.cnx.prepareStatement(qry);
			statement.setInt(1, uid);
            ResultSet rs = statement.executeQuery();
            
            //O(n)
            while (rs.next())
            {
            	String type = rs.getString(2).trim();
            	String ticker = rs.getString(3).trim();
            	String date = rs.getDate(4) != null ? rs.getDate(4).toString().trim() : ""; //should normally not be nullable...
            	transactions.add(date+","+type+","+ticker);
            }
            return transactions;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        } finally 
		{
        	connector = null;
		}
	}
	
	public List<Transaction> getAlerts(User user)
	{
		return null;
	}
	
	/***
	 * 
	 * @param ls
	 */
	public static <T> void printData(List<T> ls)
	{
		for (T x : ls)
			System.out.println(x.toString());
	}
}
