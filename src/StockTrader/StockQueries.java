package StockTrader;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import DataModel.Rank;
import DataModel.Transaction;
import DataModel.User;

public class StockQueries {

	private static PreparedStatement statement;
	private static String qry;
	
	/***
	 * Grabs a list of friends for a particular user.
	 * @param uid the id of the user.
	 * @return list of friends.
	 */
	//This operation is O(n)
	public static List<User> getFriendsListForUser(int uid)
	{
		List<User> friendsList = new LinkedList<User>();
		DataConnector connector;
		try {
            connector = new DataConnector();
			
            qry = 	"SELECT fl.\"FriendID\", u.\"FirstName\", u.\"LastName\" "+ 
					"FROM public.\"FriendsList\" fl "+
				  	"	INNER JOIN public.\"User\" u "+
					"	ON (u.\"UserID\" = fl.\"FriendID\") "+
					"WHERE fl.\"UserID\" = ?;";
            
			//Have to escape quotation marks because I used caps in database
            statement = connector.cnx.prepareStatement(qry);
			statement.setInt(1, uid);
            ResultSet rs = statement.executeQuery();
            
            //O(n)
            while (rs.next())
            {
            	int id = rs.getInt(1);
            	String fname = rs.getString(2);
            	String lname = rs.getString(3);
            	friendsList.add(new User(id, fname, lname)); 
            }

            rs.close();
        	connector.close();
            return friendsList;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        } 
	}
	
	/***
	 * Grabs a list of friend ids for a particular user.
	 * @param uid the id of the user.
	 * @return list of friends.
	 */
	//This operation is O(n)
	public static List<Integer> getFriendsListIDsForUser(int uid)
	{
		List friendsListIDs = new LinkedList<Integer>();
		DataConnector connector;
		try {
            connector = new DataConnector();
			
            qry = 	"SELECT \"FriendID\" "+ 
					"FROM public.\"FriendsList\" "+
					"WHERE \"UserID\" = ? ;";
            
			//Have to escape quotation marks because I used caps in database
            statement = connector.cnx.prepareStatement(qry);
			statement.setInt(1, uid);
            ResultSet rs = statement.executeQuery();
            
            //O(n)
            while (rs.next())
            {
            	friendsListIDs.add(rs.getInt(1));
            }
            
            rs.close();
            connector.close();
            return friendsListIDs;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        } 
	}
	
	/**
	 * Grabs the trade transaction information for a particular user.
	 * @param uid is the user's key.
	 * @return a list of transactions.
	 */
	//This is an O(*) runtime
	public static List<Transaction> getTradeTransactionsForUser(int uid)
	{ 
		List<Transaction> transactions = new LinkedList<Transaction>();
		DataConnector connector;
		try {
            connector = new DataConnector();
			
            qry =  "SELECT 	st.\"StockTransactionID\", tt.\"Type\", sti.\"ShortName\", st.date "+
            		"FROM public.\"StockTransaction\" st "+ 
	            	"	INNER JOIN public.\"TransactionType\" tt "+
	            	"	ON (st.\"TransactionTypeID\" = tt.\"TransactionTypeID\") "+
	            	"	INNER JOIN public.\"Ticker\" sti "+
            		"ON (st.\"StockTickerID\" = sti.\"TickerID\") "+  
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
            	Date date = rs.getDate(4);
            	transactions.add(new Transaction(date,type,ticker));
            }
            
            rs.close();
        	connector.close();
            return transactions;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        } 
	}
	
	/***
	 * 
	 * @param userIDs a list of user IDs (such as a friends list)
	 * @return a list of ranks.
	 */
	public static List<Rank> getStockRanks(List<Integer> userIDs)
	{
		List<Rank> rankedList = new LinkedList<Rank>();
		DataConnector connector;
		try {
            connector = new DataConnector();
			
            //Making queries like this in Java is not fun. Missing LINQ.
            qry =  "SELECT 	sum(CASE WHEN tt.\"Type\" = 'SELL' THEN -1 "+ 
            	   "			     WHEN tt.\"Type\" = 'BUY' THEN 1 "+
            	   "			END) As \"Rank\", "+
            	   "sti.\"ShortName\" "+
            	   "FROM public.\"StockTransaction\" st "+ 
            	   "	INNER JOIN public.\"TransactionType\" tt "+
            	   "		ON (st.\"TransactionTypeID\" = tt.\"TransactionTypeID\") "+
            	   "	INNER JOIN public.\"Ticker\" sti "+
            	   "		ON (st.\"StockTickerID\" = sti.\"TickerID\") "+
            	   "	WHERE 	st.\"UserID\" = ANY (?) AND "+
            	   "		st.date >= (CURRENT_DATE - INTERVAL '7 days') "+
            	   "GROUP BY sti.\"ShortName\" "+
            	   "ORDER BY \"Rank\" DESC"; 
            
            statement = connector.cnx.prepareStatement(qry);
			statement.setArray(1, connector.cnx.createArrayOf("int", userIDs.toArray()));
            ResultSet rs = statement.executeQuery();
            
            //O(n)
            while (rs.next())
            {
            	int rank = rs.getInt(1);
            	String ticker = rs.getString(2);
            	rankedList.add(new Rank(rank, ticker));
            }
            
            rs.close();
        	connector.close();
        	return rankedList;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        } 
	}
	
	/***
	 * A generic list printer.
	 * @param ls
	 */
	public static <T> void printData(List<T> ls)
	{
		for (T x : ls)
			System.out.println(x.toString());
	}
}
