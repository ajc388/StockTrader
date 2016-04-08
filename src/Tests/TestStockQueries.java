package Tests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DataModel.Rank;
import StockTrader.DataConnector;
import StockTrader.StockQueries;

public class TestStockQueries {

	private DataConnector dc;
	private String qry;
	private PreparedStatement statement;
	private long time = System.currentTimeMillis();
	private Date today = new Date(time);
	private Date lastWeek = new Date(time-7*24*60*60*1000);
	private Date lastEightDays = new Date(time-8*24*60*60*1000);
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		//deleteData();
	}

	//================================================
	//			 PRIVATE DATA METHODS
	//================================================
	private void deleteFriendsLists()
	{
		dc = new DataConnector();
		try {
			qry = "DELETE FROM public.\"FriendsList\" ";
			statement = dc.cnx.prepareStatement(qry);
			statement.execute();
			dc.close();
		} catch (Exception e )
		{
			System.out.println(e);
			return;
		}
	}
	
	private void deleteStockTransactions()
	{
		dc = new DataConnector();
		try {
			qry = "DELETE FROM public.\"StockTransaction\" ";
			statement = dc.cnx.prepareStatement(qry);
			statement.execute();
			dc.close();
		} catch (Exception e )
		{
			System.out.println(e);
			return;
		}
	}
	
	/**
	 * As a note there are 5 users in the database by default.
	 */
	private void insertFriendsList(int uid, int fid)
	{
		dc = new DataConnector();
		try {
			qry = "INSERT INTO public.\"FriendsList\"(\"UserID\", \"FriendID\") "+
				  "VALUES (?, ?) ";
			statement = dc.cnx.prepareStatement(qry);
			statement.setInt(1, uid);
			statement.setInt(2, fid);
			statement.execute();
			dc.close();
		} catch (Exception e )
		{
			System.out.println(e);
			return;
		}
	}
	
	/**
	 * As a note: 
	 * There are 5 users
	 * There are two TransactionTypes: BUY (1) or SELL (2)
	 * There are four tickers: "GOOG" (1), "YHOO" (2), "UBER" (3), "STKI" (4)
	 */
	private void insertStockTransaction(int uid, int ttid, int stid, Date date)
	{
		dc = new DataConnector();
		try {
			qry = "INSERT INTO public.\"StockTransaction\"(\"UserID\", \"TransactionTypeID\", \"StockTickerID\", date) "+
				  "VALUES (?, ?, ?, ?) ";
			statement = dc.cnx.prepareStatement(qry);
			statement.setInt(1, uid);
			statement.setInt(2, ttid);
			statement.setInt(3, stid);
			statement.setDate(4, date);	
			statement.execute();
			dc.close();
		} catch (Exception e )
		{
			System.out.println(e);
			return;
		}
	}
	
	
	//================================================
	//	       Test getFriendsListIDsForUser
	//================================================
	@Test
	public void testNoFriends() {
		deleteFriendsLists();
		assertEquals(0,StockQueries.getFriendsListIDsForUser(1).size());
	}
	
	@Test
	public void testOneAssociatedFriend() {
		deleteFriendsLists();
		insertFriendsList(1, 2);
		insertFriendsList(2, 1); //ensure no duplicates
		insertFriendsList(3, 1); //ensure correct direction of relationship
		List<Integer> friendsIDs = StockQueries.getFriendsListIDsForUser(1);
		assertEquals(1,friendsIDs.size());
		assertEquals(2,(int)friendsIDs.get(0));	
	}
	
	@Test
	public void testManyFriends() {
		deleteFriendsLists();
		insertFriendsList(1, 1);
		insertFriendsList(1, 2);
		insertFriendsList(1, 3);
		insertFriendsList(1, 4);
		insertFriendsList(1, 5);
		insertFriendsList(2, 1); //ensure correct direction of relationship
		insertFriendsList(3, 1);
		List<Integer> friendsIDs = StockQueries.getFriendsListIDsForUser(1);
		assertEquals(5,friendsIDs.size());
		assertEquals(1,(int)friendsIDs.get(0));
		assertEquals(2,(int)friendsIDs.get(1));
		assertEquals(3,(int)friendsIDs.get(2));
		assertEquals(4,(int)friendsIDs.get(3));
		assertEquals(5,(int)friendsIDs.get(4));	
	}
	
	//================================================
	//	       		Test getStockRanks
	//================================================
	@Test
	public void testNoStockRanks()
	{
		deleteStockTransactions();
		List<Integer> userIDs = new LinkedList<Integer>();
		userIDs.add(1);
		assertEquals(0,StockQueries.getStockRanks(userIDs).size());
	}
	
	@Test
	public void testStockRanksNegativeNet()
	{
		deleteStockTransactions();
		insertStockTransaction(1, 2, 1, today);
		insertStockTransaction(1, 2, 1, today);
		insertStockTransaction(1, 1, 1, today);
		List<Integer> userIDs = new LinkedList<Integer>();
		userIDs.add(1);
		List<Rank> ranks = StockQueries.getStockRanks(userIDs); 
		assertEquals(1, ranks.size());
		assertEquals(-1, ranks.get(0).rank);
	}
	
	@Test
	public void testStockRanksPositiveNet()
	{
		deleteStockTransactions();
		insertStockTransaction(1, 1, 1, today);
		insertStockTransaction(1, 1, 1, today);
		insertStockTransaction(1, 2, 1, today);
		List<Integer> userIDs = new LinkedList<Integer>();
		userIDs.add(1);
		List<Rank> ranks = StockQueries.getStockRanks(userIDs); 
		assertEquals(1, ranks.size());
		assertEquals(1, ranks.get(0).rank);
	}
	
	@Test
	public void testStockRanksOlderDates()
	{
		deleteStockTransactions();
		insertStockTransaction(1, 1, 1, today);
		insertStockTransaction(1, 1, 1, lastWeek);
		insertStockTransaction(1, 1, 1, lastEightDays); //should drop this
		List<Integer> userIDs = new LinkedList<Integer>();
		userIDs.add(1);
		List<Rank> ranks = StockQueries.getStockRanks(userIDs); 
		assertEquals(1, ranks.size());
		assertEquals(2, ranks.get(0).rank);
	}
	
	@Test
	public void testStockRanksStandardOneUser()
	{
		deleteStockTransactions();
		insertStockTransaction(1, 1, 1, today);
		insertStockTransaction(1, 1, 1, today);
		insertStockTransaction(1, 1, 1, today);
		insertStockTransaction(1, 2, 2, today);
		insertStockTransaction(1, 2, 2, today);
		insertStockTransaction(1, 1, 3, today);
		insertStockTransaction(1, 2, 3, today);
		List<Integer> userIDs = new LinkedList<Integer>();
		userIDs.add(1);
		List<Rank> ranks = StockQueries.getStockRanks(userIDs); 
		assertEquals(3, ranks.size());
		assertEquals(3, ranks.get(0).rank);
		assertEquals(0, ranks.get(1).rank);
		assertEquals(-2, ranks.get(2).rank);	
	}
	
	@Test
	public void testStockRanksMultipleUsers()
	{
		deleteStockTransactions();
		insertStockTransaction(1, 1, 1, today);
		insertStockTransaction(2, 1, 1, today);
		insertStockTransaction(3, 1, 1, today); //should drop this
		List<Integer> userIDs = new LinkedList<Integer>();
		userIDs.add(1);
		userIDs.add(2);
		List<Rank> ranks = StockQueries.getStockRanks(userIDs); 
		assertEquals(1, ranks.size());
		assertEquals(2, ranks.get(0).rank);		
	}
}
