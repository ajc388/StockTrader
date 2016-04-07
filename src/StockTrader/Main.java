package StockTrader;

import java.util.List;

public class Main {

	public static void main(String[] argv) {
		List<String> ls = StockQueries.getTradeTransactionsForUser(1);
		StockQueries.printData(ls);
	}

	
}


