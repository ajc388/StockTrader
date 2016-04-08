package StockTrader;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import DataModel.Rank;

public class Main {

	public static void main(String[] argv) {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter your user identification number: ");
		String input = sc.nextLine();
		int uid = Integer.parseInt(input);
		
		System.out.println("Fetching Friends List: ");
		//O(n)
		List<Integer> friendsList = StockQueries.getFriendsListIDsForUser(uid);
		StockQueries.printData(friendsList);
		
		System.out.println("Fetching Stock Transactions: ");
		//O(2n*k) where n is the stock count, 
		//m is the buy|sell count should be a constant value of 2,
		//and k is a lookup table for tickers, which should also be a constant.
		//To design this for speed the lookup tables should be removed.
		List<Rank> ranks = StockQueries.getStockRanks(friendsList);
		System.out.println("ALERT - Trade Activity at " + (new Date()) );
		StockQueries.printData(ranks);	
	}
}


